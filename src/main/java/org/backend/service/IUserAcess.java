package org.backend.service;

import org.backend.request.BlockUserRequest;
import org.backend.request.CreateAccessRequest;
import org.backend.request.CreateUserRequest;
import org.backend.request.LoginRequest;
import org.backend.response.GetUserAccessListResponse;
import org.backend.response.embedded.*;

public interface IUserAcess {
    UserResponse createUser(CreateUserRequest request);
    BlockUserResponse blockUser(BlockUserRequest request);
    CreateAccessResponse createAccess(CreateAccessRequest request);
    GetUserResponse getUser(String username);
    GetUserAccessListResponse getUserAccess(String username);
    GetTokenResponse confirmToken(String tokenString);
    UserResponse login(LoginRequest request);

}
