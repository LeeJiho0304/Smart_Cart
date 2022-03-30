package com.example.weeheajin.cart;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by weeheajin on 2017-08-28.
 */

public class UserDBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "my_db";
    public static final String TABLE_NAME_USER = "my_table_user";

    public UserDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createUserTable = "CREATE TABLE " + TABLE_NAME_USER +
                "( _id INTEGER PRIMARY KEY AUTOINCREMENT, userId TEXT, userPw TEXT)";
        sqLiteDatabase.execSQL(createUserTable);

        sqLiteDatabase.execSQL("INSERT INTO " + TABLE_NAME_USER + " VALUES (null, 'Dongduk', 'com123');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
