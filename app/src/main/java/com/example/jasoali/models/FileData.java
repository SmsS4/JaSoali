package com.example.jasoali.models;

public class FileData {
    private int id;
    private String path;
    private String mime;

    public FileData(int id, String path, String mime) {
        this.id = id;
        this.path = path;
        this.mime = mime;
    }

    public FileData(int id, String mime) {
        this.id = id;
        this.path = null;
        this.mime = mime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

}
