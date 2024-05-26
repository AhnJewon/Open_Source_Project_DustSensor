package com.example.myapplication;


import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.AdapterView;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.myapplication.databinding.ActivityMainBinding;
import com.example.myapplication.databinding.SensingLayoutBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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
    private TabLayout tab;
    private int[] numofdata;
    private ActivityMainBinding ma_binding;
    private SensingLayoutBinding se_binding;
    private ActivityMainBinding binding;

    List<String> mac1jo = new ArrayList<>(Arrays.asList("D8:3A:DD:42:AC:7F", "D8:3A:DD:42:AC:64",
            "B8:27:EB:DA:F2:5B", "B8:27:EB:0C:F3:83"));
    List<String> mac2jo = new ArrayList<>(Arrays.asList("D8:3A:DD:79:8F:97", "D8:3A:DD:79:8F:B9",
            "D8:3A:DD:79:8F:54", "D8:3A:DD:79:8F:80"));
    List<String> mac3jo = new ArrayList<>(Arrays.asList("D8:3A:DD:79:8E:D9", "D8:3A:DD:42:AC:9A",
            "D8:3A:DD:42:AB:FB", "D8:3A:DD:79:8E:9B"));
    List<String> mac4jo = new ArrayList<>(Arrays.asList("D8:3A:DD:78:A7:1A", "D8:3A:DD:79:8E:BF",
            "D8:3A:DD:79:8E:92", "D8:3A:DD:79:8F:59"));
    List<String> mac5jo = new ArrayList<>(Arrays.asList("B8:27:EB:47:8D:50", "B8:27:EB:D3:40:06",
            "B8:27:EB:E4:D0:FC", "B8:27:EB:57:71:7D"));

    List<String> dust_sensorMac = new ArrayList<>(Arrays.asList("D8:3A:DD:42:AC:7F", "D8:3A:DD:42:AC:64",
            "B8:27:EB:DA:F2:5B", "B8:27:EB:0C:F3:83"));

    List<String> air_sensorMac = new ArrayList<>(Arrays.asList("D8:3A:DD:C1:89:2E", "D8:3A:DD:C1:88:DD",
            "D8:3A:DD:C1:89:1E", "D8:3A:DD:C1:88:99"));



    ViewPager2Adapter viewPager2Adapter
            = new ViewPager2Adapter(getSupportFragmentManager(), getLifecycle());

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        ma_binding = ActivityMainBinding.inflate(getLayoutInflater());
        se_binding = SensingLayoutBinding.inflate(getLayoutInflater());

        setContentView(ma_binding.getRoot());

        tab = ma_binding.tab;

        ViewPager2 viewPager2 = ma_binding.pager;
        viewPager2.setAdapter(viewPager2Adapter);

        viewPager2.setPageTransformer(new ZoomOutTransformer());

        new TabLayoutMediator(tab, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int i) {
                tab.setText("Tab " + (i + 1));
            }
        }).attach();

        //BluetoothAdapter blead = BluetoothAdapter.getDefaultAdapter();

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
}


