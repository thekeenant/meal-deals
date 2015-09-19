package com.keenant.mealdeal;

import java.util.Date;

public class Deal {

    private final int id;
    private final Restaurant restaurant;
    private final String text;
    private final String details;
    private final int image;
    private final Date end;

    public Deal(int id, Restaurant restaurant, String text, String details, int image, Date end) {
        this.id = id;
        this.restaurant = restaurant;
        this.text = text;
        this.details = details;
        this.image = image;
        this.end = end;
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

    public int getImage() {
        return image;
    }

    public Date getEnd() {
        return end;
    }
}