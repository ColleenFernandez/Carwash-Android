package com.wagen.cl.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wagen.cl.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PromotionfullFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PromotionfullFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String mParam3;
    View view;
    ImageView imv_full;
    TextView txv_title, txv_des;

    public PromotionfullFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PromotionfullFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PromotionfullFragment newInstance(String param1, String param2, String param3) {
        PromotionfullFragment fragment = new PromotionfullFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_promotionfull, container, false);
        imv_full =(ImageView)view.findViewById(R.id.imv_full);
        Glide.with(this)
                .load(Uri.parse(mParam1))
                .into(imv_full);

        txv_title =(TextView)view.findViewById(R.id.txv_title);
        txv_des =(TextView)view.findViewById(R.id.txv_des);
        txv_title.setText(mParam2);
        txv_des.setText(mParam3);
        return view;
    }
}