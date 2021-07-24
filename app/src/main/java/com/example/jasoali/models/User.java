package com.example.jasoali.models;

public class User {
    private int id;
    private String username;
    private FileData image;
    private String password;
    private String name;
    private boolean isAdmin;

    public User(int id, String username, FileData image, String password, String name, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.image = image;
        this.password = password;
        this.name = name;
        this.isAdmin = isAdmin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
