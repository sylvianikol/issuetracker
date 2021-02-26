package com.syn.issuetracker.exception.error;

public class ErrorResponse {

    private int statusCode;
    private String message;
    private String description;
    private ErrorContainer errorContainer;

    public ErrorResponse(int statusCode, String message, String description, ErrorContainer errorContainer) {
        this.statusCode = statusCode;
        this.message = message;
        this.description = description;
        this.errorContainer = errorContainer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ErrorContainer getErrorContainer() {
        return errorContainer;
    }

    public void setErrorContainer(ErrorContainer errorContainer) {
        this.errorContainer = errorContainer;
    }
}
