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

    EditText etxemail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        etxemail=(EditText)findViewById(R.id.etx_email);
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
        if(etxemail.getText().toString().length()==0){
            Toast.makeText(this, R.string.enter_email, Toast.LENGTH_SHORT).show();
        }else{
            callconfirmemail();
        }

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
                Constants.verifycode = response.getString("verificationcode");
                Constants.email = etxemail.getText().toString();
                Log.d("verifycode==", Constants.verifycode);
                Intent intent = new Intent(this, Forgot2Activity.class);
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

}