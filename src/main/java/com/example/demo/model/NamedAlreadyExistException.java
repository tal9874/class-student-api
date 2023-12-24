package com.example.demo.model;

public class NamedAlreadyExistException extends UserFaultExcetion{
    public NamedAlreadyExistException(String message) {
        super(message);
    }
}
