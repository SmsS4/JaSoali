package com.example.jasoali.models;

import com.example.jasoali.exceptions.LengthExceeded;

import java.io.File;

public class FileQuestion extends Question {
    private File file;

    public FileQuestion(String description, File file) throws LengthExceeded {
        super(description);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
