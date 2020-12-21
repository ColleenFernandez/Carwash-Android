package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import java.security.NoSuchAlgorithmException;

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
import com.wagen.cl.Model.Service;
import com.wagen.cl.Model.UserModel;
import com.wagen.cl.R;
import com.wagen.cl.Utils.BaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends BaseActivity {
   // CircleImageView imvphoto;
    EditText etx_firstname, etx_lastname, etx_email, etx_phone, etx_password, etx_confirmpass;
    String photourl = "", socialid="";

    public static CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //imvphoto=(CircleImageView)findViewById(R.id.imv_photo);
        etx_firstname=(EditText)findViewById(R.id.etx_firstname);
        etx_lastname=(EditText)findViewById(R.id.etx_lastname);
        etx_email=(EditText)findViewById(R.id.etx_email);
        etx_phone=(EditText)findViewById(R.id.etx_phone);
        etx_password=(EditText)findViewById(R.id.etx_password);
        etx_confirmpass=(EditText)findViewById(R.id.etx_confirmpassword);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.app.imageagent", PackageManager.GET_SIGNATURES);
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

    public void backtologin(View view) {
        back();
    }

    /*public void editphoto(View view) {
        selectPhoto();
    }
    public void returnimageurl(String imageurl){
        photourl = imageurl;
        Glide.with(this)
                .load(Uri.parse(imageurl))
                .into(imvphoto);
    }*/

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
                userModel.membership_last_renew =user_info.getString("membership_last_renew");
                userModel.membership_id = user_info.getInt("membership_id");
                userModel.membership_count = user_info.getInt("membership_count");
                Constants.userModel = userModel;


                Preference.getInstance().put(this, PrefConst.PREFKEY_ID,String.valueOf(userModel.user_id));

                JSONArray workshop = response.getJSONArray("workshop");
                ArrayList<String>workship_array = new ArrayList<>();
                for(int i=0; i<workshop.length(); i++){
                    JSONObject oneshop = workshop.getJSONObject(i);

                    workship_array.add(oneshop.getString("name"));
                }
                Preference.getInstance().putShared_cities_Preference(SignupActivity.this, PrefConst.PREFKEY_WORKSHOP, workship_array);

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
                    oneserviceModel.cu_status = oneservice.getInt("cu_status");
                    services1.add(oneserviceModel);
                }
                Preference.getInstance().putSharedservicePreference(SignupActivity.this, PrefConst.PREFKEY_SERVICES, services1);

                JSONArray cities = response.getJSONArray("cities");
                ArrayList<String>cites_array = new ArrayList<>();
                for(int i=0; i<cities.length(); i++){
                    JSONObject city = cities.getJSONObject(i);

                    cites_array.add(city.getString("city_name"));
                }
                Preference.getInstance().putShared_cities_Preference(SignupActivity.this, PrefConst.PREFKEY_CITIES, cites_array);

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
                Preference.getInstance().putSharedmembershipPreference(SignupActivity.this, PrefConst.PREFKEY_MEMBERSHIP, membershipModels);

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
                Preference.getInstance().putSharedcouponPreference(SignupActivity.this, PrefConst.PREFKEY_COUPON, couponcodes1);

                if( userModel.account_type== 1) socialLogout();

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
                                        String email = object.getString("email");
                                        String first_name = object.getString("first_name");
                                        String last_name = object.getString("last_name");
                                        String picture = object.getString("picture");
                                        Log.d("FB email: ", email);
                                        processSocial(email, first_name, last_name, picture,"2");
                                    }else{
                                        Toast.makeText(SignupActivity.this, "We are sorry, we can't get your email address. Please try another signup option", Toast.LENGTH_SHORT).show();
                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, first_name, last_name, email, gender, birthday, picture.type(large)");
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
            String firstname = account.getGivenName();
            String lastname = account.getFamilyName();
            String photourl = String.valueOf(account.getPhotoUrl());
            String personEmail = account.getEmail();
            Log.d("name/email===>", name + "/" + personEmail);
            processSocial(personEmail, firstname, lastname, photourl,"1");
        }
    }

    private void processSocial(String email, String firstname, String lastname, String photourl, String signuptype) {
        if(lastname == null) lastname = "";
        Map<String, String> params = new HashMap<>();
        params.put("first_name", firstname);
        params.put("last_name", lastname);
        params.put("email", email);
        params.put("password", "social");
        params.put("phonenumber", "");
        params.put("photo", photourl);
        params.put("account_type", signuptype);
        params.put("social_id", "");
        call_postApi(Constants.BASE_URL, "registerUser", params);
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