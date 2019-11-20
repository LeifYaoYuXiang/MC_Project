package com.example.project;

public class Preference {
    private String description;
    private int imageViewId;

    public Preference(String description, int imageViewId) {
        this.description = description;
        this.imageViewId = imageViewId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageViewId() {
        return imageViewId;
    }

    public void setImageViewId(int imageViewId) {
        this.imageViewId = imageViewId;
    }
}
