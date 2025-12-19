package org.backend.response.embedded;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.backend.constant.ResponseCode;
import org.backend.response.BaseResponseDTO;
import org.backend.response.ResponseDTO;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponse extends BaseResponseDTO {

    private DTO detail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DTO {
        private String username;
        private boolean status;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String name;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private String email;
    }

    public static GetUserResponse buildResponse(DTO detail, ResponseCode responseCode) {
        GetUserResponse response = new GetUserResponse();
        response.setResponse(ResponseDTO.toResponse(responseCode));
        response.setDetail(detail);
        return response;
    }
}
