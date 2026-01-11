package org.backend.response.embedded;

import lombok.*;
import org.backend.constant.ResponseCode;
import org.backend.response.BaseResponseDTO;
import org.backend.response.ResponseDTO;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse extends BaseResponseDTO {

    private DTO user;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime expiresIn;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DTO {
        private String email;
    }

    public static UserResponse buildLoginResponse(DTO user, ResponseCode responseCode, UserResponse userResponse) {
        UserResponse response = new UserResponse();
        if(userResponse != null) {
            response.setExpiresIn(userResponse.getExpiresIn());
            response.setAccessToken(userResponse.getAccessToken());
        }

        response.setResponse(ResponseDTO.toResponse(responseCode));
        response.setUser(user);
        return response;
    }

    public static UserResponse buildResponse(DTO user, ResponseCode responseCode) {
        UserResponse response = new UserResponse();
        response.setResponse(ResponseDTO.toResponse(responseCode));
        response.setUser(user);
        return response;
    }

    public static UserResponse buildRefreshTokenResponse(ResponseCode responseCode, UserResponse userResponse) {
        UserResponse response = new UserResponse();
        if(userResponse != null) {
            response.setExpiresIn(userResponse.getExpiresIn());
            response.setAccessToken(userResponse.getAccessToken());
        }
        response.setResponse(ResponseDTO.toResponse(responseCode));
        return response;
    }
}
