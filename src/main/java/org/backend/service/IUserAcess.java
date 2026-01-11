package org.backend.service;

import org.backend.request.CreateUserRequest;
import org.backend.request.LoginRequest;
import org.backend.request.PasswordRequest;
import org.backend.response.BaseResponseDTO;
import org.backend.response.embedded.GetTokenResponse;
import org.backend.response.embedded.GetUserResponse;
import org.backend.response.embedded.UserResponse;

public interface IUserAcess {
    GetUserResponse getUser(String email);
    UserResponse createUser(CreateUserRequest request);
    UserResponse refreshToken(String tokenString);
    UserResponse login(LoginRequest request);
    UserResponse logout(String request);
    GetTokenResponse verify(String request);
    GetTokenResponse password(PasswordRequest request);

}
