package com.syn.issuetracker.exception.error;

public class ErrorResponse {

    private int statusCode;
    private String description;
    private String error;

    public ErrorResponse(int statusCode, String description, String error) {
        this.statusCode = statusCode;
        this.description = description;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
