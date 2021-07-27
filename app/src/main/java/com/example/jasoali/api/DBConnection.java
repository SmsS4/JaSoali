package com.example.jasoali.api;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.jasoali.MainActivity;
import com.example.jasoali.exceptions.LengthExceeded;
import com.example.jasoali.models.Category;
import com.example.jasoali.models.CategoryType;
import com.example.jasoali.models.Comment;
import com.example.jasoali.models.FileQuestion;
import com.example.jasoali.models.Question;
import com.example.jasoali.models.QuestionsHolder;
import com.example.jasoali.models.TextQuestion;
import com.example.jasoali.models.User;
import com.example.jasoali.ui.problem.QuestionHolderRecyclerViewAdapter;
import com.example.jasoali.ui.sign_in_up.LoginActivity;
import com.example.jasoali.ui.sign_in_up.RegisterActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {

    final String QUESTION_HOLDERS = "QUESTION_HOLDERS";
    private final Handler handler;


    public DBConnection(Handler handler) {
        this.handler = handler;
    }


    private void sendMessage(int msgId) {
        Message msg = new Message();
        msg.what = msgId;
        handler.sendMessage(msg);
    }


    public QuestionsHolder getWholeQuestionHolderData(String questionHolderId) {

        sendMessage(MainActivity.MyHandler.START_PROGRESS_BAR);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestionHolder");
        ParseObject parseQuestionHolder;
        try {
            parseQuestionHolder = query.get(questionHolderId);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        try {
            parseQuestionHolder.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ParseUser creator = getUserFromParseObject(parseQuestionHolder, "creator");
        QuestionsHolder questionsHolder = new QuestionsHolder(
                parseQuestionHolder.getObjectId(),
                parseQuestionHolder.getString("title"),
                parseQuestionHolder.getString("description"),
                creator.getObjectId(),
                creator.getString("name"),
                getCategoryListFromParseObject(parseQuestionHolder),
                getCommentsListFromParseObject(parseQuestionHolder),
                getQuestionsListFromParseObject(parseQuestionHolder));

        sendMessage(MainActivity.MyHandler.STOP_PROGRESS_BAR);
        return questionsHolder;
    }


    public void getAllQuestionsHolder(QuestionHolderRecyclerViewAdapter adapter) {
        getQuestionsHolderByCategories(new ArrayList<>(), adapter);
    }


    public void getQuestionsHolderByCategories(ArrayList<Category> categories, QuestionHolderRecyclerViewAdapter adapter) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestionHolder");

        for (Category category : categories) {
            if (category.getValue() != null && !category.getValue().equals(""))
                query.whereEqualTo(category.getType().toString().toLowerCase(), category.getValue());
        }

        sendMessage(MainActivity.MyHandler.START_PROGRESS_BAR);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException error) {
                Log.e("FETCH", "2");
                ArrayList<QuestionsHolder> result = new ArrayList<>();
                if (error == null) {
                    List<ParseObject> questionHoldersList = objects;
                    for (ParseObject parseObject : questionHoldersList) {
                        try {
                            parseObject.fetchIfNeeded();
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        ParseUser creator = getUserFromParseObject(parseObject, "creator");
                        result.add(new QuestionsHolder(
                                parseObject.getObjectId(),
                                parseObject.getString("title"),
                                parseObject.getString("description"),
                                creator.getObjectId(),
                                creator.getString("name"),
                                getCategoryListFromParseObject(parseObject)));

                    }
                    Log.e("FETCH", "3" + result.size());
                    adapter.replaceData(result);
                    sendMessage(MainActivity.MyHandler.STOP_PROGRESS_BAR);
                    sendMessage(MainActivity.MyHandler.NOTIFY_SEARCH_RECYCLER_VIEW);
                }
            }
        });
    }


    public void getAllFavouriteQuestionsHolder(QuestionHolderRecyclerViewAdapter adapter) {
        sendMessage(MainActivity.MyHandler.START_PROGRESS_BAR);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteQuestionHolder");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        ArrayList<QuestionsHolder> favoriteQuestionHolders = new ArrayList<>();
        query.findInBackground((parseFavoriteQuestionHolders, e) -> {
            if (e == null) {
                for (ParseObject parseFavoriteQuestionHolder : parseFavoriteQuestionHolders) {
                    try {
                        parseFavoriteQuestionHolder.fetchIfNeeded();
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                    ParseUser creator = getUserFromParseObject(parseFavoriteQuestionHolder, "user");
                    ParseObject questionHolder = parseFavoriteQuestionHolder.getParseObject("questionHolder");
                    try {
                        questionHolder.fetchIfNeeded();
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }
                    favoriteQuestionHolders.add(new QuestionsHolder(
                            questionHolder.getObjectId(),
                            questionHolder.getString("title"),
                            questionHolder.getString("description"),
                            creator.getObjectId(),
                            creator.getString("name"),
                            getCategoryListFromParseObject(questionHolder)));
                }
                adapter.replaceData(favoriteQuestionHolders);
                sendMessage(MainActivity.MyHandler.STOP_PROGRESS_BAR);
                sendMessage(MainActivity.MyHandler.NOTIFY_FAVORITE_RECYCLER_VIEW);
            }
        });
    }


    public void addToFavouriteQuestionsHolders(String questionsHolderId) {
        sendMessage(MainActivity.MyHandler.START_PROGRESS_BAR);

        ParseObject favoriteQuestionHolder = new ParseObject("FavoriteQuestionHolder");
        favoriteQuestionHolder.put("user", ParseUser.getCurrentUser());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestionHolder");
        ParseObject questionHolder = null;
        try {
            // todo: handle in a better way
            questionHolder = query.fromNetwork().get(questionsHolderId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        favoriteQuestionHolder.put("questionHolder", questionHolder);

        favoriteQuestionHolder.saveInBackground(e -> {
            if (e == null) {
                sendMessage(MainActivity.MyHandler.STOP_PROGRESS_BAR);
            }
        });
    }


    public void removeFromFavouriteQuestionsHolder(String questionsHolderId) {
        sendMessage(MainActivity.MyHandler.START_PROGRESS_BAR);

        ParseObject favoriteQuestionHolder = new ParseObject("FavoriteQuestionHolder");
        favoriteQuestionHolder.put("user", ParseUser.getCurrentUser());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestionHolder");
        ParseObject questionHolder = null;
        try {
            // todo: handle in a better way
            questionHolder = query.fromLocalDatastore().get(questionsHolderId);
            if (questionHolder == null) {
                questionHolder = query.fromNetwork().get(questionsHolderId);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        favoriteQuestionHolder.put("questionHolder", questionHolder);

        favoriteQuestionHolder.deleteInBackground(e -> {
            if (e == null) {
                sendMessage(MainActivity.MyHandler.STOP_PROGRESS_BAR);
            }
        });
    }


    public void addQuestionsHolder(QuestionsHolder questionsHolder, ParseObject baseParseQuestionHolder) {
        sendMessage(MainActivity.MyHandler.START_PROGRESS_BAR);

        ParseObject parseObject;
        if (baseParseQuestionHolder != null) {
            parseObject = baseParseQuestionHolder;
        } else {
            parseObject = new ParseObject("QuestionHolder");
        }
        parseObject.put("title", questionsHolder.getTitle());
        parseObject.put("description", questionsHolder.getDescription());
        parseObject.put("creator", ParseUser.getCurrentUser());
        for (Category category : questionsHolder.getCategories()) {
            parseObject.put(category.getType().toString().toLowerCase(), category.getValue());
        }

        ArrayList<ParseObject> parseComments = new ArrayList<>();
        for (Comment comment : questionsHolder.getComments()) {
            parseComments.add(getCommentAsParseObject(parseObject.getObjectId(), comment));
        }
        parseObject.put("comments", parseComments);


        ArrayList<ParseObject> parseQuestions = new ArrayList<>();
        for (Question question : questionsHolder.getQuestions()) {
            parseQuestions.add(getQuestionAsParseObject(parseObject.getObjectId(), question));
        }
        parseObject.put("questions", parseQuestions);

        parseObject.saveInBackground(e -> {
            if (e == null) {
                sendMessage(MainActivity.MyHandler.STOP_PROGRESS_BAR);
                //todo: add notification of creating object
                Log.e("Hoora!", "object saved!");
            } else {
                e.printStackTrace();
            }
        });
    }


    public void editQuestionsHolder(QuestionsHolder newQuestionsHolder) {
        sendMessage(MainActivity.MyHandler.START_PROGRESS_BAR);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestionHolder");
        ParseObject parseQuestionHolder = null;
        try {
            parseQuestionHolder = query.get(newQuestionsHolder.getId());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        parseQuestionHolder = eraseQuestionHolder(parseQuestionHolder);
        addQuestionsHolder(newQuestionsHolder, parseQuestionHolder);
        sendMessage(MainActivity.MyHandler.STOP_PROGRESS_BAR);
    }

    public ParseObject eraseQuestionHolder(ParseObject parseQuestionsHolder) {
        parseQuestionsHolder.remove("title");
        parseQuestionsHolder.remove("description");
        parseQuestionsHolder.remove("creator");
        parseQuestionsHolder.remove("categories");
        parseQuestionsHolder.remove("comments");
        parseQuestionsHolder.remove("questions");

        return parseQuestionsHolder;
    }


    public void addComment(Comment comment) {
        sendMessage(MainActivity.MyHandler.START_PROGRESS_BAR);

        ParseObject parseObject = getCommentAsParseObject(comment.getQuestionsHolderId(), comment);
        try {
            parseObject.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestionHolder");
        ParseObject parseQuestionHolder = null;
        try {
            parseQuestionHolder = query.get(comment.getQuestionsHolderId());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        parseQuestionHolder.add("comments", parseObject);
        try {
            parseQuestionHolder.save();
            sendMessage(MainActivity.MyHandler.STOP_PROGRESS_BAR);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void login(String username, String password, String email) {
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username);
        parseUser.setPassword(password);
        parseUser.setEmail(email);
        ParseUser.logInInBackground(username, password,
                (user, e) -> {
                    if (user != null) {
                        sendMessage(LoginActivity.LOGIN_SUCCESSFUL_RESULT_CODE);
                    } else {
                        Message msg = new Message();
                        msg.what = LoginActivity.LOGIN_FAILED_RESULT_CODE;
                        msg.obj = e.getLocalizedMessage();
                        handler.sendMessage(msg);
                    }
                });
    }


    public void register(User user) {
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(user.getUsername());
        parseUser.setPassword(user.getPassword());
        parseUser.setEmail(user.getEmail());
        parseUser.put("name", user.getName());
        parseUser.put("isAdmin", false);
        parseUser.signUpInBackground(e -> {
            if (e == null) {
                sendMessage(RegisterActivity.REGISTER_SUCCESSFUL_RESULT_CODE);
            } else {
                e.printStackTrace();
                Message msg = new Message();
                msg.what = RegisterActivity.REGISTER_FAILED_RESULT_CODE;
                msg.obj = e.getLocalizedMessage();
                handler.sendMessage(msg);
            }
        });
    }


    public User getLocalUser() {
        ParseUser user = null;
        try {
            user = ParseUser.getCurrentUser();
            if (user != null)
                user = user.fetch();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (user != null) {
            return new User(
                    user.getObjectId(),
                    user.getString("username"),
                    user.getString("password"),
                    user.getString("email"),
                    user.getString("name"),
                    user.getBoolean("isAdmin"));
        }
        return null;
    }


    public void updateName(String userId, String newName) {
        ParseUser user = ParseUser.getCurrentUser();
        user.put("name", newName);
        user.saveInBackground();
    }


    public void adminRequest(String userId) {
        // todo:
    }


    ////////////////////////


    private ParseObject getQuestionAsParseObject(String questionHolderId, Question question) {
        ParseObject parseObject = new ParseObject("Question");
        parseObject.put("title", question.getTitle());
        if (question instanceof TextQuestion) {
            if (((TextQuestion) question).getText() != null) {
                parseObject.put("text", ((TextQuestion) question).getText());
            }
        }
        if (question instanceof FileQuestion) {
            File file = ((FileQuestion) question).getFile();
            if (file != null) {
                int size = (int) file.length();
                byte[] pdfBytes = new byte[size];

                try {
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
                    buf.read(pdfBytes, 0, pdfBytes.length);
                    buf.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Create the ParseFile
                ParseFile parseFile = new ParseFile(file.getName(), pdfBytes);
                parseObject.put("file", parseFile);

                // Upload the file into Parse Cloud
                try {
                    parseFile.save();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return parseObject;
    }


    private ParseObject getCommentAsParseObject(String questionHolderId, Comment comment) {
        ParseObject parseObject = new ParseObject("Comment");
        parseObject.put("maker", ParseUser.getCurrentUser());
        parseObject.put("text", comment.getText());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestionHolder");
        ParseObject questionHolder = null;
        try {
            // todo: handle in a better way
            questionHolder = query.get(questionHolderId);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        parseObject.put("questionHolder", questionHolder);
        return parseObject;
    }


    ////////////////////////


    private ParseUser getUserFromParseObject(ParseObject parseObject, String key) {
        ParseUser user = null;
        try {
            user = parseObject.getParseUser(key).fetchIfNeeded();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }
        return user;
    }


    private ArrayList<Category> getCategoryListFromParseObject(ParseObject parseObject) {
        ArrayList<Category> result = new ArrayList<>();
        for (CategoryType categoryType : CategoryType.values()) {
            String value = parseObject.getString(categoryType.toString().toLowerCase());
            if (value == null) value = "";
            result.add(new Category(categoryType, value));
        }
        return result;
    }


    private ArrayList<Comment> getCommentsListFromParseObject(ParseObject parseObject) {
        ArrayList<Comment> result = new ArrayList<>();
        List<ParseObject> parseComments = parseObject.getList("comments");
        if (parseComments == null) return result;
        for (ParseObject parseComment : parseComments) {
            try {
                parseComment.fetch();
                ParseUser user = parseComment.getParseUser("maker");
                result.add(new Comment(
                        user.getObjectId(),
                        parseComment.getString("text"),
                        user.getString("name"),
                        parseComment.getString("questionsHolder"))
                );
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

    public void logout() {
        /// todo?
        ParseUser.logOut();
    }
}
