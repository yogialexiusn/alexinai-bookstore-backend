package org.backend.response.embedded;


import lombok.*;
import org.backend.constant.ResponseCode;
import org.backend.response.BaseResponseDTO;
import org.backend.response.ResponseDTO;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBooksResponse extends BaseResponseDTO {

    private CreateBooksResponse.DTO detail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DTO {
        private String title;
    }

    public static CreateBooksResponse buildResponse(CreateBooksResponse.DTO detail, ResponseCode responseCode) {
        CreateBooksResponse response = new CreateBooksResponse();
        response.setResponse(ResponseDTO.toResponse(responseCode));
        response.setDetail(detail);
        return response;
    }
}
