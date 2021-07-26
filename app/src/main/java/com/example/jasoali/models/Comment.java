package com.example.jasoali.models;

public class Comment {
    private String makerId;
    private String name;
    private String text;

    public Comment(String userId, String text, String name) {
        this.makerId = userId;
        this.text = text;
        this.name = name;
    }

    public String getMakerId() {
        return makerId;
    }

    public void setMakerId(String makerId) {
        this.makerId = makerId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
