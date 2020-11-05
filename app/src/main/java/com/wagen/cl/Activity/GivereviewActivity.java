package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wagen.cl.Adapter.BookingAdapter;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Model.CarModel;
import com.wagen.cl.Model.OrderModel;
import com.wagen.cl.Model.Packages;
import com.wagen.cl.Model.Service;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GivereviewActivity extends BaseActivity {
    ArrayList<OrderModel> orderModels = new ArrayList<>();
    ImageView imv_1;
    TextView txv_1,txv_package,txv_package_price,txv_package_durationtime,txv_package_descripton;
    LinearLayout lyt_services;
    RatingBar ratingbar;
    EditText etx_review;
    TextView txv_givereview;
    OrderModel orderModel = new OrderModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_givereview);
        imv_1=(ImageView)findViewById(R.id.imv_1);
        txv_1=(TextView)findViewById(R.id.txv_1);
        txv_package=(TextView)findViewById(R.id.txv_package);
        txv_package_price=(TextView)findViewById(R.id.txv_package_price);
        txv_package_durationtime=(TextView)findViewById(R.id.txv_package_durationtime);
        txv_package_descripton=(TextView)findViewById(R.id.txv_package_descripton);
        lyt_services = (LinearLayout)findViewById(R.id.lyt_services);

        ratingbar =(RatingBar)findViewById(R.id.ratingbar);
        etx_review =(EditText)findViewById(R.id.etx_review);
        txv_givereview =(TextView)findViewById(R.id.txv_givereview);
        orderModel = Constants.orderModel;
        showorder(orderModel);
    }

    public void goback(View view) {
        backtomain();
    }

    public void callreviewapi(View view) {
        Map<String, String> params = new HashMap<>();
        params.put("order_id", String.valueOf(orderModel.order_id));
        params.put("rating", String.valueOf(ratingbar.getRating()));
        params.put("comment", etx_review.getText().toString());
        call_postApi(Constants.BASE_URL, "giveorderreivew", params);
    }

    @Override
    public void onBackPressed() {
        backtomain();
    }
    public void backtomain(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }



    public void returnapireponse(JSONObject response, String method) {
        try {
            String result_code = response.getString("message");
            if (result_code.equals("success")) {
                showmessage(getString(R.string.sentreview));
            }else {
                Toast.makeText(GivereviewActivity.this, result_code, Toast.LENGTH_SHORT).show();
            }
        } catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(GivereviewActivity.this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
        }
    }
    public void showorder(OrderModel orderModel){
        ArrayList<CarModel> carModels = Preference.getInstance().getSharedcarmodelPreference(this, PrefConst.PREFKEY_CARS);
        ArrayList<Packages> packages = Preference.getInstance().getSharedpackagesmodelPreference(this, PrefConst.PREFKEY_PACKAGES);
        //ArrayList<Service> services = Preference.getInstance().getSharedservicePreference(this, PrefConst.PREFKEY_SERVICES);

        for(int i=0; i<carModels.size(); i++){
            if(orderModel.car_id == carModels.get(i).car_id){
                txv_1.setText(carModels.get(i).car_name);
                Glide.with(this).load(Uri.parse(carModels.get(0).car_photo)).into(imv_1);
            }
        }

        for(int i=0; i<packages.size(); i++){
            if(orderModel.package_id == packages.get(i).package_id){
                txv_package.setText(packages.get(i).package_name);
                txv_package_durationtime.setText(packages.get(i).package_time+"MINS");
                txv_package_descripton.setText(packages.get(i).package_description.replaceAll("_","\n"));
                for(int i1 = 0; i1<packages.get(i).packagePricesModels.size(); i1++){
                    if(packages.get(i).packagePricesModels.get(i1).car_id == orderModel.car_id){
                        if(orderModel.order_type == 0)
                            txv_package_price.setText(chagnenumberformat(packages.get(i).packagePricesModels.get(i1).price)+"CLP");
                        else if(orderModel.order_type == 1)
                            txv_package_price.setText(chagnenumberformat(packages.get(i).packagePricesModels.get(i1).price_home)+"CLP");
                    }
                }
            }
        }

        ArrayList<Service> services1 = orderModel.services;
        for(int i=0; i<services1.size(); i++){
            View child = getLayoutInflater().inflate(R.layout.service_item, null);
            TextView txvtitle = (TextView) child.findViewById(R.id.txv_title1);
            TextView txvprice =(TextView)child.findViewById(R.id.txv_price1);
            TextView txvtime =(TextView)child.findViewById(R.id.txv_time1);
            txvtitle.setText(services1.get(i).service_name);
            txvprice.setText(chagnenumberformat(services1.get(i).service_price)+"CLP");
            txvtime.setText(services1.get(i).service_time+"Mins");
            lyt_services.addView(child);
        }

        if(orderModel.order_rating != -1){
            ratingbar.setRating(orderModel.order_rating);
        }
        if(orderModel.order_comment != null && !orderModel.order_comment.equals("null")){
            etx_review.setText(orderModel.order_comment);
        }
    }

    public String chagnenumberformat(String number){
        DecimalFormat f = new DecimalFormat("#,###");
        return  f.format(Double.parseDouble(number));
    }

    public void showmessage(String message){
        Dialog settingdialog = new Dialog(GivereviewActivity.this);
        settingdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingdialog.setContentView(R.layout.message_dialog);
        settingdialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        settingdialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        TextView txv_title =(TextView)settingdialog.findViewById(R.id.txv_title);
        ImageView imv_close =(ImageView)settingdialog.findViewById(R.id.imv_close);
        imv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(GivereviewActivity.this, MainActivity.class);
                startActivity(intent);*/
                finish();
            }
        });
        txv_title.setText(message);
        settingdialog.show();
        settingdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                /*Intent intent = new Intent(GivereviewActivity.this, MainActivity.class);
                startActivity(intent);*/
                finish();
            }
        });
    }
}