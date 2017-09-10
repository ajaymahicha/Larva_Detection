package com.example.asderfers.larva_detection.App;

import com.example.asderfers.larva_detection.Model.Device;
import com.example.asderfers.larva_detection.Model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by akm on 09-Sep-17.
 */
public class ParseResponse {
    private static final String TAG = ParseResponse.class.getSimpleName();

    public static User parseUserData(JSONObject userData) throws JSONException {
        String userId = userData.getString("userId");
        String name = userData.getString("name");
        String emailId = userData.getString("emailId");
        String phoneNo = userData.getString("phoneNo");
        return new User(userId,name,emailId,phoneNo);
    }
    public static Device parseDeviceData(JSONObject deviceData) throws JSONException{
        String devicename = deviceData.getString("name");
        String deviceid = deviceData.getString("deviceId");
        String devicestatus = deviceData.getString("status");
        String lastupdated = deviceData.getString("lastUpdated");
        return new Device(deviceid,devicename,devicestatus,lastupdated);
    }
    public static ArrayList<Device> parseDeviceList(JSONArray deviceList) throws JSONException{
        ArrayList<Device> devices = new ArrayList<>(deviceList.length());
        for(int i = 0; i < deviceList.length(); i++) {
            devices.add(parseDeviceData(deviceList.getJSONObject(i)));
        }
        return devices;
    }
}
