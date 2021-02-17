package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {
    EditText etxemail, etxpassword;
    CheckBox cb_remember;

    public static CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

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



        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.wagen.cl", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash::", Base64.encodeToString(md.digest(), Base64.DEFAULT));//will give developer key hash
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //        Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
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
                if(cb_remember.isChecked() || userModel.account_type != 0){
                    Preference.getInstance().put(this, PrefConst.PREFKEY_USEREMAIL,userModel.email);
                    Preference.getInstance().put(this, PrefConst.PREFKEY_USERPWD,userModel.password);
                    Preference.getInstance().put(this, PrefConst.PREFKEY_ACCOUNTTYPE,String.valueOf(userModel.account_type));
                }

                JSONArray workshop = response.getJSONArray("workshop");
                ArrayList<String>workship_array = new ArrayList<>();
                for(int i=0; i<workshop.length(); i++){
                    JSONObject oneshop = workshop.getJSONObject(i);

                    workship_array.add(oneshop.getString("name"));
                }
                Preference.getInstance().putShared_cities_Preference(LoginActivity.this, PrefConst.PREFKEY_WORKSHOP, workship_array);


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
                Preference.getInstance().putSharedcarmodelPreference(LoginActivity.this, PrefConst.PREFKEY_CARS, carModels);

                JSONArray cities = response.getJSONArray("cities");
                ArrayList<String>cites_array = new ArrayList<>();
                for(int i=0; i<cities.length(); i++){
                    JSONObject city = cities.getJSONObject(i);

                    cites_array.add(city.getString("city_name"));
                }
                Preference.getInstance().putShared_cities_Preference(LoginActivity.this, PrefConst.PREFKEY_CITIES, cites_array);

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
                Preference.getInstance().putSharedmembershipPreference(LoginActivity.this, PrefConst.PREFKEY_MEMBERSHIP, membershipModels);

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
                    oneserviceModel.cu_status = oneservice.getInt("cu_status");
                    services1.add(oneserviceModel);
                }
                Preference.getInstance().putSharedservicePreference(LoginActivity.this, PrefConst.PREFKEY_SERVICES, services1);

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
                Preference.getInstance().putSharedcouponPreference(LoginActivity.this, PrefConst.PREFKEY_COUPON, couponcodes1);

               // if( userModel.account_type== 1) socialLogout();

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
        Intent intent = new Intent(LoginActivity.this, ForgotActivity.class);
        startActivity(intent);
        finish();
    }

    public void gotosignup(View view) {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    public void withGoogle(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void withFacebook(View view) {
        loginWithFB();
    }

    private void loginWithFB() {
        // set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();

                // Facebook Email address
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                Log.v("LoginActivity Response ", response.toString());

                                AccessToken accessToken = AccessToken.getCurrentAccessToken();
                                boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
                                Log.d("IsLoggedIn???", String.valueOf(isLoggedIn));

                                Log.d("Login Token!!!", loginResult.getAccessToken().getToken());

                                try {
                                    if(object.has("email")){
                                        String email = "";
                                        if(object.getString("email")!= null) email = object.getString("email");
                                        String first_name = "";
                                        if(object.getString("first_name")!= null) first_name=object.getString("first_name");
                                        String last_name = "";
                                        if(object.getString("last_name") != null) last_name = object.getString("last_name");
                                        String picture = "";
                                       // if(object.getString("picture") != null) picture =object.getString("picture");
                                        if(object.getString("id") != null) picture ="http://graph.facebook.com/"+object.getString("id")+"/picture?type=large";

                                        Log.d("photo_facebook==", object.toString());

                                        if(email.length()==0) {
                                            Toast.makeText(LoginActivity.this, getString(R.string.cannotgetaddress), Toast.LENGTH_SHORT).show();

                                        }else{
                                            Log.d("FB email: ", email);
                                            processSocial(email,"2", first_name, last_name, picture);
                                        }
                                    }else{
                                        Toast.makeText(LoginActivity.this, "We are sorry, we can't get your email address. Please try another login option", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, first_name, last_name, email, gender, birthday, picture");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                LoginManager.getInstance().logOut();

            }

            @Override
            public void onError(FacebookException e) {
                Log.d("Facebook login error!!!", e.getMessage());
            }
        });
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("LoginActivity", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateUI(GoogleSignInAccount account){
        if(account != null){
            String name = account.getDisplayName();
            String firstname = "";
            if(account.getGivenName() != null) firstname = account.getGivenName();
            String lastname = "";
            if(account.getFamilyName() != null) lastname = account.getFamilyName();
            String photourl = "";
            if(account.getPhotoUrl() != null) photourl = String.valueOf(account.getPhotoUrl());

            String personEmail = "";
            if(account.getEmail() != null) personEmail = account.getEmail();
            if(personEmail.length()==0){
                Toast.makeText(this, getString(R.string.cannotgetaddress), Toast.LENGTH_SHORT).show();
            }else{
                Log.d("name/email===>", name + "/" + personEmail);
                processSocial(personEmail,"1", firstname, lastname, photourl);
            }
        }
    }

    private void processSocial(String email, String signuptype, String firstname, String lastname, String photourl) {
        Map<String, String> params = new HashMap<>();

        params.put("first_name", firstname);
        params.put("last_name", lastname);
        params.put("email", email);
        params.put("password", "social");
        params.put("phonenumber", "");
        params.put("photo", photourl);
        params.put("account_type", signuptype);
        params.put("social_id", "");
        call_postApi(Constants.BASE_URL, "loginonloginpage", params);
    }

    private void socialLogout(){
        if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null){
            //Logged in so show the login button
            LoginManager.getInstance().logOut();
        }
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                    }
                });
    }
}