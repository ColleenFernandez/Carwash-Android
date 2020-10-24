package com.wagen.cl.Constant;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wagen.cl.Model.CarModel;
import com.wagen.cl.Model.MembershipModel;
import com.wagen.cl.Model.Packages;
import com.wagen.cl.Model.Promotionmodel;
import com.wagen.cl.Model.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by zhan on 4/19/2018.
 */

public class Preference {
    private static final String FILE_NAME = "Coffeefarm.pref";

    private static Preference mInstance = null;

    public static Preference getInstance() {
        if (null == mInstance) {
            mInstance = new Preference();
        }
        return mInstance;
    }

    public void put(Context context, String key, String value){


        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public void put(Context context, String key, boolean value){

        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putBoolean(key, value);
        editor.commit();
    }

    public void put(Context context, String key, int value){

        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putInt(key, value);
        editor.commit();
    }

    public void put(Context context, String key, long value){

        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);

        SharedPreferences.Editor editor = pref.edit();

        editor.putLong(key, value);
        editor.commit();
    }

    public String getValue(Context context, String key, String defaultValue){

        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);

        try{
            return pref.getString(key, defaultValue);
        }catch (Exception e){

            return defaultValue;
        }
    }

    public int getValue(Context context, String key, int defaultValue){

        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);

        try{
            return pref.getInt(key, defaultValue);
        }catch (Exception e){
            return defaultValue;
        }
    }

    public boolean getValue(Context context, String key, boolean defaultValue){

        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);

        try{
            return pref.getBoolean(key, defaultValue);
        }catch (Exception e){
            return defaultValue;
        }
    }

    public long getValue(Context context, String key, long defaultValue){

        SharedPreferences pref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);

        try{
            return pref.getLong(key, defaultValue);
        }catch (Exception e){
            return defaultValue;
        }
    }

    public void putSharedcarmodelPreference(Context context, String key, ArrayList<CarModel> value){
        SharedPreferences mPref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        Gson gSon = new Gson();
        String json = gSon.toJson(value);
        editor.putString(key,json);
        editor.commit();
    }


    public ArrayList<CarModel> getSharedcarmodelPreference(Context context, String key ){
        ArrayList<CarModel> mSelectedList = new ArrayList<CarModel>();
        SharedPreferences mPref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPref.getString(key, "");
        if (json.isEmpty()){
            mSelectedList = new ArrayList<CarModel>();
        }else {
            Type type =  new TypeToken<ArrayList<CarModel>>(){}.getType();
            mSelectedList = gson.fromJson(json, type);
        }
        return mSelectedList;
    }

    public void putSharedmembershipPreference(Context context, String key, ArrayList<MembershipModel> value){
        SharedPreferences mPref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        Gson gSon = new Gson();
        String json = gSon.toJson(value);
        editor.putString(key,json);
        editor.commit();
    }


    public ArrayList<MembershipModel> getSharedmembershipPreference(Context context, String key ){
        ArrayList<MembershipModel> mSelectedList = new ArrayList<MembershipModel>();
        SharedPreferences mPref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPref.getString(key, "");
        if (json.isEmpty()){
            mSelectedList = new ArrayList<MembershipModel>();
        }else {
            Type type =  new TypeToken<ArrayList<MembershipModel>>(){}.getType();
            mSelectedList = gson.fromJson(json, type);
        }
        return mSelectedList;
    }


    public void putSharedpackagesPreference(Context context, String key, ArrayList<Packages> value){
        SharedPreferences mPref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        Gson gSon = new Gson();
        String json = gSon.toJson(value);
        editor.putString(key,json);
        editor.commit();
    }


    public ArrayList<Packages> getSharedpackagesmodelPreference(Context context, String key ){
        ArrayList<Packages> mSelectedList = new ArrayList<Packages>();
        SharedPreferences mPref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPref.getString(key, "");
        if (json.isEmpty()){
            mSelectedList = new ArrayList<Packages>();
        }else {
            Type type =  new TypeToken<ArrayList<Packages>>(){}.getType();
            mSelectedList = gson.fromJson(json, type);
        }
        return mSelectedList;
    }

    public void putSharedservicePreference(Context context, String key, ArrayList<Service> value){
        SharedPreferences mPref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        Gson gSon = new Gson();
        String json = gSon.toJson(value);
        editor.putString(key,json);
        editor.commit();
    }


    public ArrayList<Service> getSharedservicePreference(Context context, String key ){
        ArrayList<Service> mSelectedList = new ArrayList<Service>();
        SharedPreferences mPref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPref.getString(key, "");
        if (json.isEmpty()){
            mSelectedList = new ArrayList<Service>();
        }else {
            Type type =  new TypeToken<ArrayList<Service>>(){}.getType();
            mSelectedList = gson.fromJson(json, type);
        }
        return mSelectedList;
    }

    public void putSharedpromotionPreference(Context context, String key, ArrayList<Promotionmodel> value){
        SharedPreferences mPref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        Gson gSon = new Gson();
        String json = gSon.toJson(value);
        editor.putString(key,json);
        editor.commit();
    }


    public ArrayList<Promotionmodel> getSharedpromoPreference(Context context, String key ){
        ArrayList<Promotionmodel> mSelectedList = new ArrayList<Promotionmodel>();
        SharedPreferences mPref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPref.getString(key, "");
        if (json.isEmpty()){
            mSelectedList = new ArrayList<Promotionmodel>();
        }else {
            Type type =  new TypeToken<ArrayList<Promotionmodel>>(){}.getType();
            mSelectedList = gson.fromJson(json, type);
        }
        return mSelectedList;
    }





    public void putShared_cities_Preference(Context context, String key, ArrayList<String> value){
        SharedPreferences mPref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = mPref.edit();
        Gson gSon = new Gson();
        String json = gSon.toJson(value);
        editor.putString(key,json);
        editor.commit();
    }


    public ArrayList<String> getShared_cities_Preference(Context context, String key ){
        ArrayList<String> mSelectedList = new ArrayList<String>();
        SharedPreferences mPref = context.getSharedPreferences(FILE_NAME, Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPref.getString(key, "");
        if (json.isEmpty()){
            mSelectedList = new ArrayList<String>();
        }else {
            Type type =  new TypeToken<ArrayList<String>>(){}.getType();
            mSelectedList = gson.fromJson(json, type);
        }
        return mSelectedList;
    }


}
