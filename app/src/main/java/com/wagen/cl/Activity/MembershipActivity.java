package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.wagen.cl.Adapter.MembershipAdapter;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Model.MembershipModel;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

import java.util.ArrayList;

public class MembershipActivity extends BaseActivity {
    ListView listView;
    MembershipAdapter membershipAdapter;
    ArrayList<MembershipModel> membershipModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        listView =(ListView)findViewById(R.id.list_membership);


        for(int i=0; i< 2; i++){
            MembershipModel membershipModel = new MembershipModel();
            membershipModel.id = i;
            membershipModel.title = "Schedule "+(i+1)+" service per month";
            membershipModel.price = String.valueOf((i+1)*1000);
            membershipModel.renew_date = "2020-10-18";
            membershipModel.remaining_order = 2;
            if(i == 0) membershipModel.purchase_status = true;
            else  membershipModel.purchase_status = false;
            membershipModel.max_order_per_month = i+2;
            membershipModels.add(membershipModel);
        }

        listView.setAdapter(new MembershipAdapter(this, membershipModels,false));

    }

    public void goback(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}