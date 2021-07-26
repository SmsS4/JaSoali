package com.example.jasoali.ui.questionsholder;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jasoali.R;
import com.example.jasoali.inerfaces.addQuestionConnection;
import com.example.jasoali.models.FileQuestion;
import com.example.jasoali.models.Question;
import com.example.jasoali.models.TextQuestion;
import com.example.jasoali.models.WindowAndView;
import com.example.jasoali.utils.KeyboardHider;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> implements addQuestionConnection {


    List<ViewHolder> viewHolders = new ArrayList<>();
    int visibility = View.INVISIBLE;
    private final List<Question> questions;

    public List<Question> getQuestions() {
        return questions;
    }

    abstract public void onQuestionClicked(Question question);
    abstract public void selectFile();
    abstract public FileQuestion submitFile(String title);
    abstract public TextQuestion submitText(String title, String text);

    public QuestionsAdapter(List<Question> questions) {
        this.questions = new ArrayList<>();
        this.questions.addAll(questions);
    }


    @NotNull
    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder viewHolder = new ViewHolder(
                inflater.inflate(
                        R.layout.questions_list_recycler_view,
                        parent,
                        false
                )
        );
        viewHolders.add(viewHolder);
        return viewHolder;
    }


    void removeAt(int position) {
        questions.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, questions.size());
    }

    private void addAt(Question question, int position) {
        questions.add(position, question);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, questions.size());
    }

    public void enterEditMode() {
        questions.add(null);
        notifyItemInserted(questions.size() - 1);
    }


    private WindowAndView modal(ViewHolder holder, int id) {
        Context context = holder.itemView.getContext();
        PopupWindow popUp = new PopupWindow(context);
        LinearLayout layout = holder.itemView.findViewById(R.id.add_question_layout);
        popUp.setContentView(layout);
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(id, null);
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
        popupWindow.showAtLocation(holder.itemView, Gravity.CENTER, 0, 0);
        return new WindowAndView(popupWindow, popupView);
    }

    private void toast(QuestionsAdapter.ViewHolder holder, int messageId) {
        Toast.makeText(holder.itemView.getContext(),
                holder.itemView.getResources().getString(messageId),
                Toast.LENGTH_SHORT
        ).show();
    }

    private void addNewQuestion(QuestionsAdapter.ViewHolder holder, Question question, WindowAndView windowAndView) {
        if (question == null) {
            toast(holder, R.string.file_is_null);
        } else if (question.getTitle().equals("")) {
            toast(holder, R.string.title_is_empty);
        } else if (question instanceof TextQuestion && ((TextQuestion) (question)).getText().equals("")) {
            toast(holder, R.string.text_is_empty);
        } else {
            addAt(question, questions.size() - 1);
            KeyboardHider.HideKeyboard(windowAndView.getView());
            windowAndView.getPopupWindow().dismiss();
        }
    }

    private void modalAddQuestion(ViewHolder holder, boolean fileQuestion) {
        WindowAndView windowAndView = modal(holder, R.layout.add_question_modal);
        TextView questionText = windowAndView.getView().findViewById(R.id.add_question_text);
        TextView questionTitle = windowAndView.getView().findViewById(R.id.add_question_title);
        Button submit = windowAndView.getView().findViewById(R.id.upload);
        Button selectFile = windowAndView.getView().findViewById(R.id.select);
        if (fileQuestion) {
            questionText.setVisibility(View.GONE);
            windowAndView.getView().findViewById(R.id.add_question_text_parent).setVisibility(View.GONE);
            selectFile.setOnClickListener(v -> selectFile());
            submit.setOnClickListener(v -> addNewQuestion(holder, submitFile(questionTitle.getText().toString()), windowAndView));
        } else {
            selectFile.setVisibility(View.GONE);
            submit.setOnClickListener(v -> addNewQuestion(holder, submitText(questionTitle.getText().toString(), questionText.getText().toString()), windowAndView));
        }

    }


    private void modalSelectType(ViewHolder holder) {
        WindowAndView windowAndView = modal(holder, R.layout.select_add_question_mode);
        Button fileSelected = windowAndView.getView().findViewById(R.id.file_question);
        Button textSelected = windowAndView.getView().findViewById(R.id.text_question);
        fileSelected.setOnClickListener(v -> {
            modalAddQuestion(holder, true);
            windowAndView.getPopupWindow().dismiss();
        });
        textSelected.setOnClickListener(v -> {
            modalAddQuestion(holder, false);
            windowAndView.getPopupWindow().dismiss();
        });

    }

    @Override
    public void onBindViewHolder(QuestionsAdapter.ViewHolder holder, int position) {
        Question question = questions.get(position);
        if (question != null) {
            TextView textView = holder.nameTextView;
            textView.setText(question.getTitle());
            textView.setAlpha(1f);
            textView.getBackground().setAlpha(255);
            textView.setOnClickListener(v -> onQuestionClicked(question));
            holder.delButton.setVisibility(visibility);
            holder.delButton.setOnClickListener(v -> new MaterialAlertDialogBuilder(holder.itemView.getContext())
                    .setTitle("سوال حذف شود؟")
                    .setMessage(textView.getText().toString())
                    .setPositiveButton("بله", (dialogInterface, i) -> removeAt(position))
                    .setNegativeButton("خیر", null)
                    .show()
            );
        } else {
            holder.delButton.setVisibility(View.GONE);
            TextView textView = holder.nameTextView;
            textView.setText("اضافه کردن سوال");
            textView.setAlpha(0.55f);
            textView.getBackground().setAlpha(170);
            textView.setOnClickListener(v -> modalSelectType(holder));
        }

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public void changeButtonVisibility(int visibility) {
        this.visibility = visibility;
        for (ViewHolder viewHolder : viewHolders) {
            viewHolder.delButton.setVisibility(visibility);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public Button delButton;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.question_name);
            delButton = itemView.findViewById(R.id.del_btn);
            delButton.setVisibility(visibility);
        }
    }
}
