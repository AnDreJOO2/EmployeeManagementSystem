package com.example.springbackend.exception;

public class IllegalFileExtensionException extends RuntimeException {

    public IllegalFileExtensionException(String file) {
        super(String.format("File: %s has illegal extension", file));
    }
}
