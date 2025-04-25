package com.ridhoazh.obs.exception;

// @formatter:off
/**
 * Created by: Ridho Azhari Riyadi
 * Date: Apr 25, 2025
 */
// @formatter:on

@SuppressWarnings("serial")
public class InvalidParameterException extends RuntimeException {
    private final String fieldError;
    private final String errorMessage;

    public InvalidParameterException(String fieldError, String errorMessage) {
        super(errorMessage);
        this.fieldError = fieldError;
        this.errorMessage = errorMessage;
    }

    public String getFieldError() {
        return fieldError;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
