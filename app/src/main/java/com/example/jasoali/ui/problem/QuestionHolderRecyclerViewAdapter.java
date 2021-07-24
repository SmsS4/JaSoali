//package com.example.jasoali.ui.problem;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.jasoali.R;
//import com.example.jasoali.models.QuestionsHolder;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class QuestionHolderRecyclerViewAdapter
//        extends RecyclerView.Adapter<QuestionHolderRecyclerViewAdapter.ViewHolder> {
//
//    private final List<QuestionsHolder> mData;
//    private final List<QuestionsHolder> mDataCopy = new ArrayList<>();
//    private final LayoutInflater mInflater;
//    public ItemClickListener mClickListener;
//
//    // data is passed into the constructor
//    public QuestionHolderRecyclerViewAdapter(Context context, List<QuestionsHolder> data) {
//        this.mInflater = LayoutInflater.from(context);
//        this.mData = data;
//        mDataCopy.addAll(data);
//    }
//
//    // inflates the row layout from xml when needed
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = mInflater.inflate(R.layout.single_bookmark, parent, false);
//        return new ViewHolder(view);
//    }
//
//    // binds the data to the TextView in each row
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        MyLocation location = mData.get(position);
//        holder.name.setText(location.getName());
//        holder.location.setText(String.valueOf(location.getLocation()));
//    }
//
//    // total number of rows
//    @Override
//    public int getItemCount() {
//        return mData.size();
//    }
//
//
//    // stores and recycles views as they are scrolled off screen
//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
//        TextView name;
//        TextView location;
//        ImageView deleteButton;
//        LinearLayout clickableArea;
//
//        ViewHolder(View itemView) {
//            super(itemView);
//            name = itemView.findViewById(R.id.textView);
//            location = itemView.findViewById(R.id.textView2);
//            deleteButton = itemView.findViewById(R.id.delete_button);
//            clickableArea = itemView.findViewById(R.id.go_to_point);
//            deleteButton.setOnClickListener(this);
//            clickableArea.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View view) {
//            if (view.equals(deleteButton)) {
//                mClickListener.onDeleteButtonClick(getAdapterPosition());
//                mData.remove(getAdapterPosition());
//                notifyItemRemoved(getAdapterPosition());
//                notifyItemRangeChanged(getAdapterPosition(), mData.size());
//            } else if (mClickListener != null) {
//                mClickListener.onItemClick(view, getAdapterPosition());
//            }
//        }
//    }
//
//    public void filter(String text) {
//        mData.clear();
//        if (text.isEmpty()) {
//            mData.addAll(mDataCopy);
//        } else {
//            text = text.toLowerCase();
//            for (MyLocation location : mDataCopy) {
//                if (location.getName().toLowerCase().contains(text)) {
//                    mData.add(location);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }
//
//    // convenience method for getting data at click position
//    public MyLocation getItem(int id) {
//        return mData.get(id);
//    }
//
//    // allows clicks events to be caught
//    public void setClickListener(ItemClickListener itemClickListener) {
//        this.mClickListener = itemClickListener;
//    }
//
//    // parent activity will implement this method to respond to click events
//    public interface ItemClickListener {
//        void onDeleteButtonClick(int position);
//
//        void onItemClick(View view, int position);
//    }
//}