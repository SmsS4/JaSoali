package com.example.jasoali.ui.questionsholder;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasoali.R;
import com.example.jasoali.custom_packacges.RoundedBackgroundSpan;
import com.example.jasoali.models.Category;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {
    private List<Category> categories;

    public TagsAdapter(List<Category> categories) {
        this.categories = categories;
    }

    private List<TagsAdapter.ViewHolder> viewHolders = new ArrayList<>();
    boolean enable = false;

    public List<TagsAdapter.ViewHolder> getViews(){
        return viewHolders;
    }
    @Override
    public TagsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.tags_list_recycler_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        viewHolders.add(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TagsAdapter.ViewHolder holder, int position) {
        Category category = categories.get(position);

        Spinner spin = holder.typeSpinner;
        ArrayAdapter aa = new ArrayAdapter(
                holder.itemView.getContext(),
                R.layout.spinner_item,
                Category.ALL_STRINGS
        );
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);
        int spinnerPosition = aa.getPosition(category.getStringType());
        spin.setSelection(spinnerPosition);
        spin.setEnabled(enable);
        setColorFullTag(holder, category);
        holder.valueTextView.setEnabled(enable);
        holder.valueTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    setColorFullTag(holder, new Category(
                            Category.getCategoryByType(spin.getSelectedItem().toString()),
                            holder.valueTextView.getText().toString()
                    ));
                } else {
                    holder.valueTextView.setText(
                            holder.valueTextView.getText().toString()
                    );
                }
            }
        });
    }

    public void setColorFullTag(ViewHolder holder, Category category) {
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(category.getValue());
        RoundedBackgroundSpan tagSpan = new RoundedBackgroundSpan(
                ContextCompat.getColor(holder.itemView.getContext(), RoundedBackgroundSpan.getNextColor()),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.white)
        );
        stringBuilder.setSpan(tagSpan, 0, category.getValue().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        holder.valueTextView.setText(
                stringBuilder
        );
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
        for (ViewHolder viewHolder : viewHolders) {
            viewHolder.typeSpinner.setEnabled(enable);
            viewHolder.valueTextView.setEnabled(enable);

        }
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Spinner typeSpinner;
        public EditText valueTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            typeSpinner = (Spinner) itemView.findViewById(R.id.tag_type);
            valueTextView = (EditText) itemView.findViewById(R.id.tag_value);
        }
    }
}
