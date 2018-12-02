package com.worldremit.atms.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
public class ATMLocationsFinderExceptionHandler {

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<ErrorMessage> handleThrowable(Exception ex) {
    ErrorMessage errorObj = new ErrorMessage(ex.getMessage());
    return new ResponseEntity<>(errorObj, INTERNAL_SERVER_ERROR);
  }
}
