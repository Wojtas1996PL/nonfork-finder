package com.githubscanner.nonfork_finder.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final int status;

    public UserNotFoundException(int status, String message) {
        super(message);
        this.status = status;
    }
}
