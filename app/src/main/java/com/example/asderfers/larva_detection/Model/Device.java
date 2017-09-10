package com.example.asderfers.larva_detection.Model;

/**
 * Created by akm on 09-Sep-17.
 */
public class Device {
    private String deviceSecret;
    private String deviceId;
    private String name;
    private String status;

    public Device (String devicesecret, String deviceId,String name, String status)
    {
        this.deviceSecret=devicesecret;
        this.deviceId=deviceId;
        this.name=name;
        this.status=status;
    }
    public String getdeviceSecret(){return deviceSecret;}
    public String getdeviceId(){return deviceId;}
    public String getdevicename(){return name;}
    public String getdevicestatus(){return status;}
}
