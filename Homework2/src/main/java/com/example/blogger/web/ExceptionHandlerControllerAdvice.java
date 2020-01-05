package com.example.blogger.web;

import com.example.blogger.exception.InvalidEntityException;
import com.example.blogger.exception.NonexisitngEntityException;
import com.example.blogger.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.NoPermissionException;

@ControllerAdvice(basePackageClasses = {com.example.blogger.web.UserController.class,com.example.blogger.web.PostController.class})
public class ExceptionHandlerControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleExceptions(NonexisitngEntityException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage()));

    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleExceptions(InvalidEntityException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleExceptions(NoPermissionException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage()));
    }
}
