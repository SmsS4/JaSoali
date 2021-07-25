package com.example.jasoali.models;

import com.example.jasoali.exceptions.LengthExceeded;

public class TextQuestion extends Question {
    private String text;

    public TextQuestion(String description, String text) throws LengthExceeded {
        super(description);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
