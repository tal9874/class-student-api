package com.example.demo.model;

public class LimitingStudentsException extends UserFaultExcetion{
    public LimitingStudentsException(String message) {
        super(message);
    }
}
