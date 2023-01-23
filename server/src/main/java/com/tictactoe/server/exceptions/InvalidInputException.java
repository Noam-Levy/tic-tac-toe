package com.tictactoe.server.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException {
    private String message;

    public InvalidInputException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
