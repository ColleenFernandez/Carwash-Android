package com.wagen.cl.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    TextView txv_des1, txv_des2, txv_des3, txv_des4;
    LinearLayout lyt_1,lyt_2,lyt_3,lyt_4;
    LinearLayout firstitem;
    ArrayList<Packages> packages = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_step2, container, false);
        packages = Preference.getInstance().getSharedpackagesmodelPreference(order1Activity, PrefConst.PREFKEY_PACKAGES);
        firstitem=(LinearLayout)view.findViewById(R.id.lyt_firstitem);
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

        txv_des1=(TextView)view.findViewById(R.id.txv_des1);
        txv_des2=(TextView)view.findViewById(R.id.txv_des2);
        txv_des3=(TextView)view.findViewById(R.id.txv_des3);
        txv_des4=(TextView)view.findViewById(R.id.txv_des4);

        txv_title1.setText(packages.get(0).package_name);
        txv_price1.setText("$"+numberformating(packages.get(0).package_price));
        txv_des1.setText(packages.get(0).package_description.replaceAll("_","\n"));
        txv_title2.setText(packages.get(1).package_name);
        txv_price2.setText("$"+numberformating(packages.get(1).package_price));
        txv_des2.setText(packages.get(1).package_description.replaceAll("_","\n"));
        txv_title3.setText(packages.get(2).package_name);
        txv_price3.setText("$"+numberformating(packages.get(2).package_price));
        txv_des3.setText(packages.get(2).package_description.replaceAll("_","\n"));
        txv_title4.setText(packages.get(3).package_name);
        txv_price4.setText("$"+numberformating(packages.get(3).package_price));
        txv_des4.setText(packages.get(3).package_description.replaceAll("_","\n"));

        lyt_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbuttons(0);
            }
        });
        lyt_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbuttons(1);
            }
        });
        lyt_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbuttons(2);
            }
        });

        lyt_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshbuttons(3);
            }
        });




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("carid==", String.valueOf(Constants.orderModel.car_id));
        if(Constants.orderModel.package_id == 1) lyt_1.setBackgroundResource(R.drawable.package_select);
        if(Constants.orderModel.package_id == 2) lyt_2.setBackgroundResource(R.drawable.package_select);
        if(Constants.orderModel.package_id == 3) lyt_3.setBackgroundResource(R.drawable.package_select);
        if(Constants.orderModel.package_id == 4) lyt_4.setBackgroundResource(R.drawable.package_select);
    }

    public void refreshbuttons(int position){
        Constants.orderModel.package_id = position+1;
        if(position == 0){
            lyt_1.setBackgroundResource(R.drawable.package_select);
            lyt_2.setBackgroundResource(R.drawable.package_unselect);
            lyt_3.setBackgroundResource(R.drawable.package_unselect);
            lyt_4.setBackgroundResource(R.drawable.package_unselect);
        }
        if(position == 1){
            lyt_2.setBackgroundResource(R.drawable.package_select);
            lyt_1.setBackgroundResource(R.drawable.package_unselect);
            lyt_3.setBackgroundResource(R.drawable.package_unselect);
            lyt_4.setBackgroundResource(R.drawable.package_unselect);
        }
        if(position == 2){
            lyt_3.setBackgroundResource(R.drawable.package_select);
            lyt_2.setBackgroundResource(R.drawable.package_unselect);
            lyt_1.setBackgroundResource(R.drawable.package_unselect);
            lyt_4.setBackgroundResource(R.drawable.package_unselect);
        }
        if(position == 3){
            lyt_4.setBackgroundResource(R.drawable.package_select);
            lyt_2.setBackgroundResource(R.drawable.package_unselect);
            lyt_3.setBackgroundResource(R.drawable.package_unselect);
            lyt_1.setBackgroundResource(R.drawable.package_unselect);
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

}