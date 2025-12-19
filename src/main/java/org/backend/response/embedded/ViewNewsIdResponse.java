package org.backend.response.embedded;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.backend.constant.ResponseCode;
import org.backend.response.BaseResponseDTO;
import org.backend.response.ResponseDTO;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewNewsIdResponse extends BaseResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ViewNewsIdResponse.DTO detail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DTO {
        private int id;
        private String title;
        private String content;
        private String category;
        private String imageUrl;
        private Timestamp createdTime;
        private Timestamp updatedTime;
    }

    public static ViewNewsIdResponse buildResponse(ViewNewsIdResponse.DTO detail, ResponseCode responseCode) {
        ViewNewsIdResponse response = new ViewNewsIdResponse();
        response.setResponse(ResponseDTO.toResponse(responseCode));
        response.setDetail(detail);
        return response;
    }
}
