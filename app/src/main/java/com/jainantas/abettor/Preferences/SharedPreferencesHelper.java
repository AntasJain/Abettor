package com.jainantas.abettor.Preferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    static Context context;
    static SharedPreferences.Editor editor;
    private static SharedPreferences mPref;
    public static void init(Context mContext){
         context=mContext;
         if(mPref==null){
             mPref=context.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE);
             editor=mPref.edit();
         }

    }
    public static void setBiometric(String authName, String authState){
        editor.putString(authName,authState).commit();
    }
    public static String getBiometric(String authName,String defaultState){
        return mPref.getString(authName, defaultState);
    }
    public static void setUserInfo(String info, String value){
        editor.putString(info, value).commit();
    }
    public static String getUserInfo(String info,String defaultValue){
        return mPref.getString(info, defaultValue);
    }
    public static void setWeather(String type,String value){
        editor.putString(type, value).commit();
    }
    public static String getWeather(String type, String def){
        return  mPref.getString(type, def);
    }
    public static void setBoolean(String type, boolean val){
        editor.putBoolean(type, val).commit();
    }
    public static boolean getBoolean(String type,boolean val){
        return mPref.getBoolean(type, val);
    }
    public static String getCurrency(String curr,String def){
        return mPref.getString(curr,def);
    }
    public static void setCurrency(String currency,String val)
    {
        editor.putString(currency,val).commit();
    }
    public static void setSteps(String key, int step)
    {
        editor.putInt(key,step).commit();
    }
    public static int getSteps(String key, int num)
    {
        return mPref.getInt(key, num);
    }
}
