package com.example.myapplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MacAddress {
    private List<String> mac1jo = new ArrayList<>(Arrays.asList(
            "D8:3A:DD:42:AC:7F", "D8:3A:DD:42:AC:64", "B8:27:EB:DA:F2:5B", "B8:27:EB:0C:F3:83",
            "D8:3A:DD:C1:89:2E", "D8:3A:DD:C1:88:DD", "D8:3A:DD:C1:89:1E", "D8:3A:DD:C1:88:99"));
    private List<String> mac2jo = new ArrayList<>(Arrays.asList(
            "D8:3A:DD:79:8F:97", "D8:3A:DD:79:8F:B9", "D8:3A:DD:79:8F:54", "D8:3A:DD:79:8F:80"));
    private List<String> mac3jo = new ArrayList<>(Arrays.asList(
            "D8:3A:DD:79:8E:D9", "D8:3A:DD:42:AC:9A", "D8:3A:DD:42:AB:FB", "D8:3A:DD:79:8E:9B"));
    private List<String> mac4jo = new ArrayList<>(Arrays.asList(
            "D8:3A:DD:78:A7:1A", "D8:3A:DD:79:8E:BF", "D8:3A:DD:79:8E:92", "D8:3A:DD:79:8F:59",
            "D8:3A:DD:C1:89:64", "D8:3A:DD:C1:88:C8", "D8:3A:DD:C1:88:62", "D8:3A:DD:C1:88:AD"));
    private List<String> mac5jo = new ArrayList<>(Arrays.asList(
            "B8:27:EB:47:8D:50", "B8:27:EB:D3:40:06", "B8:27:EB:E4:D0:FC", "B8:27:EB:57:71:7D",
            "D8:3A:DD:C1:89:87", "D8:3A:DD:C1:88:9B", "D8:3A:DD:C1:89:07", "D8:3A:DD:C1:88:95"));
    private List<String> dust = new ArrayList<>(Arrays.asList(
            "D8:3A:DD:42:AC:7F", "D8:3A:DD:42:AC:64", "B8:27:EB:DA:F2:5B", "B8:27:EB:0C:F3:83",
            "D8:3A:DD:79:8F:97", "D8:3A:DD:79:8F:B9", "D8:3A:DD:79:8F:54", "D8:3A:DD:79:8F:80",
            "D8:3A:DD:79:8E:D9", "D8:3A:DD:42:AC:9A", "D8:3A:DD:42:AB:FB", "D8:3A:DD:79:8E:9B",
            "D8:3A:DD:78:A7:1A", "D8:3A:DD:79:8E:BF", "D8:3A:DD:79:8E:92", "D8:3A:DD:79:8F:59",
            "B8:27:EB:47:8D:50", "B8:27:EB:D3:40:06", "B8:27:EB:E4:D0:FC", "B8:27:EB:57:71:7D"
            ));
    public String witchJo(String mac){
        if(mac1jo.contains(mac)) {return "1jo";}
        else if (mac2jo.contains(mac)) {return "2jo";}
        else if (mac3jo.contains(mac)) {return "3jo";}
        else if (mac4jo.contains(mac)) {return "4jo";}
        else if (mac5jo.contains(mac)) {return "5jo";}
        else return null;
    }
    public Boolean isContain(String mac){
        return(mac1jo.contains(mac) || mac2jo.contains(mac) || mac3jo.contains(mac) ||
                mac4jo.contains(mac) ||mac5jo.contains(mac));
    }
    public Boolean isAir(String mac){
        return !dust.contains(mac);
    }



}
