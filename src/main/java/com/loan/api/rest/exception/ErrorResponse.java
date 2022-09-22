package com.loan.api.rest.exception;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class ErrorResponse {
    private String message;
    private int status;
    private String path;
    private Map<String, String> errors;
}
