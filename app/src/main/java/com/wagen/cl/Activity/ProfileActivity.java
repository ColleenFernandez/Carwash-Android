package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Model.CarModel;
import com.wagen.cl.Model.MembershipModel;
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

public class ProfileActivity extends BaseActivity {
    ImageView imv_photo;
    EditText etx_firstname,etx_lastname,etx_email,etx_phone;
    String newphotourl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        imv_photo=(ImageView)findViewById(R.id.imv_photo);
        etx_firstname=(EditText)findViewById(R.id.etx_firstname);
        etx_lastname=(EditText)findViewById(R.id.etx_lastname);
        etx_email=(EditText)findViewById(R.id.etx_email);
        etx_phone=(EditText)findViewById(R.id.etx_phone);
        etx_firstname.setText(Constants.userModel.first_name);
        etx_lastname.setText(Constants.userModel.last_name);
        etx_email.setText(Constants.userModel.email);
        etx_email.setEnabled(false);
        etx_phone.setText(Constants.userModel.phonenumber);

        if(Constants.userModel.photo != null && Constants.userModel.photo.length()>5){
            newphotourl = Constants.userModel.photo;
            Glide.with(this).load(Uri.parse(Constants.userModel.photo)).into(imv_photo);
        }

        imv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhoto();
            }
        });
    }

    public void returnimageurl(String imageurl){
        newphotourl = imageurl;
        Glide.with(this).load(Uri.parse(imageurl)).into(imv_photo);
    }

    @Override
    public void onBackPressed() {
        goback();
    }

    public void goback(View view) {
       goback();
    }
    public void goback(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void callupdateprofile(View view) {
        if(etx_phone.getText().toString().length()==0){
            Toast.makeText(this, getString(R.string.enterphone), Toast.LENGTH_SHORT).show();
        }else if(Constants.userModel.phoneverify_status == 0 || !Constants.userModel.phonenumber.equals(etx_phone.getText().toString())){
           // firebasephoneverification(etx_phone.getText().toString());
        }else{
            updateprofile(etx_phone.getText().toString());
        }
    }

    public void updateprofile(String phonenumber){
       /* Map<String, String> params = new HashMap<>();
        params.put("first_name", etx_firstname.getText().toString());
        params.put("last_name", etx_lastname.getText().toString());
        params.put("email", etx_email.getText().toString());
        //params.put("password", etx_password.getText().toString());
        params.put("phonenumber", phonenumber);
        params.put("photo", newphotourl);
        params.put("phone_verifystatus", "1");
        params.put("user_id", String.valueOf(Constants.userModel.user_id));
        call_postApi(Constants.BASE_URL, "updateprofile", params);*/
    }

    public void confirmedphonenumber(String toString) {
        etx_phone.setText(toString);
        updateprofile(toString);
    }
    public void returnapireponse(JSONObject response, String method) {
        try {
            String result_code = response.getString("message");
            if (result_code.equals("success")) {
                Toast.makeText(ProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                Constants.userModel.photo = newphotourl;
                Constants.userModel.first_name = etx_firstname.getText().toString();
                Constants.userModel.last_name = etx_lastname.getText().toString();
                Constants.userModel.phonenumber = etx_phone.getText().toString();
                Constants.userModel.phoneverify_status = 1;

                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(ProfileActivity.this, result_code, Toast.LENGTH_SHORT).show();
            }
        } catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(ProfileActivity.this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
        }
    }
}