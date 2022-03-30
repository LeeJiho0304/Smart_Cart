package com.example.weeheajin.cart;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by weeheajin on 2017-08-09.
 */

public class BluetoothActivity extends AppCompatActivity {

    Check ch = new Check();
    String cartId = null;
    ArrayList<String> cartArr = null;
    ConnectivityManager conManager = null;
    NetworkInfo netInfo = null;
    WebView myWebView;
    String url = "";
    int total = 0;
    TextView tvTotal;
    int same = 0; //0=같지 않음, 1=같음
    AlertDialog.Builder builder;
    Intent intent;
    private ShoppingDBHelper shoppingDBHelper;
    String str = "";
    LinearLayout L1;
    LinearLayout L2;
    TextView tv_shop;
    int ok_check = 0;

    public BluetoothAdapter mBluetoothAdapter = null;
    public Set<BluetoothDevice> mDevices;
    private static final int REQUEST_ENABLE_BT = 100;

    BluetoothSocket mSocket = null;
    public OutputStream mOutputStream = null;
    public InputStream mInputStream = null;
    String mStrDelimiter = "\n";
    char mCharDelimiter =  '\n';

    public Thread mWorkerThread = null;
    public byte[] readBuffer;
    public int readBufferPosition;

    private ArrayList<BasketData> basketDataArrayList;
    private BasketAdapter basketAdapter;
    private ListView listView;

    private final Handler mHandler = new Handler() {
        //핸들러의 기능을 수행할 클래스(handleMessage)
        public void handleMessage(Message msg) {
            //BluetoothService로부터 메시지(msg)를 받는다.
            super.handleMessage(msg);
            Log.i("BluetoothTask", "블루투스 연결 성공");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        getSupportActionBar().setTitle("D's Smart Cart");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF0099CC));

        myWebView = (WebView) findViewById(R.id.webView);
        myWebView.getSettings().setJavaScriptEnabled(true);   // 자바스크립트를 사용할 수 있도록 해줍니다.
        myWebView.getSettings().setBuiltInZoomControls(true);
        myWebView.loadUrl("http://52.79.205.255:8080/Sample/CartConnectionStateList.jsp");

        Button cartList = (Button)findViewById(R.id.cart_list_page);
        cartList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myWebView.reload();
            }
        });

        Button stop = (Button)findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cartId != null) {
                    sendData("D");
                    usingCart(cartId, 0);
                    ch.setCheck(false);
                    Toast.makeText(BluetoothActivity.this, "블루투스 연결이 끊어졌습니다", Toast.LENGTH_SHORT).show();
                    try {
                        mWorkerThread.interrupt();   // 데이터 수신 쓰레드 종료
                        mInputStream.close();
                        mOutputStream.close();
                        mSocket.close();
                    }catch(Exception e) {
                        Log.i("BluetoothTask", "stop function Error");
                    }
                    finish();
                }
            }
        });

        tv_shop = (TextView)findViewById(R.id.tv_shop);
        tv_shop.setText("카트와 통신 준비 중입니다");

        tvTotal = (TextView) findViewById(R.id.tv_total);
        basketDataArrayList = new ArrayList<BasketData>();
        basketAdapter = new BasketAdapter(this, R.layout.basket_adapter_view, basketDataArrayList);
        listView = (ListView) findViewById(R.id.list_nfc);
        listView.setAdapter(basketAdapter);

        shoppingDBHelper = new ShoppingDBHelper(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cartId != null) {
            sendData("D");
            usingCart(cartId, 0);
            ch.setCheck(false);
            Toast.makeText(BluetoothActivity.this, "블루투스 연결이 끊어졌습니다", Toast.LENGTH_SHORT).show();
            try {
                mWorkerThread.interrupt();   // 데이터 수신 쓰레드 종료
                mInputStream.close();
                mOutputStream.close();
                mSocket.close();
            } catch (Exception e) {
                Log.i("BluetoothTask", "stop function Error");
            }
        }
    }

    public void checkBluetooth(View view) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter == null ) {
            // 블루투스 미지원 기기일 경우
            Toast.makeText(this, "기기가 블루투스를 지원하지 않습니다", Toast.LENGTH_LONG).show();
        }
        else {
            if (!mBluetoothAdapter.isEnabled()) {
                // 장치가 블루투스를 지원하지만 비활성 상태인 경우
                // 블루투스를 활성 상태로 바꾸기 위해 사용자 동의 요청

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                // onActivityResult메소드가 결과로 RESULT_OK를 받으면 블루투스를 활성화 시키고
                // 페어링 된 기기 목록을 보여주며 연결할 장치를 선택하게 할 것임.
            }
            else { // 블루투스가 활성화 되어 있는 상태
                select();
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK)
                    select();// 페어링 된 기기 목록을 보여주고 연결할 장치를 선택
                else if (resultCode == Activity.RESULT_CANCELED)
                    Toast.makeText(this, "블루투스를 사용하지 않습니다", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void select() {
        // getBondedDevices() : 페어링된 장치 목록 얻어오는 함수.
        mDevices = mBluetoothAdapter.getBondedDevices();

        if(mDevices.size() == 0 ) {
            // 페어링된 장치가 없는 경우.
            Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            finish(); // App 종료.
        }
        else {
            // 페어링된 장치가 있는 경우.
            final AlertDialog.Builder builderBT = new AlertDialog.Builder(this);
            builderBT.setTitle("블루투스 장치 선택");

            // 페어링 된 디바이스들 목록 표시
            List<String> listItems = new ArrayList<String>();
            try {
                conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = conManager.getActiveNetworkInfo();
                url = "http://52.79.205.255:8080/Sample/CartConnectionJson.jsp";
                if (netInfo != null && netInfo.isConnected()) {
                    new DownloadJson().execute(url);
                    for (BluetoothDevice device : mDevices) {
                        for (int i=0; i<cartArr.size(); i++) {
                            String cart = cartArr.get(i).toString();
                            if (cart.equals(device.getName()))
                                listItems.add(device.getName());
                        }
                    }
//                    for (BluetoothDevice device : mDevices)
//                        listItems.add(device.getName());
                }
                else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Network isn't connected", Toast.LENGTH_LONG);
                    toast.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // CharSequence : 변경 가능한 문자열.
            // toArray : List형태로 넘어온것 배열로 바꿔서 처리하기 위한 toArray() 함수.
            final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
            // toArray 함수를 이용해서 size만큼 배열이 생성 되었다.
            listItems.toArray(new CharSequence[listItems.size()]);

            builderBT.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    // 연결할 장치를 선택한 경우, 선택한 장치와 연결을 시도함.
                    connectToSelectedDevice(items[item].toString()); //장치와 연결
                }
            });
            builderBT.setNegativeButton("취소", null);
            builderBT.setCancelable(false);  // 뒤로 가기 버튼 사용 금지.
            builderBT.show();
        }
    }

    public BluetoothDevice getDeviceFromBondedList(String name) {
        BluetoothDevice selectedDevice = null;
        Set<BluetoothDevice> md = mDevices;
        for(BluetoothDevice device : md) {
            if(name.equals(device.getName())) {
                selectedDevice = device;
                break;
            }
        }
        return selectedDevice;
    }

    //  connectToSelectedDevice() : 원격 장치와 연결하는 과정
    //  실제 데이터 송수신을 위해서는 소켓으로부터 입출력 스트림을 얻고 입출력 스트림을 이용하여 이루어 진다.
    public void connectToSelectedDevice(String selectedDeviceName) {
        // BluetoothDevice 원격 블루투스 기기를 나타냄.
        BluetoothDevice mRemoteDevie = getDeviceFromBondedList(selectedDeviceName);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

        try {
            // 소켓 생성, RFCOMM 채널을 통한 연결.
            // createRfcommSocketToServiceRecord(uuid) = 원격 블루투스 장치와 통신할 수 있는 소켓을 생성하는 함수
            // 이 메소드가 성공하면 스마트폰과 페어링 된 디바이스간 통신 채널에 대응하는 BluetoothSocket 오브젝트를 리턴함.
            mSocket = mRemoteDevie.createRfcommSocketToServiceRecord(uuid);
            mSocket.connect(); // 소켓이 생성 되면 connect() 함수를 호출함으로써 두기기의 연결은 완료된다.

            // 데이터 송수신을 위한 스트림 얻기.
            // BluetoothSocket 오브젝트는 두개의 Stream을 제공한다.
            mOutputStream = mSocket.getOutputStream();  // 데이터를 보내기 위한 Strem
            mInputStream = mSocket.getInputStream();  //데이터를 받기 위한 Stream

            sendData("D");
            sendData("C");
            usingCart(selectedDeviceName, 1);
            ch.setCheck(true);
            cartId = selectedDeviceName;
            Toast.makeText(BluetoothActivity.this, "블루투스 연결에 성공했습니다", Toast.LENGTH_SHORT).show();

            L1 = (LinearLayout)findViewById(R.id.llshop1);
            L2 = (LinearLayout)findViewById(R.id.llshop2);
            L1.setVisibility(View.INVISIBLE);
            L2.setVisibility(View.VISIBLE);

            beginListenForData();  // 데이터 수신 준비
        }
        catch(Exception e) {
            // 블루투스 연결 중 오류 발생
            Log.i("BluetoothTask", "블루투스 연결 중 오류가 발생했습니다");
        }
    }

    // 데이터 수신(쓰레드를 사용하여 수신된 메시지를 계속 검사한다)
    public void beginListenForData() {

        readBufferPosition = 0;  // 버퍼 내 수신 문자 저장 위치.
        readBuffer = new byte[1024];  // 수신 버퍼.
        final ArrayList<String> dataArr = new ArrayList<String>();

        Toast.makeText(BluetoothActivity.this, "카트와의 통신을 준비중입니다.\n" +
                "카트에 가까이 다가가세요.", Toast.LENGTH_LONG).show();
        // 문자열 수신 쓰레드.
        mWorkerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()) {
                    try {
                        int byteAvailable = mInputStream.available();  // 수신 데이터 확인
                        if(byteAvailable > 0) {  // 데이터가 수신된 경우.
                            byte[] packetBytes = new byte[byteAvailable];
                            mInputStream.read(packetBytes);
                            for(int i=0; i<byteAvailable; i++) {
                                byte b = packetBytes[i];
                                if(b == mCharDelimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "UTF-8");
                                    readBufferPosition = 0;

                                    mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            str = data + mStrDelimiter;
                                            Log.i("BluetoothTask", str);
                                            shop(data);
                                        }
                                    });
                                }
                                else {   readBuffer[readBufferPosition++] = b;   }
                            }
                        }
                    } catch (Exception e) {
                        // 데이터 수신 중 오류 발생.
                        Log.i("BluetoothTask", "데이터 수신 중 오류가 발생 했습니다");
                    }
                }
            }
        });
        mWorkerThread.start();
    }

    public void sendData(String msg) {
        msg += mCharDelimiter; // 문자열 종료 표시
        try {
            mOutputStream.write(msg.getBytes()); // 문자열 전송
        } catch(Exception e) {
            // 문자열 전송 도중 오류가 발생한 경우.
            Log.i("BluetoothTask", "문자열 데이터 전송 도중 오류 발생");
        }
    }

    public void shop(String result) {
        conManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        netInfo = conManager.getActiveNetworkInfo();
        Log.i("testTagResult", result);

        if (result != null) {
//            if (result.equals("OK") || result.equals("PAYMENT")) {
//                if (result.equals("OK")) {
//                    builder = new AlertDialog.Builder(BluetoothActivity.this);
//                    builder.setTitle("카트와 통신에 성공했습니다");
//                    builder.setMessage("즐거운 쇼핑 되세요!");
//                    builder.setPositiveButton("쇼핑 시작", null);
//                    builder.show();
//                }
//                else if (result.equals("PAYMENT")) {
//                    builder = new AlertDialog.Builder(BluetoothActivity.this);
//                    builder.setTitle("결제를 진행할까요?");
//                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            makeData();
//                            intent = new Intent(BluetoothActivity.this, PaymentActivity.class);
//                            intent.putExtra("total", total);
//                            startActivity(intent);
//                        }
//                    });
//                    builder.setNegativeButton("아니요", null);
//                    builder.show();
//                }
//                return;
//            }
            url = "http://52.79.205.255:8080/Sample/NFCQuery.jsp?nfc_id=" + result;
            if (netInfo != null && netInfo.isConnected()) {
                    new DownloadJsonShop().execute(url);
            } else {
                Toast toast = Toast.makeText(getApplicationContext(), "Network isn't connected", Toast.LENGTH_LONG);
                toast.show();
            }
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

        protected void onPostExecute(String result) {
            try {
                JSONObject jsonMain = new JSONObject(result);
                JSONArray jarr = jsonMain.getJSONArray("List");

                cartArr = new ArrayList<String>();
                int cartArrNum = 0;
                for (int i=0; i<jarr.length(); i++) {
                    jsonMain = jarr.getJSONObject(i);
                    int use = jsonMain.getInt("isUse");
                    if (use == 0) { //현재 사용되지 않는 카트
                        String str = jsonMain.getString("id");
                        Log.i("btName: ", str);
                        cartArr.add(str);
                        Log.i("cartArr: ", cartArr.get(cartArrNum).toString());
                        cartArrNum++;
                    }
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class DownloadJsonShop extends AsyncTask<String,String,String> {
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
            Log.i("testTagResultShop", result);
            if (result.equals("일치하는 상품이 없습니다.")) {
                Log.i("str", str);
//                if (str.equals("OK"))
                tv_shop.setText("카트와 통신 중입니다.");
                if (tv_shop.getText()!=null && ok_check==1) {
                    AlertDialog.Builder builder2 = new AlertDialog.Builder(BluetoothActivity.this);
                    builder2.setTitle("결제를 진행할까요?");
                    builder2.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            makeData();
                            intent = new Intent(BluetoothActivity.this, PaymentActivity.class);
                            intent.putExtra("total", total);
                            startActivity(intent);
                        }
                    });
                    builder2.setNegativeButton("아니요", null);
                    builder2.show();
                }
            }
            else {
                try {
                    JSONObject jsonMain = new JSONObject(result);
                    String pn = jsonMain.getString("proName");
                    String pr = jsonMain.getString("proPrice");

                    int i;
                    for (i = 0; i < basketDataArrayList.size(); i++) {
                        if ((basketDataArrayList.get(i).getbName()).equals(pn)) {
                            same = 1;
                            basketDataArrayList.remove(i);
                            break;
                        }
                    }

                    if (same == 0) { //같지 않음
                        total += Integer.valueOf(pr);
                        basketDataArrayList.add(new BasketData(basketAdapter.getCount() + 1, pn, pr + " 원"));
                    } else if (same == 1) {
                        total -= Integer.valueOf(pr);
                    }
                    tvTotal.setText(total + " 원");
                    basketAdapter.notifyDataSetChanged();
                    same = 0;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ok_check = 1;
        }
    }

    public void payView(View view) {
        builder = new AlertDialog.Builder(BluetoothActivity.this);
        builder.setTitle("결제를 진행할까요?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                makeData();
                intent = new Intent(BluetoothActivity.this, PaymentActivity.class);
                intent.putExtra("total", total);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("아니요", null);
        builder.show();
    }

    public void makeData() {
        SQLiteDatabase db = shoppingDBHelper.getWritableDatabase();  //쓰기 가능 데이터베이스 가져오기
        ContentValues row = new ContentValues();  //테이블에 추가할 항목 설정

        SimpleDateFormat dateFormat = new  SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault());
        Date date = new Date();
        String strDate = dateFormat.format(date);

        for (int i=0; i<basketDataArrayList.size(); i++) {
            row.put("date", strDate);
            row.put("shopPname", basketDataArrayList.get(i).getbName());
            row.put("shopPrice", basketDataArrayList.get(i).getbPrice());
            db.insert(shoppingDBHelper.TABLE_NAME_SHOP, null, row);
        }
        //마무리 작업
        shoppingDBHelper.close();
    }

    public void usingCart (String id, int isUse) {
        try {
            if (netInfo != null && netInfo.isConnected()) {
//            http://52.79.205.255:8080/Sample/CartConnectionChange.jsp?id=카트번호(ddwucomBT,ddwucomBT01~03 중 택1)&isUse(0:사용가능,1사용중)
                url = "http://52.79.205.255:8080/Sample/CartConnectionChange.jsp?id=" + id + "&isUse=" + isUse;
                new UsingCartSend().execute(url);
            }
            else {
                Toast toast = Toast.makeText(getApplicationContext(), "Network isn't connected", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class UsingCartSend extends AsyncTask<String,String,String> {
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
            Log.i("usingCart send: ", result);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.shop_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                intent = new Intent(BluetoothActivity.this, SearchActivity.class);
                break;
            case R.id.menu_wish:
                intent = new Intent(BluetoothActivity.this, WishActivity.class);
                break;
            case R.id.menu_household:
                intent = new Intent(BluetoothActivity.this, Household_ledger_Activity.class);
                break;
        }
        startActivity(intent);
        return true;
    }



    @Override
    public void onBackPressed() {
        builder = new AlertDialog.Builder(BluetoothActivity.this);
        builder.setTitle("쇼핑을 중단하시겠습니까?");
        builder.setMessage("중단할 경우 결제는 진행되지 않습니다.");
        builder.setPositiveButton("중단", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("계속", null);
        builder.show();
    }

}