package com.example.myapplication;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class postdata {

    //@Expose
    //@SerializedName("user") private String user;
   // @SerializedName("data") private String data;

    @SerializedName("sensor")
    private String sensor;

    @SerializedName("mode")
    private String mode;
    @SerializedName("mac")
    private String mac;
    @SerializedName("receiver")
    private String receiver;

    @SerializedName("time")
    private String time;
    @SerializedName("otp")
    private String otp;

    @SerializedName("key")
    private String key;

    @SerializedName("data")
    private String data;


    public void set_data(String sensor, String mode, String mac, String receiver, String time, String otp,String key, String data) {
        this.sensor = sensor;
        this.mode = mode;

        this.mac = mac;
        this.receiver = receiver;
        this.time = time;
        this.otp = otp;
        this.key = key;
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
    public String get_time() {return time;}
    public String get_otp() {
        return otp;
    }

    public String get_key() {return key; }

    public String get_data() {return data; }

    public String get_mode() { return mode; }
}


