package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends BaseActivity {
    CircleImageView imvphoto;
    EditText etx_firstname, etx_lastname, etx_email, etx_phone, etx_password, etx_confirmpass;
    String photourl = "", socialid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        imvphoto=(CircleImageView)findViewById(R.id.imv_photo);
        etx_firstname=(EditText)findViewById(R.id.etx_firstname);
        etx_lastname=(EditText)findViewById(R.id.etx_lastname);
        etx_email=(EditText)findViewById(R.id.etx_email);
        etx_phone=(EditText)findViewById(R.id.etx_phone);
        etx_password=(EditText)findViewById(R.id.etx_password);
        etx_confirmpass=(EditText)findViewById(R.id.etx_confirmpassword);
    }

    public void backtologin(View view) {
        back();
    }

    public void editphoto(View view) {
        selectPhoto();
    }
    public void returnimageurl(String imageurl){
        photourl = imageurl;
        Glide.with(this)
                .load(Uri.parse(imageurl))
                .into(imvphoto);
    }

    public void callsignupapi(View view) {
        if(checkvalid()){
            callsignup(0);
        }
    }

    private boolean checkvalid() {
        if(etx_firstname.getText().toString().length()==0){
            Toast.makeText(this, getString(R.string.enter_firstname), Toast.LENGTH_SHORT).show();
            return false;
        }else if(etx_lastname.getText().toString().length()==0){
            Toast.makeText(this, getString(R.string.enter_lastname), Toast.LENGTH_SHORT).show();
            return false;
        }else if(etx_email.getText().toString().length()==0){
            Toast.makeText(this, getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
            return false;
        }else if(etx_password.getText().toString().length()==0){
            Toast.makeText(this, getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
            return false;
        }else if(etx_confirmpass.getText().toString().length()==0){
            Toast.makeText(this, getString(R.string.enterconfirmpass), Toast.LENGTH_SHORT).show();
            return false;
        }else if(!etx_password.getText().toString().equals(etx_confirmpass.getText().toString())){
            Toast.makeText(this, getString(R.string.entercorrectpass), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void callsignup(int signuptype){
        Map<String, String> params = new HashMap<>();
        params.put("first_name", etx_firstname.getText().toString());
        params.put("last_name", etx_lastname.getText().toString());
        params.put("email", etx_email.getText().toString());
        params.put("password", etx_password.getText().toString());
        params.put("phonenumber", etx_phone.getText().toString());
        params.put("photo", photourl);
        params.put("account_type", String.valueOf(signuptype));
        params.put("social_id", socialid);
        call_postApi(Constants.BASE_URL, "registerUser", params);
    }

    public void gotologin(View view) {
        back();
    }
    public void back(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void returnapireponse(JSONObject response, String method) {
        try {
            String result_code = response.getString("message");
            if (result_code.equals("success")) {
                Toast.makeText(SignupActivity.this, "Success", Toast.LENGTH_SHORT).show();
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
                Preference.getInstance().putSharedcarmodelPreference(SignupActivity.this, PrefConst.PREFKEY_CARS, carModels);

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
                Preference.getInstance().putSharedpackagesPreference(SignupActivity.this, PrefConst.PREFKEY_PACKAGES, packages1);

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
                Preference.getInstance().putSharedservicePreference(SignupActivity.this, PrefConst.PREFKEY_SERVICES, services1);

                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(SignupActivity.this, result_code, Toast.LENGTH_SHORT).show();
            }
        } catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(SignupActivity.this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
        }
    }
}