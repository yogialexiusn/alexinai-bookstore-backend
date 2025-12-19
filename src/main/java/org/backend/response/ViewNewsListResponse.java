package org.backend.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.backend.constant.ResponseCode;
import org.backend.response.embedded.ViewNewsResponse;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ViewNewsListResponse extends BaseResponseDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private DTO detail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DTO {

        @JsonInclude(JsonInclude.Include.NON_NULL)
        private List<ViewNewsResponse> listNews;

    }

    public static ViewNewsListResponse buildResponse(List<ViewNewsResponse> newsResponses, ResponseCode responseCode) {
        ViewNewsListResponse response = new ViewNewsListResponse();
        ViewNewsListResponse.DTO dto = new ViewNewsListResponse.DTO();
        dto.setListNews(newsResponses);

        response.setResponse(ResponseDTO.toResponse(responseCode));
        response.setDetail(dto);
        return response;
    }

}

