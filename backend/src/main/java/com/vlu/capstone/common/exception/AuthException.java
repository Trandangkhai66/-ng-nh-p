package com.vlu.capstone.common.exception;

import org.springframework.http.HttpStatus;

public class AuthException extends BusinessException {
    public AuthException(ErrorCode errorCode) {
        super(errorCode, HttpStatus.UNAUTHORIZED);
    }
}
