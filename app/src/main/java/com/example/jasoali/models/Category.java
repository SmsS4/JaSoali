package com.example.jasoali.models;

public class Category {
    private CategoryType type;
    private String value;

    public Category(CategoryType type, String value) {
        this.type = type;
        this.value = value;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
