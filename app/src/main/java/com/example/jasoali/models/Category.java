package com.example.jasoali.models;

public class Category {
    private CategoryType type;
    private String value;

    public Category(CategoryType type, String value) {
        this.type = type;
        this.value = value;
    }

    final public static String TERM = "ترم";
    final public static String COURSE = "درس";
    final public static String PROFESSOR = "استاد";
    final public static String DEPARTMENT = "دانشکده";
    final public static String UNIVERSITY = "دانشگاه";
    final public static String[] ALL_STRINGS = new String[]{
            TERM,
            COURSE,
            PROFESSOR,
            DEPARTMENT,
            UNIVERSITY
    };
    final public static CategoryType[] ALL_TYPES = new CategoryType[]{
            CategoryType.TERM,
            CategoryType.COURSE,
            CategoryType.PROFESSOR,
            CategoryType.DEPARTMENT,
            CategoryType.UNIVERSITY
    };
    public CategoryType getType() {
        return type;
    }

    static public CategoryType getCategoryByType(String typeString){
        switch (typeString){
            case Category.TERM:
                return CategoryType.TERM;
            case Category.COURSE:
                return CategoryType.COURSE;
            case Category.PROFESSOR:
                return CategoryType.PROFESSOR;
            case Category.DEPARTMENT:
                return CategoryType.DEPARTMENT;
            case Category.UNIVERSITY:
                return CategoryType.UNIVERSITY;
            default:
                return null;
        }
    }

    public String getStringType() {
        switch (type){
            case TERM:
                return Category.TERM;
            case COURSE:
                return Category.COURSE;
            case PROFESSOR:
                return Category.PROFESSOR;
            case DEPARTMENT:
                return Category.DEPARTMENT;
            case UNIVERSITY:
                return Category.UNIVERSITY;
            default:
                return null;
        }
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
