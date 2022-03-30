package com.example.weeheajin.cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by weeheajin on 2017-09-17.
 */

public class BasketAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<BasketData> BasketDataList;
    private LayoutInflater layoutInflater;

    public BasketAdapter(Context context, int layout, ArrayList<BasketData> myBasketList) {
        this.context = context;
        this.layout = layout;
        BasketDataList = myBasketList;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return BasketDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return BasketDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return BasketDataList.get(position).get_id();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final int pos = position;

        if (view == null) {
            view = layoutInflater.inflate(layout, viewGroup, false);
        }

        TextView bsNum = (TextView)view.findViewById(R.id.bs_num);
        TextView bsName = (TextView)view.findViewById(R.id.bs_name);
        TextView bsPrice = (TextView)view.findViewById(R.id.bs_price);

        bsNum.setText(Integer.valueOf(BasketDataList.get(pos).get_id()).toString());
        bsName.setText(BasketDataList.get(pos).getbName());
        bsPrice.setText(BasketDataList.get(pos).getbPrice());

        return view;
    }
}
