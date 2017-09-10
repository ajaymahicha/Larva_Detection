package com.example.asderfers.larva_detection.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by akm on 09-Sep-17.
 */
public class User implements Serializable {
    private String userId;
    private String name;
    private String emailId;
    private String phoneNo;
    private ArrayList<Device> devices;
    public User(String userId, String name, String emailId, String phoneNo) {
        this.userId = userId;
        this.name = name;
        this.emailId = emailId;
        this.phoneNo = phoneNo;
    }
    public String getuserId(){return userId;}
    public String getname(){return name;}
    public String getemailId(){return emailId;}
    public String getphoneNo(){return phoneNo;}
    public ArrayList<Device> getDeviceList()
    {
        return devices;
    }
    public void setDeviceList(ArrayList<Device> de)
    {
        devices=de;
    }
    synchronized public void onUpdate() {
        //TODO
    }

}
