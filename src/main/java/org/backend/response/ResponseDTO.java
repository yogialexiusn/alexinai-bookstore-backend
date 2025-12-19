package org.backend.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.backend.constant.ResponseCode;
import org.backend.util.DateUtil;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ResponseDTO {
    private String responseCode;
    private String responseDescription;
    private String executionTime;

    public static ResponseDTO toResponse(ResponseCode responseCode) {
        return toResponse(responseCode, responseCode.getDescription());
    }

    public static ResponseDTO toResponse(ResponseCode responseCode, String description) {
        ResponseDTO respDto = ResponseDTO.builder()
                .responseCode(responseCode.getCode())
                .responseDescription(description)
                .build();

        respDto.prepareExecutionTime();

        return respDto;
    }

    public void prepareExecutionTime() {
        this.executionTime = DateUtil.getIso8601Time();
    }
}
