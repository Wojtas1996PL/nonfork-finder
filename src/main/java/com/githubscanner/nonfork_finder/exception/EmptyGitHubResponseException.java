package com.githubscanner.nonfork_finder.exception;

import lombok.Getter;

@Getter
public class EmptyGitHubResponseException extends RuntimeException {
    private final int status;

    public EmptyGitHubResponseException(int status, String message) {
        super(message);
        this.status = status;
    }
}
