package org.backend.request;

import lombok.Data;
import org.backend.validation.ValueMustValid;

@Data
public class BlockUserRequest {
    private String username;
    private boolean status;

}