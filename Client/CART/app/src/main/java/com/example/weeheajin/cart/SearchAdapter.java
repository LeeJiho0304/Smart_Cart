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

public class SearchAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<ProductData> productDataList;
    private LayoutInflater layoutInflater;

    public SearchAdapter(Context context, int layout, ArrayList<ProductData> mySearchList) {
        this.context = context;
        this.layout = layout;
        this.productDataList = mySearchList;
        layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return productDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return productDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productDataList.get(position).get_id();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        final int pos = position;

        if (view == null) {
            view = layoutInflater.inflate(layout, viewGroup, false);
        }

        TextView tvNum = (TextView)view.findViewById(R.id.tv_num);
        TextView tvInfo = (TextView)view.findViewById(R.id.tv_info);
        TextView tvDetail = (TextView)view.findViewById(R.id.tv_detail_info);

        tvNum.setText(Integer.valueOf(productDataList.get(pos).get_id()).toString());
        tvInfo.setText(productDataList.get(pos).getInfo());
        tvDetail.setText(productDataList.get(pos).getDetail_info());

        return view;
    }
}
