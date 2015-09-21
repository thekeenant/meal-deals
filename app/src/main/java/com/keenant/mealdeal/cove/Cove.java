package com.keenant.mealdeal.cove;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Cove {

    private static Cove cove;

    public static Cove getInstance(Context context) {
        if (cove != null)
            return cove;
        return cove = new Cove(context, "http://md.keenant.com/");
    }

    private Context context;
    private String base;

    private List<Mall> malls = new ArrayList<>();
    private List<Restaurant> restaurants = new ArrayList<>();
    private List<Deal> deals = new ArrayList<>();

    public Cove(Context context, String base) {
        this.context = context;
        this.base = base;
        load();
    }

    public boolean authenticate(String username, String password) throws Exception {
        return Boolean.valueOf(getBody(base + "/users/auth.json?username=" + username + "&password=" + password));
    }

    public List<Mall> getMalls() {
        return malls;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    private String jsonMalls() {
        return base + "/malls.json";
    }

    private String getBody(String urlText) throws Exception {
        URL url = new URL(urlText);
        InputStream input = url.openStream();
        String body = "";
        Scanner scan = new Scanner(input, "UTF-8").useDelimiter("\\A");
        while (scan.hasNext())
            body += scan.next();
        return body;
    }

    public void load() {
        new DownloadJsonTask().execute(jsonMalls());
    }

    public void init(JSONArray jsonMalls) throws Exception {
        Geocoder geo = new Geocoder(context);

        for (int m = 0; m < jsonMalls.length(); m++) {
            JSONObject jsonMall = (JSONObject) jsonMalls.get(m);

            int mId = jsonMall.getInt("id");
            String name = jsonMall.getString("name");
            Address address = geo.getFromLocation(jsonMall.getDouble("latitude"), jsonMall.getDouble("longitude"), 1).get(0);

            Mall mall = new Mall(mId, name, address);

            malls.add(mall);

            JSONArray jsonRests = jsonMall.getJSONArray("restaurants");
            for (int r = 0; r < jsonRests.length(); r++) {
                JSONObject jsonRest = (JSONObject) jsonRests.get(r);

                int id = jsonRest.getInt("id");
                String rName = jsonRest.getString("name");
                String location = jsonRest.getString("location");

                Restaurant rest = new Restaurant(mall, id, rName, location);

                mall.getRestaurants().add(rest);
                restaurants.add(rest);

                JSONArray jsonDeals = jsonRest.getJSONArray("deals");
                for (int d = 0; d < jsonDeals.length(); d++) {
                    JSONObject jsonDeal = (JSONObject) jsonDeals.get(d);
                    SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");

                    int dId = jsonDeal.getInt("id");
                    String text = jsonDeal.getString("text");
                    String details = jsonDeal.getString("details");
                    Date date = simpleFormat.parse(jsonDeal.getString("date"));
                    int code = jsonDeal.getInt("code");
                    String imagePath = jsonDeal.getString("image_small");

                    Deal deal = new Deal(dId, rest, text, details, imagePath, date, code);

                    rest.getDeals().add(deal);
                    deals.add(deal);

                    new DownloadImageTask().execute(deal);
                }

            }
        }
    }

    public Deal getDeal(int id) {
        for (Deal deal : deals)
            if (id == deal.getId())
                return deal;
        return null;
    }

    public Mall getMall(int id) {
        for (Mall mall : malls)
            if (mall.getId() == id)
                return mall;
        return null;
    }

    private class DownloadJsonTask extends AsyncTask<String,Void,JSONArray> {
        protected JSONArray doInBackground(String... urls) {
            try {


                JSONArray array = new JSONArray(getBody(urls[0]));
                init(array);

                Log.e("MealDeals", "1" + deals.toString());
                Log.e("MealDeals", "2" + restaurants.toString());
                Log.e("MealDeals", "3" + malls.toString());



                return array;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private class DownloadImageTask extends AsyncTask<Deal, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Deal... deal) {
            try {
                Bitmap bmp =  BitmapFactory.decodeStream(new URL(base + deal[0].getImagePath()).openStream());
                deal[0].setImage(bmp);
                return bmp;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }


}
