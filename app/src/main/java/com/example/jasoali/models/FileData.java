package com.example.jasoali.models;

public class FileData {
    private String id;
    private String path;
    private String mime;

    public FileData(String id, String path, String mime) {
        this.id = id;
        this.path = path;
        this.mime = mime;
    }

    public FileData(String id, String mime) {
        this.id = id;
        this.path = null;
        this.mime = mime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
