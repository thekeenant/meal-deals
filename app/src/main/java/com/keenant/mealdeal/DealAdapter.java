package com.keenant.mealdeal;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class DealAdapter extends ArrayAdapter<Deal> {
    public DealAdapter(Context context, int resource) {
        super(context, resource, new ArrayList<Deal>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DealHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
            row = inflater.inflate(R.layout.item_deal, parent, false);

            holder = new DealHolder();
            holder.text = (TextView)row.findViewById(R.id.item_deal_text);
            holder.image = (ImageView)row.findViewById(R.id.item_deal_image);

            row.setTag(holder);
        }
        else {
            holder = (DealHolder) row.getTag();
        }

        Deal deal = getItem(position);
        holder.text.setText(deal.getText());
        holder.image.setImageResource(deal.getImage());

        return row;
    }

    private static class DealHolder {
        TextView text;
        ImageView image;
    }
}
