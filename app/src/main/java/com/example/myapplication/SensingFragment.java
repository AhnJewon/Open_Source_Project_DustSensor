package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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

    BluetoothAdapter btAdapter;
    private Retrofit retrofit;
    private SoundPool soundPool;
    private int soundID;

    public postdata postdata = new postdata();
    ArrayList<BLEdata_storage> datalist = new ArrayList<BLEdata_storage>();
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

            if (mainActivity.mac1jo.contains(MacAdd) || mainActivity.mac2jo.contains(MacAdd) || mainActivity.mac3jo.contains(MacAdd) ||
                    mainActivity.mac4jo.contains(MacAdd) || mainActivity.mac5jo.contains(MacAdd) || MacAdd.equals("B8:27:EB:7F:E7:58")) {
                Log.i("MAC", MacAdd);

                soundPool.play(soundID, 1, 1, 0, 0, 1);

                //blead.stopLeScan(scancallback_le);
                //sw_bt.setChecked(false);

                if (mainActivity.mac1jo.contains(MacAdd)) {
                    sensor = "1jo";
                } else if (mainActivity.mac2jo.contains(MacAdd)) {
                    sensor = "2jo";
                } else if (mainActivity.mac3jo.contains(MacAdd)) {
                    sensor = "3jo";
                } else if (mainActivity.mac4jo.contains(MacAdd)) {
                    sensor = "4jo";
                } else if (mainActivity.mac5jo.contains(MacAdd)) {
                    sensor = "5jo";
                } else if (MacAdd.equals("B8:27:EB:7F:E7:58")) {
                    sensor = "ta";
                }

                postdata.set_data( sensor ,  "advertising",MacAdd, "1jo", postdata.get_time(),
                        datasplit[1], datasplit[0], datasplit[2]);
                String pm[] = datasplit[2].split("/");
                ed_sens.setText(sensor);
                ed_mac.setText(MacAdd);
                ed_time.setText(datasplit[1]);
                ed_otp.setText(datasplit[0]);
                ed_rcv.setText("1jo");
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

                BLEdata_storage ble = new BLEdata_storage(sensor, MacAdd, Integer.parseInt(datasplit[1]), Integer.parseInt(datasplit[0]), "1jo", pm[0], pm[1], pm[2]);

                datalist.add(ble);

//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }

                //sendData(postdata);


            }


        }
    };

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

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

            {
            Gson gson = new GsonBuilder().setLenient().create();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://203.255.81.72:10021//")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            comm_data service = retrofit.create(comm_data.class);
                Call<String> call = null;
                final String[] callback = new String[1];

                if (mainActivity.dust_sensorMac.contains(postjson.get_mac())) {
                        call = service.sensing(postjson.get_sensor(),
                                postjson.get_mode(),
                                postjson.get_mac(),
                                postjson.get_receiver(),
                                postjson.get_time(),
                                postjson.get_otp(),
                                postjson.get_key(),
                                postjson.get_data());

                    }

                if (mainActivity.air_sensorMac.contains(postjson.get_mac())) {
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
                        callback[0] = response.body().toString();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                    }




        });


        return true;
    }
}
