package com.syn.issuetracker.exception.error;

import java.util.List;

public class ErrorResponse {

    private int statusCode;
    private String description;
    private String message;
    private List<String> error;

    public ErrorResponse(int statusCode, String description) {
        this.statusCode = statusCode;
        this.description = description;
    }

    public ErrorResponse(int statusCode, String description, String message) {
        this(statusCode, description);
        this.message = message;
    }

    public ErrorResponse(int statusCode, String description, List<String> error) {
        this(statusCode, description);
        this.error = error;
    }

    public ErrorResponse(int statusCode, String description, String message, List<String> error) {
        this(statusCode, description, message);
        this.error = error;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }
}
