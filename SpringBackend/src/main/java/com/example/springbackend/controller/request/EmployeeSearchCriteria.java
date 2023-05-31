package com.example.springbackend.controller.request;

import org.springframework.data.domain.Sort;

public class EmployeeSearchCriteria {

    private int number = 0;
    private int size = 15;

    private String sortBy = "id";
    private Sort.Direction direction = Sort.Direction.ASC;
    private String firstNameOrLastNameOrEmailLike;

    private Double salaryGreaterEqual;
    private Double salaryLessEqual;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Sort.Direction getDirection() {
        return direction;
    }

    public void setDirection(Sort.Direction direction) {
        this.direction = direction;
    }

    public String getFirstNameOrLastNameOrEmailLike() {
        return firstNameOrLastNameOrEmailLike;
    }

    public void setFirstNameOrLastNameOrEmailLike(String firstNameOrLastNameOrEmailLike) {
        this.firstNameOrLastNameOrEmailLike = firstNameOrLastNameOrEmailLike;
    }

    public Double getSalaryGreaterEqual() {
        return salaryGreaterEqual;
    }

    public void setSalaryGreaterEqual(Double salaryGreaterEqual) {
        this.salaryGreaterEqual = salaryGreaterEqual;
    }

    public Double getSalaryLessEqual() {
        return salaryLessEqual;
    }

    public void setSalaryLessEqual(Double salaryLessEqual) {
        this.salaryLessEqual = salaryLessEqual;
    }

    @Override
    public String toString() {
        return "EmployeeSearchCriteria{" +
                "number=" + number +
                ", size=" + size +
                ", sortBy='" + sortBy + '\'' +
                ", direction=" + direction +
                ", firstNameOrLastNameOrEmailLike='" + firstNameOrLastNameOrEmailLike + '\'' +
                ", salaryGreaterEqual=" + salaryGreaterEqual +
                ", salaryLessEqual=" + salaryLessEqual +
                '}';
    }
}
