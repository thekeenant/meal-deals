package com.keenant.mealdeal.activities;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.keenant.mealdeal.R;
import com.keenant.mealdeal.cove.Cove;
import com.keenant.mealdeal.cove.Deal;
import com.nineoldandroids.view.ViewHelper;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.w3c.dom.Text;

public class DealActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {
    private Deal deal;
    private MaterialEditText[] pins;
    private AlertDialog codeDialog;

    private ImageView imageView;
    private Toolbar toolbarView;
    private ObservableScrollView scrollView;
    private int imageHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        setContentView(R.layout.activity_deal);

        toolbarView = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbarView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView) findViewById(R.id.deal_image);
        toolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0f, getResources().getColor(R.color.theme_green)));
        toolbarView.setTitleTextColor(ScrollUtils.getColorWithAlpha(0f, Color.WHITE));

        scrollView = (ObservableScrollView) findViewById(R.id.scrollview);
        scrollView.setScrollViewCallbacks(this);

        imageHeight = 240;

        int id = getIntent().getExtras().getInt("deal");
        this.deal = Cove.getInstance(this).getDeal(id);

        getSupportActionBar().setTitle(deal.getRestaurant().getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView.setImageDrawable(new BitmapDrawable(getResources(), deal.getImage()));

        TextView text = (TextView) findViewById(R.id.deal_text);
        text.setText(deal.getText());

        TextView details = (TextView) findViewById(R.id.deal_details);
        details.setText(deal.getDetails());

        TextView location = (TextView) findViewById(R.id.deal_location);
        location.setText(deal.getRestaurant().getName() + "\n" + deal.getRestaurant().getLocation());

        ButtonRectangle claim = (ButtonRectangle) findViewById(R.id.claim_deal);
        claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeDialog = dialog();
                codeDialog.show();
            }
        });

        MapView mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        GoogleMap map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this);

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(
                deal.getRestaurant().getMall().getAddress().getLatitude(),
                deal.getRestaurant().getMall().getAddress().getLongitude()),
                1);

        map.animateCamera(cameraUpdate);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(scrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.theme_green);
        float alpha = 1 - (float) Math.max(0, imageHeight - scrollY) / imageHeight;
        toolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        toolbarView.setTitleTextColor(ScrollUtils.getColorWithAlpha(alpha, Color.WHITE));
        ViewHelper.setTranslationY(imageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }


    private AlertDialog dialog() {
        AlertDialog.Builder codeDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_pin, null);

        codeDialog.setTitle("Coupon Code");
        codeDialog.setMessage("Ask for the Meal Deal code!");
        codeDialog.setCancelable(true);
        codeDialog.setView(view);

        final MaterialEditText pin1 = (MaterialEditText) view.findViewById(R.id.pin_1);
        final MaterialEditText pin2 = (MaterialEditText) view.findViewById(R.id.pin_2);
        final MaterialEditText pin3 = (MaterialEditText) view.findViewById(R.id.pin_3);
        final MaterialEditText pin4 = (MaterialEditText) view.findViewById(R.id.pin_4);
        pins = new MaterialEditText[] {pin1, pin2, pin3, pin4};

        for (int x = 0; x < pins.length; x++) {
            final MaterialEditText pin = pins[x];

            final int i = x;

            pin.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (i + 1 <= pins.length - 1) {
                        MaterialEditText next = pins[i + 1];
                        if (pin.getText().toString().length() == 1)
                            next.requestFocus();
                    }
                    else if (pin.getText().toString().length() == 1) {
                        attemptDeal();
                    }
                    if (i - 1 >= 0) {
                        MaterialEditText prev = pins[i - 1];
                        if (s.length() == 0)
                            prev.requestFocus();
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });

            if (x - 1 >= 0) {
                final MaterialEditText prev = pins[x - 1];

                pin.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_DEL)
                            prev.requestFocus();
                        return false;
                    }
                });
            }

        }

        return codeDialog.create();
    }

    private void attemptDeal() {
        String nums = pins[0].getText() + "" + pins[1].getText() + "" + pins[2].getText() + "" + pins[3].getText() + "";
        int num = Integer.parseInt(nums);

        if (num == deal.getCode()) {
            success().show();
            codeDialog.hide();
        }
        else
            fail().show();
    }

    private AlertDialog success() {
        AlertDialog.Builder codeDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_success, null);


        TextView text = (TextView) view.findViewById(R.id.success_msg);
        text.setText("Deal Sealed!\n+5 points");


        codeDialog.setView(view);

        return codeDialog.create();
    }

    private AlertDialog fail() {
        AlertDialog.Builder codeDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_fail, null);

        codeDialog.setView(view);

        return codeDialog.create();
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