package com.syn.issuetracker.exception;

public class DataConflictException extends RuntimeException {

    public DataConflictException(String error) {
        super(error);
    }
}
