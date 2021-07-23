package com.example.jasoali.api;

import com.example.jasoali.models.FileData;
import com.example.jasoali.models.QuestionsHolder;
import com.example.jasoali.models.User;

import java.util.ArrayList;

public class DBConnection {
    static private DBConnection instance = new DBConnection();
    static private User user = null;

    private DBConnection() {

    }

    public DBConnection getInstance() {
        return DBConnection.instance;
    }

    public User login(String username, String password) {
        return null;
    }

    public String register(User user) {
        return null;
    }

    public ArrayList<QuestionsHolder> getAllQuestions() {
        return null;
    }

    public FileData getFile(int fileId) {
        /// download and store file
        return null;
    }
}
