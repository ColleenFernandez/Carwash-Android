package com.wagen.cl.Activity;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.shuhart.stepview.StepView;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Fragments.Step1Fragment;
import com.wagen.cl.Fragments.Step2Fragment;
import com.wagen.cl.Fragments.Step3Fragment;
import com.wagen.cl.Fragments.Step4Fragment;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

import java.util.ArrayList;


public class Order1Activity extends BaseActivity {
    StepView stepView;
    LinearLayout lytcontainer;
    TextView txvtitle;
    ArrayList<Fragment> fragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order1);
        stepView =(StepView)findViewById(R.id.step_view);
        lytcontainer =(LinearLayout) findViewById(R.id.container);
        txvtitle=(TextView)findViewById(R.id.txv_title);
        txvtitle.setText(getResources().getStringArray(R.array.steptitles)[0]);
        fragments.add(new Step1Fragment());
        fragments.add(new Step2Fragment());
        fragments.add(new Step3Fragment());
        fragments.add(new Step4Fragment());

        ImageView goback =(ImageView)findViewById(R.id.goback);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoback();
            }
        });
        gotonextstep(0);

    }

    public void gotonextstep(int step){
        stepView.go(step, true);
        beginTransction(fragments.get(step));
        txvtitle.setText(getResources().getStringArray(R.array.steptitles)[step]);
    }

    public void beginTransction(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        // transaction.addToBackStack(null);
        transaction.commit();

    }


    @Override
    public void onBackPressed() {
        gotoback();
    }

    public void gotoback(){
        if(stepView.getCurrentStep() == 0){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            beginTransction(fragments.get(stepView.getCurrentStep()-1));
            stepView.go(stepView.getCurrentStep()-1, true);
            txvtitle.setText(getResources().getStringArray(R.array.steptitles)[stepView.getCurrentStep()-1]);
        }
    }

    public void gotoprev(View view) {
        gotoback();
    }

    public void gotonext(View view) {
        if(stepView.getCurrentStep() != 3)
        gotonextstep(stepView.getCurrentStep()+1);
    }



}