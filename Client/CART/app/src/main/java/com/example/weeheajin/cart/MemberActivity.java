package com.example.weeheajin.cart;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import static com.example.weeheajin.cart.R.id.et_id;

public class MemberActivity extends AppCompatActivity {

    EditText id;
    EditText name;
    EditText pwd;
    EditText phone;
    String url = "";
    private UserDBHelper userDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        getSupportActionBar().setTitle("D's Smart Cart");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF0099CC));

        name = (EditText) findViewById(R.id.et_name);
        id = (EditText) findViewById(et_id);
        pwd = (EditText) findViewById(R.id.et_pwd);
        phone = (EditText) findViewById(R.id.et_phone);

        userDBHelper = new UserDBHelper(this);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.btn_become_member) {
            String mId = id.getText().toString();
            String mName = name.getText().toString();
            String mPasswd = pwd.getText().toString();
            String mPhone = phone.getText().toString();

            if (mId.equals(null) || mName.equals(null) || mPasswd.equals(null) || mPhone.equals(null)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MemberActivity.this);
                builder.setMessage("정보가 모두 기입되지 않았습니다.\n" + "다시 입력해주세요.");
                builder.setPositiveButton("확인", null);
                builder.show();
            }
            else {
                memberProcess(mId, mName, mPasswd, mPhone);
                SQLiteDatabase db = userDBHelper.getWritableDatabase();  //쓰기 가능 데이터베이스 가져오기
                ContentValues row = new ContentValues();  //테이블에 추가할 항목 설정
                row.put("userId", mId);
                row.put("userPw", mPasswd);
                db.insert("my_table_user", null, row);
                userDBHelper.close(); //마무리 작업
            }
            id.setText(null);
            name.setText(null);
            pwd.setText(null);
            phone.setText(null);
        }
    }

    private void memberProcess(String mId, String mName, String mPwd, String mPhone) {
        try {
            url = "http://52.79.205.255:8080/Sample/join.jsp?id=" + mId + "&name=" + mName + "&passwd=" + mPwd + "&phone=" + mPhone;
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
            AlertDialog.Builder builder = new AlertDialog.Builder(MemberActivity.this);
            if (result.equals("1"))
                builder.setMessage("회원가입 성공\n" + "환영합니다!");
            else
                builder.setMessage("회원가입 실패\n" + "정보를 다시 입력해주세요.");
            builder.setPositiveButton("확인", null);
            builder.show();
        }
    }

}