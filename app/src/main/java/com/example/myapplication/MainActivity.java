package com.example.myapplication;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

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

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1000);
        }



        if(!blead.isEnabled())
            blead.enable();

        //blead.startLeScan(scancallback_le);
        //blead.stopLeScan(scancallback_le);



    }


    protected void onStart(){
        super.onStart();

        blead.startLeScan(scancallback_le);

    }

    protected void onResume(){
        super.onResume();




    }

    protected void onStop(){
        super.onStop();
        blead.stopLeScan(scancallback_le);
    }

    private BluetoothAdapter.LeScanCallback scancallback_le = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

            String MacAdd = device.getAddress();
            String data = bytearrayToHex(scanRecord);

            Gson gson = new GsonBuilder().setLenient().create();
            retrofit = new Retrofit.Builder()
                    .baseUrl("http:203.255.81.72:10021/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            comm_data service = retrofit.create(comm_data.class);

            Call<String> call = null;
            call = service.get("AHN", "test");

            call.enqueue(new Callback<String>(){
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.e("test", response.body().toString());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }

            });

        }
    };
    private String bytearrayToHex(byte[] scanRecord){
        StringBuilder sb = new StringBuilder(scanRecord.length * 2);
        for(byte b: scanRecord)
            sb.append(String.format("%02x", b));

        Log.e("password", sb.toString());
        return sb.toString();
    }








}

