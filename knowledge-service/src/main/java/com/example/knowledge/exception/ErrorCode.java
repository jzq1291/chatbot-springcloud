package com.example.knowledge.exception;

public enum ErrorCode {
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    RESOURCE_NOT_FOUND(404, "Resource Not Found"),
    BAD_REQUEST(400, "Bad Request");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
} 