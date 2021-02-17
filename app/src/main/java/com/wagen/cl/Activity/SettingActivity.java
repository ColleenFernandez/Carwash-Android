package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;
import com.wagen.cl.Utils.TouchImageView;

public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void goback(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void logout(View view) {
        Preference.getInstance().put(this, PrefConst.PREFKEY_USEREMAIL,"");
        Preference.getInstance().put(this, PrefConst.PREFKEY_USERPWD,"");
        Preference.getInstance().put(this, PrefConst.PREFKEY_ACCOUNTTYPE,"");

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void gototerms(View view) {
        String url = "https://servicioswagen.cl/admin/terms.pdf";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void gotoprivicy(View view) {
        String url = "https://servicioswagen.cl/admin/Privacy_Policy.pdf";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    public void showservice(View view) {
        TouchImageView img = new TouchImageView(this);
        img.setBackgroundColor(getResources().getColor(R.color.black));
        img.setImageResource(R.drawable.aboutservice);
        img.setMaxZoom(4f);
        setContentView(img);
    }
}