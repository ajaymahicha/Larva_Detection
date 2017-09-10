package com.example.asderfers.larva_detection.Network;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.asderfers.larva_detection.App.ApiUrls;
import com.example.asderfers.larva_detection.App.lardet;

import org.json.JSONObject;

/**
 * Created by akm on 09-Sep-17.
 */
public class LoginHelper {
    private static final String TAG = LoginHelper.class.getSimpleName();
    private final String loginUrl;
    private final RequestQueue requestQueue;
    private final String requestTag;
    private final LoginResponseHandler loginResponseHandler;

    public interface LoginResponseHandler {
        void onSuccess(JSONObject user);
        void onFailure();
        void onError(Exception exception);
    }
    public void manageCookie(String cookie) {
        lardet.App.setCookie(cookie);
    }

    public LoginHelper(String username, String password, LoginResponseHandler loginResponseHandler) {
        this.loginUrl = ApiUrls.LOGIN + String.format("?userid=%s&password=%s",username,password);
        this.requestTag = String.format("Login{uid=%s}",username);
        this.requestQueue = lardet.getRequestQueue();
        this.loginResponseHandler = loginResponseHandler;
    }
    public void sendLoginRequest() {
        Log.d(TAG, "login url : " + loginUrl);
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (!response.getBoolean("error"))
                        loginResponseHandler.onSuccess(response.getJSONObject("user"));
                    else
                        loginResponseHandler.onFailure();
                } catch (Exception e) {
                    e.printStackTrace();
                    loginResponseHandler.onError(e);
                }
            }
        };
        Response.ErrorListener errorListener =  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loginResponseHandler.onError(error);
            }
        };
        try {
            JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST,loginUrl,null,responseListener,errorListener){
                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    Log.d(TAG,"raw network response : " + response.toString());
                    String cookie =  response.headers.get("token");
                    Log.d(TAG,"Login cookie : "+cookie);
                    manageCookie(cookie);
                    return super.parseNetworkResponse(response);
                }
            };
            loginRequest.setTag(requestTag);
            requestQueue.add(loginRequest);
        } catch (Exception e) {
            loginResponseHandler.onError(e);
        }
    }
}
