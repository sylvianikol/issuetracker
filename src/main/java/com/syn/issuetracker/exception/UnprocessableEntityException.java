package com.syn.issuetracker.exception;

public class UnprocessableEntityException extends RuntimeException {
    private Object object;

    public UnprocessableEntityException(String error) {
        super(error);
    }

    public UnprocessableEntityException(String error, Object object) {
        super(error);
        this.object = object;
    }
}
