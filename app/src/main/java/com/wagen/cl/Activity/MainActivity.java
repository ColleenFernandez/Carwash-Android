package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Model.OrderModel;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Constants.orderModel = new OrderModel();
    }

    public void gotobookingservice(View view) {
        Intent intent = new Intent(this, Order1Activity.class);
        startActivity(intent);
        finish();
    }

    public void gotomybooking(View view) {
    }

    public void gotomyprofile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotosetting(View view) {
    }
}