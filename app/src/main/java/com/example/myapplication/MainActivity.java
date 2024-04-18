package com.example.myapplication;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import android.media.AudioAttributes;
import android.media.SoundPool;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class MainActivity extends AppCompatActivity {
    BluetoothAdapter blead = BluetoothAdapter.getDefaultAdapter();

    ArrayList<BLEdata_storage> datalist = new ArrayList<BLEdata_storage>();

    private Retrofit retrofit;
    private TextView tv;
    private Switch sw_bt;
    private EditText ed_sens;
    private EditText ed_mac;
    private EditText ed_time;
    private EditText ed_otp;
    private EditText ed_rcv;
    private EditText ed_pm1_0;
    private EditText ed_pm25;
    private EditText ed_pm10;
    private SoundPool soundPool;
    private int soundID;
    private int[] numofdata;

    public postdata postdata = new postdata();

    List<String> mac1jo = new ArrayList<>(Arrays.asList("D8:3A:DD:42:AC:7F", "D8:3A:DD:42:AC:64",
            "B8:27:EB:DA:F2:5B", "B8:27:EB:0C:F3:83"));
    List<String> mac2jo = new ArrayList<>(Arrays.asList("D8:3A:DD:79:8F:97", "D8:3A:DD:79:8F:B9",
            "D8:3A:DD:79:8F:54", "D8:3A:DD:79:8F:80"));
    List<String> mac3jo = new ArrayList<>(Arrays.asList("D8:3A:DD:79:8E:D9", "D8:3A:DD:42:AC:9A",
            "D8:3A:DD:42:A8:FB", "D8:3A:DD:79:8E:9B"));
    List<String> mac4jo = new ArrayList<>(Arrays.asList("D8:3A:DD:78:A7:1A", "D8:3A:DD:79:8E:BF",
            "D8:3A:DD:79:8E:92", "D8:3A:DD:79:8F:59"));
    List<String> mac5jo = new ArrayList<>(Arrays.asList("B8:27:EB:47:8D:50", "B8:27:EB:D3:40:06",
            "B8:27:EB:E4:D0:FC", "B8:27:EB:57:71:7D"));



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.tv);
        Button bt_show = findViewById(R.id.bt_show);

        Button bt_store = findViewById(R.id.bt_store);
        Button bt_delet = findViewById(R.id.bt_delet);

        sw_bt = findViewById(R.id.sw_bt);
        ed_sens = findViewById(R.id.ed_sens);
        ed_mac = findViewById(R.id.ed_mac);
        ed_time = findViewById(R.id.ed_time);
        ed_otp = findViewById(R.id.ed_otp);
        ed_rcv = findViewById(R.id.ed_rcv);
        ed_pm1_0 = findViewById(R.id.ed_pm1_0);
        ed_pm25 = findViewById(R.id.ed_pm2_5);
        ed_pm10 = findViewById(R.id.ed_pm10);


        BluetoothAdapter blead = BluetoothAdapter.getDefaultAdapter();

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1000);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1000);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 1000);
        }

        if (!blead.isEnabled())
            blead.enable();
        blead.startLeScan(scancallback_le);


        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        soundID = soundPool.load(this, R.raw.success, 1);

        bt_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/store_test.csv");
                    if (!file.exists()) {
                        file.createNewFile();
                    }


                    FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
                    BufferedWriter bw = new BufferedWriter(fw);

                    for (int j = 0; j < datalist.size(); j++) {

                        bw.write(String.valueOf(datalist.get(j).get_sens()));
                        bw.write("," + String.valueOf(datalist.get(j).get_mac()));
                        bw.write("," + String.valueOf(datalist.get(j).get_time()));
                        bw.write("," + String.valueOf(datalist.get(j).get_otp()));
                        bw.write("," + String.valueOf(datalist.get(j).get_rcv()));
                        bw.write("," + String.valueOf(datalist.get(j).get_p01()));
                        bw.write("," + String.valueOf(datalist.get(j).get_p25()));
                        bw.write("," + String.valueOf(datalist.get(j).get_p10()));

                        bw.newLine();
                    }


                    bw.close();
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                datalist.clear();


            }
        });
        bt_delet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/store_test.csv");
                    if (!file.exists()) {
                        file.createNewFile();
                    }


                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);

                    bw.write("");

                    bw.close();
                    fw.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }



            }
                                    });

        bt_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    tv.setText("");
                    String line;
                    BufferedReader br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/store_test.csv"));
                    while ((line = br.readLine()) != null) {
                        tv.setText(tv.getText() + line + "\n");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        sw_bt.setOnCheckedChangeListener(new OnCheckedChangeListener(){

            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    sw_bt.setChecked(true);
                    blead.startLeScan(scancallback_le);
                    sw_bt.setText("블루투스 스캔중");
                }
                else {
                    sw_bt.setChecked(false);
                    blead.stopLeScan(scancallback_le);
                    sw_bt.setText("블루투스 스캔 종료");
                }



            }

        });


    }


    private BluetoothAdapter.LeScanCallback scancallback_le = new BluetoothAdapter.LeScanCallback() {



        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.i("ble", "스캔됨");

            String MacAdd = device.getAddress();
            String data = bytearrayToHex(scanRecord);
            String datasplit[] = data.split(",");
            String sensor = "";


            //Log.i("MAC", MacAdd);
            Log.i("password", data);

            //text1.setText("암호획득");

            if (mac1jo.contains(MacAdd) || mac2jo.contains(MacAdd) || mac3jo.contains(MacAdd) ||
                    mac4jo.contains(MacAdd) || mac5jo.contains(MacAdd) || MacAdd.equals("B8:27:EB:7F:E7:58")) {
                Log.i("MAC", MacAdd);

                soundPool.play(soundID, 1, 1, 0, 0, 1);

                blead.stopLeScan(scancallback_le);
                sw_bt.setChecked(false);

                if (mac1jo.contains(MacAdd)) {
                    sensor = "1jo";
                } else if (mac2jo.contains(MacAdd)) {
                    sensor = "2jo";
                } else if (mac3jo.contains(MacAdd)) {
                    sensor = "3jo";
                } else if (mac4jo.contains(MacAdd)) {
                    sensor = "4jo";
                } else if (mac5jo.contains(MacAdd)) {
                    sensor = "5jo";
                } else if (MacAdd.equals("B8:27:EB:7F:E7:58")) {
                    sensor = "ta";
                }

                postdata.set_data(sensor, MacAdd, "1jo",
                       datasplit[1], datasplit[0], datasplit[2]);
                String pm[] = datasplit[2].split("/");
                ed_sens.setText(sensor);
                ed_mac.setText(MacAdd);
                ed_time.setText(datasplit[1]);
                ed_otp.setText(datasplit[0]);
                ed_rcv.setText("1jo");
                ed_pm1_0.setText(pm[0]);
                ed_pm25.setText(pm[1]);
                ed_pm10.setText(pm[2]);

                BLEdata_storage ble = new BLEdata_storage(sensor, MacAdd, Integer.parseInt(datasplit[1]), Integer.parseInt(datasplit[0]), "1jo", pm[0],pm[1],pm[2]);

                datalist.add(ble);

                sendData(postdata);



            }


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    private String bytearrayToHex(byte[] scanRecord) {
        StringBuilder sb = new StringBuilder(scanRecord.length * 2);
        for (byte b : scanRecord)
            sb.append(String.format("%02x", b));

        String pass = sb.toString();
        Log.i("Pass", pass);

        //바뀐 부분 127~182번줄
        //timeotp, sensingtime, sensordata는 NULL이 아니게 설정해야 join할 때 오류 안남
        String timeotp = "", sensingtime = "", sensorone, sensortwo, sensorthree, sensordata = "";

        int index = pass.indexOf("f0f0");
        if (index != -1) {
            pass = pass.substring(index + 4);
            //TimeOTP = pass.substring(0, 6);
            //bool을 1로 해서 0 없이 나오게 하기 (%d)
            timeotp = hexToDec(pass.substring(0, 6), 1);
            Log.i("TimeOTP", timeotp);
        }


        index = pass.indexOf("9999");
        if (index != -1) {
            pass = pass.substring(index + 4);
            //sensingtime = pass.substring(0, 10);
            //bool을 0으로 해서 앞에 0 붙여서 나오게 하기 (%02d)
            sensingtime = hexToDec(pass.substring(0, 10), 0);
            Log.i("SensingTime", sensingtime);
        }


        index = pass.indexOf("fd");
        if (index != -1) {
            pass = pass.substring(index + 2);
            //sensorone = pass.substring(0, 2);
            //sensortwo = pass.substring(2, 4);
            //sensorthree = pass.substring(4, 6);
            //bool을 1로 해서 0 없이 나오게 하기 (%d)
            sensorone = hexToDec(pass.substring(0, 2), 1);
            sensortwo = hexToDec(pass.substring(2, 4), 1);
            sensorthree = hexToDec(pass.substring(4, 6), 1);
            //join으로 /넣어서 합치기
            sensordata = String.join("/", sensorone, sensortwo, sensorthree);

            Log.i("SensorOne", sensorone);
            Log.i("SensorTwo", sensortwo);
            Log.i("SensorThree", sensorthree);
            Log.i("sensordata: ", sensordata);
        }


        //,로 join
        pass = String.join(",", timeotp, sensingtime, sensordata);

        return pass;
    }


    private String hexToDec(String scanRecord, int bool) {
        StringBuilder sb = new StringBuilder(scanRecord.length() * 2);

        for (int i = 0; i < scanRecord.length(); i += 2) {
            String hex = scanRecord.substring(i, i + 2);
            int decimal = Integer.parseInt(hex, 16);

            if (bool == 1) sb.append(String.format(Locale.US, "%d", decimal));
            else sb.append(String.format(Locale.US, "%02d", decimal));
        }

        String pass = sb.toString();

        return pass;
    }

    private Boolean sendData(postdata postjson) {

        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://203.255.81.72:10021/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        comm_data service = retrofit.create(comm_data.class);

        Call<String> call = null;
        call = service.post(postjson.sensor, postjson.mac, postjson.receiver, postjson.time, postjson.otp, postjson.data);

        final String[] callback = new String[1];
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("test", response.body().toString());
                callback[0] = response.body().toString();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }

        });


        return true;
    }


}
