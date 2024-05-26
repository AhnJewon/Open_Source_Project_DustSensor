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

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;


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

    ViewPager2Adapter viewPager2Adapter
            = new ViewPager2Adapter(getSupportFragmentManager(), getLifecycle());

}