package com.testingexercises.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = CustomerNotFoundException.class)
    public ResponseEntity<ApiException> handleCustomerNotFoundException(CustomerNotFoundException e) {
        return new ResponseEntity<>(new ApiException(e.getMessage(), ZonedDateTime.now()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = DuplicateEmailException.class)
    public ResponseEntity<ApiException> handleDuplicateEmailException(DuplicateEmailException e) {
        return new ResponseEntity<>(new ApiException(e.getMessage(), ZonedDateTime.now()), HttpStatus.NOT_ACCEPTABLE);
    }
}
