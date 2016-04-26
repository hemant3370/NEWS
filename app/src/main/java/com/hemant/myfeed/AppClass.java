package com.hemant.news;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.einmalfel.earl.Item;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.hemant.news.Util.Utils;
import com.squareup.picasso.Picasso;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration;

import java.util.ArrayList;

/**
 * Created by anuraggupta on 31/01/16.
 */
public class AppClass extends Application{

    private static AppClass sInstance;
    DisplayMetrics metrics;



    public ArrayList<Item> rssitemforwebview ;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Firebase.setAndroidContext(this);
        ConnectionBuddyConfiguration networkInspectorConfiguration = new ConnectionBuddyConfiguration.Builder(this).build();
        ConnectionBuddy.getInstance().init(networkInspectorConfiguration);

    }
    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    public static AppClass getsInstance() {
        return sInstance;
    }
    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }
    public static void saveToPreferences(Context context, String preferenceName, String preferenceValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValue);
        editor.apply();
    }

    public static void saveToPreferences(Context context, String preferenceName, boolean preferenceValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(preferenceName, preferenceValue);
        editor.apply();
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    public static boolean readFromPreferences(Context context, String preferenceName, boolean defaultValue) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return sharedPreferences.getBoolean(preferenceName, defaultValue);
    }
    public DisplayMetrics getMetrics() {
        metrics = new DisplayMetrics();
        metrics = getResources().getDisplayMetrics();
        return metrics;
    }
    public  ArrayList<Item>  getRssitemforwebview() {
        return rssitemforwebview;
    }

    public void setRssitemforwebview( ArrayList<Item>  rssitemforwebview) {
        this.rssitemforwebview = rssitemforwebview;
    }

}
