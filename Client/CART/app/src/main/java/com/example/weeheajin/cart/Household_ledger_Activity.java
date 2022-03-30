package com.example.weeheajin.cart;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class Household_ledger_Activity extends AppCompatActivity {

    private ArrayList<String> ledgerList;
    private ArrayAdapter<String> adapter;
    private ListView ledgerListView;

    private ShoppingDBHelper shoppingDBHelper;      // SQLiteOpenHelper 상속 클래스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_household);

        getSupportActionBar().setTitle("D's Smart Cart");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF0099CC));

        ledgerList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(Household_ledger_Activity.this, android.R.layout.simple_list_item_1, ledgerList);
        ledgerListView = (ListView) findViewById(R.id.list_house);
        ledgerListView.setAdapter(adapter);
//        makeList1();
    }

    private void makeList1() {
//        ledgerList.clear();

        //읽기 가능 데이터베이스 가져오기
        SQLiteDatabase db = shoppingDBHelper.getReadableDatabase();

        String[] cols = null;
        String selection = null;
        String[] selectArgs = null;

        Cursor cursor = db.query(ShoppingDBHelper.TABLE_NAME_SHOP, cols, selection, selectArgs, null, null, null, null);
        int num = 1;
        String date1 = "";
        while (cursor.moveToNext()) {
            String date = cursor.getString(1);
            if (!date.equals(date1)) {
                num = 1;
                ledgerList.add(date);
                date1 = date;
            }
            else {
                String name = cursor.getString(2);
                String price = cursor.getString(3);
                String brand = cursor.getString(4);
                ledgerList.add(num++ + "\t\t\t" + name + "\t\t\t" + price + "\t\t\t" + brand);
            }
        }

        cursor.close();
        shoppingDBHelper.close();

        adapter.notifyDataSetChanged();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.house_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.menu_search:
                intent = new Intent(Household_ledger_Activity.this, SearchActivity.class);
                break;
            case R.id.menu_wish:
                intent = new Intent(Household_ledger_Activity.this, WishActivity.class);
                break;
            case R.id.menu_userInfo:
                intent = new Intent(Household_ledger_Activity.this, UserInfoActivity.class);
                break;
        }
        startActivity(intent);
        return true;
    }

}
