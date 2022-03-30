package com.example.weeheajin.cart;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.weeheajin.cart.UserDBHelper.DB_NAME;

/**
 * Created by weeheajin on 2017-09-22.
 */

public class WishDBHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME_WISH = "my_table_wish";

    public WishDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createWishTable = "CREATE TABLE " + TABLE_NAME_WISH +
                "( _id INTEGER PRIMARY KEY AUTOINCREMENT, wishItem TEXT, wishSelect TEXT)";
        sqLiteDatabase.execSQL(createWishTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
