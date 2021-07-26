package com.example.jasoali.ui.questionsholder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jasoali.R;
import com.example.jasoali.models.Comment;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private final List<Comment> comments;

    public CommentsAdapter(List<Comment> comments) {
        this.comments = comments;
    }

    @NotNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.comments_list_recycler_view, parent, false);
        return new ViewHolder(contactView);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NotNull CommentsAdapter.ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        if (position % 2 == 0) {
            holder.getCommentLayout().setBackground(
                    holder.itemView.getResources().getDrawable(R.drawable.rounded_comments)
            );
        } else {
            holder.getCommentLayout().setBackground(
                    holder.itemView.getResources().getDrawable(R.drawable.rounded_comments_second)
            );
        }
        holder.getNameTextView().setText(comment.getName() + ":");
        holder.getValueTextView().setText(comment.getText());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView valueTextView;
        private final LinearLayout commentLayout;

        public TextView getNameTextView() {
            return nameTextView;
        }

        public TextView getValueTextView() {
            return valueTextView;
        }

        public LinearLayout getCommentLayout() {
            return commentLayout;
        }


        public ViewHolder(View itemView) {
            super(itemView);
            commentLayout = itemView.findViewById(R.id.comment_holder);
            nameTextView = itemView.findViewById(R.id.comment_name);
            valueTextView = itemView.findViewById(R.id.comment_value);
        }
    }
}
