package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wagen.cl.Adapter.MembershipAdapter;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Model.MembershipModel;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MembershipActivity extends BaseActivity {
    ListView listView;
    int memberhsipposition= -1;
    ArrayList<MembershipModel> membershipModels = new ArrayList<>();
    MembershipAdapter membershipAdapter;
    boolean purchase_status = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        listView =(ListView)findViewById(R.id.list_membership);
        membershipModels = Preference.getInstance().getSharedmembershipPreference(this,  PrefConst.PREFKEY_MEMBERSHIP);


        for(int i=0; i< membershipModels.size(); i++){
            /*MembershipModel membershipModel = new MembershipModel();
            membershipModel.id = i;
            membershipModel.title = "Schedule "+(i+1)+" service per month";
            membershipModel.price = String.valueOf((i+1)*1000);
            membershipModel.renew_date = "2020-10-18";
            membershipModel.remaining_order = 2;
            if(i == 0) membershipModel.purchase_status = true;
            else  membershipModel.purchase_status = false;
            membershipModel.max_order_per_month = i+2;
            membershipModels.add(membershipModel);*/

            if(membershipModels.get(i).id == Constants.userModel.membership_id && Constants.userModel.membership_count>0){
                membershipModels.get(i).remaining_order = Constants.userModel.membership_count;
                membershipModels.get(i).renew_date = Constants.userModel.membership_last_renew;
                membershipModels.get(i).purchase_status = true;
                purchase_status = true;
            }
        }
        membershipAdapter = new MembershipAdapter(this, membershipModels,purchase_status);

        listView.setAdapter(membershipAdapter);

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

    public void purchasemembership(MembershipModel trip1, int position) {
        memberhsipposition = position;
        Map<String, String> params = new HashMap<>();
        params.put("membershipid", String.valueOf(trip1.id));
        params.put("userid", String.valueOf(Constants.userModel.user_id));
        params.put("memership_count", String.valueOf(trip1.max_order_per_month));
        call_postApi(Constants.BASE_URL, "purchasemembership", params);
    }
    public void returnapireponse(JSONObject response, String method) {
        try {
            String result_code = response.getString("message");
            if (result_code.equals("success")) {
                String renewdate = response.getString("renewdate");

                Constants.userModel.membership_count = Integer.parseInt(response.getString("remaining_order"));
                Constants.userModel.membership_id = membershipModels.get(memberhsipposition).id;
                Constants.userModel.membership_last_renew = renewdate;

                membershipModels.get(memberhsipposition).renew_date = renewdate;
                membershipModels.get(memberhsipposition).purchase_status = true;
                membershipModels.get(memberhsipposition).remaining_order = Integer.parseInt(response.getString("remaining_order"));
                purchase_status = true;

                membershipAdapter = new MembershipAdapter(MembershipActivity.this, membershipModels, true);
                //membershipAdapter.notifyDataSetChanged();
                listView.setAdapter(membershipAdapter);

                showmessage(getString(R.string.purchasesuccess));
            }else {
                Toast.makeText(this, result_code, Toast.LENGTH_SHORT).show();
            }
        } catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
        }
    }

    public void showmessage(String message){
        Dialog settingdialog = new Dialog(MembershipActivity.this);
        settingdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingdialog.setContentView(R.layout.message_dialog);
        settingdialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        settingdialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        TextView txv_title =(TextView)settingdialog.findViewById(R.id.txv_title);
        ImageView imv_close =(ImageView)settingdialog.findViewById(R.id.imv_close);
        imv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(GivereviewActivity.this, MainActivity.class);
                startActivity(intent);*/
                //finish();
                settingdialog.dismiss();
            }
        });
        txv_title.setText(message);
        settingdialog.show();
        settingdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                settingdialog.dismiss();
                /*Intent intent = new Intent(GivereviewActivity.this, MainActivity.class);
                startActivity(intent);*/
               // finish();
            }
        });
    }
}