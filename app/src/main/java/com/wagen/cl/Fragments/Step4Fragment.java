package com.wagen.cl.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.wagen.cl.Activity.Order1Activity;
import com.wagen.cl.Activity.OrderdetailActivity;
import com.wagen.cl.Constant.Constants;

import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;
import com.wagen.cl.WagenApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import segmented_control.widget.custom.android.com.segmentedcontrol.SegmentedControl;
import segmented_control.widget.custom.android.com.segmentedcontrol.item_row_column.SegmentViewHolder;
import segmented_control.widget.custom.android.com.segmentedcontrol.listeners.OnSegmentSelectRequestListener;


public class Step4Fragment extends Fragment {
    View view;
    Order1Activity order1Activity;
    CalendarView calendarView;
    SegmentedControl segmented_control;
    String selecteddate = "";
    String selecetedtime = "";
    String currentdate = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_step4, container, false);
        calendarView=(CalendarView)view.findViewById(R.id.calendar);
        segmented_control =(SegmentedControl)view.findViewById(R.id.segmented_control);

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selecteddate = df.format(c);
        currentdate = selecteddate;

        if(Constants.orderModel.order_time.length()==0){
            getallavailabletime(selecteddate);
        }else{
            selecteddate = Constants.orderModel.order_date;
            getallavailabletime(Constants.orderModel.order_date);
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selecteddate = String.valueOf(year)+"-"+String.valueOf(month+1)+"-"+String.valueOf(dayOfMonth);
                Log.d("selecteddate==", selecteddate);
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    long parsedMillis = sdf.parse(selecteddate).getTime();
                    //long now = System.currentTimeMillis(); // 22:54:15
                    long now = sdf.parse(currentdate).getTime();// 22:54:15

                    if (parsedMillis >= now) {
                        Log.d("TAG", "In the future!");
                        getallavailabletime(selecteddate);

                    } else {
                        Log.d("TAG", "In the past...");
                        Toast.makeText(order1Activity, getString(R.string.cannotselectolddate), Toast.LENGTH_SHORT).show();
                        segmented_control.removeAllSegments();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        segmented_control.setOnSegmentSelectRequestListener(new OnSegmentSelectRequestListener() {
            @Override
            public boolean onSegmentSelectRequest(SegmentViewHolder segmentViewHolder) {
                selecetedtime = segmentViewHolder.getSegmentData().toString();
                Log.d("selecteddate==", selecetedtime);
                return true;
            }
        });




        TextView txv_next =(TextView)view.findViewById(R.id.txv_next);
        txv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //order1Activity.gotonextstep(3);
                if(selecetedtime.length()==0){
                    Toast.makeText(order1Activity, getString(R.string.selectatime), Toast.LENGTH_SHORT).show();
                    return;
                }
                Constants.orderModel.order_date = selecteddate;
                Constants.orderModel.order_time = selecetedtime;
                Intent intent = new Intent(order1Activity, OrderdetailActivity.class);
                startActivity(intent);
                order1Activity.finish();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        order1Activity = (Order1Activity) context;
    }

    private void getallavailabletime(String date) {
        segmented_control.removeAllSegments();
        selecetedtime = "";
        order1Activity.progressBar.show();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                Constants.BASE_URL+"getdateavailabletime",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        order1Activity.progressBar.dismiss();
                        Parseresonse(json);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error============", String.valueOf(error));
                        order1Activity.progressBar.dismiss();
                        Toast.makeText(order1Activity, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("city", Constants.orderModel.city);
                params.put("date", date);
                params.put("order_type", String.valueOf(Constants.orderModel.order_type));
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        WagenApplication.getInstance().addToRequestQueue(myRequest, "tag");
    }

    public void Parseresonse(String json) {
        try {
            Log.d("response==", json);
            JSONObject response = new JSONObject(json);
            String result_code = response.getString("message");
            if (result_code.equals("success")) {
                //Toast.makeText(order1Activity, "Success", Toast.LENGTH_SHORT).show();
                JSONArray items =  response.getJSONArray("useabletime");
                ArrayList<String> availabletimes = new ArrayList<>();

                int existingstatus = -1;
                for(int i = 0; i < items.length(); i++){
                    availabletimes.add(items.getString(i));
                    if(Constants.orderModel.order_time.length()>0 && items.getString(i).equals(Constants.orderModel.order_time)){
                        existingstatus = i;
                    }
                }
                if(Constants.orderModel.order_time.length()> 0 && existingstatus == -1){
                    availabletimes.add(Constants.orderModel.order_time);
                    existingstatus = availabletimes.size()-1;
                }
                if(availabletimes.size()==0){
                    Toast.makeText(order1Activity, getString(R.string.noavailabletime), Toast.LENGTH_SHORT).show();
                }else{
                    segmented_control.addSegments(availabletimes);
                    if(Constants.orderModel.order_time.length()> 0)       segmented_control.setSelectedSegment(existingstatus);
                }

            }else {
                Toast.makeText(order1Activity, result_code, Toast.LENGTH_SHORT).show();
            }
        } catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(order1Activity, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
        }
    }
}