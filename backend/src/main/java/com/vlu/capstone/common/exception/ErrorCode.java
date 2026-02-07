package com.vlu.capstone.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    AUTH_001("AUTH_001", "Invalid credentials"),
    AUTH_002("AUTH_002", "Token expired"),
    AUTH_003("AUTH_003", "Unauthorized"),
    USER_001("USER_001", "User not found"),
    USER_002("USER_002", "User already exists"),
    USER_003("USER_003", "Invalid user data"),
    VAL_001("VAL_001", "Validation failed"),
    SYS_001("SYS_001", "Internal server error"),
    SYS_002("SYS_002", "Service unavailable");

    private final String code;
    private final String message;
}
