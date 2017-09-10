package com.example.asderfers.larva_detection.Model;

/**
 * Created by akm on 09-Sep-17.
 */
public class Device {
    private String lastupdated;
    private String deviceId;
    private String name;
    private String status;

    public Device ( String deviceId,String name, String status,String lastupdated)
    {
        this.lastupdated=lastupdated;
        this.deviceId=deviceId;
        this.name=name;
        this.status=status;
    }
    public String getlastUpdated(){return lastupdated;}
    public String getdeviceId(){return deviceId;}
    public String getdevicename(){return name;}
    public String getdevicestatus(){return status;}
}
