package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.wagen.cl.Adapter.BookingAdapter;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Model.CarModel;
import com.wagen.cl.Model.MembershipModel;
import com.wagen.cl.Model.OrderModel;
import com.wagen.cl.Model.Packages;
import com.wagen.cl.Model.Service;
import com.wagen.cl.Model.UserModel;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MybookingActivity extends BaseActivity {
    ListView listview;
    ArrayList<OrderModel> orderModels = new ArrayList<>();
    BookingAdapter bookingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mybooking);
        listview =(ListView)findViewById(R.id.listview);
        callorderhistory();

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

    private void callorderhistory() {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(Constants.userModel.user_id));
        call_postApi(Constants.BASE_URL, "getallmyorder", params);
    }

    public void returnapireponse(JSONObject response, String method) {
        try {
            String result_code = response.getString("message");
            if (result_code.equals("success")) {
               // Toast.makeText(MybookingActivity.this, "Success", Toast.LENGTH_SHORT).show();

                JSONArray orders = response.getJSONArray("orders");
                orderModels = new ArrayList<>();
                for(int i=0; i<orders.length(); i++){
                    JSONObject oneorder = orders.getJSONObject(i);
                    OrderModel orderModel = new OrderModel();
                    orderModel.order_id = oneorder.getInt("order_id");
                    orderModel.car_id = oneorder.getInt("car_id");
                    orderModel.package_id = oneorder.getInt("package_id");
                    orderModel.order_date = oneorder.getString("order_date");
                    orderModel.order_time = oneorder.getString("order_time");
                    orderModel.duration_time = oneorder.getInt("duration_time");
                    orderModel.total_price = oneorder.getString("total_price");
                    orderModel.membership_id= oneorder.getInt("membership_id");
                    orderModel.order_type=oneorder.getInt("order_type");
                    orderModel.city = oneorder.getString("order_city");
                    orderModel.address = oneorder.getString("order_address");
                    orderModel.order_rating = Float.parseFloat(oneorder.getString("order_rating"));
                    orderModel.order_comment = oneorder.getString("order_comment");
                    orderModels.add(orderModel);
                }
                bookingAdapter = new BookingAdapter(this, orderModels);
                listview.setAdapter(bookingAdapter);
            }else {
                Toast.makeText(MybookingActivity.this, result_code, Toast.LENGTH_SHORT).show();
            }
        } catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(MybookingActivity.this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
        }
    }
}