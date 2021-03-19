package com.example.myapplication.model;

/**
 * This class represents the Activity model used in front-end representing the ActivityDTO from
 * back-end
 */
public class Activity extends AbstractModel {
    private String name;

    private String description;

    private Category category;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
