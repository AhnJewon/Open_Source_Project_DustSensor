package com.example.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SensingFragment extends Fragment {

    private Switch sw_bt;

    private TextView tv;
    private Button bt_show;
    private Button bt_store;
    private Button bt_delet;
    private EditText ed_sens;
    private EditText ed_mac;
    private EditText ed_time;
    private EditText ed_otp;
    private EditText ed_rcv;
    private EditText ed_pm0_1;
    private EditText ed_pm25;
    private EditText ed_pm10;

    MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sensing_layout, container, false);

        tv = rootView.findViewById(R.id.tv);

        sw_bt = rootView.findViewById(R.id.sw_bt);

        bt_show = rootView.findViewById(R.id.bt_show);
        bt_store = rootView.findViewById(R.id.bt_store);
        bt_delet = rootView.findViewById(R.id.bt_delet);

        ed_sens = rootView.findViewById(R.id.ed_sens);
        ed_mac = rootView.findViewById(R.id.ed_mac);
        ed_time = rootView.findViewById(R.id.ed_time);
        ed_otp = rootView.findViewById(R.id.ed_otp);
        ed_rcv = rootView.findViewById(R.id.ed_rcv);

        ed_pm0_1 = rootView.findViewById(R.id.ed_pm0_1);
        ed_pm25 = rootView.findViewById(R.id.ed_pm2_5);
        ed_pm10 = rootView.findViewById(R.id.ed_pm10);

        mainActivity.setSwitch(sw_bt);

        mainActivity.setBtShow(bt_show);
        mainActivity.setBtStore(bt_store);
        mainActivity.setBtDelet(bt_delet);
        mainActivity.setText(tv,ed_sens,ed_mac,ed_time,ed_otp,ed_rcv,ed_pm0_1,ed_pm25,ed_pm10);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public Switch getSw_bt(){
        return sw_bt;
    }

}
