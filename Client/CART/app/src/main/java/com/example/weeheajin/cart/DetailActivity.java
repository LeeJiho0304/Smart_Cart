package com.example.weeheajin.cart;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class DetailActivity extends AppCompatActivity {

    TextView product_name;
    TextView product_price;
    TextView product_brand;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setTitle("D's Smart Cart");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF0099CC));

        product_name = (TextView)findViewById(R.id.tv_product_name);
        product_price = (TextView)findViewById(R.id.tv_product_price);
        product_brand = (TextView)findViewById(R.id.tv_product_brand);

        Intent intent = getIntent();
        String pName = intent.getStringExtra("pName");
        Log.i("Detail pName", pName);

        url = "http://52.79.205.255:8080/Sample/ProductQuery.jsp?product_name=" + pName + "&kind_name=&brand_name=";
        try {
            ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                new DownloadJson().execute(url);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Network isn't connected", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (Exception e) {
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
            }
            catch (Exception e) {
                return "Data download failed";
            }
        }
        //{"Total":1,"List":[{"proId":"0001","proName":"서울우유","proPrice":"1500","proBrandName":"서울우유"}]}
        protected void onPostExecute(String result) {
            Log.i("DetailResult", result);
            if (!result.equals("해당상품이 없습니다.")) {
                try {
                    JSONObject jsonMain = new JSONObject(result);
                    JSONArray jarr = jsonMain.getJSONArray("List");
                    for (int i = 0; i < jarr.length(); i++) {
                        jsonMain = jarr.getJSONObject(i);
                        product_name.setText(jsonMain.getString("proName"));
                        product_price.setText(jsonMain.getString("proPrice") + " 원");
                        product_brand.setText(jsonMain.getString("proBrandName"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
