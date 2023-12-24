package com.example.demo.model;

public class ClassNotEmptyException extends UserFaultExcetion{
    public ClassNotEmptyException(String message) {
        super(message);
    }
}
