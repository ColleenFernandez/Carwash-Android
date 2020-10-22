package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

public class OrderdetailActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
    }

    public void placeorder(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void goback(View view) {
        Intent intent = new Intent(this, Order1Activity.class);
        startActivity(intent);
        finish();
    }
}