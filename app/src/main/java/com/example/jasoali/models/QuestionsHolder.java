package com.example.jasoali.models;

import java.util.ArrayList;
import java.util.Date;

public class QuestionsHolder {
    private String title;
    private String description;
    private Date createDate;
    private int creatorId;
    private String creatorName;
    private ArrayList<Category> categories;
    private ArrayList<Comment> comments;
    private ArrayList<Question> questions;
    private ArrayList<String> tags;

    public QuestionsHolder(String title, String description, Date createDate, int creatorId, String creatorName, ArrayList<Category> categories, ArrayList<Comment> comments, ArrayList<Question> questions, ArrayList<String> tags) {
        this.title = title;
        this.description = description;
        this.createDate = createDate;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.categories = categories;
        this.comments = comments;
        this.questions = questions;
        this.tags = tags;
    }

    public QuestionsHolder(Date createDate, int creatorId, String creatorName) {
        this.createDate = createDate;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.categories = new ArrayList<>();
        this.questions = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }
}
