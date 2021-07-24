package com.example.jasoali.exceptions;

public class MessageException extends Exception{
    private String message;

    public MessageException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
