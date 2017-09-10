package com.example.asderfers.larva_detection.Network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.asderfers.larva_detection.App.ApiUrls;
import com.example.asderfers.larva_detection.App.lardet;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by akm on 09-Sep-17.
 */
public class LogoutHelper {
    private static final String TAG = LogoutHelper.class.getSimpleName();
    private final String logoutUrl;
    private final RequestQueue requestQueue;
    //    private final String requestTag;
    private final LogoutResponseHandler logoutResponseHandler;
    private final String cookie;
    public interface LogoutResponseHandler {
        void onSuccess(JSONObject logoutResponse);
        void onFailure();
        void onError(Exception exception);
    }
    public LogoutHelper(LogoutResponseHandler logoutResponseHandler) {
        this.logoutUrl = ApiUrls.LOGOUT;
//        this.requestTag = String.format("Login{uid=%s}",username);
        this.requestQueue = lardet.getRequestQueue();
        this.logoutResponseHandler = logoutResponseHandler;
        this.cookie = lardet.App.getCookie();
    }


    public void sendLogoutRequest() {
        Log.d(TAG, "logout url : " + logoutUrl);
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,"Logout Successful. response : " + response);
                logoutResponseHandler.onSuccess(response);
            }
        };
        Response.ErrorListener errorListener =  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                logoutResponseHandler.onError(error);
            }
        };
        try {
            JsonObjectRequest logoutRequest = new JsonObjectRequest(Request.Method.GET,logoutUrl,null,responseListener,errorListener){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers = new HashMap<>();
                    headers.put("Cookie",cookie);
                    return headers;
                }
            };
//            logoutRequest.setTag(requestTag);
            requestQueue.add(logoutRequest);
        } catch (Exception e) {
            logoutResponseHandler.onError(e);
        }
    }

}
