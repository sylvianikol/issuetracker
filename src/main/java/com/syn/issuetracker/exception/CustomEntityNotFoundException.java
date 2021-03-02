package com.syn.issuetracker.exception;

public class CustomEntityNotFoundException extends RuntimeException {

    public CustomEntityNotFoundException(String error) {
        super(error);
    }
}
