package com.syn.issuetracker.exception;

import com.syn.issuetracker.exception.error.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    private static final Logger log =
            LoggerFactory.getLogger(ExceptionHandlerAdvice.class);


    @ExceptionHandler(CustomEntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleBuildingNotFoundException(CustomEntityNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getDescription(true),
                ex.getErrorContainer());
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorResponse handleUnprocessableEntityException(UnprocessableEntityException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ex.getMessage(),
                request.getDescription(true),
                ex.getErrorContainer());
    }

    @ExceptionHandler(DataConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorResponse handleEntityExistsException(DataConflictException ex, WebRequest request) {
        log.error(ex.getMessage());
        return new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getDescription(true),
                ex.getErrorContainer());
    }
}
