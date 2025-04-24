package com.ridhoazh.obs.exception;

// @formatter:off
/**
 * 🧠 Created by: Ridho Azhari Riyadi
 * 🗓️ Date: Apr 25, 2025
 * 💻 Auto-generated because Ridho too lazy to type this manually
 */
// @formatter:on

@SuppressWarnings("serial")
public class EntityNotFoundException extends RuntimeException {
    private final String fieldError;
    private final String errorMessage;

    public EntityNotFoundException(String fieldError, String errorMessage) {
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
