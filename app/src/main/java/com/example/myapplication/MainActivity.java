package com.example.myapplication;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.SensingLayoutBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class MainActivity extends AppCompatActivity {
    BluetoothAdapter blead = BluetoothAdapter.getDefaultAdapter();
    ConnectivityManager connectman;
    WifiManager wifiman;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private Retrofit retrofit;
    private TabLayout tab;
    private String wifidata;
    private int count;
    private ActivityMainBinding ma_binding;
    private SensingLayoutBinding se_binding;
    private ActivityMainBinding binding;
    private String key;
    private String id;
    ViewPager2Adapter viewPager2Adapter
            = new ViewPager2Adapter(getSupportFragmentManager(), getLifecycle());
    ViewPager2 viewPager2;
    private long mNow;
    private Date mDate;
    private SimpleDateFormat mFormat = new SimpleDateFormat("HH");


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ma_binding = ActivityMainBinding.inflate(getLayoutInflater());
        se_binding = SensingLayoutBinding.inflate(getLayoutInflater());

        setContentView(ma_binding.getRoot());

        id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        registerReceiver(rssiReceiver, new IntentFilter((WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)));
        registerReceiver(rssiReceiver, new IntentFilter((WifiManager.RSSI_CHANGED_ACTION)));

        tab = ma_binding.tab;

        viewPager2 = ma_binding.pager;

        viewPager2.setAdapter(viewPager2Adapter);

        viewPager2.setPageTransformer(new ZoomOutTransformer());

        new TabLayoutMediator(tab, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int i) {
                switch(i){
                    case 0:
                        tab.setText("Advertising");
                    case 1:
                        tab.setText("Connection");
                    case 2:
                        tab.setText("CheckingPage");
                    case 3:
                        tab.setText("Location");
                }
            }
        }).attach();

        tab.getTabAt(0).setText("Advertising");
        tab.getTabAt(1).setText("Connection");
        tab.getTabAt(2).setText("Result");
        tab.getTabAt(3).setText("Location");


       connectman = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        if(connectman.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
            Log.i("wifi", "wifi connected!");
        }else{
            Log.i("wifi", "wifi not connected!");
        }

        wifiman = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        //blead.startLeScan(scancallback_le);

        Gson gson = new GsonBuilder().setLenient().create();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://203.255.81.72:10021/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


    }

    @Override
    public void onDestroy(){
        unregisterReceiver(rssiReceiver);
        super.onDestroy();
    }

    private class ZoomOutTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(@NonNull View page, float position) {
            int pageWidth = page.getWidth();
            int pageHeight = page.getHeight();
            if (position < -1) {            // [-Infinity, -1) 왼쪽 화면 밖
                page.setAlpha(0f);
            } else if (position <= 1) {     // [-1, 1]
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;

                if (position < 0) {
                    page.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    page.setTranslationX(-horzMargin + vertMargin / 2);
                }
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            } else {                        // (1, +Infinity] 오른쪽 화면 밖
                page.setAlpha(0f);
            }
        }
    }


    public void start() throws InterruptedException{
        Log.i("wifi", "Start");

        count = 0;

        wifidata="";

        if(!wifiman.startScan()){
            Log.e("wifiScan1", "wifi scan fail!");
        }


    }

    public BroadcastReceiver rssiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
            if(success){
                scanSuccess();
            }else{
                Log.e("wifiScan2","wifi scan fail!");
            }
        }

        private void scanSuccess(){
            Log.i("wifi", String.valueOf(count));

            List<ScanResult> scanresult = wifiman.getScanResults();
            for (int i =0; i< scanresult.size(); i++){
                int RSSI = scanresult.get(i).level;
                String BSSID = scanresult.get(i).BSSID;

                wifidata += (BSSID + "!" + String.valueOf(RSSI) + "/");
                Log.i("wifidata", wifidata);
            }
            count++;

            comm_data service = retrofit.create(comm_data.class);

            Call<String>  call = null;

            call = service.location(wifidata);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.i("wifiResponse", response.body().toString());
                    key = response.body().toString();

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });

        }
    };

    public Retrofit get_retrofit(){
        return retrofit;
    }
    public String getLocation(){
        return key;
    }
    public void setLocation(String a){key = a;}
    public  String getId(){
        return id;
    }

}


