package com.ipinxitin.identity_service.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    USER_NOT_FOUND(1001, "User not found!"),
    USER_EXISTED(1002, "User existed!"),
    INVALID_USERNAME(1003, "Username must be at least {min} characters!"),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters!"),
    UNAUTHENTICATED(1005, "Unauthenticated!"),
    INVALID_DATE_OF_BIRTH(1006, "Your age must be at least {min}!"),
    TOKEN_LOGGED_OUT(1007, "Token already logged out!");
    
    int code;
    String message;
}
