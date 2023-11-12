package com.example.api.controller.advice;

import com.example.api.exception.InvoiceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice()
public class InvoiceControllerAdvice {
    @ExceptionHandler(InvoiceNotFoundException.class)
    public ResponseEntity<String> handleException(InvoiceNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }
}
