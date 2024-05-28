package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

public class SensingFragment extends Fragment {

    private Switch sw_bt;
    private TextView tv;
    private TextView rcv;
    private TextView pm01;
    private TextView pm25;
    private TextView pm10;
    private LinearLayout layout1;
    private LinearLayout layout2;
    private ToggleButton tbt_change;
    private Button bt_show;
    private Button bt_store;
    private Button bt_delet;
    private TextView ed_sens;
    private TextView ed_mac;
    private TextView ed_time;
    private TextView ed_otp;
    private TextView ed_rcv;
    private TextView ed_pm0_1;
    private TextView ed_pm25;
    private TextView ed_pm10;

    BluetoothAdapter btAdapter;
    private Retrofit retrofit;
    private SoundPool soundPool;
    private int soundID;

    private postdata postdata = new postdata();
    private MacAddress macAddress = new MacAddress();
    ArrayList<BLEdata_storage> datalist = new ArrayList<BLEdata_storage>();
    MainActivity mainActivity;
    LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams tparams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
    LinearLayout.LayoutParams pparams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainActivity = null;
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        btAdapter = mainActivity.blead;

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(1)
                .setAudioAttributes(audioAttributes)
                .build();

        soundID = soundPool.load(mainActivity, R.raw.success, 1);

        postdata.set_receiver(mainActivity.getId());
        postdata.set_mode("advertising");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.sensing_layout, container, false);

        tv = rootView.findViewById(R.id.tv);
        rcv = rootView.findViewById(R.id.rcv);
        pm01 = rootView.findViewById(R.id.pm01);
        pm25 = rootView.findViewById(R.id.pm25);
        pm10 = rootView.findViewById(R.id.pm10);

        sw_bt = rootView.findViewById(R.id.sw_bt);

        bt_show = rootView.findViewById(R.id.bt_show);
        bt_store = rootView.findViewById(R.id.bt_store);
        bt_delet = rootView.findViewById(R.id.bt_delet);

        tbt_change = rootView.findViewById(R.id.tbt_change);

        ed_sens = rootView.findViewById(R.id.ed_sens);
        ed_mac = rootView.findViewById(R.id.ed_mac);
        ed_time = rootView.findViewById(R.id.ed_time);
        ed_otp = rootView.findViewById(R.id.ed_otp);
        ed_rcv = rootView.findViewById(R.id.ed_rcv);

        ed_pm0_1 = rootView.findViewById(R.id.ed_pm0_1);
        ed_pm25 = rootView.findViewById(R.id.ed_pm2_5);
        ed_pm10 = rootView.findViewById(R.id.ed_pm10);

        layout1 = rootView.findViewById(R.id.layout1);
        layout2 = rootView.findViewById(R.id.layout2);


        sw_bt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true) {
                    sw_bt.setChecked(true);
                    btAdapter.startLeScan(scancallback_le);
                    sw_bt.setText("블루투스 스캔중");
                } else {
                    sw_bt.setChecked(false);
                    btAdapter.stopLeScan(scancallback_le);
                    sw_bt.setText("블루투스 스캔 종료");
                }
            }
        });

        tbt_change.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    lparams.weight = 0.5f;
                    layout2.setLayoutParams(lparams);
                    lparams.weight = 0.5f;
                    layout1.setLayoutParams(lparams);
                    tparams.weight = 0.25f;
                    rcv.setLayoutParams(tparams);
                    pm01.setLayoutParams(tparams);
                    pparams.weight = 0.25f;
                    ed_rcv.setLayoutParams(pparams);
                    ed_pm0_1.setLayoutParams(pparams);
                    pm01.setText("PM0.1");

                } else {
                    lparams.weight = 0f;
                    layout2.setLayoutParams(lparams);
                    lparams.weight = 1f;
                    layout1.setLayoutParams(lparams);
                    tparams.weight = 0.125f;
                    rcv.setLayoutParams(tparams);
                    pm01.setLayoutParams(tparams);
                    pparams.weight = 0.375f;
                    ed_rcv.setLayoutParams(pparams);
                    ed_pm0_1.setLayoutParams(pparams);
                    pm01.setText("AirQ");

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

                        bw.write(String.valueOf(datalist.get(j).get_sensor()));
                        bw.write("," + String.valueOf(datalist.get(j).get_mode()));
                        bw.write("," + String.valueOf(datalist.get(j).get_mac()));
                        bw.write("," + String.valueOf(datalist.get(j).get_receiver()));
                        bw.write("," + String.valueOf(datalist.get(j).get_time()));
                        bw.write("," + String.valueOf(datalist.get(j).get_otp()));
                        bw.write("," + String.valueOf(datalist.get(j).get_key()));
                        bw.write("," + String.valueOf(datalist.get(j).get_data()));



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

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void byteToHex(byte[] scanRecord){
        StringBuilder sb = new StringBuilder(scanRecord.length * 2);
        for (byte b : scanRecord)
            sb.append(String.format("%02x", b));

        String scan = sb.toString();
        Log.i("advertised data", scan);

        sb.delete(0, sb.length());

        int index = scan.indexOf("f0f0");
        for(int i=index+4; i<index+10; i+=2){
            int t = Integer.parseInt(scan.substring(i,i+2),16);
            if(t != 0 || i !=index+4){sb.append(t);}
        }
        postdata.set_otp(sb.toString());
        Log.i("otp", sb.toString());

        sb.delete(0, sb.length());

        index = scan.indexOf("9999");
        for(int i=index+4; i<index+14; i+=2){
            sb.append(String.format("%02d",Integer.parseInt(scan.substring(i,i+2),16)));
        }
        postdata.set_time(sb.toString());
        Log.i("time", sb.toString());

        sb.delete(0, sb.length());

        index = scan.indexOf("fd");
        if(!tbt_change.isChecked()){
            for(int i=index+2; i<index+8; i+=2){
                sb.append(Integer.parseInt(scan.substring(i,i+2),16));
                sb.append("/");
            }
            postdata.set_data(sb.toString());
        } else {
            for(int i=index+2; i<index+6; i+=2){
                sb.append(Integer.parseInt(scan.substring(i,i+2),16));
            }
            postdata.set_data(sb.toString());
        }
        Log.i("data", sb.toString());
        sb.delete(0, sb.length());
    }


    private BluetoothAdapter.LeScanCallback scancallback_le = new BluetoothAdapter.LeScanCallback() {


        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {


            String MacAdd = device.getAddress();
            postdata.set_mac(MacAdd);

            if (macAddress.isContain(MacAdd) && (macAddress.isAir(MacAdd)==tbt_change.isChecked())) {
                Log.i("ble", "스캔됨");
                byteToHex(scanRecord);

                postdata.set_sensor(macAddress.witchJo(MacAdd));

                soundPool.play(soundID, 1, 1, 0, 0, 1);

                //blead.stopLeScan(scancallback_le);
                //sw_bt.setChecked(false);

                try {
                    mainActivity.start();
                    postdata.set_key(mainActivity.getLocation());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                postdata.set_key("1-1");

                ed_sens.setText(postdata.get_sensor() + "/" + postdata.get_key());
                ed_mac.setText(MacAdd);
                ed_time.setText(postdata.get_time());
                ed_otp.setText(postdata.get_otp());
                ed_rcv.setText(postdata.get_receiver());

                if(!tbt_change.isChecked()) {
                    String[] pm = postdata.get_data().split("/");
                    ed_pm0_1.setText(pm[0]);
                    ed_pm25.setText(pm[1]);
                    ed_pm10.setText(pm[2]);

                    //여기부터 토스트메시지+ 색상바꾸기 코드
                    int intTwo = Integer.parseInt(pm[1]);
                    int intThree = Integer.parseInt(pm[2]);
                    String text1, text2;

                    //초미세먼지 색 바꾸기, 토스트메시지 띄우기
                    if (intTwo >= 0 && intTwo <= 15) {
                        ed_pm25.setTextColor(Color.parseColor("#549FF8"));
                        text1 = "초미세먼지 좋음";
                    } else if (intTwo <= 35) {
                        ed_pm25.setTextColor(Color.parseColor("#52C148"));
                        text1 = "초미세먼지 보통";
                    } else if (intTwo <= 75) {
                        ed_pm25.setTextColor(Color.parseColor("#EE9D62"));
                        text1 = "초미세먼지 나쁨";
                    } else {
                        ed_pm25.setTextColor(Color.parseColor("#EC655F"));
                        text1 = "초미세먼지 매우 나쁨";
                    }

                    //미세먼지 색 바꾸기, 토스트메시지 띄우기
                    if (intThree >= 0 && intThree <= 30) {
                        ed_pm10.setTextColor(Color.parseColor("#549FF8"));
                        text2 = "미세먼지 좋음";
                    } else if (intThree <= 80) {
                        ed_pm10.setTextColor(Color.parseColor("#52C148"));
                        text2 = "미세먼지 보통";
                    } else if (intThree <= 150) {
                        ed_pm10.setTextColor(Color.parseColor("#EE9D62"));
                        text2 = "미세먼지 나쁨";
                    } else {
                        ed_pm25.setTextColor(Color.parseColor("#EC655F"));
                        text2 = "미세먼지 매우 나쁨";
                    }

                    Toast.makeText(mainActivity.getApplicationContext(), text1 + "\n" + text2, Toast.LENGTH_LONG).show();
                } else {
                    ed_pm0_1.setText(postdata.get_data());
                    String text3;
                    if (Integer.parseInt(postdata.get_data()) >= 0 && Integer.parseInt(postdata.get_data()) <= 50) {
                        ed_pm0_1.setTextColor(Color.parseColor("#549FF8"));
                        text3 = "공기질 좋음 : 오늘은 야외활동 어떠신가요?";
                    }
                    else if (Integer.parseInt(postdata.get_data()) <= 100) {
                        ed_pm0_1.setTextColor(Color.parseColor("#52C148"));
                        text3 = "공기질 보통 : 오늘은 메타세콰이어길에서 피크닉 한번?! ";

                    } else if (Integer.parseInt(postdata.get_data()) <= 250) {
                        ed_pm0_1.setTextColor(Color.parseColor("#EC655F"));
                        text3 = "공기질 나쁨 : 오늘은 도서관에서 공부합시다ㅜ ";
                    } else
                    {
                        ed_pm0_1.setTextColor(Color.parseColor("#7E0023"));
                        text3 = " 공기질 매우 나쁨 : 이불 안이 최고야~ ";

                    }
                    Toast.makeText(mainActivity.getApplicationContext(), text3, Toast.LENGTH_LONG).show();

                }

                BLEdata_storage ble = new BLEdata_storage(postdata.get_sensor(), postdata.get_mode(), postdata.get_mac(), postdata.get_time(), postdata.get_otp(), postdata.get_receiver(), postdata.get_data(), postdata.get_key());

                datalist.add(ble);

                sendData(postdata);


            }

        }
    };



    private void sendData(postdata postjson) {

        {

            retrofit = mainActivity.get_retrofit();

            comm_data service = retrofit.create(comm_data.class);
            Call<String> call = null;
            final String[] callback = new String[1];

            if (!tbt_change.isChecked()) {
                call = service.sensing(postjson.get_sensor(),
                        postjson.get_mode(),
                        postjson.get_mac(),
                        postjson.get_receiver(),
                        postjson.get_time(),
                        postjson.get_otp(),
                        postjson.get_key(),
                        postjson.get_data());

            }
            else {
                call = service.air_sensing(postjson.get_sensor(),
                        postjson.get_mode(),
                        postjson.get_mac(),
                        postjson.get_receiver(),
                        postjson.get_time(),
                        postjson.get_otp(),
                        postjson.get_key(),
                        postjson.get_data());

            }

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.e("test", response.body().toString());

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                }


            });
        }
    }

    private void setAir(){
        pm01.setText("AirQ");


//        params.weight = 2f;
//        layout1.setLayoutParams(params);
//
//        params.weight = 1.5f;
//        ed_rcv.setLayoutParams(params);
//        ed_pm0_1.setLayoutParams(params);
//
//        params.weight = 0.5f;
//        rcv.setLayoutParams(params);
//        pm01.setLayoutParams(params);
    }


    /* private String bytearrayToHex(byte[] scanRecord) {
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
    }*/

    /* private String hexToDec(String scanRecord, int bool) {
        StringBuilder sb = new StringBuilder(scanRecord.length() * 2);

        for (int i = 0; i < scanRecord.length(); i += 2) {
            String hex = scanRecord.substring(i, i + 2);
            int decimal = Integer.parseInt(hex, 16);

            if (bool == 1) sb.append(String.format(Locale.US, "%d", decimal));
            else sb.append(String.format(Locale.US, "%02d", decimal));
        }

        String pass = sb.toString();

        return pass;
    }*/

}


