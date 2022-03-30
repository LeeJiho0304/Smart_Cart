package com.example.weeheajin.cart;

/**
 * Created by weeheajin on 2017-08-09.
 */

public class ProductData {

    private int _id;
    private String info;
    private String detail_info;

    public ProductData(int _id, String info, String detail_info) {
        this._id = _id;
        this.info = info;
        this.detail_info = detail_info;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDetail_info() {
        return detail_info;
    }

    public void setDetail_info(String detail_info) {
        this.detail_info = detail_info;
    }

}
