package com.example.jasoali.exceptions;

public class LengthExceeded extends MessageException{
    public LengthExceeded(int maxLength){
        super("Length of string should be less than " + maxLength);
    }
}
