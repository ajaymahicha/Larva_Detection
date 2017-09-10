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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by akm on 09-Sep-17.
 */
public class DeviceListFetcher {
private static final String TAG = DeviceListFetcher.class.getSimpleName();

private final String deviceListUrl;
private final RequestQueue requestQueue;
private final DeviceListResponseHandler responseHandler;
private final String cookie;

public interface DeviceListResponseHandler{
    void onSuccess( JSONArray deviceList, JSONObject user);
    void onFailure();
    void onError(Exception e);
}

    public DeviceListFetcher(DeviceListResponseHandler deviceListResponseHandler){
        deviceListUrl =  ApiUrls.DEVICELIST;
        requestQueue = lardet.getRequestQueue();
        responseHandler = deviceListResponseHandler;
        cookie = lardet.App.getCookie();
    }

    public void getDevicesList(){
        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG,"received response : " + response);
                    JSONArray deviceList = response.getJSONArray("devices");
                    JSONObject user = response.getJSONObject("user");
                    responseHandler.onSuccess(deviceList,user);
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
            JsonObjectRequest deviceListRequest = new JsonObjectRequest(Request.Method.GET, deviceListUrl, null, responseListener, errorListener) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String,String> headers = new HashMap<>();
                    headers.put("Cookie",cookie);
                    return headers;
                }
            };
            requestQueue.add(deviceListRequest);
        } catch (Exception e) {
            responseHandler.onError(e);
        }
    }
}
