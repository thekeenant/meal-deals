package com.keenant.mealdeal.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.provider.BigImageCardProvider;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.keenant.mealdeal.R;
import com.keenant.mealdeal.cove.Cove;
import com.keenant.mealdeal.cove.Mall;

import java.util.List;

public class MallListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_list);

        getSupportActionBar().setTitle("Select a Mall");

        Cove cove = Cove.getInstance(this);

        final List<Mall> malls = cove.getMalls();

        MaterialListView list = (MaterialListView) findViewById(R.id.mall_list);

        int[] images = new int[] {R.drawable.logo_sunway, R.drawable.logo_utama, R.drawable.logo_midvalley};

        for (Mall mall : malls) {
            Card card = new Card.Builder(this)
                    .withProvider(BigImageCardProvider.class)
                    .setDescription(mall.getName())
                    .setTitleColor(Color.WHITE)
                    .setDrawable(images[malls.indexOf(mall)])
                    .endConfig()
                    .build();
            list.add(card);
        }

        list.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, int i) {
                Intent intent = new Intent(MallListActivity.this, CategoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("mall", malls.get(i).getId());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int i) {

            }
        });
    }
}
