package org.backend.controller;

import org.backend.constant.ResponseCode;
import org.backend.response.BaseResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseController {

  protected <T extends BaseResponseDTO> ResponseEntity<T> execute(T response) {
    String responseCode = response.getResponse().getResponseCode();
    HttpStatus httpStatus = ResponseCode.SUCCESS.getCode().equals(responseCode) ?
        HttpStatus.OK : HttpStatus.BAD_REQUEST;
    return ResponseEntity.status(httpStatus).body(response);
  }
}
