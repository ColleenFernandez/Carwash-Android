package com.wagen.cl.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.wagen.cl.Activity.Order1Activity;
import com.wagen.cl.Activity.OrderdetailActivity;
import com.wagen.cl.R;


public class Step4Fragment extends Fragment {
    View view;
    Order1Activity order1Activity;
    CalendarView calendarView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_step4, container, false);
        calendarView=(CalendarView)view.findViewById(R.id.calendar);

        TextView txv_next =(TextView)view.findViewById(R.id.txv_next);
        txv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //order1Activity.gotonextstep(3);
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
}