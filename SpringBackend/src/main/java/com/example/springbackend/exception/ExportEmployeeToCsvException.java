package com.example.springbackend.exception;

public class ExportEmployeeToCsvException extends RuntimeException {

    public ExportEmployeeToCsvException() {
        super("Something went wrong during exporting employees to csv file");
    }
}
