package com.wagen.cl.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.CalendarView;
import com.wagen.cl.Activity.Order1Activity;
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
        calendarView=(CalendarView)view.findViewById(R.id.calendarView);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        order1Activity = (Order1Activity) context;
    }
}