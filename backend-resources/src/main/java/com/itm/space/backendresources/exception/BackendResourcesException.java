package com.itm.space.backendresources.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BackendResourcesException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BackendResourcesException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }
}