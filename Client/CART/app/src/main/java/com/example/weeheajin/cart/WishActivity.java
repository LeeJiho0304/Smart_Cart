package com.example.weeheajin.cart;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class WishActivity extends AppCompatActivity {

    private EditText et_wish;
    Button add;
    String selectNum = "1";

    private ArrayList<WishData> wishDataArrayList;
    private WishAdapter wishAdapter;
    private ListView wishListView;
    AlertDialog.Builder builder;

    private WishDBHelper wishDBHelper;      // SQLiteOpenHelper 상속 클래스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);

        getSupportActionBar().setTitle("D's Smart Cart");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF0099CC));

        add = (Button)findViewById(R.id.btn_add);
        add.setOnClickListener(addWish);     // 추가 버튼 클릭 시

        wishDBHelper = new WishDBHelper(this);

        wishDataArrayList = new ArrayList<WishData>();
        wishAdapter = new WishAdapter(this, R.layout.wish_adapter_view, wishDataArrayList);
        wishListView = (ListView) findViewById(R.id.list_wish);
        wishListView.setAdapter(wishAdapter);
        wishListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //항목 클릭 시 수행할 동작
                String pName = wishListView.getItemAtPosition(position).toString();
                Intent intent = new Intent(WishActivity.this, DetailActivity.class);
                intent.putExtra("pName", pName);
                startActivity(intent);
            }
        });
        wishListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                builder = new AlertDialog.Builder(WishActivity.this);
                final int pos = position;

                builder.setTitle("항목을 삭제하시겠습니까?");
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase db = wishDBHelper.getWritableDatabase();  //쓰기 가능 데이터베이스 가져오기
                        String whereClause = "wishItem=?";  //삭제를 위한 where 절 구성
                        String[] whereArgs = new String[] { wishDataArrayList.get(pos).getItem().toString() };

                        //삭제 수행 - 성공 시 삭제된 레코드 개수를 반환
                        long result = db.delete(WishDBHelper.TABLE_NAME_WISH, whereClause, whereArgs);
                        if (result > 0)
                            Toast.makeText(WishActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(WishActivity.this, "Failure!", Toast.LENGTH_SHORT).show();

                        //마무리 작업 = 헬퍼 객체 및 커서 객체 등 close
                        wishDBHelper.close();
                        viewTable();
                    }
                });
                builder.setNegativeButton("아니요", null);
                builder.show();
                return true;
            }
        });
        viewTable();
    }

    //버튼 눌러서 항목 추가
    View.OnClickListener addWish = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            SQLiteDatabase db = wishDBHelper.getWritableDatabase();  //쓰기 가능 데이터베이스 가져오기
            ContentValues row = new ContentValues();  //테이블에 추가할 항목 설정
            et_wish = (EditText) findViewById(R.id.et_wish);
            String wishItem = et_wish.getText().toString();
            row.put("wishItem", wishItem);

            //레코드 추가 - 레코드 추가를 성공할 경우 추가한 레코드 개수 반환
            long result = db.insert(WishDBHelper.TABLE_NAME_WISH, null, row);
            if (result > 0)
                Toast.makeText(WishActivity.this, "Success!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(WishActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
//            wishDataArrayList.add(new WishData(wishDataArrayList.size()+1, wishItem));

            //마무리 작업
            wishDBHelper.close();
            wishAdapter.notifyDataSetChanged();
            viewTable();
            et_wish.setText(null);
        }
    };

    private void viewTable() {
        wishDataArrayList.clear();

        //읽기 가능 데이터베이스 가져오기
        SQLiteDatabase db = wishDBHelper.getReadableDatabase();

        String[] cols = null;
        String whereClause = null;
        String[] whereArgs = null;

        Cursor cursor = db.query(WishDBHelper.TABLE_NAME_WISH, cols, whereClause, whereArgs, null, null, null, null);
        int i = 1;
        while (cursor.moveToNext()) {
//            int _id = cursor.getInt(0);
            String pName = cursor.getString(1);
            wishDataArrayList.add(new WishData(i++, pName));
        }

        //마무리 작업 = 헬퍼 객체 및 커서 객체 등 close
        cursor.close();
        wishDBHelper.close();

        wishAdapter.notifyDataSetChanged();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wish_menu, menu);
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
                intent = new Intent(WishActivity.this, SearchActivity.class);
                break;
            case R.id.menu_household:
                intent = new Intent(WishActivity.this, Household_ledger_Activity.class);
                break;
            case R.id.menu_userInfo:
                intent = new Intent(WishActivity.this, UserInfoActivity.class);
                break;
        }
        startActivity(intent);
        return true;
    }

}
