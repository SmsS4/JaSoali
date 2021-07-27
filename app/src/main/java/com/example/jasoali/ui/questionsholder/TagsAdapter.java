package com.example.jasoali.ui.questionsholder;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasoali.R;
import com.example.jasoali.custom_packacges.RoundedBackgroundSpan;
import com.example.jasoali.models.Category;
import com.example.jasoali.models.CategoryType;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {


    private final List<Category> categories;
    boolean editMode = false;
    private final List<TagsAdapter.ViewHolder> viewHolders = new ArrayList<>();

    public TagsAdapter(List<Category> categories) {
//        System.out.println("hooy");
//        for(Category category:categories){
//            System.out.println(category.getValue());
//        }
        this.categories = new ArrayList<>();
        for (Category category : categories) {
            Category categoryToAdd = new Category(
                    category.getType(),
                    category.getValue()
            );
            if (categoryToAdd.getValue() == null){
                categoryToAdd.setValue("");
            }
            this.categories.add(
                    categoryToAdd
            );

        }
        for (CategoryType categoryType : Category.ALL_TYPES) {
            boolean found = false;
            for (Category category : this.categories) {
                if (category.getType() == categoryType) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                this.categories.add(new Category(categoryType, ""));
            }
        }
    }


    public List<TagsAdapter.ViewHolder> getViews() {
        return viewHolders;
    }

    @NotNull
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
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                holder.itemView.getContext(),
                R.layout.spinner_item,
                Category.ALL_STRINGS
        );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(arrayAdapter);
        int spinnerPosition = arrayAdapter.getPosition(category.getStringType());
        spin.setSelection(spinnerPosition);
        spin.setEnabled(false);
        setColorFullTag(holder, category, position);
        holder.getValueTextView().setEnabled(editMode);
        if (!editMode && holder.getValueTextView().getText().toString().equals("")) {
            holder.getTagCell().setVisibility(View.GONE);
            holder.getValueTextView().setVisibility(View.GONE);
            holder.getTypeSpinner().setVisibility(View.GONE);
        }
        holder.getValueTextView().setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                setColorFullTag(holder,
                        new Category(
                                Category.getCategoryByType(spin.getSelectedItem().toString()),
                                holder.getValueTextView().getText().toString()
                        ),
                        position
                );
            } else {
                holder.getValueTextView().setText(
                        holder.getValueTextView().getText().toString()
                );
            }
        });
    }


    public void enterEditMode() {
        editMode = true;
        for (ViewHolder viewHolder : getViews()) {
            viewHolder.getTagCell().setVisibility(View.VISIBLE);
            viewHolder.getValueTextView().setVisibility(View.VISIBLE);
            viewHolder.getTypeSpinner().setVisibility(View.VISIBLE);
            viewHolder.getValueTextView().setEnabled(true);
        }
    }

    public void exitEditMode() {
        editMode = false;
        for (ViewHolder viewHolder : getViews()) {
            if (viewHolder.getValueTextView().getText().toString().equals("")) {
                viewHolder.getTagCell().setVisibility(View.GONE);
                viewHolder.getValueTextView().setVisibility(View.GONE);
                viewHolder.getTypeSpinner().setVisibility(View.GONE);
            }
            viewHolder.getValueTextView().setEnabled(false);
        }

    }

    public void setColorFullTag(ViewHolder holder, Category category, int position) {
        /// += 4 is just for shifting colors not important
        position += 4;
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(category.getValue());
        RoundedBackgroundSpan tagSpan = new RoundedBackgroundSpan(
                ContextCompat.getColor(holder.itemView.getContext(), RoundedBackgroundSpan.getColor(position)),
                ContextCompat.getColor(holder.itemView.getContext(), R.color.white)
        );
        stringBuilder.setSpan(tagSpan, 0, category.getValue().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.getValueTextView().setText(
                stringBuilder
        );
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Spinner typeSpinner;
        private final EditText valueTextView;
        private final LinearLayout tagCell;

        public Spinner getTypeSpinner() {
            return typeSpinner;
        }

        public EditText getValueTextView() {
            return valueTextView;
        }

        public LinearLayout getTagCell() {
            return tagCell;
        }


        public ViewHolder(View itemView) {
            super(itemView);
            tagCell = itemView.findViewById(R.id.tag_cell);
            typeSpinner = itemView.findViewById(R.id.tag_type);
            typeSpinner.setEnabled(false);
            valueTextView = itemView.findViewById(R.id.tag_value);
        }
    }
}
