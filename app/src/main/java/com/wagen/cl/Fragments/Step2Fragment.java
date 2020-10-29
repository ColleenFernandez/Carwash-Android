package com.wagen.cl.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wagen.cl.Activity.Order1Activity;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Model.Packages;
import com.wagen.cl.R;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Step2Fragment extends Fragment {
    View view;
    Order1Activity order1Activity;
    TextView txv_title1, txv_title2, txv_title3, txv_title4;
    TextView txv_price1, txv_price2, txv_price3, txv_price4;
    TextView txv_time1, txv_time2, txv_time3, txv_time4;
    TextView txv_des1, txv_des2, txv_des3, txv_des4;
    LinearLayout lyt_1,lyt_2,lyt_3,lyt_4;
    TextView txv_1,txv_2,txv_3,txv_4;

    ArrayList<Packages> packages = new ArrayList<>();
    TextView txv_option1, txv_option2;
    int previtem = 0, packageposition = -1;
    LinearLayout lyt_firstitem, lyt_seconditem, lyt_thirditem, lyt_fourthitem;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_step2, container, false);
        packages = Preference.getInstance().getSharedpackagesmodelPreference(order1Activity, PrefConst.PREFKEY_PACKAGES);

        lyt_firstitem=(LinearLayout)view.findViewById(R.id.lyt_firstitem);
        lyt_seconditem=(LinearLayout)view.findViewById(R.id.lyt_seconditem);
        lyt_thirditem=(LinearLayout)view.findViewById(R.id.lyt_thirditem);
        lyt_fourthitem=(LinearLayout)view.findViewById(R.id.lyt_fourthitem);

        lyt_1=(LinearLayout)view.findViewById(R.id.lyt_1);
        lyt_2=(LinearLayout)view.findViewById(R.id.lyt_2);
        lyt_3=(LinearLayout)view.findViewById(R.id.lyt_3);
        lyt_4=(LinearLayout)view.findViewById(R.id.lyt_4);

        txv_title1=(TextView)view.findViewById(R.id.txv_title1);
        txv_title2=(TextView)view.findViewById(R.id.txv_title2);
        txv_title3=(TextView)view.findViewById(R.id.txv_title3);
        txv_title4=(TextView)view.findViewById(R.id.txv_title4);

        txv_price1=(TextView)view.findViewById(R.id.txv_price1);
        txv_price2=(TextView)view.findViewById(R.id.txv_price2);
        txv_price3=(TextView)view.findViewById(R.id.txv_price3);
        txv_price4=(TextView)view.findViewById(R.id.txv_price4);

        txv_time1=(TextView)view.findViewById(R.id.txv_time1);
        txv_time2=(TextView)view.findViewById(R.id.txv_time2);
        txv_time3=(TextView)view.findViewById(R.id.txv_time3);
        txv_time4=(TextView)view.findViewById(R.id.txv_time4);

        txv_des1=(TextView)view.findViewById(R.id.txv_des1);
        txv_des2=(TextView)view.findViewById(R.id.txv_des2);
        txv_des3=(TextView)view.findViewById(R.id.txv_des3);
        txv_des4=(TextView)view.findViewById(R.id.txv_des4);

        txv_1 =(TextView)view.findViewById(R.id.txv_1);
        txv_2 =(TextView)view.findViewById(R.id.txv_2);
        txv_3 =(TextView)view.findViewById(R.id.txv_3);
        txv_4 =(TextView)view.findViewById(R.id.txv_4);


        txv_title1.setText(packages.get(0).package_name);
        txv_price1.setText("$"+numberformating(packages.get(0).package_price));
        txv_time1.setText(packages.get(0).package_time+"Mins");
        txv_des1.setText(packages.get(0).package_description.replaceAll("_","\n"));
        txv_title2.setText(packages.get(1).package_name);
        txv_price2.setText("$"+numberformating(packages.get(1).package_price));
        txv_time2.setText(packages.get(1).package_time+"Mins");
        txv_des2.setText(packages.get(1).package_description.replaceAll("_","\n"));
        txv_title3.setText(packages.get(2).package_name);
        txv_price3.setText("$"+numberformating(packages.get(2).package_price));
        txv_time3.setText(packages.get(2).package_time+"Mins");
        txv_des3.setText(packages.get(2).package_description.replaceAll("_","\n"));
        txv_title4.setText(packages.get(3).package_name);
        txv_price4.setText("$"+numberformating(packages.get(3).package_price));
        txv_time4.setText(packages.get(3).package_time+"Mins");
        txv_des4.setText(packages.get(3).package_description.replaceAll("_","\n"));

        lyt_firstitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbuttons(0);
            }
        });
        lyt_seconditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbuttons(1);
            }
        });
        lyt_thirditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbuttons(2);
            }
        });

        lyt_fourthitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbuttons(3);
            }
        });

        txv_option1 =(TextView)view.findViewById(R.id.txv_option1);
        txv_option2 =(TextView)view.findViewById(R.id.txv_option2);
        txv_option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_option1();
            }
        });
        txv_option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected_option2();
            }
        });

        TextView txv_next =(TextView)view.findViewById(R.id.txv_next);
        txv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(packageposition == -1){
                    Toast.makeText(order1Activity, R.string.selectpackage, Toast.LENGTH_SHORT).show();
                }else{
                    Constants.orderModel.package_id = packageposition+1;
                    ArrayList<String> cities = Preference.getInstance().getShared_cities_Preference(order1Activity, PrefConst.PREFKEY_CITIES);
                    Constants.orderModel.order_type = previtem;
                    String workshop = Preference.getInstance().getValue(order1Activity, "workshop","");
                    selectcitydialog(workshop, cities);
                }

            }
        });

        return view;
    }

    private void selected_option2() {
        if(previtem != 1){
            packageposition = -1;
            txv_option1.setTextColor(getResources().getColor(R.color.black));
            txv_option1.setBackgroundResource(R.drawable.segment_bg);
            txv_option2.setTextColor(getResources().getColor(R.color.white));
            txv_option2.setBackgroundResource(R.drawable.loginbuttonback1);
            lyt_firstitem.setVisibility(View.GONE);
            previtem = 1;
            refreshbuttons(-1);
        }
    }

    private void selected_option1() {
        if(previtem != 0){
            txv_option2.setTextColor(getResources().getColor(R.color.black));
            txv_option2.setBackgroundResource(R.drawable.segment_bg);
            txv_option1.setTextColor(getResources().getColor(R.color.white));
            txv_option1.setBackgroundResource(R.drawable.loginbuttonback1);
            lyt_firstitem.setVisibility(View.VISIBLE);
            previtem = 0;
            packageposition = -1;
            refreshbuttons(-1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("carid==", String.valueOf(Constants.orderModel.car_id));
        previtem = -1;
        if(Constants.orderModel.order_type == 0) {
            selected_option1();
        }
        else if(Constants.orderModel.order_type == 1) selected_option2();
        else previtem = 0;

        refreshbuttons(Constants.orderModel.package_id-1);
    }

    public void refreshbuttons(int position){
        packageposition = position;
        if(previtem == 0){
            txv_price1.setText("$"+numberformating(packages.get(0).package_price));
            txv_price2.setText("$"+numberformating(packages.get(1).package_price));
            txv_price3.setText("$"+numberformating(packages.get(2).package_price));
            txv_price4.setText("$"+numberformating(packages.get(3).package_price));
        }else{
            txv_price1.setText("$"+numberformating(packages.get(0).package_price_home));
            txv_price2.setText("$"+numberformating(packages.get(1).package_price_home));
            txv_price3.setText("$"+numberformating(packages.get(2).package_price_home));
            txv_price4.setText("$"+numberformating(packages.get(3).package_price_home));
        }

        if(position == 0){
            lyt_1.setBackgroundResource(R.drawable.package_select);
            lyt_2.setBackgroundResource(R.drawable.package_unselect);
            lyt_3.setBackgroundResource(R.drawable.package_unselect);
            lyt_4.setBackgroundResource(R.drawable.package_unselect);
            txv_1.setTextColor(getResources().getColor(R.color.white));
            txv_2.setTextColor(getResources().getColor(R.color.black));
            txv_3.setTextColor(getResources().getColor(R.color.black));
            txv_4.setTextColor(getResources().getColor(R.color.black));
        }
        if(position == 1){
            lyt_2.setBackgroundResource(R.drawable.package_select);
            lyt_1.setBackgroundResource(R.drawable.package_unselect);
            lyt_3.setBackgroundResource(R.drawable.package_unselect);
            lyt_4.setBackgroundResource(R.drawable.package_unselect);
            txv_2.setTextColor(getResources().getColor(R.color.white));
            txv_1.setTextColor(getResources().getColor(R.color.black));
            txv_3.setTextColor(getResources().getColor(R.color.black));
            txv_4.setTextColor(getResources().getColor(R.color.black));
        }
        if(position == 2){
            lyt_3.setBackgroundResource(R.drawable.package_select);
            lyt_2.setBackgroundResource(R.drawable.package_unselect);
            lyt_1.setBackgroundResource(R.drawable.package_unselect);
            lyt_4.setBackgroundResource(R.drawable.package_unselect);
            txv_3.setTextColor(getResources().getColor(R.color.white));
            txv_2.setTextColor(getResources().getColor(R.color.black));
            txv_1.setTextColor(getResources().getColor(R.color.black));
            txv_4.setTextColor(getResources().getColor(R.color.black));
        }
        if(position == 3){
            lyt_4.setBackgroundResource(R.drawable.package_select);
            lyt_2.setBackgroundResource(R.drawable.package_unselect);
            lyt_3.setBackgroundResource(R.drawable.package_unselect);
            lyt_1.setBackgroundResource(R.drawable.package_unselect);
            txv_4.setTextColor(getResources().getColor(R.color.white));
            txv_2.setTextColor(getResources().getColor(R.color.black));
            txv_3.setTextColor(getResources().getColor(R.color.black));
            txv_1.setTextColor(getResources().getColor(R.color.black));
        }
        if(position == -1){
            lyt_4.setBackgroundResource(R.drawable.package_unselect);
            lyt_2.setBackgroundResource(R.drawable.package_unselect);
            lyt_3.setBackgroundResource(R.drawable.package_unselect);
            lyt_1.setBackgroundResource(R.drawable.package_unselect);
            txv_1.setTextColor(getResources().getColor(R.color.black));
            txv_2.setTextColor(getResources().getColor(R.color.black));
            txv_3.setTextColor(getResources().getColor(R.color.black));
            txv_4.setTextColor(getResources().getColor(R.color.black));
        }
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


    public void selectcitydialog(String shopaddress, ArrayList<String> cities){
        LinearLayout lyt_address;
        TextView txvtitle, txvshopaddress, txvnext;
        MaterialSpinner spinner;

        EditText etx_address;
        Dialog settingdialog = new Dialog(order1Activity);
        settingdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingdialog.setContentView(R.layout.selectcity_dialog);
        settingdialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        settingdialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        lyt_address=(LinearLayout) settingdialog.findViewById(R.id.lyt_address);
        txvtitle=(TextView) settingdialog.findViewById(R.id.txv_title);
        txvshopaddress=(TextView) settingdialog.findViewById(R.id.txv_shopaddress);
        txvnext =(TextView)settingdialog.findViewById(R.id.goto_next);
        spinner = (MaterialSpinner) settingdialog.findViewById(R.id.spinner);
        etx_address=(EditText)settingdialog.findViewById(R.id.etx_address);



        if(Constants.orderModel.order_type == 0){
            txvtitle.setText(getResources().getString(R.string.shopaddressis));
            lyt_address.setVisibility(View.GONE);
            txvshopaddress.setVisibility(View.VISIBLE);
            txvshopaddress.setText(shopaddress);
        }else{
            lyt_address.setVisibility(View.VISIBLE);
            txvshopaddress.setVisibility(View.GONE);
            txvtitle.setText(getResources().getString(R.string.inputaddress));


            spinner.setItems(cities);
            spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

                @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                }
            });

            if(Constants.orderModel.city.length()>0){
                spinner.setSelectedIndex(selectedcitypostion(cities));
                etx_address.setText(Constants.orderModel.address);
            }
        }

        txvnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.orderModel.order_type == 1 && etx_address.getText().toString().length()==0){
                    Toast.makeText(order1Activity, getResources().getString(R.string.inputaddress), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Constants.orderModel.order_type == 1){
                    Constants.orderModel.city =cities.get(spinner.getSelectedIndex());
                    Constants.orderModel.address = etx_address.getText().toString();
                }
                settingdialog.dismiss();
                order1Activity.gotonextstep(2);
            }
        });


        settingdialog.show();
    }

    public int selectedcitypostion(ArrayList<String> cities){
        int postion= 0;
        for(int i=0; i<cities.size(); i++){
            if(cities.get(i).equals(Constants.orderModel.city)) postion = i;
        }
        return  postion;
    }


}