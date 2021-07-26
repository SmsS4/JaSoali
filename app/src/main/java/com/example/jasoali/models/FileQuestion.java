package com.example.jasoali.models;

import com.example.jasoali.exceptions.LengthExceeded;

import java.io.File;

public class FileQuestion extends Question {
    private File file;

    public FileQuestion(String title, File file) throws LengthExceeded {
        super(title);
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
