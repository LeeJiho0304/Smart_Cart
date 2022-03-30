package com.example.weeheajin.cart;

/**
 * Created by weeheajin on 2017-09-17.
 */

public class BasketData {

    private int _id;
    private String bName;
    private String bPrice;
//    private String bBrand;

    public BasketData(int _id, String bName, String bPrice) {//, String bBrand) {
        this._id = _id;
        this.bName = bName;
        this.bPrice = bPrice;
//        this.bBrand = bBrand;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getbName() {
        return bName;
    }

    public void setbName(String bName) {
        this.bName = bName;
    }

    public String getbPrice() {
        return bPrice;
    }

    public void setbPrice(String bPrice) {
        this.bPrice = bPrice;
    }

//    public String getbBrand() {
//        return bBrand;
//    }
//
//    public void setbBrand(String bBrand) {
//        this.bBrand = bBrand;
//    }

}
