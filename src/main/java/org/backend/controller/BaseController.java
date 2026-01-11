package org.backend.controller;

import org.backend.constant.ResponseCode;
import org.backend.response.BaseResponseDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

public class BaseController {

    protected <T extends BaseResponseDTO> ResponseEntity<T> execute(T response) {
        if (response.isLogout) {
            getDeleteCookie();
            return ResponseEntity.status(HttpStatus.OK)
                    .header(HttpHeaders.SET_COOKIE, getDeleteCookie().toString())
                    .body(response);
        }
        String responseCode = response.getResponse().getResponseCode();
        HttpStatus httpStatus = ResponseCode.SUCCESS.getCode().equals(responseCode) ?
                HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus).body(response);
    }

    private ResponseCookie getDeleteCookie() {
        return ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(true)
//                .sameSite("Strict")
//                .path("/api/auth")
//                .maxAge(0)
                .build();
    }

}
