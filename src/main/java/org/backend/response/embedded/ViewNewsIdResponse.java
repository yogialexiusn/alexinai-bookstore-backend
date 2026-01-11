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
public class ViewNewsIdResponse extends BaseResponseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ViewNewsIdResponse.DTO detail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DTO {
        private String slug;
        private String title;
        private String author;
        private String coverUrl;
        private String pdfUrl;
        private String category;
        private int totalReads;
        private int totalBookmarks;
        private int totalFavorites;
        private boolean bookmarked;
    }

    public static ViewNewsIdResponse buildResponse(ViewNewsIdResponse.DTO detail, ResponseCode responseCode) {
        ViewNewsIdResponse response = new ViewNewsIdResponse();
        response.setResponse(ResponseDTO.toResponse(responseCode));
        response.setDetail(detail);
        return response;
    }
}
