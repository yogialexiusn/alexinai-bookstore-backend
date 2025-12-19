package org.backend.request;

import lombok.Data;
import org.backend.validation.ValueMustValid;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class CreateUserRequest {
    @NotBlank(message = "NotBlank")
    private String username;

    @NotBlank(message = "NotBlank")
    @ValueMustValid(valueAllowed = {"Superadmin", "Admin", "User"})
    private String role;

    @NotBlank(message = "NotBlank")
    private String name;

    @NotBlank(message = "NotBlank")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "NotBlank")
    private String password;

}