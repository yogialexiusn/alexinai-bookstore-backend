package org.backend;

import lombok.extern.slf4j.Slf4j;
import org.backend.constant.ResponseCode;
import org.backend.response.ResponseDTO;
import org.backend.response.DefaultErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@Slf4j
@RestControllerAdvice
public class APIErrorHandler {
    private static final String NOT_VALID_KEY = "NotValid";
    private static final String ERROR_LOG = "#Error - exception, ";

    private static final Map<String, String> MAPPED_ERRORS = Map.of(
            NOT_VALID_KEY, "'%s' field is not valid"
    );

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Object> argumentNotValid(MethodArgumentNotValidException ex) {
        log.error(ERROR_LOG, ex);
        String errorDetail = composeEventDescription(ex);
        return toFailedResponse(errorDetail, ResponseCode.REQ_FORMAT_ERROR);
    }

    private ResponseEntity<Object> toFailedResponse(String errorDetail, ResponseCode responseCode) {
        DefaultErrorResponse response = toResponse(responseCode, errorDetail);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private DefaultErrorResponse toResponse(ResponseCode errorCode, String errorDescription) {
        ResponseDTO responseDTO = ResponseDTO.builder()
                .responseCode(errorCode.getCode())
                .responseDescription(errorDescription)
                .build();

        responseDTO.prepareExecutionTime();
        return DefaultErrorResponse.builder()
                .response(responseDTO)
                .build();
    }

    private String getErrorMessage(String key, String messsage) {
        if (MAPPED_ERRORS.containsKey(messsage)) {
            return String.format(MAPPED_ERRORS.get(messsage), key);
        }
        return messsage;
    }

    private String composeEventDescription(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errorMaps = from(ex.getBindingResult());
        Map.Entry<String, List<String>> entry = errorMaps.entrySet().stream().findFirst().orElse(null);
        return entry == null ? null : getErrorMessage(entry.getKey(), entry.getValue().get(0));
    }

    private Map<String, List<String>> from(BindingResult result) {
        if (!result.hasFieldErrors()) {
            return Collections.emptyMap();
        }

        Map<String, List<String>> map = new HashMap<>();

        for (FieldError fieldError : result.getFieldErrors()) {
            String field = fieldError.getField();
            if (!map.containsKey(fieldError.getField())) {
                map.put(field, new ArrayList<>());
            }
            String errorMessage = fieldError.getDefaultMessage();
            map.get(field).add(errorMessage);
        }

        return map;
    }

}
