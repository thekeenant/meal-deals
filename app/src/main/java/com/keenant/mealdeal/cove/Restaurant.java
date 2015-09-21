package com.keenant.mealdeal.cove;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {

    private Mall mall;
    private int id;
    private String name;
    private String location;
    private List<Deal> deals = new ArrayList<>();

    public Restaurant(Mall mall, int id, String name, String location) {
        this.mall = mall;
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public Mall getMall() {
        return mall;
    }
}
