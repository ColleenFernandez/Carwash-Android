package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Model.CarModel;
import com.wagen.cl.Model.Packages;
import com.wagen.cl.Model.Service;
import com.wagen.cl.Model.UserModel;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ForgotActivity extends BaseActivity {
    LinearLayout lytforgot;
    EditText etxemail, etxcode, etxpassword, etxconfirmpass;
    String verifycode = "-1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        lytforgot=(LinearLayout)findViewById(R.id.lyt_forgot);
        etxemail=(EditText)findViewById(R.id.etx_email);
        etxcode=(EditText)findViewById(R.id.etx_code);
        etxpassword=(EditText)findViewById(R.id.etx_password);
        etxconfirmpass=(EditText)findViewById(R.id.etx_confirmpassword);
        lytforgot.setVisibility(View.GONE);
    }

    public void backtologin(View view) {
        back();
    }
    public void back(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        back();
    }

    public void callforgot(View view) {
        if(lytforgot.getVisibility() == View.VISIBLE){
            if(checkvalid()) callupdatepassword();
        }else{
            if(etxemail.getText().toString().length()==0){
                Toast.makeText(this, R.string.enter_email, Toast.LENGTH_SHORT).show();
            }else{
                callconfirmemail();
            }
        }
    }
    public Boolean checkvalid(){
        if(etxemail.getText().toString().length()==0){
            Toast.makeText(this, R.string.enter_email, Toast.LENGTH_SHORT).show();
            return false;
        }else if(etxcode.getText().toString().length()==0 || !etxcode.getText().toString().equals(verifycode)){
            Toast.makeText(this, R.string.enter_corret_code, Toast.LENGTH_SHORT).show();
            return false;
        }else if(etxpassword.getText().toString().length()==0){
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

    private void callupdatepassword() {
        Map<String, String> params = new HashMap<>();
        params.put("email", etxemail.getText().toString());
        params.put("password", etxpassword.getText().toString());
        call_postApi(Constants.BASE_URL, "updatepassword", params);
    }

    private void callconfirmemail() {
        Map<String, String> params = new HashMap<>();
        params.put("email", etxemail.getText().toString());
        call_postApi(Constants.BASE_URL, "forgotconfirmemail", params);
    }
    public void returnapireponse(JSONObject response, String method) {
        try {
            String result_code = response.getString("message");
            if (result_code.equals("success")) {
                if(method.equals("forgotconfirmemail")){
                    verifycode = response.getString("verificationcode");
                    Log.d("verifycode==", verifycode);
                    if(lytforgot.getVisibility() == View.GONE) lytforgot.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(this, R.string.passupdatesuccess, Toast.LENGTH_SHORT).show();
                    back();
                }

            }else {
                Toast.makeText(this, result_code, Toast.LENGTH_SHORT).show();
            }
        } catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
        }
    }

}