package com.example.jasoali.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import com.example.jasoali.MainActivity;
import com.example.jasoali.R;
import com.example.jasoali.api.DBConnection;
import com.example.jasoali.exceptions.MessageException;
import com.example.jasoali.models.User;
import com.example.jasoali.ui.problem.ShowQuestionsHolderFragment;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.sql.Struct;

import fr.tkeunebr.gravatar.Gravatar;


public class ProfileFragment extends Fragment {

    private final DBConnection db = new DBConnection(MainActivity.getHandler());
    private final Context context;
    boolean isBlockedScrollView = false;
    View.OnClickListener onChange;
    View.OnClickListener onSubmit;
    private User user;
    private View view;
    private EditText email;
    private EditText name;
    private EditText username;
    private ImageView profile;
    private MaterialButton addQuestion;
    private MaterialButton changeProfile;
    private MaterialButton logout;
    private ScrollView scrollView;
    private ProgressBar progressBar;

    private HandlerThread mHandlerThread;
    private ProfileHandler handler;

    public ProfileFragment(Context context) {
        this.context = context;
        mHandlerThread = new HandlerThread("HandlerThread");
        mHandlerThread.start();
        handler = new ProfileHandler(this, mHandlerThread);
    }


    public static ProfileFragment newInstance(Context context) {
        ProfileFragment fragment = new ProfileFragment(context);
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
        logout.setAlpha(alpha);
    }


    private void setChangeProfileListeners() {
        onChange = v -> {
            addQuestion.setOnClickListener(null);
            logout.setOnClickListener(null);
            changeProfile.setText(view.getContext().getString(R.string.submit_changes));
            changeProfile.setOnClickListener(onSubmit);
            name.setEnabled(true);
            isBlockedScrollView = true;
            changeAlphaForChange(0.4f);
        };
        User finalUser = user;
        onSubmit = v -> {
            setAddQuestionListener();
            logout.setOnClickListener(v2 -> logoutUser());
            changeProfile.setText(view.getContext().getString(R.string.change_user));
            changeProfile.setOnClickListener(onChange);
            name.setEnabled(false);
            isBlockedScrollView = false;
            changeAlphaForChange(1f);
            db.updateName(finalUser.getId(), name.getText().toString());

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
        logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(v -> logoutUser());
    }

    private void logoutUser(){
        db.logout();
        ((MainActivity)context).checkUserState();
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

    public void setAddQuestionListener() {
        if (user.isAdmin()) {
            addQuestion.setOnClickListener(v -> {
                ShowQuestionsHolderFragment fragInfo = ShowQuestionsHolderFragment.newAddQuestionsInstance(context);
                MainActivity activity = (MainActivity) ProfileFragment.this.context;
                activity.showInfoFragment(fragInfo);
            });

        } else {
            addQuestion.setOnClickListener(v -> {
                db.adminRequest(user.getId());
                addQuestion.setEnabled(false);
                addQuestion.setText(view.getContext().getString(R.string.admin_request_done));
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        Message fetch = new Message();
        fetch.what = ProfileHandler.START_FETCH_PROFILE;
        handler.sendMessage(fetch);
        return view;
    }

    public static class ImageHandler extends Handler {
        WeakReference<ProfileFragment> fragmentWeakReference;

        public ImageHandler(ProfileFragment fragment) {
            super();
            this.fragmentWeakReference = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            ProfileFragment fragment = this.fragmentWeakReference.get();
            if (fragment == null)
                return;
            fragment.getProfileImage();
            fragment.view.findViewById(R.id.scroller).setVisibility(View.VISIBLE);
            fragment.view.findViewById(R.id.fetchProfileBar).setVisibility(View.GONE);
        }
    }

    public static class ProfileHandler extends Handler {

        public static final int START_FETCH_PROFILE = 1;
        private final WeakReference<ProfileFragment> profileFragmentWeakReference;
        private ImageHandler imageHandler;

        public ProfileHandler(ProfileFragment fragment, HandlerThread handlerLoop) {
            super(handlerLoop.getLooper());
            this.profileFragmentWeakReference = new WeakReference<>(fragment);
            this.imageHandler = new ImageHandler(fragment);

        }

        @Override
        public void handleMessage(Message msg) {
            Log.e("HANDLER", String.valueOf(msg.what));
            ProfileFragment fragment = profileFragmentWeakReference.get();
            if (fragment == null)
                return;
            if (msg.what == START_FETCH_PROFILE) {
                fragment.user = fragment.db.getLocalUser();
                fragment.findElements();
                fragment.setText();
                this.imageHandler.sendEmptyMessage(0);
                fragment.setChangeProfileListeners();
                fragment.email.setEnabled(false);
                fragment.name.setEnabled(false);
                fragment.username.setEnabled(false);
                fragment.setAddQuestionListener();

            }
        }
    }

}