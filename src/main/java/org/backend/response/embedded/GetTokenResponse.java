package org.backend.response.embedded;

import lombok.*;
import org.backend.constant.ResponseCode;
import org.backend.response.BaseResponseDTO;
import org.backend.response.ResponseDTO;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetTokenResponse extends BaseResponseDTO {

    private DTO detail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DTO {
        private String tokenString;
    }

    public static GetTokenResponse buildResponse(DTO detail, ResponseCode responseCode) {
        GetTokenResponse response = new GetTokenResponse();
        response.setResponse(ResponseDTO.toResponse(responseCode));
        response.setDetail(detail);
        return response;
    }
}
