package com.githubscanner.nonfork_finder.exception;

public class EmptyGitHubResponseException extends RuntimeException {
    public EmptyGitHubResponseException(String message) {
        super(message);
    }
}
