package com.example.myapplication;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class postdata {

    //@Expose
    //@SerializedName("user") private String user;
   // @SerializedName("data") private String data;

    @SerializedName("sensor")
    public String sensor;
    @SerializedName("mac")
    public String mac;
    @SerializedName("receiver")
    public String receiver;

    @SerializedName("time")
    public String time;
    @SerializedName("otp")
    public String otp;
    @SerializedName("data")
    public String data;


    public void set_data(String sensor, String mac, String receiver, String time, String otp, String data) {
        this.sensor = sensor;
        this.mac = mac;
        this.receiver = receiver;
        this.time = time;
        this.otp = otp;
        this.data = data;
    }

    public void data_show(){Log.e("test", sensor + data);}

    public String get_sensor() {
    return sensor;
    }
    public String get_mac() {
        return mac;
    }
    public String get_receiver() {
        return receiver;
    }
    public String get_time() {
        return time;
    }
    public String get_otp() {
        return otp;
    }
    public String get_data() {
        return data;
    }
}


