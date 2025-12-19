package org.backend.response;

import lombok.*;
import org.backend.constant.ResponseCode;
import org.backend.response.embedded.GetUserAccessResponse;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetUserAccessListResponse extends BaseResponseDTO {

    private DTO detail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DTO {
        private List<GetUserAccessResponse> userAccessList;

    }

    public static GetUserAccessListResponse buildResponse(List<GetUserAccessResponse> accessResponses, ResponseCode responseCode) {
        GetUserAccessListResponse response = new GetUserAccessListResponse();
        GetUserAccessListResponse.DTO dto = new GetUserAccessListResponse.DTO();
        dto.setUserAccessList(accessResponses);

        response.setResponse(ResponseDTO.toResponse(responseCode));
        response.setDetail(dto);
        return response;
    }

}

