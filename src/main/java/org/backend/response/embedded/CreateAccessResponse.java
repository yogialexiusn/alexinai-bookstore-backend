package org.backend.response.embedded;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.backend.constant.ResponseCode;
import org.backend.response.ResponseDTO;
import org.backend.response.BaseResponseDTO;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAccessResponse extends BaseResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DTO detail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DTO {
        private String username;
        private String menuAccess;
        private String roleName;
    }

    public static CreateAccessResponse buildResponse(DTO detail, ResponseCode responseCode) {
        CreateAccessResponse response = new CreateAccessResponse();
        response.setResponse(ResponseDTO.toResponse(responseCode));
        response.setDetail(detail);
        return response;

    }
}
