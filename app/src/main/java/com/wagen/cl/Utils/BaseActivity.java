package com.wagen.cl.Utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.soundcloud.android.crop.Crop;
import com.wagen.cl.Constant.Constants;
import com.wagen.cl.R;
import com.wagen.cl.WagenApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BaseActivity extends AppCompatActivity {

    public ProgressDialog progressBar;
    private Uri _imageCaptureUri;
    String _photoPath = "", imageurl="";
    String pdffilename = "";
    DatePickerDialog datePickerDialog;
    String  mVerificationId = "";

    EditText etx_phone;
    Pinview etx_code;
    PhoneAuthCredential credential;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);
        progressBar.setMessage("Please wait ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setCancelable(false);
        progressBar.setProgress(0);

    }
    public void call_postApiwithoutprogress(String baseurl, String method, Map<String, String> params){

        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                baseurl+method,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {

                        Parseresonse(json, method);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error============", String.valueOf(error));

                        Toast.makeText(BaseActivity.this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("paramsnewprduct==", params.toString());
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        WagenApplication.getInstance().addToRequestQueue(myRequest, "tag");
    }

    public void call_postApi(String baseurl, String method, Map<String, String> params){
        progressBar.show();
        StringRequest myRequest = new StringRequest(
                Request.Method.POST,
                baseurl+method,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        progressBar.dismiss();
                        Parseresonse(json, method);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error============", String.valueOf(error));
                        progressBar.dismiss();
                        Toast.makeText(BaseActivity.this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Log.d("paramsnewprduct==", params.toString());
                return params;
            }
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        WagenApplication.getInstance().addToRequestQueue(myRequest, "tag");
    }
    public void call_GetApi(String baseurl, String method,String parameter){
        progressBar.show();
        StringRequest myRequest = new StringRequest(
                Request.Method.GET,
                baseurl+method+"/"+parameter,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String json) {
                        progressBar.dismiss();
                        Parseresonse(json, method);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error============", String.valueOf(error));
                        progressBar.dismiss();
                        Toast.makeText(BaseActivity.this, getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();

                    }
                }) {
        };
        myRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        WagenApplication.getInstance().addToRequestQueue(myRequest, "tag");
    }

    private void Parseresonse(String json, String method) {
        Log.d("response=====", String.valueOf(json));
        progressBar.dismiss();
        try {
            JSONObject response = new JSONObject(json);
            String result_code = response.getString("message");
            if (result_code.equals("success")) {
               returnapireponse(response, method);
            }else {
                if(method.equals("login"))  returnapireponse(response, method);
                else Toast.makeText(BaseActivity.this, result_code, Toast.LENGTH_SHORT).show();
            }
        } catch(JSONException e){
            e.printStackTrace();
            Toast.makeText(BaseActivity.this,  getString(R.string.somethingwentwrong), Toast.LENGTH_SHORT).show();
        }
    }

    public void returnapireponse(JSONObject response, String method) {
    }


    //============================= Camera Part ========================
    public void selectPhoto() {
        final String[] items = {getString(R.string.take_photo),getString(R.string.choose_from_gallery), getString(R.string.cancel)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    doTakePhoto();
                } else if(item==1) {
                    doTakeGallery();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    public void doTakeGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, Constants.PICK_FROM_ALBUM);
    }
    public void doTakePhoto() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        _photoPath = BitmapUtils.getTempFolderPath() + "photo_temp.jpg";
        _imageCaptureUri = Uri.fromFile(new File(_photoPath));
        _imageCaptureUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(_photoPath));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, _imageCaptureUri);
        startActivityForResult(intent, Constants.PICK_FROM_CAMERA);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Crop.REQUEST_CROP: {

                if (resultCode == RESULT_OK) {
                    try {

                        File outFile = BitmapUtils.getOutputMediaFile(this, "temp.png");

                        InputStream in = getContentResolver().openInputStream(Uri.fromFile(outFile));
                        BitmapFactory.Options bitOpt = new BitmapFactory.Options();
                        Bitmap bitmap = BitmapFactory.decodeStream(in, null, bitOpt);
                        in.close();

                        ExifInterface ei = new ExifInterface(outFile.getAbsolutePath());
                        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                ExifInterface.ORIENTATION_NORMAL);

                        Bitmap returnedBitmap = bitmap;

                        switch (orientation) {

                            case ExifInterface.ORIENTATION_ROTATE_90:
                                returnedBitmap = BitmapUtils.rotateImage(bitmap, 90);
                                bitmap.recycle();
                                bitmap = null;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_180:
                                returnedBitmap = BitmapUtils.rotateImage(bitmap, 180);
                                bitmap.recycle();
                                bitmap = null;
                                break;
                            case ExifInterface.ORIENTATION_ROTATE_270:
                                returnedBitmap = BitmapUtils.rotateImage(bitmap, 270);
                                bitmap.recycle();
                                bitmap = null;
                                break;

                            default:
                                break;
                        }

                        Bitmap w_bmpSizeLimited = Bitmap.createScaledBitmap(returnedBitmap, returnedBitmap.getWidth(), returnedBitmap.getHeight(), true);
                        File newFile = BitmapUtils.getOutputMediaFile(this, System.currentTimeMillis() + ".png");
                        BitmapUtils.saveOutput(newFile, w_bmpSizeLimited);
                        _photoPath = newFile.getAbsolutePath();
                        Log.d("photopath===", _photoPath);
                        uploadimage(_photoPath);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            }


            case Constants.PICK_FROM_ALBUM:

                if (resultCode == RESULT_OK) {
                    _imageCaptureUri = data.getData();
                    beginCrop(_imageCaptureUri);
                }
                break;

            case Constants.PICK_FROM_CAMERA: {
                if (resultCode == RESULT_OK) {
                    try {
                        String filename = "IMAGE_" + System.currentTimeMillis() + ".jpg";

                        Bitmap bitmap = BitmapUtils.loadOrientationAdjustedBitmap(_photoPath);
                        String w_strFilePath = "";
                        String w_strLimitedImageFilePath = BitmapUtils.getUploadImageFilePath(bitmap, filename);
                        if (w_strLimitedImageFilePath != null) {
                            w_strFilePath = w_strLimitedImageFilePath;
                        }

                        _photoPath = w_strFilePath;
                        _imageCaptureUri = Uri.fromFile(new File(_photoPath));
                        //  _photoPath= BitmapUtils.getSizeLimitedImageFilePath(_photoPath);
                        _imageCaptureUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", new File(_photoPath));

                        beginCrop(_imageCaptureUri);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
                break;
            }

        }


    }
    private void beginCrop(Uri source) {
        // setToOther(true);
        Uri destination = Uri.fromFile(BitmapUtils.getOutputMediaFile(this, "temp.png"));
        // Crop.of(source, destination).asSquare().start(this);
        Crop.of(source, destination).withMaxSize(840, 1024).start(this);
    }
    public void uploadimage(String photoPath) {
        Log.d("photopath==", photoPath);
        progressBar.show();
        try {
            File file = null;
            file= new File(photoPath);
            Map<String, String> params = new HashMap<>();

            String url = Constants.BASE_URL + "uploadImage";

            MultiPartRequest reqMultiPart = new MultiPartRequest(url, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    progressBar.dismiss();
                    Log.d("error", error.getMessage());
                    // Toast.makeText(AddnewexpensesActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    //  showAlertDialog("Photo Upload is failed. Please try again.");
                }
            }, new Response.Listener<String>() {
                @Override
                public void onResponse(String json) {
                    progressBar.dismiss();
                    try {
                        JSONObject jsonObject=new JSONObject(json);
                        String message=jsonObject.getString("message");

                        if(message.equals("success")){

                            imageurl=jsonObject.getString("image_url");
                            Log.d("imageurl==", imageurl);
                            returnimageurl(imageurl);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, file, "image", params);
            reqMultiPart.setRetryPolicy(new DefaultRetryPolicy(
                    6000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            WagenApplication.getInstance().addToRequestQueue(reqMultiPart, url);

        } catch (Exception e) {

            e.printStackTrace();
            progressBar.dismiss();
            Toast.makeText(BaseActivity.this, getString(R.string.photo_upload_is_failed), Toast.LENGTH_SHORT).show();
        }
    }

    public void returnimageurl(String imageurl) {

    }





    public String changedatefomattoserverstyle(String date){   // from 12/28/2018 to 2018-12-28
        String[] datearray=date.split("/");
        String newformate=datearray[2]+"-"+datearray[0]+"-"+datearray[1];
        return newformate;
    }

    public String changedateformattoappstyle(String date){ // from 2018-12-28 to 12/28/2018
        String[] datearray=date.split("-");
        String newformate=datearray[1]+"/"+datearray[2]+"/"+datearray[0];
        return newformate;
    }



    public void setDateonview(TextView textView){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(BaseActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year,
                                          final int monthOfYear, final int dayOfMonth) {
                        String date = String.valueOf(dayOfMonth);
                        String month = String.valueOf(monthOfYear+1);

                        if(dayOfMonth<10) date = "0"+date;
                        if(monthOfYear+1<10) month = "0"+month;
                        textView.setText(month + "/" + date + "/" + year);
                        datePickerDialog.dismiss();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void setdatetime(TextView textView){
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        datePickerDialog = new DatePickerDialog(BaseActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, final int year,
                                          final int monthOfYear, final int dayOfMonth) {
                        String date = String.valueOf(dayOfMonth);
                        String month = String.valueOf(monthOfYear+1);

                        if(dayOfMonth<10) date = "0"+date;
                        if(monthOfYear+1<10) month = "0"+month;

                        String datemonthyear = month + "/" + date + "/" + year;

                        datePickerDialog.dismiss();

                        int  mHour, mMinute;
                        Calendar c = Calendar.getInstance();
                        mHour = c.get(Calendar.HOUR_OF_DAY);
                        mMinute = c.get(Calendar.MINUTE);

                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(BaseActivity.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {
                                        String strhourofday = String.valueOf(hourOfDay);
                                        if(hourOfDay<10) strhourofday = "0"+String.valueOf(hourOfDay);
                                        String strminute = String.valueOf(minute);
                                        if(minute<10) strminute = "0"+String.valueOf(strminute);

                                        textView.setText(datemonthyear+" "+strhourofday + ":" + strminute);
                                    }
                                }, mHour, mMinute, false);
                        timePickerDialog.show();

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void firebasephoneverification(String phonenumber){

        TextView goto_next, goto_confirm;

        final Dialog settingdialog = new Dialog(BaseActivity.this);
        settingdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        settingdialog.setContentView(R.layout.phoneverify_dialog);
        settingdialog.getWindow().setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        settingdialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

        etx_phone=(EditText) settingdialog.findViewById(R.id.etx_phone);
        etx_code=(Pinview) settingdialog.findViewById(R.id.etx_code);
        goto_next =(TextView)settingdialog.findViewById(R.id.goto_next);
        goto_confirm=(TextView) settingdialog.findViewById(R.id.goto_confirm);

        etx_phone.setText(phonenumber);
        etx_code.setVisibility(View.GONE);
        goto_confirm.setVisibility(View.GONE);

        goto_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        etx_phone.getText().toString(),        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        BaseActivity.this,               // Activity (for callback binding)
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential credential) {
                                // This callback will be invoked in two situations:
                                // 1 - Instant verification. In some cases the phone number can be instantly
                                //     verified without needing to send or enter a verification code.
                                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                                //     detect the incoming verification SMS and perform verification without
                                //     user action.
                                Log.d("phone", "onVerificationCompleted:" + credential);

                               // signInWithPhoneAuthCredential(credential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    // Invalid request
                                    // ...
                                } else if (e instanceof FirebaseTooManyRequestsException) {
                                    // The SMS quota for the project has been exceeded
                                    // ...
                                }
                            }



                            @Override
                            public void onCodeSent(@NonNull String verificationId,
                                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                                // The SMS verification code has been sent to the provided phone number, we
                                // now need to ask the user to enter the code and then construct a credential
                                // by combining the code with a verification ID.
                                Log.d("phone", "onCodeSent:" + verificationId);

                                // Save verification ID and resending token so we can use them later
                                mVerificationId = verificationId;
                                etx_code.setVisibility(View.VISIBLE);
                                etx_phone.setEnabled(false);
                                goto_confirm.setVisibility(View.VISIBLE);
                                //mResendToken = token;


                            }
                        });        // OnVerificationStateChangedCallbacks
                settingdialog.dismiss();
            }
        });

        goto_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmedphonenumber1(etx_phone.getText().toString());
            }
        });

        settingdialog.show();
    }

    public void confirmedphonenumber1(String toString) {
        if (etx_code.getValue().length() < 6 ) {
            Toast.makeText(this, "Please enter correct code", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mVerificationId.length() == 0){
            Toast.makeText(this, "Please use correct phone", Toast.LENGTH_SHORT).show();
            return;
        }

        credential = PhoneAuthProvider.getCredential(mVerificationId, etx_code.getValue());
        progressBar.show();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            confirmedphonenumber(etx_phone.getText().toString());

                        } else {
                            progressBar.dismiss();
                            //verification unsuccessful.. display an error message
                            Toast.makeText(BaseActivity.this, "Wrong verify code", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void confirmedphonenumber(String toString) {
    }


}
