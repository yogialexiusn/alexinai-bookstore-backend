package org.backend.response.embedded;

import lombok.*;
import org.backend.constant.ResponseCode;
import org.backend.response.ResponseDTO;
import org.backend.response.BaseResponseDTO;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse extends BaseResponseDTO {

    private DTO detail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DTO {
        private String username;
        private String name;
        private String email;
        private String tokenJwt;
    }

    public static UserResponse buildResponse(DTO detail, ResponseCode responseCode) {
        UserResponse response = new UserResponse();
        response.setResponse(ResponseDTO.toResponse(responseCode));
        response.setDetail(detail);
        return response;
    }
}
