package com.example.weeheajin.cart;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class UserInfoActivity extends AppCompatActivity {

    TextView userName;
    TextView userId;
    TextView userPhone;
    TextView userPoint;
    String loginId;
    String loginPw;

    private UserDBHelper userDBHelper;      // SQLiteOpenHelper 상속 클래스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        getSupportActionBar().setTitle("D's Smart Cart");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF0099CC));

        userName = (TextView)findViewById(R.id.user_name);
        userId = (TextView)findViewById(R.id.user_id);
        userPhone = (TextView)findViewById(R.id.user_phone);
        userPoint = (TextView)findViewById(R.id.user_point);

        //읽기 가능 데이터베이스 가져오기
        userDBHelper = new UserDBHelper(this);
        SQLiteDatabase db = userDBHelper.getReadableDatabase();

        String[] cols = null;
        String whereClause = null;
        String[] whereArgs = null;

        Cursor cursor = db.query(UserDBHelper.TABLE_NAME_USER, cols, whereClause, whereArgs, null, null, null, null);
        if (cursor.moveToNext()) {
            loginId = cursor.getString(1);
            loginPw = cursor.getString(2);
            userId.setText(loginId.toString());
        }
        cursor.close();
        userDBHelper.close();

        try {
            String url = "http://52.79.205.255:8080/Sample/Login.jsp?strID=" + loginId + "&strPwd=" + loginPw;
            ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conManager.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
                new DownloadJson().execute(url);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Network isn't connected", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onClick(View view) {
        finish();
    }

    private class DownloadJson extends AsyncTask<String,String,String> {
        @Override
        protected String doInBackground(String... arg0) {
            try {
                Document doc = Jsoup.connect((String) arg0[0]).get();
                Elements body = doc.select("body");
                String bodyStr = body.text();
                return bodyStr;
            } catch (Exception e) {
                return "Data download failed";
            }
        }

        protected void onPostExecute(String result) {
            try {
                JSONObject jsonMain = new JSONObject(result);
                String point = jsonMain.getString("userPoint");
                String phone = jsonMain.getString("userPhone");
                String name = jsonMain.getString("userName");

                userName.setText(name);
                userId.setText(loginId);
                userPhone.setText(phone);
                userPoint.setText(point);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
