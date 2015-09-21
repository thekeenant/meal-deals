package com.keenant.mealdeal.cove;

import android.graphics.Bitmap;

import java.util.Date;

public class Deal {
    private final int id;
    private final Restaurant restaurant;
    private final String text;
    private final String details;
    private final String imagePath;
    private final Date end;
    private int code;

    private Bitmap image;

    public Deal(int id, Restaurant restaurant, String text, String details, String imagePath, Date end, int code) {
        this.id = id;
        this.restaurant = restaurant;
        this.text = text;
        this.details = details;
        this.imagePath = imagePath;
        this.end = end;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public String getText() {
        return text;
    }

    public String getDetails() {
        return details;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Date getEnd() {
        return end;
    }

    public int getCode() {
        return code;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}