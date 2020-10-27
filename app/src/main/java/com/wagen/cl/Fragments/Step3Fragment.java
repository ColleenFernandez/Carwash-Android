package com.wagen.cl.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wagen.cl.Activity.Order1Activity;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Model.Service;
import com.wagen.cl.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Step3Fragment extends Fragment {
    View view;
    Order1Activity order1Activity;
    ArrayList<Service> services = new ArrayList<>();
    LinearLayout lyt_container;
    ArrayList<EditText> etx_quantities = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_step3, container, false);
        lyt_container=(LinearLayout)view.findViewById(R.id.lyt_container);
        services = Preference.getInstance().getSharedservicePreference(order1Activity, PrefConst.PREFKEY_SERVICES);
        TextView txv_next =(TextView)view.findViewById(R.id.txv_next);
        txv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.orderModel.services = getservices();
                order1Activity.gotonextstep(3);
            }
        });

        return view;
    }

    private ArrayList<Service> getservices() {
        ArrayList<Service> selectedservices = new ArrayList<>();
        for(int i = 0; i<services.size(); i++){
            Service oneservice = services.get(i);
            if(etx_quantities.get(i).getText().toString().length()>0 && Integer.parseInt(etx_quantities.get(i).getText().toString())>0){
                oneservice.number_of_order = Integer.parseInt(etx_quantities.get(i).getText().toString());
                selectedservices.add(oneservice);
            }
        }
        return  selectedservices;
    }

    @Override
    public void onResume() {
        super.onResume();
        for(int i = 0; i<services.size(); i++){
            View child = getLayoutInflater().inflate(R.layout.lyt_services, null);
            TextView txvtitle = (TextView) child.findViewById(R.id.txv_title1);
            TextView txvprice =(TextView)child.findViewById(R.id.txv_price1);
            TextView txvtime =(TextView)child.findViewById(R.id.txv_time1);
            txvtitle.setText(services.get(i).service_name);
            txvprice.setText("$"+ numberformating(services.get(i).service_price));
            txvtime.setText(services.get(i).service_time+"Mins");
            TextView txv_minus = (TextView)child.findViewById(R.id.txv_minus);
            TextView txv_plus = (TextView)child.findViewById(R.id.txv_plus);
            EditText etx_quantity = (EditText)child.findViewById(R.id.etx_quantity);
            TextView txv_button =(TextView)child.findViewById(R.id.txv_select1);
            LinearLayout lyt_cu =(LinearLayout)child.findViewById(R.id.lyt_cu);

            if(services.get(i).cu_status == 0){
                txv_button.setVisibility(View.VISIBLE);
                lyt_cu.setVisibility(View.GONE);
                etx_quantity.setText(String.valueOf(getitemquantity(i)));
                etx_quantities.add(etx_quantity);
                if(getitemquantity(i)==1){
                    txv_button.setBackgroundResource(R.drawable.homebuttonback);
                    txv_button.setTextColor(getResources().getColor(R.color.black));
                    txv_button.setText("Selected");
                }else{
                    txv_button.setBackgroundResource(R.drawable.selectedbuttonback);
                    txv_button.setTextColor(getResources().getColor(R.color.white));
                    txv_button.setText("Select");
                }
                txv_button.setId(i);
                int finalI = i;
                txv_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etx_quantity.getText().toString().equals("1")){
                            txv_button.setBackgroundResource(R.drawable.selectedbuttonback);
                            etx_quantity.setText("0");
                            etx_quantities.get(finalI).setText("0");
                            txv_button.setTextColor(getResources().getColor(R.color.white));
                            txv_button.setText("Select");
                        }else{
                            txv_button.setBackgroundResource(R.drawable.homebuttonback);
                            etx_quantity.setText("1");
                            etx_quantities.get(finalI).setText("1");
                            txv_button.setTextColor(getResources().getColor(R.color.black));
                            txv_button.setText("Selected");
                        }
                    }
                });
            }else{
                txv_button.setVisibility(View.GONE);
                lyt_cu.setVisibility(View.VISIBLE);
                etx_quantity.setText(String.valueOf(getitemquantity(i)));
                etx_quantity.setEnabled(false);
                etx_quantity.setId(i);
                etx_quantities.add(etx_quantity);
                txv_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(etx_quantity.getText().toString().length()>0 && Integer.parseInt(etx_quantity.getText().toString())>0){
                            etx_quantity.setText(String.valueOf(Integer.parseInt(etx_quantity.getText().toString())-1));
                        }
                    }
                });
                txv_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        etx_quantity.setText(String.valueOf(Integer.parseInt(etx_quantity.getText().toString())+1));
                    }
                });
            }


            lyt_container.addView(child);
        }
    }

    public int getitemquantity(int position){
        Service service = services.get(position);
        int quantity = 0;
        for(int i=0; i<Constants.orderModel.services.size(); i++){
            if(Constants.orderModel.services.get(i).service_id == service.service_id){
                quantity = Constants.orderModel.services.get(i).number_of_order;
            }
        }
        return quantity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        order1Activity = (Order1Activity) context;
    }

    public String numberformating(String pricevalue){
        if(pricevalue == null || pricevalue.length()==0) return  "";
        else{
            DecimalFormat formatter = new DecimalFormat("#,###,###");
            String yourFormattedString = formatter.format(Long.parseLong(pricevalue));
            return  yourFormattedString;
        }
    }
}