package com.example.weeheajin.cart;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    Check check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check = new Check();
        check.setCheck(false);

        getSupportActionBar().setTitle("D's Smart Cart");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF0099CC));
    }

    public void onClick(View v) {
        Intent intent = null;
        switch(v.getId()) {
            case R.id.btn_search:
                intent = new Intent(this, SearchActivity.class);
                break;
            case R.id.btn_wishlist:
                intent = new Intent(this, WishActivity.class);
                break;
            case R.id.btn_basket: //shopping
                intent = new Intent(this, BluetoothActivity.class);
                break;
            case R.id.btn_household_ledger:
                intent = new Intent(this, Household_ledger_Activity.class);
                break;
            case R.id.btn_mine:
                intent = new Intent(this, UserInfoActivity.class);
//                check.setWhere(1);
//                intent = new Intent(this, LoginActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
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
            case R.id.menu_logout:
                intent = new Intent(MainActivity.this, LoginActivity.class);
                break;
        }
        startActivity(intent);
        return true;
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("로그아웃 하시겠습니까?");
        builder.setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
//                check.setWhere(0);
            }
        });
        builder.setNegativeButton("아니요", null);
        builder.show();
    }

}
