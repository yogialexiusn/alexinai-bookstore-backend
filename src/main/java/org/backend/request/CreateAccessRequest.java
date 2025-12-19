package org.backend.request;

import lombok.Data;
import org.backend.validation.ValueMustValid;

@Data
public class CreateAccessRequest {
    private String username;
    @ValueMustValid(valueAllowed = {"SUPERADMIN", "ADMIN", "VIEWER"})
    private String roleName;
    private String menuAccess;
}