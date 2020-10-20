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

import com.bumptech.glide.Glide;
import com.wagen.cl.Activity.MainActivity;
import com.wagen.cl.Activity.Order1Activity;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Model.CarModel;
import com.wagen.cl.R;

import java.util.ArrayList;


public class Step1Fragment extends Fragment {
    View view;
    Order1Activity order1Activity;

    ArrayList<TextView> textViews = new ArrayList<>();
    ArrayList<ImageView> imageViews = new ArrayList<>();
    ArrayList<CarModel> carModels;

    LinearLayout lyt_1, lyt_2, lyt_3, lyt_4, lyt_5;

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


        textViews.add((TextView) view.findViewById(R.id.txv_1));
        textViews.add((TextView) view.findViewById(R.id.txv_2));
        textViews.add((TextView) view.findViewById(R.id.txv_3));
        textViews.add((TextView) view.findViewById(R.id.txv_4));
        textViews.add((TextView) view.findViewById(R.id.txv_5));

        imageViews.add((ImageView) view.findViewById(R.id.imv_1));
        imageViews.add((ImageView) view.findViewById(R.id.imv_2));
        imageViews.add((ImageView) view.findViewById(R.id.imv_3));
        imageViews.add((ImageView) view.findViewById(R.id.imv_4));
        imageViews.add((ImageView) view.findViewById(R.id.imv_5));

        for(int i=0; i<carModels.size(); i++){
            CarModel carModel = carModels.get(i);
            textViews.get(i).setText(carModel.car_name);
            Glide.with(this)
                    .load(Uri.parse(carModel.car_photo))
                    .into(imageViews.get(i));
        }

        lyt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbackground(0);
            }
        });
        lyt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbackground(1);
            }
        });
        lyt_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbackground(2);
            }
        });
        lyt_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbackground(3);
            }
        });
        lyt_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbackground(4);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("carid==", String.valueOf(Constants.orderModel.car_id));
        if(Constants.orderModel.car_id == 1) lyt_1.setBackgroundResource(R.drawable.selectedbuttonback);
        if(Constants.orderModel.car_id == 2) lyt_2.setBackgroundResource(R.drawable.selectedbuttonback);
        if(Constants.orderModel.car_id == 3) lyt_3.setBackgroundResource(R.drawable.selectedbuttonback);
        if(Constants.orderModel.car_id == 4) lyt_4.setBackgroundResource(R.drawable.selectedbuttonback);
        if(Constants.orderModel.car_id == 5) lyt_5.setBackgroundResource(R.drawable.selectedbuttonback);
    }

    private void refreshbackground(int finalI) {
        Constants.orderModel.car_id = finalI+1;
        if(finalI == 0){
            lyt_1.setBackgroundResource(R.drawable.selectedbuttonback);
            lyt_2.setBackgroundResource(R.drawable.homebuttonback);
            lyt_3.setBackgroundResource(R.drawable.homebuttonback);
            lyt_4.setBackgroundResource(R.drawable.homebuttonback);
            lyt_5.setBackgroundResource(R.drawable.homebuttonback);
        }
        if(finalI == 1){
            lyt_2.setBackgroundResource(R.drawable.selectedbuttonback);
            lyt_1.setBackgroundResource(R.drawable.homebuttonback);
            lyt_3.setBackgroundResource(R.drawable.homebuttonback);
            lyt_4.setBackgroundResource(R.drawable.homebuttonback);
            lyt_5.setBackgroundResource(R.drawable.homebuttonback);
        }
        if(finalI == 2){
            lyt_3.setBackgroundResource(R.drawable.selectedbuttonback);
            lyt_2.setBackgroundResource(R.drawable.homebuttonback);
            lyt_1.setBackgroundResource(R.drawable.homebuttonback);
            lyt_4.setBackgroundResource(R.drawable.homebuttonback);
            lyt_5.setBackgroundResource(R.drawable.homebuttonback);
        }
        if(finalI ==3){
            lyt_4.setBackgroundResource(R.drawable.selectedbuttonback);
            lyt_2.setBackgroundResource(R.drawable.homebuttonback);
            lyt_3.setBackgroundResource(R.drawable.homebuttonback);
            lyt_1.setBackgroundResource(R.drawable.homebuttonback);
            lyt_5.setBackgroundResource(R.drawable.homebuttonback);
        }
        if(finalI == 4) {
            lyt_5.setBackgroundResource(R.drawable.selectedbuttonback);
            lyt_2.setBackgroundResource(R.drawable.homebuttonback);
            lyt_3.setBackgroundResource(R.drawable.homebuttonback);
            lyt_4.setBackgroundResource(R.drawable.homebuttonback);
            lyt_1.setBackgroundResource(R.drawable.homebuttonback);
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