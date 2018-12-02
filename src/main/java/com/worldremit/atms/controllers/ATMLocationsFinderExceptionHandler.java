package com.worldremit.atms.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class ATMLocationsFinderExceptionHandler {

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ErrorMessage> handleThrowable(Exception ex) {
    ErrorMessage errorObj = new ErrorMessage(ex.getMessage());
    return new ResponseEntity<>(errorObj, INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErrorMessage> handleConstraintViolation(ConstraintViolationException ex) {
    ErrorMessage errorObj = new ErrorMessage(ex.getMessage());
    return new ResponseEntity<>(errorObj, BAD_REQUEST);
  }
}
