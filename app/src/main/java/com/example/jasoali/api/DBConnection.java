package com.example.jasoali.api;

import android.util.Log;

import com.example.jasoali.exceptions.LengthExceeded;
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
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestionHolder");
        if (name != null) {
            query.whereContains("title", name);
        }
        for (Category category : categories) {
            query.whereEqualTo(category.getType().toString(), category.getValue());
        }
        ArrayList<QuestionsHolder> result = new ArrayList<>();
        try {
            List<ParseObject> questionHolderList = query.find();
            for (ParseObject parseObject : questionHolderList) {
                ParseUser user = getUserFromParseObject(parseObject, "creator");
                result.add(new QuestionsHolder(
                        parseObject.getString("id"),
                        parseObject.getString("title"),
                        parseObject.getString("description"),
                        user.getObjectId(),
                        user.getString("name"),
                        getCategoryListFromParseObject(parseObject),
                        getCommentsListFromParseObject(parseObject),
                        getQuestionsListFromParseObject(parseObject)
                ));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<QuestionsHolder> getFavouritesQuestionsHolder(String userId) throws NetworkError {
        return new ArrayList<>();
    }

    public void addFavouritesQuestionsHolder(String questionsHolderId, String userId) throws NetworkError {

    }

    public void addQuestionsHolder(QuestionsHolder questionsHolder) {
        ParseObject parseObject = new ParseObject("QuestionHolder");
        parseObject.put("title", questionsHolder.getTitle());
        parseObject.put("description", questionsHolder.getDescription());
        parseObject.put("creator", ParseUser.getCurrentUser());
        for (Category category : questionsHolder.getCategories()) {
            parseObject.put(category.getType().toString().toLowerCase(), category.getValue());
        }
        ArrayList<ParseObject> parseComments = new ArrayList<>();
        for (Comment comment : questionsHolder.getComments()) {
            parseComments.add(getCommentAsParseObject(comment));
        }
        parseObject.put("comments", parseComments);

        ArrayList<ParseObject> parseQuestions = new ArrayList<>();
        for (Question question : questionsHolder.getQuestions()) {
            parseQuestions.add(getQuestionAsParseObject(question));
        }
        parseObject.put("questions", parseQuestions);

        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.e("Hoora!", "object saved!");
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void removeQuestionsHolder(String questionsHolderId) {

    }

    public void editQuestionsHolder(QuestionsHolder newQuestionsHolder) {
        removeQuestionsHolder(newQuestionsHolder.getId());
        addQuestionsHolder(newQuestionsHolder);
    }

    public void addComment(Comment comment) {
        ParseObject parseObject = getCommentAsParseObject(comment);
        try {
            parseObject.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public FileData getFile(String fileId) throws NetworkError {
        /// download and store file
        return null;
    }

    public QuestionsHolder getLocalQuestionsHolder() {
        String currentUserId = ParseUser.getCurrentUser().getObjectId();
        // todo
        ArrayList<Category> categories = new ArrayList<>();
        try {
            categories.add(
                    new Category(CategoryType.COURSE, "شبیه سازی")
            );
            categories.add(
                    new Category(CategoryType.DEPARTMENT, "مهندسی کامپیوتر")
            );
            categories.add(
                    new Category(CategoryType.OTHER, "سوال_سخت")
            );
            categories.add(
                    new Category(CategoryType.OTHER, "مخصوص_خودته")
            );
            categories.add(
                    new Category(CategoryType.TERM, "بهار ۱۴۰۰")
            );
            ArrayList<Question> questions = new ArrayList<>();
            questions.add(
                    new TextQuestion("سوال‌ها یک عنوان طولانی همیشگی", "بلا بلا متن سوال بلا بلا")
            );
//            questions.add(
//                    new FileQuestion("جواب‌ها", null)
//            );
//            questions.add(
//                    new FileQuestion("راهنمایی‌ها", null)
//            );
//            questions.add(
//                    new FileQuestion("جواب‌ها", null)
//            );
//            questions.add(
//                    new FileQuestion("راهنمایی‌ها", null)
//            );
//            questions.add(
//                    new FileQuestion("جواب‌ها", null)
//            );
//            questions.add(
//                    new FileQuestion("راهنمایی‌ها", null)
//            );
            ArrayList<Comment> comments = new ArrayList<>();
            comments.add(
                    new Comment(currentUserId, "عالی و طلایی", "ممد")
            );
            comments.add(
                    new Comment(currentUserId, "ماورای تصور", "یک پدیده")
            );
            return new QuestionsHolder(
                    "0",
                    "سلام سوال",
                    "این یک توضیحات تستی برای یک تست دستی است.",
                    currentUserId,
                    "آقا صادق",
                    categories,
                    comments,
                    questions
            );
        } catch (LengthExceeded le) {
            return null;
        }

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

    private ParseObject getQuestionAsParseObject(Question question) {
        ParseObject parseObject = new ParseObject("Question");
        parseObject.put("title", question.getTitle());
        if (question instanceof TextQuestion) {
            parseObject.put("text", ((TextQuestion) question).getText());
        }
        if (question instanceof FileQuestion) {
            parseObject.put("file", ((FileQuestion) question).getFile());
        }
        return parseObject;
    }

    private ParseObject getCommentAsParseObject(Comment comment) {
        ParseObject parseObject = new ParseObject("Comment");
        parseObject.put("maker", ParseUser.getCurrentUser());
        parseObject.put("text", comment.getText());
        return parseObject;
    }


    ////////////////////////

    private ArrayList<Category> getCategoryListFromParseObject(ParseObject parseObject) {
        ArrayList<Category> result = new ArrayList<>();
        for (CategoryType categoryType : CategoryType.values()) {
            result.add(new Category(categoryType, parseObject.getString(categoryType.toString().toLowerCase())));
        }
        return result;
    }

    private ParseUser getUserFromParseObject(ParseObject parseObject, String key) {
        ParseUser user = null;
        try {
            user = parseObject.getParseUser(key).fetchIfNeeded();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return user;
    }

    private ArrayList<Comment> getCommentsListFromParseObject(ParseObject parseObject) {
        ArrayList<Comment> result = new ArrayList<>();
        List<ParseObject> parseComments = parseObject.getList("comments");
        for (ParseObject parseComment : parseComments) {
            try {
                parseComment.fetch();
                ParseUser user = parseComment.getParseUser("maker");
                result.add(new Comment(
                        user.getObjectId(),
                        parseComment.getString("text"),
                        user.getString("name")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private ArrayList<Question> getQuestionsListFromParseObject(ParseObject parseObject) {
        ArrayList<Question> result = new ArrayList<>();
        List<ParseObject> parseQuestions = parseObject.getList("questions");
        for (ParseObject parseQuestion : parseQuestions) {
            try {
                parseQuestion.fetch();
                ParseFile parseFile = parseQuestion.getParseFile("file");
                String text = parseQuestion.getString("text");
                if (parseFile != null) {
                    result.add(new FileQuestion(parseQuestion.getString("title"), parseFile.getFile()));
                } else if (text != null) {
                    result.add(new TextQuestion(parseQuestion.getString("title"), text));
                }
            } catch (LengthExceeded | ParseException exception) {
                exception.printStackTrace();
            }
        }
        return result;
    }

}
