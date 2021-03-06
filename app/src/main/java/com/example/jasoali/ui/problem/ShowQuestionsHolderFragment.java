package com.example.jasoali.ui.problem;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasoali.MainActivity;
import com.example.jasoali.R;
import com.example.jasoali.api.DBConnection;
import com.example.jasoali.custom_packacges.RoundedBackgroundSpan;
import com.example.jasoali.exceptions.LengthExceeded;
import com.example.jasoali.models.Category;
import com.example.jasoali.models.CategoryType;
import com.example.jasoali.models.Comment;
import com.example.jasoali.models.FileQuestion;
import com.example.jasoali.models.Question;
import com.example.jasoali.models.QuestionsHolder;
import com.example.jasoali.models.TextQuestion;
import com.example.jasoali.models.User;
import com.example.jasoali.ui.questionsholder.CommentsAdapter;
import com.example.jasoali.ui.questionsholder.QuestionsAdapter;
import com.example.jasoali.ui.questionsholder.TagsAdapter;
import com.example.jasoali.utils.KeyboardHider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class ShowQuestionsHolderFragment extends Fragment {

    public static final String QUESTIONS_HOLDER_ID = "questionsHolderId";
    public static final String ADD_QUESTIONS_ID = "-1";
    private static final int PICKFILE_RESULT_CODE = -1;
    private final DBConnection db = new DBConnection(MainActivity.getHandler());
    private final Context context;
    boolean addQuestionMode;
    boolean added = false;
    File source;
    private QuestionsHolder questionsHolder;
    private User user;
    private EditText title;
    private EditText description;
    private EditText tags;
    private EditText newComment;
    private Button editButton;
    private Button submitComment;
    private RecyclerView questionsRecyclerView;
    private RecyclerView recyclerViewComments;
    private RecyclerView recyclerViewTags;
    private View view;
    private QuestionsAdapter questionsAdapter;
    private TagsAdapter tagsAdapter;
    private ImageView favButton;
    private ScrollView scrollView;
    private LinearLayout comemntSection;
    private boolean editModeActive = false;
    String questionsHolderId;
    private HandlerThread mHandlerThread;
    FetchHandler fetchHandler;

    public ShowQuestionsHolderFragment(Context context) {
        mHandlerThread = new HandlerThread("FetchHandlerThread");
        mHandlerThread.start();
        fetchHandler = new FetchHandler(this, mHandlerThread);
        this.context = context;
    }


    public static ShowQuestionsHolderFragment newInstance(String questionsHolderId, Context context) {
        ShowQuestionsHolderFragment fragment = new ShowQuestionsHolderFragment(context);
        Bundle args = new Bundle();
        args.putString(QUESTIONS_HOLDER_ID, questionsHolderId);
        fragment.setArguments(args);
        return fragment;
    }

    public static ShowQuestionsHolderFragment newAddQuestionsInstance(Context context) {
        return newInstance(ADD_QUESTIONS_ID, context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = db.getLocalUser();
            questionsHolderId = getArguments().getString(QUESTIONS_HOLDER_ID);

        }
    }

    private void setEditButtonToEdit() {
        editButton.setOnClickListener(v -> enterEditMode());
    }

    private void showTextQuestion(TextQuestion textQuestion) {
        ShowQuestionTextFragment fragInfo = ShowQuestionTextFragment.newInstance(textQuestion);
        MainActivity activity = (MainActivity) this.context;
        activity.addInfoFragment(fragInfo);
    }

    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = view.getContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void showFileQuestion(FileQuestion fileQuestion) {
        Uri path = Uri.fromFile(fileQuestion.getFile());
        Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
        pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfOpenintent.setDataAndType(path, getMimeType(path));
        try {
            startActivity(pdfOpenintent);
        } catch (ActivityNotFoundException e) {

        }
    }

    private void changeMode(boolean show) {
        title.setEnabled(show);
        description.setEnabled(show);
        tags.setEnabled(show);
    }

    @NotNull
    private ArrayList<Category> getEditedTags() {
        ArrayList<Category> result = new ArrayList<>();
        String tags_string = tags.getText().toString();
        for (String tag : tags_string.split("[ \n]")) {
            result.add(new Category(CategoryType.OTHER, tag));
        }
        for (TagsAdapter.ViewHolder viewHolder : tagsAdapter.getViews()) {
            if (!viewHolder.getValueTextView().getText().toString().equals("")) {
                result.add(
                        new Category(
                                Category.getCategoryByType(viewHolder.getTypeSpinner().getSelectedItem().toString()),
                                viewHolder.getValueTextView().getText().toString()
                        )
                );
            }
        }
        return result;
    }

    @NotNull
    private ArrayList<Question> getEditedQuestions() {
        ArrayList<Question> result = new ArrayList<>(questionsAdapter.getQuestions());
        result.remove(result.size() - 1);
        return result;

    }

    private QuestionsHolder getEditedQuestionsHolder() {

        return new QuestionsHolder(
                questionsHolder.getId(),
                title.getText().toString(),
                description.getText().toString(),
                questionsHolder.getCreatorId(),
                questionsHolder.getCreatorName(),
                getEditedTags(),
                questionsHolder.getComments(),
                getEditedQuestions()
        );
    }


    private void enterEditMode() {
        editModeActive = true;
        tagsAdapter.enterEditMode();
        editButton.setText("??????????");
        editButton.setOnClickListener(v -> new MaterialAlertDialogBuilder(view.getContext())
                .setPositiveButton("??????????", (dialogInterface, i) -> {
                    tagsAdapter.exitEditMode();
                    if (addQuestionMode && !added) {
                        added = true;
                        QuestionsHolder addedQuestionsHolder = getEditedQuestionsHolder();
                        db.addQuestionsHolder(
                                addedQuestionsHolder, null
                        );
                        questionsHolder = addedQuestionsHolder;
                        ((MainActivity) context).showSearchFragment();
                    } else {
                        QuestionsHolder editedQuestionsHolder = getEditedQuestionsHolder();
                        db.editQuestionsHolder(
                                editedQuestionsHolder
                        );
                        questionsHolder = editedQuestionsHolder;
                    }
//

                    exitEditMode();
                })
                .setNegativeButton("??????", (dialogInterface, i) -> {
                    tagsAdapter.exitEditMode();
                    exitEditMode();
                }).setNeutralButton("?????????? ????????????", null).show());
        changeMode(true);
        questionsAdapter.changeButtonVisibility(View.VISIBLE);
        questionsAdapter.enterEditMode();

    }

    private void exitEditMode() {
        editModeActive = false;
        showQuestionsHolder();
        editButton.setText("????????????");
        setEditButtonToEdit();
        changeMode(false);
        questionsAdapter.changeButtonVisibility(View.INVISIBLE);
        tagsAdapter.exitEditMode();
    }


    private void showQuestions() {
        questionsAdapter = new QuestionsAdapter(questionsHolder.getQuestions()) {

            @Override
            public void onQuestionClicked(Question question) {
                if (editModeActive) {
                    return;
                }
                if (question instanceof TextQuestion) {
                    showTextQuestion((TextQuestion) question);
                } else {
                    showFileQuestion((FileQuestion) question);
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void selectFile() {
                qhSelectFile();
            }

            @Override
            public TextQuestion submitText(String title, String text) {
                try {
                    return new TextQuestion(title, text);
                } catch (LengthExceeded e) {
                    /// this won't happen
                    return null;
                }

            }

            @Override
            public FileQuestion submitFile(String title) {
                if (source == null) {
                    System.out.println("hmm nabyad intor mishoda");
                    return null;
                }
                try {
                    return new FileQuestion(title, source);
                } catch (LengthExceeded e) {
                    /// this won't happen
                    System.out.println("sssssss");
                    return null;
                }
            }
        };
        questionsRecyclerView.setAdapter(questionsAdapter);
    }


    public void qhSelectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*|application/pdf");
        startActivityForResult(intent, PICKFILE_RESULT_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Uri content_describer = data.getData();
            String src = content_describer.getPath();
            source = new File(src);
        }
    }

    private void showComments() {
        recyclerViewComments.setAdapter(
                new CommentsAdapter(questionsHolder.getComments())
        );
    }

    private ArrayList<Category> getTags(boolean special) {
        ArrayList<Category> result = new ArrayList<>();
        for (Category category : questionsHolder.getCategories()) {
            if (special ^ (category.getType() == CategoryType.OTHER)) {
                result.add(category);
            }
        }
        return result;
    }

    private ArrayList<Category> getSpecialTags() {
        return getTags(true);
    }

    private ArrayList<Category> getOtherTags() {
        return getTags(false);
    }

    private void showTags() {
        TagsAdapter tagsAdapter = new TagsAdapter(getSpecialTags());
        recyclerViewTags.setAdapter(tagsAdapter);
        this.tagsAdapter = tagsAdapter;
    }


    private void initQuestionsHolder() {
        favButton.setClickable(true);
        favButton.bringToFront();
        favButton.setOnClickListener(v -> {
            System.out.println("hey");
            db.addToFavouriteQuestionsHolders(questionsHolder.getId());
            Toast.makeText(view.getContext(),
                    view.getResources().getString(R.string.added_to_favs),
                    Toast.LENGTH_SHORT
            ).show();
        });
        questionsRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        recyclerViewComments.setLayoutManager(new LinearLayoutManager(view.getContext()));

        recyclerViewTags.setLayoutManager(new LinearLayoutManager(view.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        submitComment.setOnClickListener(v -> {
            Comment comment = new Comment(
                    user.getId(),
                    newComment.getText().toString(),
                    user.getName(),
                    questionsHolder.getId()
            );
            questionsHolder.getComments().add(0, comment);
            System.out.println(questionsHolder.getId());
            db.addComment(comment);
            showComments();
            newComment.setText("");
            KeyboardHider.HideKeyboard(view);
        });

    }

    private void colorFulTags() {
        colorFulTags(getOtherTags());
    }

    private void colorFulTags(ArrayList<Category> source) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        int tagStart = 0;
        for (Category category : source) {
            if (category.getType() != CategoryType.OTHER) {
                continue;
            }
            stringBuilder.append(category.getValue());
            stringBuilder.append(" ");
            RoundedBackgroundSpan tagSpan = new RoundedBackgroundSpan(
                    ContextCompat.getColor(view.getContext(), RoundedBackgroundSpan.getNextColor()),
                    ContextCompat.getColor(view.getContext(), R.color.white)
            );
            stringBuilder.setSpan(tagSpan, tagStart, tagStart + category.getValue().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tagStart += category.getValue().length() + 1;
        }
        tags.setText(stringBuilder);

    }


    private void showOtherTags() {
        tags.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                colorFulTags(getEditedTags());
            }
        });
        colorFulTags();

    }

    private void showQuestionsHolder() {
        RoundedBackgroundSpan.resetColors();
        showQuestions();
        showComments();
        showTags();
        showOtherTags();
        title.setText(questionsHolder.getTitle());
        description.setText(questionsHolder.getDescription());
    }

    private void findViews() {
        favButton = view.findViewById(R.id.favButton);
        questionsRecyclerView = view.findViewById(R.id.questions);
        recyclerViewComments = view.findViewById(R.id.comments);
        recyclerViewTags = view.findViewById(R.id.special_tags);
        title = view.findViewById(R.id.titleQH);
        submitComment = view.findViewById(R.id.submit_comment);
        newComment = view.findViewById(R.id.new_comment);
        description = view.findViewById(R.id.descriptionQH);
        editButton = view.findViewById(R.id.edit_btn);
        tags = view.findViewById(R.id.tagQH);
        scrollView = view.findViewById(R.id.mainScroller);
        comemntSection = view.findViewById(R.id.comemnt_section);

    }

    private void start() {
        initQuestionsHolder();
        showQuestionsHolder();
        setEditButtonToEdit();
        changeMode(addQuestionMode);
        if (addQuestionMode) {
            comemntSection.setVisibility(View.GONE);
            favButton.setVisibility(View.GONE);
            editModeActive = true;
            enterEditMode();
        }
        if (!user.isAdmin()) {
            editButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_show_questions_holder, container, false);
        findViews();

        fetchHandler.sendEmptyMessage(FetchHandler.START_FETCH_USER);
        return view;
    }


    public static class ShowHandler extends Handler {

        public static final int SHOW = 2;
        private final WeakReference<ShowQuestionsHolderFragment> fragmentWeakReference;

        public ShowHandler(ShowQuestionsHolderFragment fragment) {
            super();
            this.fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            Log.e("HANDLER", String.valueOf(msg.what));
            ShowQuestionsHolderFragment fragment = fragmentWeakReference.get();

            if (fragment == null)
                return;

            if (msg.what == SHOW) {
                fragment.scrollView.setVisibility(View.VISIBLE);
                fragment.start();
            }
        }
    }

    public static class FetchHandler extends Handler {

        public static final int START_FETCH_USER = 1;
        private final WeakReference<ShowQuestionsHolderFragment> fragmentWeakReference;
        private ShowQuestionsHolderFragment.ShowHandler showHandler;

        public FetchHandler(ShowQuestionsHolderFragment fragment, HandlerThread handlerLoop) {
            super(handlerLoop.getLooper());
            this.fragmentWeakReference = new WeakReference<>(fragment);
            this.showHandler = new ShowQuestionsHolderFragment.ShowHandler(fragment);

        }

        @Override
        public void handleMessage(Message msg) {
            Log.e("HANDLER", String.valueOf(msg.what));
            ShowQuestionsHolderFragment fragment = fragmentWeakReference.get();
            if (fragment == null)
                return;
            if (msg.what == START_FETCH_USER) {
                if (!fragment.questionsHolderId.equals(ADD_QUESTIONS_ID)) {
                    fragment.questionsHolder = fragment.db.getWholeQuestionHolderData(fragment.questionsHolderId);
                    fragment.addQuestionMode = false;
                } else {
                    fragment.questionsHolder = new QuestionsHolder(fragment.user.getId(), fragment.user.getName());
                    fragment.addQuestionMode = true;
                }

                this.showHandler.sendEmptyMessage(ShowHandler.SHOW);
            }
        }
    }
}