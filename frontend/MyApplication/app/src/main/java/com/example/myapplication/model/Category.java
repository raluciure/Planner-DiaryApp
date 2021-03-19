package com.example.myapplication.model;

/**
 * This class represents the Category model used in front-end representing the CategoryDTO from
 * back-end
 */
public class Category extends AbstractModel {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
