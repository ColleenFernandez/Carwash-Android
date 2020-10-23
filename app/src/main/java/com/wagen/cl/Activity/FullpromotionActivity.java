package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lwj.widget.viewpagerindicator.ViewPagerIndicator;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Fragments.PromotionbannerFragment;
import com.wagen.cl.Fragments.PromotionfullFragment;
import com.wagen.cl.Model.Promotionmodel;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FullpromotionActivity extends BaseActivity {
    ArrayList<Promotionmodel> promotionmodels = new ArrayList<>();
    ViewPager viewPager;
    ViewPagerIndicator indicator_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullpromotion);

        promotionmodels = Preference.getInstance().getSharedpromoPreference(this, PrefConst.PREFKEY_Promotion);

        viewPager=(ViewPager)findViewById(R.id.viewpager);
        indicator_line =(ViewPagerIndicator)findViewById(R.id.indicator_line);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        indicator_line.setViewPager(viewPager);
    }

    public void goback(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return PromotionfullFragment.newInstance(promotionmodels.get(pos).bigimage, promotionmodels.get(pos).title, promotionmodels.get(pos).des);
        }

        @Override
        public int getCount() {
            return promotionmodels.size();
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}