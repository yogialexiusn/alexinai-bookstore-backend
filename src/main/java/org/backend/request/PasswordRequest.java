package org.backend.request;

import lombok.Data;

@Data
public class PasswordRequest {
    private String tokenVerification;
    private String password;
}