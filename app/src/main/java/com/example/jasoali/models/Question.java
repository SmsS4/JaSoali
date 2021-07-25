package com.example.jasoali.models;

import com.example.jasoali.exceptions.LengthExceeded;

import java.util.ArrayList;

public class Question {
    private String title;
    static public int MAX_LENGTH = 30;
    public Question(String title) throws LengthExceeded {
        if (title.length() > MAX_LENGTH){
            throw new LengthExceeded(MAX_LENGTH);
        }
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}

