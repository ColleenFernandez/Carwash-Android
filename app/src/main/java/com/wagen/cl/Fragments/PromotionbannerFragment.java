package com.wagen.cl.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wagen.cl.Activity.FullpromotionActivity;
import com.wagen.cl.Activity.MainActivity;
import com.wagen.cl.Activity.Order1Activity;
import com.wagen.cl.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PromotionbannerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PromotionbannerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ImageView imv_banner;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;
    MainActivity mainActivity;

    public PromotionbannerFragment() {
        // Required empty public constructor
    }


    public static PromotionbannerFragment newInstance(String param1, String param2) {
        PromotionbannerFragment fragment = new PromotionbannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_promotionbanner, container, false);
        imv_banner=(ImageView)view.findViewById(R.id.imv_banner);
        Glide.with(this)
                .load(Uri.parse(mParam1))
                .into(imv_banner);

        imv_banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, FullpromotionActivity.class);
                startActivity(intent);
                mainActivity.finish();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }
}