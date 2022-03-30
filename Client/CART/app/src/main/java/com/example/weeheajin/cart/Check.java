package com.example.weeheajin.cart;

import android.app.Activity;

/**
 * Created by weeheajin on 2017-09-10.
 */

public class Check extends Activity{

    private boolean check;
    private int where = 0; //0 = out(로그인 안함), 1 = in(로그인한 상태)

    public boolean getCheck() {
        return check;
    }
    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getWhere() {
        return where;
    }
    public void setWhere(int where) {
        this.where = where;
    }

}
