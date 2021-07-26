package com.example.jasoali.models;

public class Comment {
    private String id;
    private String makerId;
    private String questionsHolderId;
    private String name;
    private String text;

    public Comment(String userId, String text, String name, String questionsHolderId) {
        this.makerId = userId;
        this.text = text;
        this.name = name;
        this.questionsHolderId = questionsHolderId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getQuestionsHolderId() {
        return questionsHolderId;
    }

    public void setQuestionsHolderId(String questionsHolderId) {
        this.questionsHolderId = questionsHolderId;
    }

}
