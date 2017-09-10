package com.example.asderfers.larva_detection;

import com.example.asderfers.larva_detection.Model.Device;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akm on 09-Sep-17.
 */
public class DeviceList {
    private List<Device> deviceList;

    public DeviceList(){
        deviceList = new ArrayList<>();
    }

    public void addDevice(Device device){
        deviceList.add(device);
    }
    public Device getDevice(int position){
        return deviceList.get(position);
    }
    public int deviceCount(){
        return deviceList.size();
    }

    public static DeviceList fromModel(ArrayList<Device> deviceArrayList) {
        DeviceList cl = new DeviceList();
        for (Device c: deviceArrayList) {
            cl.addDevice(c);
        }
        return cl;
    }
    public void update(DeviceList newDeviceList) {
        this.deviceList = newDeviceList.deviceList;
    }

}
