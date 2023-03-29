package com.example.springbackend.exception;

public class ImportNullFileException extends RuntimeException {

    public ImportNullFileException() {
        super("Imported file can't be null");
    }
}
