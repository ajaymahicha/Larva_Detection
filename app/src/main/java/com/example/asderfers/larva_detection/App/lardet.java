package com.example.asderfers.larva_detection.App;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.asderfers.larva_detection.Model.User;

/**
 * Created by akm on 09-Sep-17.
 */
public class lardet extends Application
{
    public static lardet App;
    private static RequestQueue requestQueue;
    private static User user;

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }
    public static void setUser(User newUser) {
        user = newUser;
    }
    public static User getUser() {
        return user;
    }
    public void setCookie(String cookie) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString("cookie", cookie).commit();
    }
    public String getCookie()
    {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("cookie","NULL");
    }
    public void saveLoginCredentials(String username, String password)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("isLoginCredentialsSaved",true);
        editor.putString("savedUsername",username);
        editor.putString("savedPassword",password);
        editor.apply();
    }
    public void clearLoginCredentials()
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putBoolean("isLoginCredentialsSaved",false);
        editor.remove("savedUsername");
        editor.remove("savedPassword");
        editor.apply();
    }
    public boolean isLoginCredentialsSaved()
    {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("isLoginCredentialsSaved",false);
    }
    public String getLoginUsername()
    {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("savedUsername", "");
    }
    public String gettLoginPassword()
    {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("savedPassword","");
    }
    @Override
    public void onCreate() {
        super.onCreate();
        App = this;
        requestQueue = Volley.newRequestQueue(this);
        Log.d("cf","created");
    }


}
