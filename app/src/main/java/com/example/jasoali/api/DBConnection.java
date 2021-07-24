package com.example.jasoali.api;

import com.example.jasoali.exceptions.AuthenticationFailed;
import com.example.jasoali.exceptions.NetworkError;
import com.example.jasoali.exceptions.RegisterFailed;
import com.example.jasoali.models.Category;
import com.example.jasoali.models.CategoryType;
import com.example.jasoali.models.Comment;
import com.example.jasoali.models.FileData;
import com.example.jasoali.models.FileQuestion;
import com.example.jasoali.models.Question;
import com.example.jasoali.models.QuestionsHolder;
import com.example.jasoali.models.TextQuestion;
import com.example.jasoali.models.User;

import java.util.ArrayList;
import java.util.Date;

public class DBConnection {
    static private DBConnection instance = new DBConnection();
    static private User user = null;

    private DBConnection() {

    }

    static public DBConnection getInstance() {
        return DBConnection.instance;
    }

    public User login(String username, String password) throws AuthenticationFailed, NetworkError {
        throw new AuthenticationFailed();
    }

    public void register(User user) throws RegisterFailed, NetworkError {
        throw new RegisterFailed("reason");
    }

    public ArrayList<QuestionsHolder> getQuestionsHolderByCategories(String name, ArrayList<Category> categories) throws NetworkError {
        /// returns list of questions that has this categories
        // and has name in title (if name is not null)
        return null;
    }

    public ArrayList<QuestionsHolder> getAllQuestionsHolder() throws NetworkError {
        return getQuestionsHolderByCategories(null, new ArrayList<>());
    }

    public ArrayList<QuestionsHolder> getFavouritesQuestionsHolder(int userId) throws NetworkError {
        return null;
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

    public QuestionsHolder getLocalQuestionsHolder(int questionsHolderId){
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

    public User getLocalUser() {
        // todo
        return new User(0, "demol", null, "ramz", "SeyedMahdi SadeghShobeiri", true);
    }

}
