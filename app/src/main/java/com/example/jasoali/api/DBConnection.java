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
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

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
        query.fromLocalDatastore().findInBackground().continueWithTask((task) -> {
            Log.e("FETCH", "2");
            ParseException error = (ParseException) task.getError();
            ArrayList<QuestionsHolder> result = new ArrayList<>();
            if (error == null) {
                List<ParseObject> questionHoldersList = task.getResult();
                for (ParseObject parseObject : questionHoldersList) {
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
                sendMessage(MainActivity.MyHandler.NOTIFY_RECYCLER_VIEW);
            }
            return query.fromNetwork().findInBackground();
        }).continueWithTask((task -> {
            Log.e("FETCH", "4");
            ParseException error = (ParseException) task.getError();
            ArrayList<QuestionsHolder> result = new ArrayList<>();
            if (error == null) {
                List<ParseObject> questionHoldersList = task.getResult();
                for (ParseObject parseObject : questionHoldersList) {
                    ParseUser creator = getUserFromParseObject(parseObject, "creator");
                    result.add(new QuestionsHolder(
                            parseObject.getObjectId(),
                            parseObject.getString("title"),
                            parseObject.getString("description"),
                            creator.getObjectId(),
                            creator.getString("name"),
                            getCategoryListFromParseObject(parseObject)));
                }
                ParseObject.unpinAllInBackground(questionHoldersList, e -> { // todo: cache in a better way
                    if (e == null) {
                        // Add the latest results for this query to the cache.
                        ParseObject.pinAllInBackground(QUESTION_HOLDERS, questionHoldersList);
                    }
                });
                Log.e("FETCH", "5" + result.size());
                adapter.replaceData(result);
                sendMessage(MainActivity.MyHandler.STOP_PROGRESS_BAR);
                sendMessage(MainActivity.MyHandler.NOTIFY_RECYCLER_VIEW);
            }
            return task;
        }));
    }


    public void getFavouriteQuestionsHolder(QuestionHolderRecyclerViewAdapter adapter) {
        sendMessage(MainActivity.MyHandler.START_PROGRESS_BAR);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("FavoriteQuestionHolder");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        ArrayList<QuestionsHolder> favoriteQuestionHolders = new ArrayList<>();
        query.findInBackground((parseFavoriteQuestionHolders, e) -> {
            for (ParseObject parseFavoriteQuestionHolder : parseFavoriteQuestionHolders) {
                ParseUser creator = getUserFromParseObject(parseFavoriteQuestionHolder, "creator");
                favoriteQuestionHolders.add(new QuestionsHolder(
                        parseFavoriteQuestionHolder.getObjectId(),
                        parseFavoriteQuestionHolder.getString("title"),
                        parseFavoriteQuestionHolder.getString("description"),
                        creator.getObjectId(),
                        creator.getString("name"),
                        getCategoryListFromParseObject(parseFavoriteQuestionHolder)));
            }
            adapter.replaceData(favoriteQuestionHolders);
            sendMessage(MainActivity.MyHandler.STOP_PROGRESS_BAR);
            sendMessage(MainActivity.MyHandler.NOTIFY_RECYCLER_VIEW);
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
            questionHolder = query.fromLocalDatastore().get(questionsHolderId);
            if (questionHolder == null) {
                questionHolder = query.fromNetwork().get(questionsHolderId);
            }
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


    public void addQuestionsHolder(QuestionsHolder questionsHolder) {
        ParseObject parseObject = new ParseObject("QuestionHolder");
        parseObject.put("title", questionsHolder.getTitle());
        parseObject.put("description", questionsHolder.getDescription());
        parseObject.put("creator", ParseUser.getCurrentUser());
        for (Category category : questionsHolder.getCategories()) {
            parseObject.put(category.getType().toString().toLowerCase(), category.getValue());
        }

        ArrayList<ParseObject> parseQuestions = new ArrayList<>();
        for (Question question : questionsHolder.getQuestions()) {
            parseQuestions.add(getQuestionAsParseObject(question));
        }
        parseObject.put("questions", parseQuestions);

        parseObject.saveInBackground(e -> {
            if (e == null) {
                //todo: add notification of creating object
                Log.e("Hoora!", "object saved!");
            } else {
                e.printStackTrace();
            }
        });
    }


    public void editQuestionsHolder(QuestionsHolder newQuestionsHolder) {
        removeQuestionsHolder(newQuestionsHolder.getId());
        addQuestionsHolder(newQuestionsHolder);
    }


    public void removeQuestionsHolder(String questionsHolderId) {
        sendMessage(MainActivity.MyHandler.START_PROGRESS_BAR);
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

        questionHolder.deleteInBackground(e -> {
            //todo: add notification of deleting object
            sendMessage(MainActivity.MyHandler.STOP_PROGRESS_BAR);
        });
    }


    public void addComment(Comment comment) {
        sendMessage(MainActivity.MyHandler.START_PROGRESS_BAR);

        ParseObject parseObject = getCommentAsParseObject(comment);
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

        sendMessage(MainActivity.MyHandler.STOP_PROGRESS_BAR);
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
        /// this method should update name of user in db
        /// optional: update comments name too XD
        // todo:
    }


    public void adminRequest(String userId) {
        // todo:
    }


    ////////////////////////


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

        ParseQuery<ParseObject> query = ParseQuery.getQuery("QuestionHolder");
        ParseObject questionHolder = null;
        try {
            // todo: handle in a better way
            questionHolder = query.fromLocalDatastore().get(comment.getQuestionsHolderId());
            if (questionHolder == null) {
                questionHolder = query.fromNetwork().get(comment.getQuestionsHolderId());
            }
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

}
