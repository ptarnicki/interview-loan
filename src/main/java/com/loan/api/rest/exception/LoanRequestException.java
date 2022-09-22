package com.loan.api.rest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class LoanRequestException extends RuntimeException {

    public LoanRequestException(String message) {
        super(message);
    }
}
