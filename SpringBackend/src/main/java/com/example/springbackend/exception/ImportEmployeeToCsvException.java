package com.example.springbackend.exception;

public class ImportEmployeeToCsvException extends RuntimeException{

    public ImportEmployeeToCsvException(){
        super("Something went wrong during importing employees from csv file");
    }
}
