package com.example.jasoali.models;

import com.example.jasoali.exceptions.LengthExceeded;

public class FileQuestion extends Question {
    private FileData file;

    public FileQuestion(String description, FileData file) throws LengthExceeded {
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
