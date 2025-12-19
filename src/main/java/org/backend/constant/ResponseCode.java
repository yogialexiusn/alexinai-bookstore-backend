package org.backend.constant;

import lombok.Getter;

@Getter
public enum ResponseCode {

    SUCCESS("000", "Success"),
    INTERNAL_SERVER_ERROR("001", "Internal server error"),
    ACCOUNT_ALREADY_EXIST("002", "Account already exist"),
    EMAIL_ALREADY_USE("003", "Email already use"),
    ACCESS_NOTFOUND("004", "Menu not found"),
    USER_ACCESS_NOTFOUND("005", "User access not found"),
    USERNAME_NOTFOUND("006", "Username not found"),
    USERNAME_ALREADY_DEACTIVE("007", "Username already deactive"),
    NEWS_NOTFOUND("008", "News not found"),
    REQ_FORMAT_ERROR("009", "Format error"),
    TOKEN_NOTFOUND("010", "Token not found"),
    TOKEN_EXPIRED("011", "Token expired"),
    TOKEN_ALREADY_CONFIRMED("012", "Token already confirmed"),
    USERNAME_OR_EMAIL_ISNOTNULL("013", "Username or email is must be filled"),
    USERNAME_OR_EMAIL_NOTFOUND("014", "Username or email not found"),
    INVALID_PASSWORD("015", "Invalid password")

    ;

    private String code;
    private String description;

    ResponseCode(String code, String description) {
        this.code = code;
        this.description = description;
    }


}