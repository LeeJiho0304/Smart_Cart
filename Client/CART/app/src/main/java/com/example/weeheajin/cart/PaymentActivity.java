package com.example.weeheajin.cart;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {

    private RadioGroup radioGroup = null;
    LinearLayout Lc;
    LinearLayout Lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getSupportActionBar().setTitle("D's Smart Cart");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF0099CC));

        Intent intent = getIntent();
        int total = intent.getIntExtra("total", 0);
        TextView sum = (TextView)findViewById(R.id.tv_sum);
        sum.setText(total + " 원");

        radioGroup = (RadioGroup) findViewById(R.id.rg1);
        radioGroup.setOnCheckedChangeListener(mRadioCheck);

        Lc = (LinearLayout)findViewById(R.id.ll_card);
        Lm = (LinearLayout)findViewById(R.id.ll_mobile);
    }

    RadioGroup.OnCheckedChangeListener mRadioCheck = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
            switch (checkedId) {
                case R.id.rb_phone:
                    Lc.setVisibility(View.GONE);
                    Lm.setVisibility(View.VISIBLE);
                    break;
                case R.id.rb_card:
                    Lc.setVisibility(View.VISIBLE);
                    Lm.setVisibility(View.GONE);
                    break;
            }
        }
    };

    public void onClick(View view) {
        Intent intent = new Intent(PaymentActivity.this, PaymentActivity2.class);
        startActivity(intent);
    }
}
