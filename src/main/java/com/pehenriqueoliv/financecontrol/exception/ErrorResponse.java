package com.pehenriqueoliv.financecontrol.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        int status,
        String message,
        List<String> errors,
        LocalDateTime timestamp
) {
    public ErrorResponse(int status, String message) {
        this(status, message, null, LocalDateTime.now());
    }
}
