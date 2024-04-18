package com.example.myapplication;

public class BLEdata_storage {
    private String sens;
    private String mac;
    private int time;
    private int otp;
    private String rcv;
    private String p01;
    private String p25;
    private String p10;


    BLEdata_storage(String sens, String mac, int time, int otp, String rcv, String p01, String p25, String p10){
        this.sens = sens;
        this.mac = mac;
        this.rcv = rcv;
        this.p01 = p01;
        this.p25 = p25;
        this.p10 = p10;
        this.time = time;
        this.otp = otp;
    }
    public String get_rcv(){
        return rcv;
    }
    public String get_p01(){
        return p01;
    }
    public String get_p25(){
        return p25;
    }
    public String get_p10(){
        return p10;
    }
    public int get_time(){
        return time;
    }
    public String get_mac(){
        return mac;
    }
    public String get_sens(){
        return sens;
    }
    public int get_otp(){
        return otp;
    }
}

