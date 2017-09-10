package com.example.asderfers.larva_detection.Network;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.asderfers.larva_detection.App.ApiUrls;
import com.example.asderfers.larva_detection.App.lardet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by akm on 09-Sep-17.
 */
public class DeviceListFetcher {
private static final String TAG = DeviceListFetcher.class.getSimpleName();

private final String deviceListUrl;
private final RequestQueue requestQueue;
private final DeviceListResponseHandler responseHandler;
private final String cookie;
private final JSONObject jsonBody;
public interface DeviceListResponseHandler{
    void onSuccess( JSONArray deviceList);
    void onFailure();
    void onError(Exception e);
}

    public DeviceListFetcher(DeviceListResponseHandler deviceListResponseHandler){
        deviceListUrl =  ApiUrls.DEVICELIST;
        requestQueue = lardet.getRequestQueue();
        responseHandler = deviceListResponseHandler;
        this.jsonBody = new JSONObject();
        cookie = lardet.App.getCookie();
        try {
            this.jsonBody.put("userId", lardet.getUser().getuserId());
            this.jsonBody.put("token",cookie);
            this.jsonBody.put("Content-Type", "application/x-www-form-urlencoded");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDevicesList(){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG,"received response : " + response);
                    JSONArray deviceList = response.getJSONArray("devices");
                    responseHandler.onSuccess(deviceList);
                } catch (Exception e) {
                    e.printStackTrace();
                    responseHandler.onError(e);
                }
            }
        };
        Response.ErrorListener errorListener =  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "some error occured : " + error);
                responseHandler.onError(error);
            }
        };
        try {
            JsonObjectRequest deviceListRequest = new JsonObjectRequest(Request.Method.POST, deviceListUrl, jsonBody, responseListener, errorListener) ;
            deviceListRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }

                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }

                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            requestQueue.add(deviceListRequest);
        } catch (Exception e) {
            responseHandler.onError(e);
        }
    }
}
