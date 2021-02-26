package com.syn.issuetracker.exception;

import com.syn.issuetracker.exception.error.ErrorContainer;

public class CustomEntityNotFoundException extends RuntimeException {

    private ErrorContainer errorContainer;

    public CustomEntityNotFoundException(String message) {
        super(message);
        this.errorContainer = new ErrorContainer();
    }

    public CustomEntityNotFoundException(String message, ErrorContainer errorContainer) {
        super(message);
        this.errorContainer = errorContainer;
    }

    public ErrorContainer getErrorContainer() {
        return errorContainer;
    }

    public void setErrorContainer(ErrorContainer errorContainer) {
        this.errorContainer = errorContainer;
    }
}
