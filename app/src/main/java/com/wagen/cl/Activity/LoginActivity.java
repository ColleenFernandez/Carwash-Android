package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class LoginActivity extends BaseActivity {
    EditText etxemail, etxpassword;
    CheckBox cb_remember;

    private static final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etxemail=(EditText)findViewById(R.id.etx_email);
        etxpassword=(EditText)findViewById(R.id.etx_password);
        cb_remember=(CheckBox)findViewById(R.id.cb_remember);

        checkAllPermission();

        String email = Preference.getInstance().getValue(this, PrefConst.PREFKEY_USEREMAIL,"");
        if(email.length()>0){
            etxemail.setText( Preference.getInstance().getValue(this, PrefConst.PREFKEY_USEREMAIL,""));
            etxpassword.setText( Preference.getInstance().getValue(this, PrefConst.PREFKEY_USERPWD,""));
            cb_remember.setChecked(true);
            calllogin(Preference.getInstance().getValue(this, PrefConst.PREFKEY_ACCOUNTTYPE,""));
        }
    }

    public void checkAllPermission() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        if (hasPermissions(this, PERMISSIONS)){
        }else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, 101);
        }
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
                Log.d("return==", "permission");
            }

        }
        return true;
    }

    public void callloginapi(View view) {
        if(checkvalid()){
            calllogin("0");
        }
    }

    private void calllogin(String accounttype) {
        Map<String, String> params = new HashMap<>();
        params.put("email", etxemail.getText().toString());
        params.put("password", etxpassword.getText().toString());
        params.put("account_type", accounttype);
        call_postApi(Constants.BASE_URL, "login", params);
    }

    public void returnapireponse(JSONObject response, String method) {
        try {
            String result_code = response.getString("message");
            if (result_code.equals("success")) {
                Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                UserModel userModel = new UserModel();
                JSONObject user_info = response.getJSONObject("user_info");
                userModel.user_id = user_info.getInt("user_id");
                userModel.first_name = user_info.getString("first_name");
                userModel.last_name = user_info.getString("last_name");
                userModel.email = user_info.getString("email");
                userModel.phonenumber = user_info.getString("phonenumber");
                userModel.password = user_info.getString("password");
                userModel.photo = user_info.getString("photo");
                userModel.account_type = user_info.getInt("account_type");
                userModel.social_id = user_info.getString("social_id");
                Constants.userModel = userModel;

                Preference.getInstance().put(this, PrefConst.PREFKEY_ID,String.valueOf(userModel.user_id));
                if(cb_remember.isChecked()){
                    Preference.getInstance().put(this, PrefConst.PREFKEY_USEREMAIL,userModel.email);
                    Preference.getInstance().put(this, PrefConst.PREFKEY_USERPWD,userModel.password);
                    Preference.getInstance().put(this, PrefConst.PREFKEY_ACCOUNTTYPE,String.valueOf(userModel.account_type));
                }

                JSONArray cars = response.getJSONArray("cars");
                ArrayList<CarModel> carModels = new ArrayList<>();
                for(int i=0; i<cars.length(); i++){
                    JSONObject car = cars.getJSONObject(i);
                    CarModel carModel = new CarModel();
                    carModel.car_id = car.getInt("car_id");
                    carModel.car_name = car.getString("car_name");
                    carModel.car_photo = car.getString("car_photo");
                    carModels.add(carModel);
                }
                Preference.getInstance().putSharedcarmodelPreference(LoginActivity.this, PrefConst.PREFKEY_CARS, carModels);

                JSONArray packages = response.getJSONArray("packages");
                ArrayList<Packages> packages1 = new ArrayList<>();
                for(int i=0; i<packages.length(); i++){
                    JSONObject onepackage = packages.getJSONObject(i);
                    Packages packages2 = new Packages();
                    packages2.package_id = onepackage.getInt("package_id");
                    packages2.package_name = onepackage.getString("package_name");
                    packages2.package_time = onepackage.getString("package_time");
                    packages2.package_description = onepackage.getString("package_description");
                    packages2.package_available_for_home = onepackage.getInt("package_available_for_home");
                    packages2.package_price = onepackage.getString("package_price");
                    packages1.add(packages2);
                }
                Preference.getInstance().putSharedpackagesPreference(LoginActivity.this, PrefConst.PREFKEY_PACKAGES, packages1);

                JSONArray services = response.getJSONArray("services");
                ArrayList<Service> services1 = new ArrayList<>();
                for(int i=0; i<services.length(); i++){
                    JSONObject oneservice = services.getJSONObject(i);
                    Service oneserviceModel = new Service();
                    oneserviceModel.service_id = oneservice.getInt("service_id");
                    oneserviceModel.service_name = oneservice.getString("service_name");
                    oneserviceModel.service_description = oneservice.getString("service_description");
                    oneserviceModel.service_time = oneservice.getString("service_time");
                    oneserviceModel.service_price = oneservice.getString("service_price");
                    services1.add(oneserviceModel);
                }
                Preference.getInstance().putSharedservicePreference(LoginActivity.this, PrefConst.PREFKEY_SERVICES, services1);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(LoginActivity.this, result_code, Toast.LENGTH_SHORT).show();
            }
        } catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(LoginActivity.this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkvalid() {
        if(etxemail.getText().toString().length()==0){
            Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
            return false;
        }else if(etxpassword.getText().toString().length()==0){
            Toast.makeText(this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void gotoforgotpassword(View view) {
    }

    public void gotosignup(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
}