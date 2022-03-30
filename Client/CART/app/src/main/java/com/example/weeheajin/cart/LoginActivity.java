package com.example.weeheajin.cart;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class LoginActivity extends AppCompatActivity {

    EditText id;
    EditText passwd;
    Button login;
    String url = "";
//    LinearLayout Lin;
//    LinearLayout Lout;
//    Check ch;

//    TextView userName;
//    TextView userId;
//    TextView userPhone;
//    TextView userPoint;
    String loginId;
    String loginPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("D's Smart Cart");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF0099CC));

        id = (EditText) findViewById(R.id.id);
        passwd = (EditText) findViewById(R.id.passwd);
        login = (Button) findViewById(R.id.btn_login);

//        ch = new Check();
//        Lin = (LinearLayout)findViewById(R.id.ll_user_info);
//        Lout = (LinearLayout)findViewById(R.id.ll_login);
//
//        if (ch.getWhere() == 0) {
//            Lout.setVisibility(View.VISIBLE);
//            Lin.setVisibility(View.INVISIBLE);
//        }
//        else if (ch.getWhere() == 1) {
//            Lout.setVisibility(View.INVISIBLE);
//            Lin.setVisibility(View.VISIBLE);
//            loginProcess(loginId, loginPw);
//        }
//
//        userName = (TextView)findViewById(R.id.user_name);
//        userId = (TextView)findViewById(R.id.user_id);
//        userPhone = (TextView)findViewById(R.id.user_phone);
//        userPoint = (TextView)findViewById(R.id.user_point);
    }

    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            loginId = id.getText().toString();
            loginPw = passwd.getText().toString();
            loginProcess(loginId, loginPw);
//            ch.setWhere(1);
        }
        else if (v.getId() == R.id.btn_member) {
            Intent intent = new Intent(LoginActivity.this, MemberActivity.class);
            startActivity(intent);
        }
//        else if (v.getId() == R.id.btn_back) {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//        }
    }

    private void loginProcess(String loginId, String loginPasswd) {
        try {
            url = "http://52.79.205.255:8080/Sample/Login.jsp?strID=" + loginId + "&strPwd=" + loginPasswd;
            ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conManager.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnected()) {
//                if (ch.getWhere() == 0)
                    new DownloadJson().execute(url);
//                else if (ch.getWhere() == 1)
//                    new DownloadJsonUser().equals(url);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Network isn't connected", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
            String str = "";
            String name = "";
            try {
                JSONObject jsonMain = new JSONObject(result);
                String point = "My Point: " + jsonMain.getString("userPoint");
                String phone = "Phone Number: " + jsonMain.getString("userPhone") + "\n";
                name = "User Name: " + jsonMain.getString("userName");
                str = name + "\n" + phone + point;
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            if (str == "") {
                builder.setMessage("로그인에 실패하셨습니다.");
                builder.setPositiveButton("확인", null);
            }
            else {
                builder.setMessage("로그인 성공\n" + name + "님, 환영합니다!\n\n" + str);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
            builder.show();
            passwd.setText(null);
            id.setText(null);
        }
    }

    private class DownloadJsonUser extends AsyncTask<String,String,String> {
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

//                userName.setText(name);
//                userId.setText(loginId);
//                userPhone.setText(phone);
//                userPoint.setText(point);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
