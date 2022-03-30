package com.example.weeheajin.cart;

/**
 * Created by weeheajin on 2017-08-09.
 */

public class WishData {

    private int _id;
    private String item;

    public WishData(int _id, String item) {
        this._id = _id;
        this.item = item;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

}
