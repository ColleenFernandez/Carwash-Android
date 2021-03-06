package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.solver.GoalRow;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Model.CarModel;
import com.wagen.cl.Model.Coupon;
import com.wagen.cl.Model.MembershipModel;
import com.wagen.cl.Model.OrderModel;
import com.wagen.cl.Model.PackagePricesModel;
import com.wagen.cl.Model.Packages;
import com.wagen.cl.Model.Service;
import com.wagen.cl.Model.UserModel;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderdetailActivity extends BaseActivity {
    TextView txv_address, txv_appointdate, txv_appointtime, txv_duration, txv_price;
    String phonenumber = "";
    LinearLayout lyt_coupon;
    EditText etx_coupon;
    int paimentmethod = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);

        txv_address=(TextView)findViewById(R.id.txv_address);
        txv_appointdate=(TextView)findViewById(R.id.txv_appointdate);
        txv_appointtime=(TextView)findViewById(R.id.txv_appointtime);
        txv_duration=(TextView)findViewById(R.id.txv_duration);
        txv_price=(TextView)findViewById(R.id.txv_price);
        lyt_coupon=(LinearLayout)findViewById(R.id.lyt_coupon);
        etx_coupon=(EditText)findViewById(R.id.etx_coupon);
        etx_coupon.setVisibility(View.GONE);
        lyt_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etx_coupon.getVisibility() == View.GONE) etx_coupon.setVisibility(View.VISIBLE);
            }
        });

        if(Constants.orderModel.order_type == 0){
            //txv_address.setText(Preference.getInstance().getValue(this, "workshop", ""));
            txv_address.setText(Constants.orderModel.city);
        }else{
            txv_address.setText(Constants.orderModel.address+", "+Constants.orderModel.city);
        }
        txv_appointdate.setText(setdateformat(Constants.orderModel.order_date));
        txv_appointtime.setText(Constants.orderModel.order_time);
        txv_duration.setText(getformatedduration()+"MINS");
        txv_price.setText(getformatedtotalprice()+"CLP");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Constants.frompagestatus_formembership == 1){ // if from membership page and payment == success
            createneworder(1);
        }else if(Constants.frompagestatus_formembership == 2){
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }
        Constants.frompagestatus_formembership = 0;
    }


    public String setdateformat(String date){
        Log.d("date==", date);
        String newdate = "";
        if(date.length()>0){
            newdate = date.split("-")[2]+"/"+date.split("-")[1]+"/"+date.split("-")[0];
        }
        return newdate;
    }

    public String getformatedduration(){
        String totaldurationtime = getdurationtime();
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(Long.parseLong(totaldurationtime));
        return  yourFormattedString.replaceAll(",",".");
    }

    public String getdurationtime() {
        ArrayList<Packages> packages = Preference.getInstance().getSharedpackagesmodelPreference(this, PrefConst.PREFKEY_PACKAGES);
        ArrayList<Service> services = Preference.getInstance().getSharedservicePreference(this, PrefConst.PREFKEY_SERVICES);
        int totaldurationtime = 0;
        for(int i = 0; i<packages.size(); i++){
            if(Constants.orderModel.package_id == packages.get(i).package_id){
                totaldurationtime+= Integer.parseInt(packages.get(i).package_time);
            }
        }

        Log.d("servicesize==", String.valueOf(Constants.orderModel.services.size()));
        for(int i = 0; i<Constants.orderModel.services.size(); i++){
            totaldurationtime+= Integer.parseInt(Constants.orderModel.services.get(i).service_time) *Constants.orderModel.services.get(i).number_of_order ;

        }
        return String.valueOf(totaldurationtime);
    }

    public String getformatedtotalprice(){
        String totalprice = gettotalprice();
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(Long.parseLong(totalprice));
        return  yourFormattedString.replaceAll(",",".");
    }

    public String gettotalprice() {
        ArrayList<Packages> packages = Preference.getInstance().getSharedpackagesmodelPreference(this, PrefConst.PREFKEY_PACKAGES);
        ArrayList<Service> services = Preference.getInstance().getSharedservicePreference(this, PrefConst.PREFKEY_SERVICES);
        int totalprice = 0;
        for(int i = 0; i<packages.size(); i++){
            if(Constants.orderModel.package_id == packages.get(i).package_id){
                ArrayList<PackagePricesModel> packagePricesModels = packages.get(i).packagePricesModels;
                for(int j=0; j< packagePricesModels.size(); j++){
                    if(packagePricesModels.get(j).car_id == Constants.orderModel.car_id){
                        if(Constants.orderModel.order_type == 0)  totalprice+=Integer.parseInt(packagePricesModels.get(j).price);
                        else if(Constants.orderModel.order_type == 1)  totalprice+=Integer.parseInt(packagePricesModels.get(j).price_home);
                    }
                }
               // totalprice+= Integer.parseInt(packages.get(i).package_price);
            }
        }

        for(int i = 0; i<Constants.orderModel.services.size(); i++){
            int oneitemprice = Constants.orderModel.services.get(i).number_of_order* Integer.parseInt(Constants.orderModel.services.get(i).service_price);
            totalprice+= oneitemprice;
        }
       return String.valueOf(totalprice);
    }


    public void placeorder(View view) {
        //showmessage(getString(R.string.thanksfororder));

       /*if(Constants.userModel.phoneverify_status == 0){
           confirmedphonenumber(Constants.userModel.phonenumber);
       }else{*/
         selectpaymentmethoddialog();
       //}

    }
    public void confirmedphonenumber(String toString) {
        phonenumber = toString;
        updateprofile(toString);
    }

    private void updateprofile(String phonenumber) {
        Map<String, String> params = new HashMap<>();
        params.put("phonenumber", phonenumber);
        params.put("phone_verifystatus", "1");
        params.put("user_id", String.valueOf(Constants.userModel.user_id));
        call_postApi(Constants.BASE_URL, "updateprofile", params);
    }

    public void goback(View view) {
        Intent intent = new Intent(this, Order1Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Order1Activity.class);
        startActivity(intent);
        finish();
    }

    public void selectpaymentmethoddialog(){
        TextView bymembership, paynow;
        final Dialog settingdialog = new Dialog(this);
        settingdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingdialog.setContentView(R.layout.selectpayment_dialog);
        settingdialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        settingdialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

        bymembership=(TextView) settingdialog.findViewById(R.id.bymembership);
        paynow=(TextView) settingdialog.findViewById(R.id.paynow);

        bymembership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createneworder(0);
            }
        });

        paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String totalprice = gettotalprice();
                String paidprice = totalprice;
                int discountvalue = 0;
                int couponid = -1;
                if(etx_coupon.getText().toString().trim().length()>0){
                    int[] checkstatus = checkcouponexists(etx_coupon.getText().toString().trim());
                    if(checkstatus[0] == -1){
                        Toast.makeText(OrderdetailActivity.this, getString(R.string.entercorrectcouponcode), Toast.LENGTH_SHORT).show();
                        return;
                    }else{
                        couponid = checkstatus[0];
                        discountvalue = checkstatus[1];
                    }
                }
                if(couponid != -1){
                    paidprice = String.valueOf((int)Integer.parseInt(totalprice)*(100 - discountvalue)/100);
                }

                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                Constants.orderid = Constants.userModel.first_name+ts;
                Constants.orderamount = paidprice;
                Intent intent = new Intent(OrderdetailActivity.this, PaymentActivity.class);
                startActivity(intent);


            }
        });

        settingdialog.show();
    }

    public String[] getserviceids(){
        String serviceid = "";
        String numberoforders = "";
        for(int i=0; i<Constants.orderModel.services.size(); i++){
            if(serviceid.length()==0) {
                serviceid= String.valueOf(Constants.orderModel.services.get(i).service_id);
                numberoforders = String.valueOf(Constants.orderModel.services.get(i).number_of_order);
            }
            else {
                serviceid+=","+ String.valueOf(Constants.orderModel.services.get(i).service_id);
                numberoforders += ","+String.valueOf(Constants.orderModel.services.get(i).number_of_order);
            }
        }
        return new String[]{serviceid, numberoforders};
    }


    public void createneworder(int paymentmethod){  // 0: by membershipm 1: by direct pay
        paimentmethod = paymentmethod;
        int discountvalue = 0;
        int couponid = -1;
        if(etx_coupon.getText().toString().trim().length()>0){
            int[] checkstatus = checkcouponexists(etx_coupon.getText().toString().trim());
            if(checkstatus[0] == -1){
                Toast.makeText(this, getString(R.string.entercorrectcouponcode), Toast.LENGTH_SHORT).show();
                return;
            }else{
                couponid = checkstatus[0];
                discountvalue = checkstatus[1];
            }
        }
        String totalprice = gettotalprice();
        String paidprice = totalprice;
        if(couponid != -1){
            paidprice = String.valueOf((int)Integer.parseInt(totalprice)*(100 - discountvalue)/100);
        }
        Log.d("paidprice==", paidprice);
        Log.d("coupon==", String.valueOf(couponid)+"__"+String.valueOf(discountvalue));

        Map<String, String> params = new HashMap<>();
        params.put("user_id", String.valueOf(Constants.userModel.user_id));
        params.put("order_date", Constants.orderModel.order_date);
        params.put("order_time", Constants.orderModel.order_time);
        params.put("duration_time", getdurationtime());
        params.put("total_price", totalprice);
        params.put("car_id", String.valueOf(Constants.orderModel.car_id));
        params.put("package_id", String.valueOf(Constants.orderModel.package_id));
        params.put("service_ids", getserviceids()[0]);
        params.put("service_qty", getserviceids()[1]);
        params.put("order_type", String.valueOf(Constants.orderModel.order_type));  // home or worshop
        params.put("order_city", Constants.orderModel.city);
        if(Constants.orderModel.order_type == 0){
            params.put("order_address", "");
        }else{
            params.put("order_address", Constants.orderModel.address);
        }
        params.put("payment_method", String.valueOf(paymentmethod));

        params.put("coupon_id", String.valueOf(couponid));
        params.put("discount_percent", String.valueOf(discountvalue));
        params.put("paid_price", paidprice);
        call_postApi(Constants.BASE_URL, "createorder", params);
    }

    public void returnapireponse(JSONObject response, String method) {
        try {
            String result_code = response.getString("message");
            if (result_code.equals("success")) {
                if(method.equals("updateprofile")){
                    Constants.userModel.phonenumber = phonenumber;
                    Constants.userModel.phoneverify_status = 1;
                    selectpaymentmethoddialog();
                }else{
                    /*Toast.makeText(OrderdetailActivity.this, "Successfully ordered", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OrderdetailActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();*/
                    if(paimentmethod == 0) Constants.userModel.membership_count --;
                    showmessage(getString(R.string.thanksfororder));
                }
            }else {
                Toast.makeText(OrderdetailActivity.this, result_code, Toast.LENGTH_SHORT).show();
            }
        } catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(OrderdetailActivity.this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
        }
    }

    public void showmessage(String message){
        Dialog settingdialog = new Dialog(OrderdetailActivity.this);
        settingdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingdialog.setContentView(R.layout.message_dialog);
        settingdialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        settingdialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        TextView txv_title =(TextView)settingdialog.findViewById(R.id.txv_title);
        ImageView imv_close =(ImageView)settingdialog.findViewById(R.id.imv_close);
        imv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderdetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        txv_title.setText(message);
        settingdialog.show();
        settingdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Intent intent = new Intent(OrderdetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public int[] checkcouponexists(String enteredcouponcode){
        int[] returnvalue = new int[]{-1,0};
        int couponpercent = 0;
        int couponid = -1;
        Coupon coupon = new Coupon();
        ArrayList<Coupon> coupons = Preference.getInstance().getSharedcouponPreference(this, PrefConst.PREFKEY_COUPON);
        for(int i=0; i<coupons.size(); i++){
            if(enteredcouponcode.equals(coupons.get(i).code)){
                couponpercent = coupons.get(i).discountvalue;
                couponid = coupons.get(i).id;
                coupon = coupons.get(i);
            }
        }
        if(couponid != -1){
            try {
                Calendar calendar = Calendar.getInstance();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd");
                String  date = simpleDateFormat.format(calendar.getTime());

                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date startdate = dateFormat.parse(coupon.start_daet);
                Date endate = dateFormat.parse(coupon.end_date);
                Date currentdate = dateFormat.parse(date);
                boolean checkdaterange =  checkdaterange(currentdate, startdate, endate);
                if(checkdaterange){
                    returnvalue[0] = coupon.id;
                    returnvalue[1] = coupon.discountvalue;
                }

            }
            catch(ParseException pe ) {
                // handle the failure
            }
        }

        return returnvalue;
    }

    public boolean checkdaterange(Date testDate, Date startDate, Date endDate){
        return testDate.after(startDate) && testDate.before(endDate);
    }
}