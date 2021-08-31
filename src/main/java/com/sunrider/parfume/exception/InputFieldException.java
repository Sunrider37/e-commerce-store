package com.sunrider.parfume.exception;

import org.springframework.validation.BindingResult;

public class InputFieldException extends RuntimeException {
    public InputFieldException(String message) {
        super(message);
    }
}
