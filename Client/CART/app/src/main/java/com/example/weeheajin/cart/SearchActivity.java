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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText sv;
    String search = null;
    int selectNum = 1;
    String url1 = "http://52.79.205.255:8080/Sample/ProductQuery.jsp?product_name=";
    String url2 = "http://52.79.205.255:8080/Sample/ProductQuery.jsp?product_name=&kind_name=";
    String url3 = "http://52.79.205.255:8080/Sample/ProductQuery.jsp?product_name=&kind_name=&brand_name=";
    String URL = null;
    ConnectivityManager conManager;
    NetworkInfo netInfo;

    private ArrayList<ProductData> productDataArrayList;
    private SearchAdapter searchAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle("D's Smart Cart");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF0099CC));

        productDataArrayList = new ArrayList<ProductData>();
        searchAdapter = new SearchAdapter(this, R.layout.search_adapter_view, productDataArrayList);
        listView = (ListView) findViewById(R.id.list_search);
        listView.setAdapter(searchAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pName = productDataArrayList.get(position).getInfo();
                Log.i("Search pName", pName);
                //상세 정보를 보여주는 화면 불러옴.
                Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                intent.putExtra("pName", pName);
                startActivity(intent);
            }
        });

        conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conManager.getActiveNetworkInfo();
        sv = (EditText) findViewById(R.id.et_search);
        sv.setHint("상품명 검색");
        search = sv.getText().toString();
        URL = url1 + search;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pName:
                selectNum = 1;
                sv.setHint("상품명 검색");
                searchAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_pKind:
                selectNum = 2;
                sv.setHint("상품 종류 검색");
                searchAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_pBrand:
                selectNum = 3;
                sv.setHint("상품 브랜드 검색");
                searchAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_remove:
//                for (int i=0; i<productDataArrayList.size();) {
                    productDataArrayList.clear();
//                }
                searchAdapter.notifyDataSetChanged();
                break;
        }
    }

    public void enter(View view) {
        search = sv.getText().toString();
        try {
            if (selectNum == 1)
                URL = url1 + search + "&kind_name=&brand_name=";
            else if (selectNum == 2)
                URL = url2 + search + "&brand_name=";
            else if (selectNum == 3)
                URL = url3 + search;

            if (netInfo != null && netInfo.isConnected())
                new DownloadJson().execute(URL);
            else
                Toast.makeText(getApplicationContext(), "Network isn't connected", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sv.setText(null);
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
        protected void onPostExecute(String result) {
            Log.i("SearchResult", result);
            if (!result.equals("해당상품이 없습니다.")) {
                try {
                    JSONObject jsonMain = new JSONObject(result);
                    JSONArray jarr = jsonMain.getJSONArray("List");

                    if (selectNum == 1) {
                        for (int i = 0; i < jarr.length(); i++) {
                            jsonMain = jarr.getJSONObject(i);
                            String str = "가격: " + jsonMain.getString("proPrice") + "\t\t\t브랜드: " + jsonMain.getString("proBrandName");
                            productDataArrayList.add(new ProductData(searchAdapter.getCount() + 1, search, str));
                        }
                    } else if (selectNum == 2) {
                        for (int i = 0; i < jarr.length(); i++) {
                            jsonMain = jarr.getJSONObject(i);
                            String name = jsonMain.getString("proName");
                            String str = "가격: " + jsonMain.getString("proPrice") + "\t\t\t브랜드: " + jsonMain.getString("proBrandName");
                            productDataArrayList.add(new ProductData(searchAdapter.getCount() + 1, name, str));
                        }
                    } else if (selectNum == 3) {
                        for (int i = 0; i < jarr.length(); i++) {
                            jsonMain = jarr.getJSONObject(i);
                            String name = jsonMain.getString("proName");
                            String str = "가격: " + jsonMain.getString("proPrice") + "\t\t\t브랜드: " + jsonMain.getString("proBrandName");
                            productDataArrayList.add(new ProductData(searchAdapter.getCount() + 1, name, str));
                        }
                    }
                    searchAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
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
            case R.id.menu_wish:
                intent = new Intent(SearchActivity.this, WishActivity.class);
                break;
            case R.id.menu_household:
                intent = new Intent(SearchActivity.this, Household_ledger_Activity.class);
                break;
            case R.id.menu_userInfo:
                intent = new Intent(SearchActivity.this, UserInfoActivity.class);
                break;
        }
        startActivity(intent);
        return true;
    }

}
