package com.keenant.mealdeal.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.BigImageCardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.keenant.mealdeal.cove.Cove;
import com.keenant.mealdeal.cove.Deal;
import com.keenant.mealdeal.LocationTracker;
import com.keenant.mealdeal.R;
import com.keenant.mealdeal.cove.Mall;
import com.yalantis.phoenix.PullToRefreshView;

public class DealListActivity extends AppCompatActivity {

    private PullToRefreshView refreshView;
    private LocationTracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int id = getIntent().getExtras().getInt("mall");
        final Mall mall = Cove.getInstance(this).getMall(id);


        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        tracker = new LocationTracker(locationManager);

        MaterialListView list = (MaterialListView) findViewById(R.id.deal_list);

        for (Deal deal : mall.getDeals()) {
            Card card = new Card.Builder(this)
                    .withProvider(BigImageCardProvider.class)
                    .setDescription(deal.getText() + " (" + deal.getRestaurant().getName() + ")")
                    .setTitleColor(Color.WHITE)
                    .setDrawable(new BitmapDrawable(getResources(), deal.getImage()))
                    .endConfig()
                    .build();
            list.add(card);
        }

        list.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(Card card, int i) {
                Deal deal = mall.getDeals().get(i);
                Intent intent = new Intent(DealListActivity.this, DealActivity.class);
                Bundle b = new Bundle();
                b.putInt("deal", deal.getId());
                intent.putExtras(b);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(Card card, int i) {
                onItemClick(card, i);
            }
        });

        list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                refreshView.setRefreshing(false);
            }
        });

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
