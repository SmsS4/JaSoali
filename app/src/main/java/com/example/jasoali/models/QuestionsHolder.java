package com.example.jasoali.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class QuestionsHolder {
    private String id;
    private String title;
    private String description;
    private String creatorId;
    private String creatorName;
    private ArrayList<Category> categories;
    private ArrayList<Comment> comments;
    private ArrayList<Question> questions;



    public QuestionsHolder(String id, String title, String description, String creatorId, String creatorName, ArrayList<Category> categories, ArrayList<Comment> comments, ArrayList<Question> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.categories = categories;
        this.comments = comments;
        this.questions = questions;
    }


    public QuestionsHolder(String creatorId, String creatorName){
        this.id = String.valueOf((int)(Math.random() * Integer.MAX_VALUE));
        this.title = "";
        this.description = "";
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.categories = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.questions = new ArrayList<>();
    }
    public QuestionsHolder(String id, String creatorId, String creatorName) {
        this.id = id;
        this.creatorId = creatorId;
        this.creatorName = creatorName;
        this.categories = new ArrayList<>();
        this.questions = new ArrayList<>();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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


    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
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

}
