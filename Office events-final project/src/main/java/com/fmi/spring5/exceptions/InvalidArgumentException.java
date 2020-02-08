package com.fmi.spring5.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidArgumentException extends Exception {
    public InvalidArgumentException() {
        super();
    }

    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidArgumentException(Throwable cause) {
        super(cause);
    }
}
