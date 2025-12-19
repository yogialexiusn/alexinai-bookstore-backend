package org.backend.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.backend.entity.User;
import org.backend.repository.UserRepository;
import org.backend.response.embedded.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Component
@Slf4j
public class UserAuthenticationProvider {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;

    private final UserRepository userRepository;

    public UserAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(UserResponse.DTO user) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + 3600000); // 1 hour
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        return JWT.create()
                .withSubject(user.getUsername())
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withClaim("name", user.getName())
                .sign(algorithm);
    }

    public Authentication validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT decoded = verifier.verify(token);
        UserResponse.DTO user = UserResponse.DTO.builder()
                .username(decoded.getSubject())
                .name(decoded.getClaim("name").asString())
                .build();

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

    public Authentication validateTokenStrongly(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secretKey);
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        DecodedJWT decoded = verifier.verify(token);
        UserResponse.DTO user = findByUsername(decoded.getSubject());

        return new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
    }

    private UserResponse.DTO findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if(user==null){
            log.error("cannot find username login {}", username);
            return null;
        }

        return UserResponse.DTO.builder().
                name(user.getName()).
                username(user.getUsername()).
                email(user.getEmail()).
                build();
    }

}