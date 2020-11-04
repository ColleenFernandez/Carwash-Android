package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.lwj.widget.viewpagerindicator.ViewPagerIndicator;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Fragments.PromotionbannerFragment;
import com.wagen.cl.Model.OrderModel;
import com.wagen.cl.Model.Promotionmodel;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;
import static androidx.viewpager.widget.PagerAdapter.POSITION_UNCHANGED;

public class MainActivity extends BaseActivity {
    ArrayList<Promotionmodel> promotionmodels = new ArrayList<>();
    ViewPager viewPager;
    ViewPagerIndicator indicator_line;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Constants.orderModel = new OrderModel();
        viewPager=(ViewPager)findViewById(R.id.viewpager);
        indicator_line =(ViewPagerIndicator)findViewById(R.id.indicator_line);
        Constants.frompagestatus_fororderlist = 0;

        promotionmodels = Preference.getInstance().getSharedpromoPreference(this, PrefConst.PREFKEY_Promotion);

        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        indicator_line.setViewPager(viewPager);
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == promotionmodels.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);



    }

    public void gotobookingservice(View view) {
        Intent intent = new Intent(this, Order1Activity.class);
        startActivity(intent);
        finish();
    }

    public void gotomybooking(View view) {
        Constants.frompagestatus_fororderlist = 0;
        Intent intent = new Intent(this, MybookingActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotomyprofile(View view) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotosetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotoreview(View view) {
        Constants.frompagestatus_fororderlist = 1;
        Intent intent = new Intent(this, MybookingActivity.class);
        startActivity(intent);
        finish();

    }

    public void gotopurchase(View view) {
        Intent intent = new Intent(this, MembershipActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotodetail(View view) {
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int pos) {
            return PromotionbannerFragment.newInstance(promotionmodels.get(pos).banner, "");
        }

        @Override
        public int getCount() {
            return promotionmodels.size();
        }
    }
}