package com.example.myapplication;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class MainActivity extends AppCompatActivity {
    BluetoothAdapter blead = BluetoothAdapter.getDefaultAdapter();

    private Retrofit retrofit;
    private TextView text1, text2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_SCAN}, 1000);
        }


        if (!blead.isEnabled())
            blead.enable();
        //Log.e("ble", String.valueOf(blead.isEnabled()));
        //Log.e("ble", String.valueOf(blead));
//        Button bt = findViewById(R.id.bt);
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        blead.startLeScan(scancallback_le);

        text1 = findViewById(R.id.text);
        text2 = findViewById(R.id.text1);

        //sendData("ac156788bdcc");

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

                text1.setText("암호획득");
                text2.setText(data);

                while(sendData(data) == "fail"){

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                }

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

