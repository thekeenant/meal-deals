package com.keenant.mealdeal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DealActivity extends AppCompatActivity {

    private PullToRefreshView refreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshView = (PullToRefreshView) findViewById(R.id.pull_to_refresh);
        refreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshView.setRefreshing(false);
                    }
                }, 1500);
            }
        });

        ListView dealListView = (ListView) findViewById(R.id.deal_list);
        DealAdapter dealAdapter = new DealAdapter(this, R.layout.item_deal);
        dealListView.setAdapter(dealAdapter);


        dealListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("MealDeal", "Clicked!");
            }
        });

        Restaurant restaurant = new Restaurant(1, "The Corner");

        {
            Date future = new Date(new Date().getTime() + 1000 * 60 * 60);
            List<Deal> deals = new ArrayList<>();

            deals.add(new Deal(1, restaurant, "Get a shake for free with purchase of a hamburger!", "This is awesome!", R.drawable.meal1, future));
            deals.add(new Deal(1, restaurant, "Free ice cream!", "This is awesome!", R.drawable.meal2, future));
            deals.add(new Deal(1, restaurant, "$5 footlong!", "This is awesome!", R.drawable.meal3, future));

            dealAdapter.addAll(deals);
        }

    }
}

