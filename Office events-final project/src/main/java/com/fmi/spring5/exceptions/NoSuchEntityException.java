package com.fmi.spring5.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoSuchEntityException extends Exception {
    public NoSuchEntityException() {
        super();
    }

    public NoSuchEntityException(String message) {
        super(message);
    }

    public NoSuchEntityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchEntityException(Throwable cause) {
        super(cause);
    }
}
