package com.wagen.cl.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wagen.cl.Activity.MainActivity;
import com.wagen.cl.Activity.Order1Activity;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Model.CarModel;
import com.wagen.cl.R;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class Step1Fragment extends Fragment {
    View view;
    Order1Activity order1Activity;

    ArrayList<CarModel> carModels;

    LinearLayout lyt_1, lyt_2, lyt_3, lyt_4, lyt_5;
    TextView txv_1,txv_2,txv_3,txv_4,txv_5;
    ImageView imv_1, imv_2,imv_3,imv_4,imv_5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_step1, container, false);

        carModels = Preference.getInstance().getSharedcarmodelPreference(order1Activity, PrefConst.PREFKEY_CARS);
        lyt_1 = (LinearLayout)view.findViewById(R.id.lyt_1);
        lyt_2 = (LinearLayout)view.findViewById(R.id.lyt_2);
        lyt_3=(LinearLayout)view.findViewById(R.id.lyt_3);
        lyt_4=(LinearLayout)view.findViewById(R.id.lyt_4);
        lyt_5 = (LinearLayout)view.findViewById(R.id.lyt_5);


        txv_1 = (TextView) view.findViewById(R.id.txv_1);
        txv_2 =(TextView) view.findViewById(R.id.txv_2);
        txv_3 = (TextView) view.findViewById(R.id.txv_3);
        txv_4 =(TextView) view.findViewById(R.id.txv_4);
        txv_5 = (TextView) view.findViewById(R.id.txv_5);
        txv_1.setText(carModels.get(0).car_name);
        txv_2.setText(carModels.get(1).car_name);
        txv_3.setText(carModels.get(2).car_name);
        txv_4.setText(carModels.get(3).car_name);
        txv_5.setText(carModels.get(4).car_name);

        imv_1 = (ImageView) view.findViewById(R.id.imv_1);
        imv_2 = (ImageView) view.findViewById(R.id.imv_2);
        imv_3 = (ImageView) view.findViewById(R.id.imv_3);
        imv_4 = (ImageView) view.findViewById(R.id.imv_4);
        imv_5 = (ImageView) view.findViewById(R.id.imv_5);
        Glide.with(this).load(Uri.parse(carModels.get(0).car_photo)).into(imv_1);
        Glide.with(this).load(Uri.parse(carModels.get(1).car_photo)).into(imv_2);
        Glide.with(this).load(Uri.parse(carModels.get(2).car_photo)).into(imv_3);
        Glide.with(this).load(Uri.parse(carModels.get(3).car_photo)).into(imv_4);
        Glide.with(this).load(Uri.parse(carModels.get(4).car_photo)).into(imv_5);

        lyt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbackground(1);
            }
        });
        lyt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbackground(2);
            }
        });
        lyt_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbackground(3);
            }
        });
        lyt_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbackground(4);
            }
        });
        lyt_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbackground(5);
            }
        });

        TextView txv_next =(TextView)view.findViewById(R.id.txv_next);
        txv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.orderModel.car_id == -1){
                    Toast.makeText(order1Activity, R.string.selectcar, Toast.LENGTH_SHORT).show();
                }else{

                    order1Activity.gotonextstep(1);
                }
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("carid==", String.valueOf(Constants.orderModel.car_id));
        refreshbackground(Constants.orderModel.car_id);

    }

    private void refreshbackground(int finalI) {
        Constants.orderModel.car_id = finalI;
        if(finalI == 1){
            lyt_1.setBackgroundResource(R.drawable.selectedbuttonback);
            lyt_2.setBackgroundResource(R.drawable.homebuttonback);
            lyt_3.setBackgroundResource(R.drawable.homebuttonback);
            lyt_4.setBackgroundResource(R.drawable.homebuttonback);
            lyt_5.setBackgroundResource(R.drawable.homebuttonback);
            txv_1.setTextColor(getResources().getColor(R.color.white));
            imv_1.setColorFilter(getResources().getColor(R.color.white));
            txv_2.setTextColor(getResources().getColor(R.color.gray_color));
            imv_2.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_3.setTextColor(getResources().getColor(R.color.gray_color));
            imv_3.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_4.setTextColor(getResources().getColor(R.color.gray_color));
            imv_4.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_5.setTextColor(getResources().getColor(R.color.gray_color));
            imv_5.setColorFilter(getResources().getColor(R.color.gray_color));
        }
        if(finalI== 2){
            lyt_2.setBackgroundResource(R.drawable.selectedbuttonback);
            lyt_1.setBackgroundResource(R.drawable.homebuttonback);
            lyt_3.setBackgroundResource(R.drawable.homebuttonback);
            lyt_4.setBackgroundResource(R.drawable.homebuttonback);
            lyt_5.setBackgroundResource(R.drawable.homebuttonback);
            txv_2.setTextColor(getResources().getColor(R.color.white));
            imv_2.setColorFilter(getResources().getColor(R.color.white));
            txv_1.setTextColor(getResources().getColor(R.color.gray_color));
            imv_1.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_3.setTextColor(getResources().getColor(R.color.gray_color));
            imv_3.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_4.setTextColor(getResources().getColor(R.color.gray_color));
            imv_4.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_5.setTextColor(getResources().getColor(R.color.gray_color));
            imv_5.setColorFilter(getResources().getColor(R.color.gray_color));
        }
        if(finalI == 3) {
            lyt_3.setBackgroundResource(R.drawable.selectedbuttonback);
            lyt_2.setBackgroundResource(R.drawable.homebuttonback);
            lyt_1.setBackgroundResource(R.drawable.homebuttonback);
            lyt_4.setBackgroundResource(R.drawable.homebuttonback);
            lyt_5.setBackgroundResource(R.drawable.homebuttonback);
            txv_3.setTextColor(getResources().getColor(R.color.white));
            imv_3.setColorFilter(getResources().getColor(R.color.white));
            txv_2.setTextColor(getResources().getColor(R.color.gray_color));
            imv_2.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_1.setTextColor(getResources().getColor(R.color.gray_color));
            imv_1.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_4.setTextColor(getResources().getColor(R.color.gray_color));
            imv_4.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_5.setTextColor(getResources().getColor(R.color.gray_color));
            imv_5.setColorFilter(getResources().getColor(R.color.gray_color));
        }
        if(finalI == 4){
            lyt_4.setBackgroundResource(R.drawable.selectedbuttonback);
            lyt_2.setBackgroundResource(R.drawable.homebuttonback);
            lyt_3.setBackgroundResource(R.drawable.homebuttonback);
            lyt_1.setBackgroundResource(R.drawable.homebuttonback);
            lyt_5.setBackgroundResource(R.drawable.homebuttonback);
            txv_4.setTextColor(getResources().getColor(R.color.white));
            imv_4.setColorFilter(getResources().getColor(R.color.white));
            txv_2.setTextColor(getResources().getColor(R.color.gray_color));
            imv_2.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_3.setTextColor(getResources().getColor(R.color.gray_color));
            imv_3.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_1.setTextColor(getResources().getColor(R.color.gray_color));
            imv_1.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_5.setTextColor(getResources().getColor(R.color.gray_color));
            imv_5.setColorFilter(getResources().getColor(R.color.gray_color));
        }
        if(finalI == 5) {
            lyt_5.setBackgroundResource(R.drawable.selectedbuttonback);
            lyt_2.setBackgroundResource(R.drawable.homebuttonback);
            lyt_3.setBackgroundResource(R.drawable.homebuttonback);
            lyt_4.setBackgroundResource(R.drawable.homebuttonback);
            lyt_1.setBackgroundResource(R.drawable.homebuttonback);
            txv_5.setTextColor(getResources().getColor(R.color.white));
            imv_5.setColorFilter(getResources().getColor(R.color.white));
            txv_2.setTextColor(getResources().getColor(R.color.gray_color));
            imv_2.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_3.setTextColor(getResources().getColor(R.color.gray_color));
            imv_3.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_4.setTextColor(getResources().getColor(R.color.gray_color));
            imv_4.setColorFilter(getResources().getColor(R.color.gray_color));
            txv_1.setTextColor(getResources().getColor(R.color.gray_color));
            imv_1.setColorFilter(getResources().getColor(R.color.gray_color));
        }

        /*for(int i = 0; i< linearLayouts.size(); i++){
            if(finalI == i){
                Log.d("carid=before=", String.valueOf(i));
                linearLayouts.get(i).setBackgroundResource(R.drawable.selectedbuttonback);
                Constants.orderModel.car_id = carModels.get(i).car_id;
                Log.d("carid=after=", String.valueOf(Constants.orderModel.car_id));
            }else {
                linearLayouts.get(i).setBackgroundResource(R.drawable.homebuttonback);
            }
        }*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        order1Activity = (Order1Activity) context;
    }


}