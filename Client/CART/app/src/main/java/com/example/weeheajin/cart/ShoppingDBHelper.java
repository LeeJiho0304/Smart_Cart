package com.example.weeheajin.cart;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.weeheajin.cart.UserDBHelper.DB_NAME;

/**
 * Created by weeheajin on 2017-09-22.
 */

public class ShoppingDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME_SHOP = "my_table_shop";

    public static final String SHOP_PNAME = "shopPname";
    public static final String SHOP_PRICE = "shopPrice";
    public static final String SHOP_BRAND = "shopBrand";

    public ShoppingDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createShopTable = "CREATE TABLE " + TABLE_NAME_SHOP +
                "( _id INTEGER PRIMARY KEY AUTOINCREMENT, date TEXT"
                + SHOP_PNAME + " TEXT, " + SHOP_PRICE + " TEXT, " + SHOP_BRAND + " TEXT)";
        sqLiteDatabase.execSQL(createShopTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
