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

        for(int i = 0; i<3; i++){
            Promotionmodel promotionmodel = new Promotionmodel();
            promotionmodel.id = i;
            promotionmodel.title = "Promotion"+String.valueOf(i);
            promotionmodel.des = "Promotion Description "+String.valueOf(i);
            promotionmodel.banner = "https://www.crushpixel.com/static13/preview2/stock-photo-flash-sale-banner-promotion-template-design-on-red-background-with-golden-thunder-big-sale-special-60-offer-labels-end-of-season-special-offer-banner-shop-now-1224771.jpg";
            promotionmodel.bigimage = "https://i.pinimg.com/474x/66/62/54/66625423329517738b250856e0732fb1.jpg";
            promotionmodels.add(promotionmodel);
        }
        Preference.getInstance().putSharedpromotionPreference(this, PrefConst.PREFKEY_Promotion, promotionmodels);

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
    }

    public void gotoreview(View view) {
    }

    public void gotopurchase(View view) {
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