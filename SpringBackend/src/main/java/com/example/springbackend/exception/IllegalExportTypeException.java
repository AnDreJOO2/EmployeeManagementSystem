package com.example.springbackend.exception;

public class IllegalExportTypeException extends RuntimeException{

    public IllegalExportTypeException(String type){
        super(String.format("Illegal export type: %s ", type));
    }
}
