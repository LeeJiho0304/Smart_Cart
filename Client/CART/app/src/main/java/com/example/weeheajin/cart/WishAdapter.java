package com.example.weeheajin.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by weeheajin on 2017-08-09.
 */

public class WishAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<WishData> wishDataList;
    private LayoutInflater layoutInflater;

    public WishAdapter(Context context, int layout, ArrayList<WishData> myWishList) {
        this.context = context;
        this.layout = layout;
        this.wishDataList = myWishList;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return wishDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return wishDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return wishDataList.get(position).get_id();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final int pos = position;

        if (view == null) {
            view = layoutInflater.inflate(layout, viewGroup, false);
        }

        TextView wNum = (TextView)view.findViewById(R.id.w_num);
        TextView wName = (TextView)view.findViewById(R.id.w_name);

        wNum.setText(Integer.valueOf(wishDataList.get(pos).get_id()).toString());
        wName.setText(wishDataList.get(pos).getItem());

        return view;
    }
}
