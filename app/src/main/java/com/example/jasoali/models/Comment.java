package com.example.jasoali.models;

public class Comment {
    private String userId;
    private String name;
    private String text;

    public Comment(String userId, String text, String name) {
        this.userId = userId;
        this.text = text;
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
