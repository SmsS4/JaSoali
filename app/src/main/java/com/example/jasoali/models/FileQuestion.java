package com.example.jasoali.models;

public class FileQuestion extends Question {
    private FileData file;

    public FileQuestion(String description, FileData file) {
        super(description);
        this.file = file;
    }

    public FileData getFile() {
        return file;
    }

    public void setFile(FileData file) {
        this.file = file;
    }

}
