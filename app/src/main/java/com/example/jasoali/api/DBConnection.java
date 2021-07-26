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
        query.findInBackground((questionHolderList, e) -> {
            if (e == null) {
                for (ParseObject parseObject : questionHolderList) {
                    ParseUser user = getUserFromParseObject(parseObject);
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
                Log.d("score", "Retrieved " + questionHolderList.size() + " scores");
            } else {
                Log.d("score", "Error: " + e.getMessage());
            }
        });
        /// returns list of questions that has this categories
        // and has name in title (if name is not null)
        return result;
    }

    public ArrayList<QuestionsHolder> getFavouritesQuestionsHolder(String userId) throws NetworkError {
        return new ArrayList<>();
    }

    public void addFavouritesQuestionsHolder(String questionsHolderId, String userId) throws NetworkError {

    }

    public void addQuestionsHolder(QuestionsHolder questionsHolder) {
        QuestionsHolder qh;
        ParseObject parseObject = new ParseObject("QuestionHolder");

    }

    public void removeQuestionsHolder(String questionsHolderId) {

    }

    public void editQuestionsHolder(QuestionsHolder newQuestionsHolder) {
        removeQuestionsHolder(newQuestionsHolder.getId());
        addQuestionsHolder(newQuestionsHolder);
    }

    public void addComment(String questionsHolderId, Comment comment) {

    }

    public FileData getFile(String fileId) throws NetworkError {
        /// download and store file
        return null;
    }

    public QuestionsHolder getLocalQuestionsHolder(String questionsHolderId) {
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
            questions.add(
                    new FileQuestion("جواب‌ها", null)
            );
            questions.add(
                    new FileQuestion("راهنمایی‌ها", null)
            );
            questions.add(
                    new FileQuestion("جواب‌ها", null)
            );
            questions.add(
                    new FileQuestion("راهنمایی‌ها", null)
            );
            questions.add(
                    new FileQuestion("جواب‌ها", null)
            );
            questions.add(
                    new FileQuestion("راهنمایی‌ها", null)
            );
            ArrayList<Comment> comments = new ArrayList<>();
            comments.add(
                    new Comment(currentUserId, questionsHolderId, "عالی و طلایی", "ممد")
            );
            comments.add(
                    new Comment(currentUserId, questionsHolderId, "ماورای تصور", "یک پدیده")
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
            parseObject.put("file", ((FileQuestion) question).getFile()); //todo: clean FileData
        }
        return parseObject;
    }

    private ParseObject getCommentAsParseObject(Comment comment) {
        ParseObject parseObject = new ParseObject("Comment");
        parseObject.put("makerId", comment.getMakerId());
        parseObject.put("questionHolderId", comment.getQuestionHolderId());
        parseObject.put("text", comment.getText());
        return parseObject;
    }


    private ArrayList<Category> getCategoryListFromParseObject(ParseObject parseObject) {
        ArrayList<Category> result = new ArrayList<>();
        for (CategoryType categoryType : CategoryType.values()) {
            result.add(new Category(categoryType, parseObject.getString(categoryType.toString())));
        }
        return result;
    }

    private ParseUser getUserFromParseObject(ParseObject parseObject) {
        ParseUser user = null;
        try {
            user = parseObject.getParseUser("creator").fetch();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return user;
    }

    private ArrayList<Comment> getCommentsListFromParseObject(ParseObject parseObject) {
        ArrayList<Comment> result = new ArrayList<>();
        List<ParseObject> parseComments = parseObject.getList("comments");
        if (parseComments == null) return result;
        Log.e("EMPTY", "SALAM!");
        for (ParseObject parseComment : parseComments) {
            ParseUser user;
            ParseUser questionHolder;
            try {
                user = parseComment.getParseUser("maker").fetch();
                questionHolder = parseComment.getParseObject("questionHolder").fetch();
                result.add(new Comment(
                        user.getObjectId(),
                        questionHolder.getObjectId(),
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
        if (parseQuestions == null) return result;
        Log.e("EMPTY", "SALAM!");
        for (ParseObject parseQuestion : parseQuestions) {
            ParseFile parseFile = parseQuestion.getParseFile("file");
            String text = parseQuestion.getString("text");
            try {
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
