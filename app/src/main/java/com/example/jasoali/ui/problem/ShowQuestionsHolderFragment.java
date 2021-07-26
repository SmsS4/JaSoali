package com.example.jasoali.ui.problem;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
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
import java.util.ArrayList;


public class ShowQuestionsHolderFragment extends Fragment {

    public static final String QUESTIONS_HOLDER_ID = "questionsHolderId";
    public static final int ADD_QUESTIONS_ID = -1;
    private static final int PICKFILE_RESULT_CODE = -1;
    private static final String[] mimes = new String[]{"image/*", "application/pdf"};
    private final DBConnection db = new DBConnection(MainActivity.getHandler());
    boolean addQuestionMode;
    boolean added = false;
    File source;
    private QuestionsHolder questionsHolder;
    private User user;

    private EditText title;
    private EditText description;
    private EditText tags;
    private Button editButton;
    private RecyclerView questionsRecyclerView;
    private RecyclerView recyclerViewComments;
    private RecyclerView recyclerViewTags;
    private View view;
    private QuestionsAdapter questionsAdapter;
    private TagsAdapter tagsAdapter;

    private static final String[] MIMES = new String[]{"image/*", "application/pdf"};


    public ShowQuestionsHolderFragment() {
    }


    public static ShowQuestionsHolderFragment newInstance(int questionsHolderId) {
        ShowQuestionsHolderFragment fragment = new ShowQuestionsHolderFragment();
        Bundle args = new Bundle();
        args.putInt(QUESTIONS_HOLDER_ID, questionsHolderId);
        fragment.setArguments(args);
        return fragment;
    }

    public static ShowQuestionsHolderFragment newAddQuestionsInstance() {
        return newInstance(ADD_QUESTIONS_ID);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

//            user = db.getLocalUser(); todo
            user = new User("pppfff", "fafsadfa", "ffffff", "f@f.com", "fff", true);
            int questionsHolderId = getArguments().getInt(QUESTIONS_HOLDER_ID);
            if (questionsHolderId != ADD_QUESTIONS_ID) {
                questionsHolder = db.getLocalQuestionsHolder();
                addQuestionMode = false;
            } else {
                questionsHolder = new QuestionsHolder(user.getId(), user.getName());
                addQuestionMode = true;
            }
        }
    }

    private void setEditButtonToEdit() {
        editButton.setOnClickListener(v -> enterEditMode());
    }

    private void showTextQuestion(TextQuestion textQuestion) {
        ShowQuestionTextFragment fragInfo = ShowQuestionTextFragment.newInstance(textQuestion);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragInfo);
        transaction.commit();
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
        }
        catch (ActivityNotFoundException e) {

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
        tagsAdapter.enterEditMode();
        editButton.setText("پایان");
        editButton.setOnClickListener(v -> new MaterialAlertDialogBuilder(view.getContext())
                .setPositiveButton("ذخیره", (dialogInterface, i) -> {
                    tagsAdapter.exitEditMode();
                    if (addQuestionMode && !added) {
                        added = true;
                        QuestionsHolder addedQuestionsHolder = getEditedQuestionsHolder();
                        db.addQuestionsHolder(
                                addedQuestionsHolder
                        );
                        questionsHolder = addedQuestionsHolder;
                    } else {
                        QuestionsHolder editedQuestionsHolder = getEditedQuestionsHolder();
                        db.editQuestionsHolder(
                                editedQuestionsHolder
                        );
                        questionsHolder = editedQuestionsHolder;
                    }
                    exitEditMode();
                })
                .setNegativeButton("لغو", (dialogInterface, i) -> {
                    tagsAdapter.exitEditMode();
                    exitEditMode();
                }).setNeutralButton("ادامه ویرایش", null).show());
        changeMode(true);
        questionsAdapter.changeButtonVisibility(View.VISIBLE);
        questionsAdapter.enterEditMode();

    }

    private void exitEditMode() {
        showQuestionsHolder();
        editButton.setText("ویرایش");
        setEditButtonToEdit();
        changeMode(false);
        questionsAdapter.changeButtonVisibility(View.INVISIBLE);
        tagsAdapter.exitEditMode();
    }


    private void showQuestions() {
        questionsAdapter = new QuestionsAdapter(questionsHolder.getQuestions()) {

            @Override
            public void onQuestionClicked(Question question) {
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
//                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
//                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
//                chooseFile.setType(String.join(MIMES[0], MIMES[1]));
//                chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, MIMES);
//                startActivityForResult(
//                        Intent.createChooser(chooseFile, "Choose a file"),
//                        PICKFILE_RESULT_CODE
//                );
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

    public void qhSelectFile(){
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);

//        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
//        chooseFile.setType(String.join(MIMES[0], MIMES[1]));
        chooseFile.setType("*/*");


//        chooseFile.putExtra(Intent.EXTRA_MIME_TYPES, MIMES);
        startActivityForResult(
                Intent.createChooser(chooseFile, "Choose a file"),
                PICKFILE_RESULT_CODE
        );
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode);
        System.out.println(PICKFILE_RESULT_CODE);
        System.out.println(resultCode);
        System.out.println(Activity.RESULT_OK);
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
        questionsRecyclerView = view.findViewById(R.id.questions);
        questionsRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        recyclerViewComments = view.findViewById(R.id.comments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(view.getContext()));

        recyclerViewTags = view.findViewById(R.id.special_tags);
        recyclerViewTags.setLayoutManager(new LinearLayoutManager(view.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        title = view.findViewById(R.id.titleQH);
        view.findViewById(R.id.submit_comment).setOnClickListener(v -> {
            EditText newComment = view.findViewById(R.id.new_comment);
            Comment comment = new Comment(
                    user.getId(),
                    newComment.getText().toString(),
                    user.getName(),
                    questionsHolder.getId()
            );
            questionsHolder.getComments().add(0, comment);
            db.addComment(comment);
            showComments();
            newComment.setText("");
            KeyboardHider.HideKeyboard(view);
        });
        description = view.findViewById(R.id.descriptionQH);
        editButton = view.findViewById(R.id.edit_btn);

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
        tags = view.findViewById(R.id.tagQH);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_show_questions_holder, container, false);
        initQuestionsHolder();
        showQuestionsHolder();
        setEditButtonToEdit();
        changeMode(addQuestionMode);
        if (addQuestionMode) {
            enterEditMode();
        }
        if (!user.isAdmin()) {
            editButton.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}