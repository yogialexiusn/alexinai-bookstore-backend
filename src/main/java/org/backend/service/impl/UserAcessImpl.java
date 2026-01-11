package org.backend.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.backend.config.UserAuthenticationProvider;
import org.backend.constant.ResponseCode;
import org.backend.entity.RefreshToken;
import org.backend.entity.TokenVerification;
import org.backend.entity.User;
import org.backend.repository.RefreshTokenRepository;
import org.backend.repository.TokenVerificationRepository;
import org.backend.repository.UserRepository;
import org.backend.request.CreateUserRequest;
import org.backend.request.LoginRequest;
import org.backend.request.PasswordRequest;
import org.backend.response.embedded.GetTokenResponse;
import org.backend.response.embedded.GetUserResponse;
import org.backend.response.embedded.UserResponse;
import org.backend.service.IUserAcess;
import org.backend.util.DateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class UserAcessImpl implements IUserAcess {

    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${app.verify.email-verification}")
    private boolean useEmailVerification;
    @Value("${app.verify.register-expiry}")
    private int registerExpiry;
    @Value("${app.verify.client}")
    private boolean thisBackendUrl;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final TokenVerificationRepository tokenVerificationRepository;
    private final EmailImpl emailImpl;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public UserAcessImpl(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, TokenVerificationRepository tokenVerificationRepository, EmailImpl emailImpl, UserAuthenticationProvider userAuthenticationProvider, RefreshTokenRepository refreshTokenRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.tokenVerificationRepository = tokenVerificationRepository;
        this.emailImpl = emailImpl;
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public GetUserResponse getUser(String email) {
        GetUserResponse.DTO dto = GetUserResponse.DTO.builder()
                .email(email)
                .build();
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return GetUserResponse.buildResponse(dto, ResponseCode.USERNAME_NOTFOUND);
        }
        dto.setEmail(user.getEmail());
        return GetUserResponse.buildResponse(dto, ResponseCode.SUCCESS);
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        UserResponse.DTO dto = UserResponse.DTO.builder()
                .email(request.getEmail())
                .build();

        User emailUsed = userRepository.findByEmail(request.getEmail());

        if (emailUsed != null) {
            return UserResponse.buildResponse(dto, ResponseCode.EMAIL_ALREADY_USE);
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setVerification(false);
        userRepository.save(user);

        if (useEmailVerification) {
            String tokenVerification = UUID.randomUUID().toString();
            TokenVerification token = new TokenVerification(
                    tokenVerification,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(registerExpiry),
                    user
            );
            tokenVerificationRepository.save(token);

            String link = thisBackendUrl + tokenVerification;
            emailImpl.send(
                    request.getEmail(),
                    emailImpl.buildEmail(request.getName(), link));
        }
        return UserResponse.buildResponse(dto, ResponseCode.SUCCESS);

    }

    @Override
    public UserResponse refreshToken(String stringRefreshToken) {

        if (stringRefreshToken == null) {
            return UserResponse.buildResponse(null, ResponseCode.REQ_FORMAT_ERROR);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(stringRefreshToken);
        if (refreshToken == null) {
            return UserResponse.buildResponse(null, ResponseCode.REFRESHTOKEN_NOTFOUND);
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(refreshToken);
            return UserResponse.buildResponse(null, ResponseCode.REFRESHTOKEN_EXPIRED);
        }

        LocalDateTime expiryDate = DateUtil.getExpiryLocalDate();
        refreshToken.setExpiresAt(expiryDate);
        refreshTokenRepository.save(refreshToken);

        User user = refreshToken.getUser();
        UserResponse userResponse = UserResponse.builder()
                .accessToken(userAuthenticationProvider.createAccessToken(user))
                .expiresIn(expiryDate)
                .build();

        return UserResponse.buildRefreshTokenResponse(ResponseCode.SUCCESS, userResponse);
    }

    public UserResponse login(LoginRequest request) {
        UserResponse.DTO dto = UserResponse.DTO.builder()
                .email(request.getEmail())
                .build();

        User user = userRepository.findByEmail(request.getEmail());

        if (user != null) {
            // Validate the password
            boolean passwordMatches = bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword());
            if (!passwordMatches) {
                return UserResponse.buildLoginResponse(null, ResponseCode.LOGIN_FAILED, null);
            }

            String stringRefreshToken = UUID.randomUUID().toString();

            LocalDateTime expiryDate = DateUtil.getExpiryLocalDate();
            UserResponse userResponse = UserResponse.builder()
                    .accessToken(userAuthenticationProvider.createAccessToken(user))
                    .refreshToken(stringRefreshToken)
                    .expiresIn(expiryDate)
                    .build();

            RefreshToken tokenVerification = RefreshToken.builder()
                    .refreshToken(stringRefreshToken)
                    .createdAt(LocalDateTime.now())
                    .expiresAt(expiryDate)
                    .revoked(false)
                    .user(user)
                    .build();
            refreshTokenRepository.save(tokenVerification);

            return UserResponse.buildLoginResponse(dto, ResponseCode.SUCCESS, userResponse);
        }
        return UserResponse.buildLoginResponse(dto, ResponseCode.LOGIN_FAILED, null);
    }

    @Override
    public UserResponse logout(String request) {
        if (request == null) {
            return UserResponse.buildResponse(null, ResponseCode.REQ_FORMAT_ERROR);
        }

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(request);
        if (refreshToken != null) {
            refreshToken.setRevoked(true);
            refreshTokenRepository.save(refreshToken);
        }

        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
                .build();

        return UserResponse.buildLoginResponse(null, ResponseCode.SUCCESS, null);
    }

    @Transactional
    public GetTokenResponse verify(String tokenString) {

        TokenVerification tokenVerification = tokenVerificationRepository.findByTokenVerification(tokenString);
        if (tokenVerification == null) {
            return GetTokenResponse.buildResponse(null, ResponseCode.TOKEN_NOTFOUND);
        }
        if (tokenVerification.getConfirmedAt() != null) {
            return GetTokenResponse.buildResponse(null, ResponseCode.TOKEN_ALREADY_CONFIRMED);
        }

        LocalDateTime expiredAt = tokenVerification.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            return GetTokenResponse.buildResponse(null, ResponseCode.TOKEN_EXPIRED);
        }

        User user = userRepository.findByEmail(tokenString);
        if (user == null) {
            return GetTokenResponse.buildResponse(null, ResponseCode.USERNAME_NOTFOUND);
        }
        tokenVerification.setConfirmedAt(LocalDateTime.now());
        user.setVerification(true);
        userRepository.save(user);
        return GetTokenResponse.buildResponse(null, ResponseCode.SUCCESS);
    }

    public GetTokenResponse password(PasswordRequest passwordRequest) {
        TokenVerification tokenVerification = tokenVerificationRepository.findByTokenVerification(passwordRequest.getTokenVerification());
        if (tokenVerification == null) {
            return GetTokenResponse.buildResponse(null, ResponseCode.TOKEN_NOTFOUND);
        }

        User user = tokenVerification.getUser();
        String encodedPassword = bCryptPasswordEncoder.encode(passwordRequest.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return GetTokenResponse.buildResponse(null, ResponseCode.SUCCESS);
    }
}
