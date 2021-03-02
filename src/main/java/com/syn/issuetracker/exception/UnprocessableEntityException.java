package com.syn.issuetracker.exception;

public class UnprocessableEntityException extends RuntimeException {

    public UnprocessableEntityException(String error) {
        super(error);
    }

}
