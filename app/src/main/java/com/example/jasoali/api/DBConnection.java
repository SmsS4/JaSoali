package com.example.jasoali.api;

import android.util.Log;

import com.example.jasoali.exceptions.NetworkError;
import com.example.jasoali.models.Category;
import com.example.jasoali.models.CategoryType;
import com.example.jasoali.models.Comment;
import com.example.jasoali.models.FileData;
import com.example.jasoali.models.FileQuestion;
import com.example.jasoali.models.Question;
import com.example.jasoali.models.QuestionsHolder;
import com.example.jasoali.models.TextQuestion;
import com.example.jasoali.models.User;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    static private DBConnection instance = new DBConnection();
    static private User user = null;

    private DBConnection() {
    }

    static public DBConnection getInstance() {
        return DBConnection.instance;
    }


    public ArrayList<QuestionsHolder> getAllQuestionsHolder() {
        return getQuestionsHolderByCategories(null, new ArrayList<>());
    }

    public ArrayList<QuestionsHolder> getQuestionsHolderByCategories(String name, ArrayList<Category> categories) {
        ArrayList<QuestionsHolder> result = new ArrayList<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestionHolder");
        if (name != null) {
            query.whereContains("title", name);
        }
        for (Category category : categories) {
            query.whereEqualTo(category.getType().toString(), category.getValue());
        }
        query.findInBackground((questionHolderList, e) -> {
            if (e == null) {
//                for(ParseObject parseObject:questionHolderList){
//                    result.add(new QuestionsHolder(pa));
//                }
//                result.addAll(questionHolderList);
                Log.d("score", "Retrieved " + questionHolderList.size() + " scores");
            } else {
                Log.d("score", "Error: " + e.getMessage());
            }
        });
        /// returns list of questions that has this categories
        // and has name in title (if name is not null)
        return result;
    }

    public ArrayList<QuestionsHolder> getFavouritesQuestionsHolder(int userId) throws NetworkError {
        return new ArrayList<>();
    }

    public void addFavouritesQuestionsHolder(int questionsHolderId, int userId) throws NetworkError {

    }

    public void addQuestionsHolder(QuestionsHolder questionsHolder) throws NetworkError {

    }

    public void removeQuestionsHolder(int questionsHolderId) throws NetworkError {

    }

    public void editQuestionsHolder(QuestionsHolder newQuestionsHolder) throws NetworkError {
        removeQuestionsHolder(newQuestionsHolder.getId());
        addQuestionsHolder(newQuestionsHolder);
    }

    public void addComment(int questionsHolderId, Comment comment) {

    }

    public FileData getFile(int fileId) throws NetworkError {
        /// download and store file
        return null;
    }

    public QuestionsHolder getLocalQuestionsHolder(int questionsHolderId) {
        // todo
        ArrayList<Category> categories = new ArrayList<>();
        categories.add(
                new Category(CategoryType.COURSE, "FolanCourse")
        );
        categories.add(
                new Category(CategoryType.DEPARTMENT, "CE")
        );
        ArrayList<Question> questions = new ArrayList<>();
        questions.add(
                new TextQuestion("soal ha", "matne soal")
        );
        questions.add(
                new FileQuestion("javab ha", new FileData(0, null, "image/jpeg"))
        );
        ArrayList<Comment> comments = new ArrayList<>();
        comments.add(
                new Comment(0, "folan", "mmd")
        );
        comments.add(
                new Comment(1, "bahman", "gholam")
        );
        return new QuestionsHolder(
                0,
                "first QH",
                "blah blah blah",
                0,
                "SeyedMahdi SadeghShobeiri",
                categories,
                comments,
                questions
        );
    }

    public void login(String username, String password, String email) {
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username);
        parseUser.setPassword(password);
        parseUser.setEmail(email);
        ParseUser.logInInBackground(username, password,
                new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            // Hooray! The user is logged in.
                        } else {
                            // Signup failed. Look at the ParseException to see what happened.
                        }
                    }
                });
    }

    public void register(User user) {
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(user.getUsername());
        parseUser.setPassword(user.getPassword());
        parseUser.setEmail(user.getEmail());
        parseUser.put("name", user.getName());
        parseUser.put("isAdmin", user.isAdmin());
        parseUser.put("image", null); //todo: add image
        try {
            parseUser.signUp();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public User getLocalUser() {
        ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            return new User(
                    user.getObjectId(),
                    user.getString("username"),
                    user.getString("password"),
                    user.getString("email"),
                    user.getString("name"),
                    null,// todo: handle image
                    user.getBoolean("isAdmin"));
        }
        return null;
    }

}
