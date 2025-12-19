package org.backend.response.embedded;


import lombok.*;
import org.backend.constant.ResponseCode;
import org.backend.response.BaseResponseDTO;
import org.backend.response.ResponseDTO;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateNewsResponse extends BaseResponseDTO {

    private CreateNewsResponse.DTO detail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DTO {
        private String title;
    }

    public static CreateNewsResponse buildResponse(CreateNewsResponse.DTO detail, ResponseCode responseCode) {
        CreateNewsResponse response = new CreateNewsResponse();
        response.setResponse(ResponseDTO.toResponse(responseCode));
        response.setDetail(detail);
        return response;
    }
}
