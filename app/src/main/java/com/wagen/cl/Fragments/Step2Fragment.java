package com.wagen.cl.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wagen.cl.Activity.Order1Activity;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Model.Packages;
import com.wagen.cl.R;
import com.wagen.cl.Utils.VerticalTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Step2Fragment extends Fragment {
    View view;
    static Order1Activity order1Activity;
    ArrayList<Packages> packages = new ArrayList<>();
    static ArrayList<Packages> filteredpackages = new ArrayList<>();
    LinearLayout lyt_container;
    ArrayList<LinearLayout> buttonlayouts = new ArrayList<>();
    ArrayList<VerticalTextView> buttontextviews = new ArrayList<>();
    BottomSheetDialog bottomSheetDialog;

    TextView txv_option1, txv_option2;
    static int filtertype = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_step2, container, false);
        packages = Preference.getInstance().getSharedpackagesmodelPreference(order1Activity, PrefConst.PREFKEY_PACKAGES);
        lyt_container=(LinearLayout)view.findViewById(R.id.lyt_container);



        txv_option1 =(TextView)view.findViewById(R.id.txv_option1);
        txv_option2 =(TextView)view.findViewById(R.id.txv_option2);
        txv_option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txv_option1.getBackground() != getResources().getDrawable(R.drawable.loginbuttonback1))
                selected_option1();
            }
        });
        txv_option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txv_option2.getBackground() != getResources().getDrawable(R.drawable.loginbuttonback1))
                selected_option2();
            }
        });




        return view;
    }

    private void selected_option2() {
       filtertype = 1;
        txv_option1.setTextColor(getResources().getColor(R.color.black));
        txv_option1.setBackgroundResource(R.drawable.segment_bg);
        txv_option2.setTextColor(getResources().getColor(R.color.white));
        txv_option2.setBackgroundResource(R.drawable.loginbuttonback1);
        refreshbuttons(1);
    }

    private void selected_option1() {
        filtertype = 0;
        txv_option2.setTextColor(getResources().getColor(R.color.black));
        txv_option2.setBackgroundResource(R.drawable.segment_bg);
        txv_option1.setTextColor(getResources().getColor(R.color.white));
        txv_option1.setBackgroundResource(R.drawable.loginbuttonback1);
        refreshbuttons(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("carid==", String.valueOf(Constants.orderModel.car_id));
        if(Constants.orderModel.order_type == 0 || Constants.orderModel.order_type == -1) {
            selected_option1();
        }
        else if(Constants.orderModel.order_type == 1) selected_option2();

    }



    public void refreshbuttons(int position){// 0 from workshop, 1: from home
        filteredpackages = new ArrayList<>();
        buttonlayouts = new ArrayList<>();
        buttontextviews = new ArrayList<>();
        for(int i=0; i<packages.size(); i++){
            Packages packageitem = packages.get(i);
            if(Constants.orderModel.package_id == packageitem.package_id) packageitem.selected_status = true;
            for(int j=0; j<packageitem.packagePricesModels.size(); j++){
                if(packageitem.packagePricesModels.get(j).car_id == Constants.orderModel.car_id){
                    if(position == 0) packageitem.selected_car_price = packageitem.packagePricesModels.get(j).price;
                    else if(position == 1) packageitem.selected_car_price = packageitem.packagePricesModels.get(j).price_home;
                    if(position == 0 || (position == 1 && packageitem.package_available_for_home.equals("Yes"))) filteredpackages.add(packageitem);
                }
            }
        }
        lyt_container.removeAllViews();
        for(int i=0; i<filteredpackages.size(); i++){
            View child = getLayoutInflater().inflate(R.layout.package_item, null);
            TextView txvtitle = (TextView) child.findViewById(R.id.txv_title1);
            TextView txvprice =(TextView)child.findViewById(R.id.txv_price1);
            TextView txvtime =(TextView)child.findViewById(R.id.txv_time1);
            TextView txvdes = (TextView)child.findViewById(R.id.txv_des1);
            VerticalTextView txv_button =(VerticalTextView) child.findViewById(R.id.txv_button);
            txv_button.setId(i+100);
            LinearLayout lyt_butto=(LinearLayout) child.findViewById(R.id.lyt_button);
            lyt_butto.setId(i);
            buttonlayouts.add(lyt_butto);
            buttontextviews.add(txv_button);
            LinearLayout lyt_item =(LinearLayout)child.findViewById(R.id.lyt_item);

            txvtitle.setText(filteredpackages.get(i).package_name);
            txvprice.setText("$"+ numberformating(filteredpackages.get(i).selected_car_price));
            txvtime.setText(filteredpackages.get(i).package_time+"Mins");
            txvdes.setText(filteredpackages.get(i).package_description.replaceAll("_","\n"));
            int finalI = i;
            lyt_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshselectedbuttons(finalI);
                }
            });

            lyt_container.addView(child);
        }


    }

    public void refreshselectedbuttons(int position){
        for(int i1=0; i1<buttonlayouts.size(); i1++){
            if(i1== position){
                buttonlayouts.get(i1).setBackgroundResource(R.drawable.package_select);
                buttontextviews.get(i1).setTextColor(getResources().getColor(R.color.white));
                filteredpackages.get(i1).selected_status = true;
            }else{
                buttonlayouts.get(i1).setBackgroundResource(R.drawable.package_unselect);
                buttontextviews.get(i1).setTextColor(getResources().getColor(R.color.black));
                filteredpackages.get(i1).selected_status = false;
            }
        }
        bottomSheetDialog = BottomSheetDialog.getInstance();
        bottomSheetDialog.show(getChildFragmentManager(),"bottomSheet");
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


    public static void selectcitydialog(String shopaddress, ArrayList<String> cities){
        LinearLayout lyt_address;
        TextView txvtitle, txvshopaddress, txvnext;
        MaterialSpinner spinner;

        EditText etx_address;
        Dialog settingdialog = new Dialog(order1Activity);
        settingdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingdialog.setContentView(R.layout.selectcity_dialog);
        settingdialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        settingdialog.getWindow().setBackgroundDrawable(new ColorDrawable(order1Activity.getResources().getColor(R.color.transparent)));
        lyt_address=(LinearLayout) settingdialog.findViewById(R.id.lyt_address);
        txvtitle=(TextView) settingdialog.findViewById(R.id.txv_title);
        txvshopaddress=(TextView) settingdialog.findViewById(R.id.txv_shopaddress);
        txvnext =(TextView)settingdialog.findViewById(R.id.goto_next);
        spinner = (MaterialSpinner) settingdialog.findViewById(R.id.spinner);
        etx_address=(EditText)settingdialog.findViewById(R.id.etx_address);



        if(Constants.orderModel.order_type == 0){
            txvtitle.setText(order1Activity.getResources().getString(R.string.shopaddressis));
            lyt_address.setVisibility(View.GONE);
            txvshopaddress.setVisibility(View.VISIBLE);
            txvshopaddress.setText(shopaddress);
        }else{
            lyt_address.setVisibility(View.VISIBLE);
            txvshopaddress.setVisibility(View.GONE);
            txvtitle.setText(order1Activity.getResources().getString(R.string.inputaddress));


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
                    Toast.makeText(order1Activity, order1Activity.getResources().getString(R.string.inputaddress), Toast.LENGTH_SHORT).show();
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

    public static int selectedcitypostion(ArrayList<String> cities){
        int postion= 0;
        for(int i=0; i<cities.size(); i++){
            if(cities.get(i).equals(Constants.orderModel.city)) postion = i;
        }
        return  postion;
    }

    public static class BottomSheetDialog extends BottomSheetDialogFragment {
        public static BottomSheetDialog getInstance() { return new BottomSheetDialog(); }
        public TextView txvnew;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.bottom_sheet_persistent, container,false);
            txvnew = view.findViewById(R.id.txv_next1);
            txvnew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] selectedpackagevalues = checkpageposition();

                    if(selectedpackagevalues[0].equals("-1")){
                        Toast.makeText(order1Activity, R.string.selectpackage, Toast.LENGTH_SHORT).show();
                    }else{
                        Constants.orderModel.package_id = Integer.parseInt(selectedpackagevalues[0]);
                        Constants.orderModel.package_price = selectedpackagevalues[1];
                        Constants.orderModel.order_type = filtertype;
                        ArrayList<String> cities = Preference.getInstance().getShared_cities_Preference(order1Activity, PrefConst.PREFKEY_CITIES);
                        String workshop = Preference.getInstance().getValue(order1Activity, "workshop","");
                        selectcitydialog(workshop, cities);
                    }


                }
            });

            return view;
        }
    }
    public static String[] checkpageposition(){
        int selectedpackageid = -1;
        String selectedpackageprice = "";
        for(int i= 0; i<filteredpackages.size(); i++){
            if(filteredpackages.get(i).selected_status == true){
                selectedpackageid = filteredpackages.get(i).package_id;
                selectedpackageprice = filteredpackages.get(i).selected_car_price;
            }
        }
        return new String[]{String.valueOf(selectedpackageid), selectedpackageprice};
    }


}