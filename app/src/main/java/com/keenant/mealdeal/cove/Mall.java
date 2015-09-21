package com.keenant.mealdeal.cove;

import android.location.Address;

import java.util.ArrayList;
import java.util.List;

public class Mall {

    private int id;
    private String name;
    private Address address;
    private List<Restaurant> restaurants = new ArrayList<>();

    public Mall(int id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public List<Deal> getDeals() {
        List<Deal> list = new ArrayList<>();
        for (Restaurant r : restaurants)
            list.addAll(r.getDeals());
        return list;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }
}
