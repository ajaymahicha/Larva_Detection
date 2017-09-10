package com.example.asderfers.larva_detection.Network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.asderfers.larva_detection.App.ApiUrls;
import com.example.asderfers.larva_detection.App.lardet;
import com.example.asderfers.larva_detection.StartActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by akm on 10-Sep-17.
 */
public class AddNewDevice {
    private static final String TAG = StartActivity.class.getSimpleName();
    private final String newDeviceUrl;
    private RequestQueue requestQueue;
    private final AddNewDeviceResponseHandler responseHandler;
    private final String cookie;
    private final JSONObject jsonBody;
    public interface AddNewDeviceResponseHandler{
        void onSuccess(JSONObject response);
        void onFailure();
        void onError(Exception e);
    }

    public AddNewDevice(AddNewDeviceResponseHandler addNewDeviceResponseHandler,String name,String id){
        newDeviceUrl = ApiUrls.ADDDEVICE;
        Log.d(TAG, "addNewDevice response : " + newDeviceUrl);
        this.jsonBody =new JSONObject();
        try {
            this.jsonBody.put("userId", lardet.getUser().getuserId());
            this.jsonBody.put("name",name);
            this.jsonBody.put("deviceId",id);
            this.jsonBody.put("token",lardet.App.getCookie());
            this.jsonBody.put("Content-Type", "application/x-www-form-urlencoded");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = lardet.getRequestQueue();
        responseHandler = addNewDeviceResponseHandler;
        cookie = lardet.App.getCookie();
    }

    public void getAddNewDeviceResponse(){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "addNewDevice response : " + response);
                    responseHandler.onSuccess(response);
                } catch (Exception e) {
                    Log.d(TAG,"Error : " + e);
                    e.printStackTrace();
                    responseHandler.onError(e);
                }
            }
        };
        Response.ErrorListener errorListener =  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"some error occured : " + error);
                responseHandler.onError(error);
            }
        };
        try {
            JsonObjectRequest addNewDeviceRequest = new JsonObjectRequest(Request.Method.POST, newDeviceUrl, jsonBody, responseListener, errorListener) ;
            requestQueue.add(addNewDeviceRequest);
        } catch (Exception e) {
            responseHandler.onError(e);
        }
    }
}
