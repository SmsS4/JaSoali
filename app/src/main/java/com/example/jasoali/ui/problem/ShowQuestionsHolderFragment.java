package com.example.jasoali.ui.problem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.nfc.Tag;
import android.os.Bundle;

import android.app.Fragment;

import android.text.Editable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasoali.MainActivity;
import com.example.jasoali.R;
import com.example.jasoali.api.DBConnection;
import com.example.jasoali.custom_packacges.RoundedBackgroundSpan;
import com.example.jasoali.models.Category;
import com.example.jasoali.models.CategoryType;
import com.example.jasoali.models.Question;
import com.example.jasoali.models.QuestionsHolder;
import com.example.jasoali.models.User;
import com.example.jasoali.ui.questionsholder.CommentsAdapter;
import com.example.jasoali.ui.questionsholder.QuestionsAdapter;
import com.example.jasoali.ui.questionsholder.TagsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowQuestionsHolderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowQuestionsHolderFragment extends Fragment {

    public static final String QUESTIONS_HOLDER_ID = "questionsHolderId";

    private QuestionsHolder questionsHolder;
    private User user;
    private DBConnection db = DBConnection.getInstance();

    private EditText title;
    private EditText description;
    private EditText tags;
    private Button editButton;
    private RecyclerView questionsRecyclerView;
    private RecyclerView recyclerViewComments;
    private RecyclerView recyclerViewTags;



    private View view;

    private boolean onEditMode = false;

    private QuestionsAdapter questionsAdapter;
    private TagsAdapter tagsAdapter;

    public ShowQuestionsHolderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ShowQuestionsHolderFragment.
     */
    public static ShowQuestionsHolderFragment newInstance(int questionsHolderId) {
        ShowQuestionsHolderFragment fragment = new ShowQuestionsHolderFragment();
        Bundle args = new Bundle();
        args.putInt(QUESTIONS_HOLDER_ID, questionsHolderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int questionsHolderId = getArguments().getInt(QUESTIONS_HOLDER_ID);
            questionsHolder = db.getLocalQuestionsHolder(questionsHolderId);
//            user = db.getLocalUser(); todo
            user = new User("pppfff", "fafsadfa", "ffffff", "f@f.com", "fff", null, true);


        }

        System.out.println("finish");
    }

    private void setEditButtonToEdit() {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterEditMode();
            }
        });
    }

    private void changeMode(boolean show) {
        title.setEnabled(show);
        description.setEnabled(show);
        tags.setEnabled(show);
    }

    private ArrayList<Category> getEditedTags() {
        ArrayList<Category> result = new ArrayList<>();
        String tags_string = tags.getText().toString();
        for (String tag : tags_string.split(" ")) {
            result.add(new Category(CategoryType.OTHER, tag));
        }
        for (TagsAdapter.ViewHolder viewHolder : tagsAdapter.getViews()) {
            result.add(
                    new Category(
                            Category.getCategoryByType(viewHolder.typeSpinner.getSelectedItem().toString()),
                            viewHolder.valueTextView.getText().toString()
                    )
            );
        }
        return result;
    }

    private ArrayList<Question> getQuestions() {
        return new ArrayList<>(questionsAdapter.getQuestions());

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
                getQuestions()
        );
    }

    private void enterEditMode() {
        editButton.setText("خروج");
        editButton.setOnClickListener(v -> {
            new AlertDialog.Builder(view.getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("ذخیره", (dialog, whichButton) -> {
                        QuestionsHolder editedQuestionsHolder = getEditedQuestionsHolder();
                        DBConnection.getInstance().editQuestionsHolder(
                                editedQuestionsHolder
                        );
                        questionsHolder = editedQuestionsHolder;
                        exitEditMode();

                    })
                    .setNegativeButton("لغو", (dialog, whichButton) -> {
                        showQuestionsHolder();
                        exitEditMode();
                    })
                    .setNeutralButton("ادامه ویرایش", null).show();

        });
        onEditMode = true;
        changeMode(true);
        questionsAdapter.changeButtonVisibility(View.VISIBLE);
        tagsAdapter.setEnable(true);

    }

    private void exitEditMode() {
        editButton.setText("ویرایش");
        setEditButtonToEdit();
        onEditMode = false;
        changeMode(false);
        questionsAdapter.changeButtonVisibility(View.INVISIBLE);
        tagsAdapter.setEnable(false);

    }

    private void showQuestions() {
        questionsAdapter = new QuestionsAdapter(questionsHolder.getQuestions());
        questionsRecyclerView.setAdapter(questionsAdapter);
    }

    private void showComments() {
        CommentsAdapter adapterComments = new CommentsAdapter(questionsHolder.getComments());
        recyclerViewComments.setAdapter(adapterComments);
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

    private void setData() {
    }

    private void initQuestionsHolder() {
        questionsRecyclerView = view.findViewById(R.id.questions);
        GridLayoutManager glm = new GridLayoutManager(view.getContext(), 2) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        questionsRecyclerView.setLayoutManager(glm);

        recyclerViewComments = view.findViewById(R.id.comments);
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(view.getContext()));

        recyclerViewTags = view.findViewById(R.id.special_tags);
        recyclerViewTags.setLayoutManager(new LinearLayoutManager(view.getContext()));

        title = view.findViewById(R.id.titleQH);
        description = view.findViewById(R.id.descriptionQH);
        editButton = view.findViewById(R.id.edit_btn);

    }

    private void colorFulTags() {
        colorFulTags(null);
    }

    private void colorFulTags(ArrayList<Category> source) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        int tagStart = 0;
        if (source == null) {
            source = getOtherTags();
        }
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

    private String myPreviousText;

    private void showOtherTags() {
        tags = view.findViewById(R.id.tagQH);
        tags.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    colorFulTags(getEditedTags());
                }
            }
        });
//            tags.addTextChangedListener(new TextWatcher() {
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    String text = s.toString();
//                    if(!text.equals(myPreviousText)){
//                        myPreviousText = text;
//                        System.out.println(text.charAt(text.length()-1));
//                        System.out.println(text);
//                        if( text.length() > 0 && text.charAt(text.length()-1) == ' '){
//                            colorFulTags(getEditedTags());
//
//                        }
//                    }
//                }
//
//                @Override
//                public void beforeTextChanged(CharSequence s, int start,
//                                              int count, int after) {
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start,
//                                          int before, int count) {
//
//                }
//            });
        colorFulTags();

//        tags.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                System.out.println(keyCode);
//                return false;
//            }
//        });
//        tags.setText(stringBuilder);
    }

    private void showQuestionsHolder() {

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


        setData();
        initQuestionsHolder();
        showQuestionsHolder();
        setEditButtonToEdit();
        changeMode(false);
        if (!user.isAdmin()){
            editButton.setVisibility(View.INVISIBLE);
        }
        return view;
    }
}