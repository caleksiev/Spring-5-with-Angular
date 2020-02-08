package com.fmi.spring5.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AttributeNullException extends Exception {
    public AttributeNullException() {
        super();
    }

    public AttributeNullException(String message) {
        super(message);
    }

    public AttributeNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttributeNullException(Throwable cause) {
        super(cause);
    }
}
