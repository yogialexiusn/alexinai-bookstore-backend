package org.backend.response.embedded;

import lombok.*;
import org.backend.constant.ResponseCode;
import org.backend.response.BaseResponseDTO;
import org.backend.response.ResponseDTO;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockUserResponse extends BaseResponseDTO {

    private DTO detail;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DTO {
        private String username;
        private boolean status;
    }

    public static BlockUserResponse buildResponse(DTO detail, ResponseCode responseCode) {
        BlockUserResponse response = new BlockUserResponse();
        response.setResponse(ResponseDTO.toResponse(responseCode));
        response.setDetail(detail);
        return response;
    }
}
