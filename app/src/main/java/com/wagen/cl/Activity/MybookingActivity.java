package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.wagen.cl.Adapter.BookingAdapter;
import com.wagen.cl.Model.OrderModel;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

import java.util.ArrayList;

public class MybookingActivity extends BaseActivity {
    ListView listview;
    ArrayList<OrderModel> orderModels = new ArrayList<>();
    BookingAdapter bookingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybooking);
        listview =(ListView)findViewById(R.id.listview);
        for(int i=0; i<10; i++){
            OrderModel orderModel = new OrderModel();
            orderModel.order_id = i;
            orderModel.car_id = 1;
            orderModel.package_id = 1;
            orderModel.order_date = "2020/09/23";
            orderModel.order_time = "10:30";
            orderModel.duration_time = i+7;
            orderModel.total_price = String.valueOf(i*13);
            orderModels.add(orderModel);
        }
        bookingAdapter = new BookingAdapter(this, orderModels);
        listview.setAdapter(bookingAdapter);
    }

    public void goback(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}