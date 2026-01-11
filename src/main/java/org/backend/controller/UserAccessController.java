package org.backend.controller;

import org.backend.request.CreateUserRequest;
import org.backend.request.LoginRequest;
import org.backend.request.PasswordRequest;
import org.backend.response.BaseResponseDTO;
import org.backend.response.embedded.GetTokenResponse;
import org.backend.response.embedded.GetUserResponse;
import org.backend.response.embedded.UserResponse;
import org.backend.service.IUserAcess;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserAccessController extends BaseController{

    private final IUserAcess iUserAcess;

    public UserAccessController(IUserAcess iUserAcess) {
        this.iUserAcess = iUserAcess;
    }

    @GetMapping("/{email}")
    public ResponseEntity<GetUserResponse> getUser(@PathVariable String email) {
        return execute(iUserAcess.getUser(email));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        return execute(iUserAcess.createUser(request));
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<UserResponse> refresh(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        return execute(iUserAcess.refreshToken(refreshToken));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
        return execute(iUserAcess.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<UserResponse> logout(@CookieValue(name = "refresh_token", required = false) String refreshToken) {
        return execute(iUserAcess.logout(refreshToken));
    }

    @PostMapping(path = "/verify")
    public ResponseEntity<GetTokenResponse> verify(@RequestParam("token") String token) {
        return execute(iUserAcess.verify(token));
    }

    @PostMapping(path = "/password")
    public ResponseEntity<GetTokenResponse> password(@RequestBody PasswordRequest passwordRequest) {
        return execute(iUserAcess.password(passwordRequest));
    }
}
