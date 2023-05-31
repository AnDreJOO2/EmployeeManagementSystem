package com.example.springbackend.controller.advice;

import com.example.springbackend.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralControllerAdvice {

    @ExceptionHandler(IllegalFileExtensionException.class)
    public ResponseEntity<String> illegalFileExtensionExceptionHandler(IllegalFileExtensionException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExportEmployeeToCsvException.class)
    public ResponseEntity<String> exportEmployeeToCsvExceptionHandler(ExportEmployeeToCsvException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ImportEmployeeToCsvException.class)
    public ResponseEntity<String> importEmployeeToCsvExceptionHandler(ImportEmployeeToCsvException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<String> employeeNotFoundExceptionHandler(EmployeeNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalExportTypeException.class)
    public ResponseEntity<String> illegalExportTypeExceptionHandler(IllegalExportTypeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
