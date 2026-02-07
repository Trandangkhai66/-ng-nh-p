package com.vlu.capstone.common.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends BusinessException {
    private final List<String> errors;

    public ValidationException(List<String> errors) {
        super(ErrorCode.VAL_001, "Validation failed");
        this.errors = errors;
    }

    public ValidationException(String message) {
        super(ErrorCode.VAL_001, message);
        this.errors = List.of(message);
    }
}
