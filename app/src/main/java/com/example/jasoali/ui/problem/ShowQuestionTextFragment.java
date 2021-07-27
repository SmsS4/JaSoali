package com.example.jasoali.ui.problem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.jasoali.R;
import com.example.jasoali.models.TextQuestion;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;


public class ShowQuestionTextFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_TEXT = "text";
    SharedPreferences sharedPref;
    private String title;
    private String text;
    private boolean dark = true;
    private MaterialTextView titleView;
    private MaterialTextView textView;
    private MaterialButton darkMode;
    private View view;

    public static ShowQuestionTextFragment newInstance(TextQuestion textQuestion) {
        ShowQuestionTextFragment fragment = new ShowQuestionTextFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, textQuestion.getTitle());
        args.putString(ARG_TEXT, textQuestion.getText());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            text = getArguments().getString(ARG_TEXT);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    void changeDarkMode(boolean newMode) {
        dark = newMode;
        sharedPref.edit().putBoolean(view.getContext().getString(R.string.dark_mode_preferences), dark).apply();
        textView.setEnabled(!dark);
        titleView.setEnabled(!dark);
        if (dark) {
            view.setBackgroundColor(view.getContext().getColor(R.color.darkmode));
            darkMode.setText("روشن");
        } else {
            view.setBackgroundColor(view.getContext().getColor(R.color.lightmode));
            darkMode.setText("تاریک");
        }
        darkMode.setOnClickListener(v -> changeDarkMode(!dark));

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_show_question_text, container, false);
        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        dark = sharedPref.getBoolean(getString(R.string.dark_mode_preferences), false);

        titleView = view.findViewById(R.id.text_view_id);
        textView = view.findViewById(R.id.text_box);
        darkMode = view.findViewById(R.id.dark_mdoe);
        titleView.setText(title);
        textView.setText(text);
        changeDarkMode(dark);
        return view;
    }
}