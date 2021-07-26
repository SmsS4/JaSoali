package com.example.jasoali.ui.profile;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.jasoali.R;
import com.example.jasoali.api.DBConnection;
import com.example.jasoali.models.User;
import com.example.jasoali.ui.problem.ShowQuestionsHolderFragment;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import fr.tkeunebr.gravatar.Gravatar;


public class ProfileFragment extends Fragment {

    private User user;
    private View view;
    private EditText email;
    private EditText name;
    private EditText username;
    private ImageView profile;
    private MaterialButton addQuestion;
    private MaterialButton changeProfile;
    private ScrollView scrollView;
    boolean isBlockedScrollView = false;

    View.OnClickListener onChange;
    View.OnClickListener onSubmit;

    public ProfileFragment() {
    }


    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void changeAlphaForChange(float alpha) {
        profile.setAlpha(alpha);
        email.setAlpha(alpha);
        username.setAlpha(alpha);
        addQuestion.setAlpha(alpha);
    }


    private void setChangeProfileListeners() {
        onChange = v -> {
            changeProfile.setText(view.getContext().getString(R.string.submit_changes));
            changeProfile.setOnClickListener(onSubmit);
            name.setEnabled(true);
            isBlockedScrollView = true;
            changeAlphaForChange(0.4f);
        };
        User finalUser = user;
        onSubmit = v -> {
            changeProfile.setText(view.getContext().getString(R.string.change_user));
            changeProfile.setOnClickListener(onChange);
            name.setEnabled(false);
            isBlockedScrollView = false;
            changeAlphaForChange(1f);
            DBConnection.getInstance().updateName(finalUser.getId(), name.getText().toString());

        };
        changeProfile.setOnClickListener(onChange);
    }

    private void findElements() {
        email = view.findViewById(R.id.email);
        name = view.findViewById(R.id.name);
        username = view.findViewById(R.id.username);
        scrollView = view.findViewById(R.id.scroller);
        profile = view.findViewById(R.id.profileImage);
        addQuestion = view.findViewById(R.id.addQuestion);
        changeProfile = view.findViewById(R.id.uploadInformation);
    }

    private void setText() {

        email.setText(user.getEmail());
        name.setText(user.getName());
        username.setText(user.getUsername());
        if (user.isAdmin()) {
            addQuestion.setText(view.getContext().getString(R.string.add_question));
        } else {
            addQuestion.setText(view.getContext().getString(R.string.admin_request));
        }
    }

    public void getProfileImage() {
        scrollView.setOnTouchListener((v, event) -> isBlockedScrollView);
        if (user.getEmail() != null && !user.getEmail().equals("")) {
            String gravatarUrl = Gravatar.init().with(user.getEmail()).size(100).build();
            Picasso.get().load(gravatarUrl).into(profile);
        }

    }
    
    public void setAddQuestionListener(){
        if(user.isAdmin()){
            addQuestion.setOnClickListener(v -> {
                ShowQuestionsHolderFragment fragInfo = ShowQuestionsHolderFragment.newAddQuestionsInstance();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragInfo);
                transaction.commit();
            });

        }else{
            addQuestion.setOnClickListener(v -> {
                DBConnection.getInstance().adminRequest(user.getId());
                addQuestion.setEnabled(false);
                addQuestion.setText(view.getContext().getString(R.string.admin_request_done));
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        user = DBConnection.getInstance().getLocalUser();
        user = new User(
                "test",
                "AghaSadegh",
                "nemigam",
                "smss.lite@gmail.com",
                "سید مهدی صادق شبیری",
                null,
                false
        );

        findElements();
        setText();
        getProfileImage();
        setChangeProfileListeners();
        email.setEnabled(false);
        name.setEnabled(false);
        username.setEnabled(false);
        setAddQuestionListener();
        return view;
    }
}