package com.example.jasoali.exceptions;

public class AuthenticationFailed extends MessageException{
    public AuthenticationFailed(){
        super("Username of Password is not correct");
    }
}
