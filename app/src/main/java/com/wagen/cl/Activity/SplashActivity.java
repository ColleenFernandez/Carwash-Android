package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.Constant.PrefConst;
import com.wagen.cl.Constant.Preference;
import com.wagen.cl.Model.CarModel;
import com.wagen.cl.Model.Coupon;
import com.wagen.cl.Model.MembershipModel;
import com.wagen.cl.Model.PackagePricesModel;
import com.wagen.cl.Model.Packages;
import com.wagen.cl.Model.Promotionmodel;
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

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        String email = Preference.getInstance().getValue(this, PrefConst.PREFKEY_USEREMAIL,"");
        if(email.length()>0){
            calllogin(Preference.getInstance().getValue(this, PrefConst.PREFKEY_USEREMAIL,""),Preference.getInstance().getValue(this, PrefConst.PREFKEY_USERPWD,""),Preference.getInstance().getValue(this, PrefConst.PREFKEY_ACCOUNTTYPE,""));
        }else{
            new CountDownTimer(2000, 1000) {

                public void onTick(long millisUntilFinished) {
                    //mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }.start();

        }


    }

    private void calllogin(String email, String password, String accounttype) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("account_type", accounttype);
        call_postApiwithoutprogress(Constants.BASE_URL, "login", params);
    }

    public void returnapireponse(JSONObject response, String method) {
        try {
            String result_code = response.getString("message");
            if (result_code.equals("success")) {
                //Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
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
                userModel.phoneverify_status = user_info.getInt("phone_verifystatus");

                userModel.membership_last_renew =user_info.getString("membership_last_renew");
                userModel.membership_id = user_info.getInt("membership_id");
                userModel.membership_count = user_info.getInt("membership_count");
                Constants.userModel = userModel;

                Preference.getInstance().put(this, PrefConst.PREFKEY_ID,String.valueOf(userModel.user_id));
                Preference.getInstance().put(this, PrefConst.PREFKEY_USEREMAIL,userModel.email);
                Preference.getInstance().put(this, PrefConst.PREFKEY_USERPWD,userModel.password);
                Preference.getInstance().put(this, PrefConst.PREFKEY_ACCOUNTTYPE,String.valueOf(userModel.account_type));


                JSONArray workshop = response.getJSONArray("workshop");
                ArrayList<String> workship_array = new ArrayList<>();
                for(int i=0; i<workshop.length(); i++){
                    JSONObject oneshop = workshop.getJSONObject(i);

                    workship_array.add(oneshop.getString("name"));
                }
                Preference.getInstance().putShared_cities_Preference(SplashActivity.this, PrefConst.PREFKEY_WORKSHOP, workship_array);


                JSONArray promotionsjson = response.getJSONArray("promotions");
                ArrayList<Promotionmodel> promotionmodels = new ArrayList<>();
                for(int i = 0; i<promotionsjson.length(); i++){
                    JSONObject oneobject = promotionsjson.getJSONObject(i);
                    Promotionmodel promotionmodel = new Promotionmodel();
                    promotionmodel.id = oneobject.getInt("id");
                    promotionmodel.title = oneobject.getString("title");
                    promotionmodel.des = oneobject.getString("description");
                    promotionmodel.banner = oneobject.getString("banner_image");
                    promotionmodel.bigimage = oneobject.getString("full_image");
                    promotionmodels.add(promotionmodel);
                }
                Preference.getInstance().putSharedpromotionPreference(this, PrefConst.PREFKEY_Promotion, promotionmodels);

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
                Preference.getInstance().putSharedcarmodelPreference(SplashActivity.this, PrefConst.PREFKEY_CARS, carModels);

                JSONArray cities = response.getJSONArray("cities");
                ArrayList<String>cites_array = new ArrayList<>();
                for(int i=0; i<cities.length(); i++){
                    JSONObject city = cities.getJSONObject(i);

                    cites_array.add(city.getString("city_name"));
                }
                Preference.getInstance().putShared_cities_Preference(SplashActivity.this, PrefConst.PREFKEY_CITIES, cites_array);

                JSONArray memberships = response.getJSONArray("memberships");
                ArrayList<MembershipModel> membershipModels = new ArrayList<>();
                for(int i=0; i<memberships.length(); i++){
                    JSONObject membership = memberships.getJSONObject(i);
                    MembershipModel membershipModel = new MembershipModel();
                    membershipModel.id = membership.getInt("id");
                    membershipModel.title = membership.getString("title");
                    membershipModel.max_order_per_month = membership.getInt("max_order_per_month");
                    membershipModel.price = membership.getString("price");
                    membershipModels.add(membershipModel);
                }
                Preference.getInstance().putSharedmembershipPreference(SplashActivity.this, PrefConst.PREFKEY_MEMBERSHIP, membershipModels);

                JSONArray packages = response.getJSONArray("packages");
                ArrayList<Packages> packages1 = new ArrayList<>();
                for(int i=0; i<packages.length(); i++){
                    JSONObject onepackage = packages.getJSONObject(i);
                    Packages packages2 = new Packages();
                    packages2.package_id = onepackage.getInt("package_id");
                    packages2.package_name = onepackage.getString("package_name");
                    packages2.package_time = onepackage.getString("package_time");
                    packages2.package_description = onepackage.getString("package_description");
                    packages2.package_available_for_home = onepackage.getString("package_available_for_home");

                    JSONArray prices_json = onepackage.getJSONArray("package_prices");
                    ArrayList<PackagePricesModel> packagePricesModels = new ArrayList<>();
                    for(int j=0; j< prices_json.length(); j++){
                        JSONObject oneprice_json = prices_json.getJSONObject(j);
                        PackagePricesModel packagePricesModel = new PackagePricesModel();
                        packagePricesModel.package_price_id = oneprice_json.getInt("package_price_id");
                        packagePricesModel.car_id = oneprice_json.getInt("car_id");
                        packagePricesModel.package_id = oneprice_json.getInt("package_id");
                        packagePricesModel.price = oneprice_json.getString("price");
                        packagePricesModel.price_home = oneprice_json.getString("price_home");
                        packagePricesModels.add(packagePricesModel);
                    }
                    packages2.packagePricesModels = packagePricesModels;
                    packages1.add(packages2);
                }
                Preference.getInstance().putSharedpackagesPreference(SplashActivity.this, PrefConst.PREFKEY_PACKAGES, packages1);

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
                    oneserviceModel.cu_status = oneservice.getInt("cu_status");
                    services1.add(oneserviceModel);
                }
                Preference.getInstance().putSharedservicePreference(SplashActivity.this, PrefConst.PREFKEY_SERVICES, services1);

                JSONArray couponcodes = response.getJSONArray("couponcodes");
                ArrayList<Coupon> couponcodes1 = new ArrayList<>();
                for(int i=0; i<couponcodes.length(); i++){
                    JSONObject oneservice = couponcodes.getJSONObject(i);
                    Coupon onecoupon = new Coupon();
                    onecoupon.id = oneservice.getInt("id");
                    onecoupon.code = oneservice.getString("code");
                    onecoupon.start_daet = oneservice.getString("start_date");
                    onecoupon.end_date = oneservice.getString("end_date");
                    onecoupon.discountvalue = Integer.parseInt(oneservice.getString("discount_value"));
                    couponcodes1.add(onecoupon);
                }
                Preference.getInstance().putSharedcouponPreference(SplashActivity.this, PrefConst.PREFKEY_COUPON, couponcodes1);

                // if( userModel.account_type== 1) socialLogout();

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                //Toast.makeText(SplashActivity.this, result_code, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        } catch(JSONException e){
            e.printStackTrace();
            //Toast.makeText(SplashActivity.this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}