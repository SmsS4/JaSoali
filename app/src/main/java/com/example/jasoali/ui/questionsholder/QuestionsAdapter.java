package com.example.jasoali.ui.questionsholder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jasoali.R;
import com.example.jasoali.models.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
    public List<Question> getQuestions() {
        return questions;
    }

    private List<Question> questions;
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access

    public QuestionsAdapter(List<Question> questions) {
        this.questions = new ArrayList<>();
        this.questions.addAll(questions);
    }

    public List<ViewHolder> getViewHolders() {
        return viewHolders;
    }

    List<ViewHolder> viewHolders = new ArrayList<>();
    int visibility = View.INVISIBLE;

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.questions_list_recycler_view, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        viewHolders.add(viewHolder);
        return viewHolder;
    }

    void removeAt(int position) {
        questions.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, questions.size());
    }
    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(QuestionsAdapter.ViewHolder holder, int position) {

        // Get the data model based on position
        Question question = questions.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText(question.getTitle());

        holder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAt(position);
            }
        });

//        Button button = holder.messageButton;
//        button.setText("blah blah");
//        button.setText(contact.isOnline() ? "Message" : "Offline");
//        button.setEnabled(contact.isOnline());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return questions.size();
    }

    public void changeButtonVisibility(int visibility){
        this.visibility = visibility;
        for (ViewHolder viewHolder: viewHolders){
            viewHolder.delButton.setVisibility(visibility);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public Button delButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.question_name);
            delButton = (Button) itemView.findViewById(R.id.del_btn);
            delButton.setVisibility(visibility);

        }
    }
}
