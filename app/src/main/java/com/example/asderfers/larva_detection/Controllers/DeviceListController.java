package com.example.asderfers.larva_detection.Controllers;

import android.util.Log;

import com.example.asderfers.larva_detection.App.ParseResponse;
import com.example.asderfers.larva_detection.App.lardet;
import com.example.asderfers.larva_detection.DeviceList;
import com.example.asderfers.larva_detection.Model.Device;
import com.example.asderfers.larva_detection.Model.User;
import com.example.asderfers.larva_detection.Network.DeviceListFetcher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by akm on 09-Sep-17.
 */
public class DeviceListController {
    public static final String TAG = DeviceListController.class.getSimpleName();

    public static DeviceList getDeviceListSynchronously(final SyncResponseHandler handler) {
        User user = lardet.getUser();
        ArrayList<Device> deviceList = user.getDeviceList();
        if(deviceList == null) {
            new DeviceListFetcher(new DeviceListFetcher.DeviceListResponseHandler() {
                @Override
                public void onSuccess(JSONArray deviceList, JSONObject user) {
                    try {
                        ArrayList<Device> updatedList = ParseResponse.parseDeviceList(deviceList);

                        lardet.getUser().setDeviceList(updatedList);

                        handler.onUpdate(DeviceList.fromModel(updatedList));
                        handler.finishSyncWait();
                    } catch (Exception e) {
                        e.printStackTrace();
                        handler.finishSyncWait();
                    }
                }

                @Override
                public void onFailure() {
                    handler.finishSyncWait();
                    Log.d(TAG, "Failed to fetch device list");
                }

                @Override
                public void onError(Exception e) {
                    handler.finishSyncWait();
                    e.printStackTrace();
                }
            }).getDevicesList();
            handler.onSyncWait();
            return new DeviceList();
        } else {
            new DeviceListFetcher(new DeviceListFetcher.DeviceListResponseHandler() {
                @Override
                public void onSuccess(JSONArray deviceList, JSONObject user) {
                    try {
                        ArrayList<Device> updatedList = ParseResponse.parseDeviceList(deviceList);

                        lardet.getUser().setDeviceList(updatedList);

                        handler.onUpdate(DeviceList.fromModel(updatedList));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure() {
                    Log.d(TAG,"Failed to update device list");
                }

                @Override
                public void onError(Exception e) {
                    e.printStackTrace();
                }
            }).getDevicesList();
            return DeviceList.fromModel(deviceList);
        }
    };

    public static void getDeviceListAsync(final AsyncResponseHandler handler) {
        handler.duringWait();
        new DeviceListFetcher(new DeviceListFetcher.DeviceListResponseHandler() {
            @Override
            public void onSuccess( JSONArray deviceList, JSONObject user) {
                try {
                    Log.d(TAG,"got deviceList : " + deviceList.toString());
                    ArrayList<Device> updatedList = ParseResponse.parseDeviceList(deviceList);
                    Log.d(TAG, "array list size : " + updatedList.size());

                    lardet.getUser().setDeviceList(updatedList);

                    handler.onResponse(DeviceList.fromModel(updatedList));
                    handler.finishWait();
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.finishWait();
                }
            }

            @Override
            public void onFailure() {
                Log.d(TAG,"Failed to update device list async");
                handler.finishWait();
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
                handler.finishWait();
            }
        }).getDevicesList();
    }
}
