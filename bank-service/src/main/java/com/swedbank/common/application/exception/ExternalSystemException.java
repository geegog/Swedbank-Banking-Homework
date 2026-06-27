package com.swedbank.common.application.exception;

import lombok.Getter;

@Getter
public class ExternalSystemException extends RuntimeException {

    public ExternalSystemException(String message) {
        super(message);
    }
}
