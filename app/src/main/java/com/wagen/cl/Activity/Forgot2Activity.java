package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Forgot2Activity extends BaseActivity {
    EditText   etxpassword, etxconfirmpass;
    Pinview etxcode;
    TextView txvcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot2);
        etxcode=(Pinview)findViewById(R.id.etx_code);
        etxpassword=(EditText)findViewById(R.id.etx_password);
        etxconfirmpass=(EditText)findViewById(R.id.etx_confirmpassword);
        txvcode=(TextView)findViewById(R.id.txv_con);

        etxcode.setPinViewEventListener(new Pinview.PinViewEventListener() {
            @Override
            public void onDataEntered(Pinview pinview, boolean fromUser) {
                //Make api calls here or what not
                //Toast.makeText(MainActivity.this, pinview.getValue(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Boolean checkvalid(){
        if(etxpassword.getText().toString().length()==0){
            Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show();
            return false;
        }else if(etxconfirmpass.getText().toString().length()==0){
            Toast.makeText(this, R.string.enterconfirmpass, Toast.LENGTH_SHORT).show();
            return false;
        }else if(!etxpassword.getText().toString().equals(etxconfirmpass.getText().toString())){
            Toast.makeText(this, R.string.entercorrectpass, Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }

    public void returnapireponse(JSONObject response, String method) {
        try {
            String result_code = response.getString("message");
            if (result_code.equals("success")) {
                Toast.makeText(this, R.string.passupdatesuccess, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(this, result_code, Toast.LENGTH_SHORT).show();
            }
        } catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
        }
    }

    public void back(){
        Intent intent = new Intent(this, ForgotActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }

    public void callupdatepassword(View view) {
        if(checkvalid()){
            Map<String, String> params = new HashMap<>();
            params.put("email", Preference.getInstance().getValue(this, PrefConst.PREFKEY_USEREMAIL,""));
            params.put("password", etxpassword.getText().toString());
            call_postApi(Constants.BASE_URL, "updatepassword", params);
        }
    }

    public void confirmcode(View view) {
        if(etxcode.getValue().length()==0 || !etxcode.getValue().equals(Constants.verifycode)){
            Toast.makeText(this, R.string.enter_corret_code, Toast.LENGTH_SHORT).show();
        }else{
            txvcode.setVisibility(View.GONE);
            Toast.makeText(this, "Verify Confirmed", Toast.LENGTH_SHORT).show();
        }
    }
}