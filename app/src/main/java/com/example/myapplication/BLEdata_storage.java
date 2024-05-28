package com.example.myapplication;

public class BLEdata_storage {
    private String sens;
    private String mode;
    private String mac;
    private String time;
    private String otp;
    private String rcv;
    private String data;

    private String key;


    BLEdata_storage(String sens, String mode, String mac, String time, String otp, String rcv, String data, String key){
        this.sens = sens;
        this.mac = mac;
        this.rcv = rcv;
        this.mode = mode;
        this.data = data;

        this.time = time;
        this.otp = otp;
        this.key = key;
    }
    public String get_sensor() {
        return sens;
    }
    public String get_mac() {
        return mac;
    }
    public String get_receiver() {
        return rcv;
    }
    public String get_time() {return time;}
    public String get_otp() {
        return otp;
    }
    public String get_key() {return key; }
    public String get_data() {return data; }
    public String get_mode() { return mode; }
}

