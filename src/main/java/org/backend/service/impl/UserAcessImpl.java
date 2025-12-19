package org.backend.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.backend.config.ReloadMenuConfig;
import org.backend.config.UserAuthenticationProvider;
import org.backend.constant.ResponseCode;
import org.backend.entity.Access;
import org.backend.entity.Menu;
import org.backend.entity.Token;
import org.backend.entity.User;
import org.backend.repository.AccessRepository;
import org.backend.repository.TokenRepository;
import org.backend.repository.UserRepository;
import org.backend.request.BlockUserRequest;
import org.backend.request.CreateAccessRequest;
import org.backend.request.CreateUserRequest;
import org.backend.request.LoginRequest;
import org.backend.response.GetUserAccessListResponse;
import org.backend.response.embedded.*;
import org.backend.service.IUserAcess;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class UserAcessImpl implements IUserAcess {

    @Value("${spring.emailVerification}")
    private boolean useEmailVerification;

    List<String> accessMenu = Arrays.asList("CAREER", "NEWS");
    private final ReloadMenuConfig reloadMenuConfig;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AccessRepository accessRepository;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailImpl emailImpl;
    private final UserAuthenticationProvider userAuthenticationProvider;

    public UserAcessImpl(ReloadMenuConfig reloadMenuConfig, BCryptPasswordEncoder bCryptPasswordEncoder, AccessRepository accessRepository, UserRepository userRepository, TokenRepository tokenRepository, EmailImpl emailImpl, UserAuthenticationProvider userAuthenticationProvider) {
        this.reloadMenuConfig = reloadMenuConfig;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accessRepository = accessRepository;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.emailImpl = emailImpl;
        this.userAuthenticationProvider = userAuthenticationProvider;
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        UserResponse.DTO dto = UserResponse.DTO.builder()
                .username(request.getUsername())
                .name(request.getName())
                .email(request.getEmail())
                .build();

        User emailUsed = userRepository.findByEmail(request.getEmail());

        if(emailUsed!=null){
            return UserResponse.buildResponse(dto, ResponseCode.EMAIL_ALREADY_USE);
        }

        User user = userRepository.findByUsername(request.getUsername());
        if(user==null){
            user = new User();
            user.setUsername(request.getUsername());
            user.setStatus(true);
            user.setName(request.getName());

            String encodedPassword = bCryptPasswordEncoder
                    .encode(request.getPassword());
            user.setPassword(encodedPassword);
            user.setEmail(request.getEmail());
            userRepository.save(user);

            List<Menu> test = reloadMenuConfig.getActiveMenus();
            for (Menu menu : test) {
                Access access = new Access();
                access.setUsername(request.getUsername());
                access.setRoleName(request.getRole());
                access.setMenuAccess(menu.getText());
                accessRepository.save(access);
            }

            if(useEmailVerification){
                String tokenVerification = UUID.randomUUID().toString();
                Token token = new Token(
                        tokenVerification,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(15),
                        user
                );
                tokenRepository.save(token);

                String link = "http://localhost:8080/api/users/confirm?token=" + tokenVerification;
                emailImpl.send(
                        request.getEmail(),
                        emailImpl.buildEmail(request.getName(), link));
            }

            dto.setTokenJwt(userAuthenticationProvider.createToken(dto));
            return UserResponse.buildResponse(dto, ResponseCode.SUCCESS);
        }
        return UserResponse.buildResponse(dto, ResponseCode.ACCOUNT_ALREADY_EXIST);
    }

    @Override
    public BlockUserResponse blockUser(BlockUserRequest request) {
        BlockUserResponse.DTO dto = BlockUserResponse.DTO.builder()
                .username(request.getUsername())
                .status(request.isStatus())
                .build();

        User user = userRepository.findByUsername(request.getUsername());
        if (user == null ) {
            dto.setStatus(false);
            return BlockUserResponse.buildResponse(dto, ResponseCode.USERNAME_NOTFOUND);
        }
        if (!user.isStatus()){
            dto.setStatus(false);
            return BlockUserResponse.buildResponse(dto, ResponseCode.USERNAME_ALREADY_DEACTIVE);
        }
        user.setUsername(request.getUsername());
        user.setStatus(false);
        userRepository.save(user);
        return BlockUserResponse.buildResponse(dto, ResponseCode.SUCCESS);
    }

    @Override
    public CreateAccessResponse createAccess(CreateAccessRequest request) {
        CreateAccessResponse.DTO dto = CreateAccessResponse.DTO.builder()
                .username(request.getUsername())
                .menuAccess(request.getMenuAccess())
                .roleName(request.getRoleName())
                .build();
        Access access = accessRepository.findByUsernameAndMenuAccess(request.getUsername(), request.getMenuAccess());
        if (access == null) {
            return CreateAccessResponse.buildResponse(null, ResponseCode.ACCESS_NOTFOUND);
        }
        access.setUsername(request.getUsername());
        access.setRoleName(request.getRoleName());
        access.setMenuAccess(request.getMenuAccess());
        accessRepository.save(access);
        return CreateAccessResponse.buildResponse(dto, ResponseCode.SUCCESS);
    }

    public GetUserResponse getUser(String username){
        GetUserResponse.DTO dto = GetUserResponse.DTO.builder()
                .username(username)
                .status(false)
                .build();
        User user = userRepository.findByUsername(username);
        if (user == null ) {
            return GetUserResponse.buildResponse(dto, ResponseCode.USERNAME_NOTFOUND);
        }
        dto.setStatus(user.isStatus());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        return GetUserResponse.buildResponse(dto, ResponseCode.SUCCESS);
    }

    public GetUserAccessListResponse getUserAccess(String username) {
        List<Access> accessList = accessRepository.findByUsername(username);
        if (accessList == null || accessList.isEmpty()) {
            return GetUserAccessListResponse.buildResponse(Collections.emptyList(), ResponseCode.USER_ACCESS_NOTFOUND);
        }
        List<Menu> reloadMenu = reloadMenuConfig.getActiveMenus();
        List<GetUserAccessResponse> userAccessResponses = new ArrayList<>();
        for (Menu menu : reloadMenu) {
            GetUserAccessResponse getUserAccessResponse = new GetUserAccessResponse();
            getUserAccessResponse.setHeading(menu.getHeading());
            getUserAccessResponse.setIcon(menu.getIcon());
            getUserAccessResponse.setText(menu.getText());
            getUserAccessResponse.setLink(menu.getLink());
//            getUserAccessResponse.setSubMenu(menu.getSubMenu());
            userAccessResponses.add(getUserAccessResponse);
        }

        return GetUserAccessListResponse.buildResponse(userAccessResponses, ResponseCode.SUCCESS);
    }

    @Transactional
    public GetTokenResponse confirmToken(String tokenString){

        Token token = tokenRepository.findByToken(tokenString);
        if(token==null){
            return GetTokenResponse.buildResponse(null, ResponseCode.TOKEN_NOTFOUND);
        }
        if (token.getConfirmedAt() != null) {
            return GetTokenResponse.buildResponse(null, ResponseCode.TOKEN_ALREADY_CONFIRMED);
        }
        LocalDateTime expiredAt = token.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            return GetTokenResponse.buildResponse(null, ResponseCode.TOKEN_EXPIRED);
        }

        User user = userRepository.findByUsername(token.getUser().getUsername());
        if(user==null){
            return GetTokenResponse.buildResponse(null, ResponseCode.USERNAME_NOTFOUND);
        }
        token.setConfirmedAt(LocalDateTime.now());
        user.setVerification(true);
        return GetTokenResponse.buildResponse(null, ResponseCode.SUCCESS);
    }

    public UserResponse login(LoginRequest request) {
        UserResponse.DTO dto = UserResponse.DTO.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .build();

        String identity;
        // Check if username is blank, use email if username is blank
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            identity = request.getUsername();
        }
        // If username is blank, check if email is provided
        else if (request.getEmail() != null && !request.getEmail().isBlank()) {
            identity = request.getEmail();
        }
        else {
            return UserResponse.buildResponse( null, ResponseCode.USERNAME_OR_EMAIL_ISNOTNULL);
        }

        User user = userRepository.findByUsername(identity);
        if (user == null) {
            user = userRepository.findByEmail(identity);
        }
        if (user != null) {
            // Validate the password
            boolean passwordMatches = bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword());
            if (!passwordMatches) {
                return UserResponse.buildResponse(null, ResponseCode.INVALID_PASSWORD);
            }

            dto.setTokenJwt(userAuthenticationProvider.createToken(dto));
            return UserResponse.buildResponse(dto, ResponseCode.SUCCESS);
        }
        return UserResponse.buildResponse(null, ResponseCode.USERNAME_OR_EMAIL_NOTFOUND);
    }
}
