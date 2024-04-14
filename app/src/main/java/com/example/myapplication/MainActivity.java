package com.example.myapplication;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

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
    private int[] numofdata;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView tv = findViewById(R.id.tv);
        Button bt_ble = findViewById(R.id.bt_blestart);
        Button bt_write = findViewById(R.id.bt_write);
        Button bt_store = findViewById(R.id.bt_store);
        Button bt_show = findViewById(R.id.bt_show);

        EditText ed_rssi = findViewById(R.id.ed_rssi);
        EditText ed_pm1_0 = findViewById(R.id.ed_pm1_0);
        EditText ed_pm25 = findViewById(R.id.ed_pm2_5);
        EditText ed_pm10 = findViewById(R.id.ed_pm10);

        BluetoothAdapter blead = BluetoothAdapter.getDefaultAdapter();

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.MANAGE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, 1000);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1000);
        }

        if (!blead.isEnabled())
            blead.enable();



        bt_ble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blead.startLeScan(scancallback_le);
            }
        });

        bt_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rssi = Integer.valueOf(ed_rssi.getText().toString());
                int pm1_0 = Integer.valueOf(ed_pm1_0.getText().toString());
                int pm2_5 = Integer.valueOf(ed_pm25.getText().toString());
                int pm10 = Integer.valueOf(ed_pm10.getText().toString());

                BLEdata_storage data = new BLEdata_storage(rssi, pm1_0,pm2_5, pm10, System.currentTimeMillis());

                datalist.add(data);
            }
        });

        bt_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                try {
                    File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/store_test.csv");
                    if (!file.exists()) {
                        file.createNewFile();
                    }


                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);

                    for (int j = 0; j < datalist.size(); j++) {

                        bw.write(String.valueOf(datalist.get(j).get_rssi()));
                        bw.write("," + String.valueOf(datalist.get(j).get_p01()));
                        bw.write("," + String.valueOf(datalist.get(j).get_p25()));
                        bw.write("," + String.valueOf(datalist.get(j).get_p10()));
                        bw.write("," + String.valueOf(datalist.get(j).get_time()));

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

        bt_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    tv.setText("");
                    String line;
                    BufferedReader br = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/store_test.csv"));
                    while((line = br.readLine())!= null) {
                        tv.setText(tv.getText()+line+"\n");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
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

            //Log.i("MAC", MacAdd);
            Log.i("password", data);

            //text1.setText("암호획득");

            if(MacAdd.equals("B8:27:EB:7F:E7:58")){
                Log.i("MAC", MacAdd);
                blead.stopLeScan(scancallback_le);

//                while(sendData(data) == "fail"){
//
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        throw new RuntimeException(e);
//                    }
//
//
//                }

            }




        }
    };
    private String bytearrayToHex(byte[] scanRecord){
        StringBuilder sb = new StringBuilder(scanRecord.length * 2);
        for(byte b: scanRecord)
            sb.append(String.format("%02x", b));

        String pass = sb.toString();

        int index = pass.indexOf("998899");
        if(index != -1) {
            pass = pass.substring(index + 6);
            index = pass.indexOf("998899");
            pass = pass.substring(0, index);
            Log.i("password", pass);
        }

        return pass;
    }

    private String sendData(String data){

        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://203.255.81.72:10021/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        comm_data service = retrofit.create(comm_data.class);

        Call<String> call = null;
        call = service.post("1jo", data);

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

        return callback[0];
    }






}

