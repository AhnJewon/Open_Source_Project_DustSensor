package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.jar.Manifest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ConnectionFragment extends Fragment {

    String TAG = "MainActivity";
    UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    TextView textStatus;
    Button btnParied, btnSearch, btnSend;
    ListView listView;
    ListView listview2;
    private postdata postdata;
    BluetoothAdapter btAdapter;
    Set<BluetoothDevice> pairedDevices;
    ArrayAdapter<String> btArrayAdapter;
    ArrayList<String> deviceAddressArray;

    private final static int REQUEST_ENABLE_BT = 1;
    BluetoothSocket btSocket = null;
    ConnectedThread connectedThread;
    private List<Map<String,String>> dataDevice;
    SimpleAdapter adapterDevice;
    MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onDestroy(){
        mainActivity.unregisterReceiver(mBluetoothSearchReceiver);
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataDevice = new ArrayList<>();
        adapterDevice = new SimpleAdapter(mainActivity.getApplicationContext(), dataDevice, android.R.layout.simple_list_item_2, new String[]{"name", "address"},new int[]{android.R.id.text1, android.R.id.text2});
        btAdapter = mainActivity.blead;
        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        IntentFilter searchFilter = new IntentFilter();
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        searchFilter.addAction(BluetoothDevice.ACTION_FOUND);
        searchFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mainActivity.registerReceiver(mBluetoothSearchReceiver, searchFilter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_connection, container, false);

        textStatus = (TextView) rootView.findViewById(R.id.text_status);
        btnParied = (Button) rootView.findViewById(R.id.btn_paired);
        btnSend = (Button) rootView.findViewById(R.id.btn_send);
        btnSearch = (Button) rootView.findViewById(R.id.btn_search);
        listView = (ListView) rootView.findViewById(R.id.listview);

        btArrayAdapter = new ArrayAdapter<>(mainActivity, android.R.layout.simple_list_item_1);
        deviceAddressArray = new ArrayList<>();
        listView.setAdapter(adapterDevice);

        listView.setOnItemClickListener(new myOnItemClickListener());

        btnParied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataDevice.clear();
                if (deviceAddressArray != null && !deviceAddressArray.isEmpty()) {
                    deviceAddressArray.clear();
                }
                pairedDevices = btAdapter.getBondedDevices();
                if (pairedDevices.size() > 0) {
                    // There are paired devices. Get the name and address of each paired device.
                    for (BluetoothDevice device : pairedDevices) {

                        Map map = new HashMap();
                        map.put("name", device.getName());
                        map.put("address", device.getAddress());
                        dataDevice.add(map);
                        adapterDevice.notifyDataSetChanged();


                    }
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectedThread != null) {
                    connectedThread.write("a");
                }
                try {
                    mainActivity.start();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                textStatus.setText(mainActivity.getLocation());
            }

        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearch.setEnabled(false);
                btnParied.setEnabled(false);
                if(btAdapter.isDiscovering()){
                    btAdapter.cancelDiscovery();
                }
                btAdapter.startDiscovery();
            }
        });

        return rootView;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    // Create a BroadcastReceiver for ACTION_FOUND.

    public class myOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(mainActivity.getApplicationContext(), dataDevice.get(position).get("name"), Toast.LENGTH_SHORT).show();

            textStatus.setText("try...");

            final String name = dataDevice.get(position).get("name"); // get name
            final String address = dataDevice.get(position).get("address"); // get address
            boolean flag = true;

            BluetoothDevice device = btAdapter.getRemoteDevice(address);

            // create & connect socket
            try {
                btSocket = createBluetoothSocket(device);
            } catch (IOException e) {
                textStatus.setText("connection failed!");
                e.printStackTrace();
            }

            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
            }
            // start bluetooth communication
            connectedThread = new ConnectedThread(btSocket, mainActivity);
            textStatus.setText("connected to " + name);
            connectedThread.start();
        }


    }
    /* public class myOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(mainActivity.getApplicationContext(), btArrayAdapter.getItem(position), Toast.LENGTH_SHORT).show();

            textStatus.setText("try...");

            final String name = btArrayAdapter.getItem(position); // get name
            final String address = deviceAddressArray.get(position); // get address
            boolean flag = true;

            BluetoothDevice device = btAdapter.getRemoteDevice(address);

            // create & connect socket
            try {
                btSocket = createBluetoothSocket(device);
            } catch (IOException e) {
                textStatus.setText("connection failed!");
                e.printStackTrace();
            }

            try {
                btSocket.connect();
            } catch (IOException e) {
                try {
                    btSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG, "unable to close() socket during connection failure", e2);
                }
            }
            // start bluetooth communication
            connectedThread = new ConnectedThread(btSocket, mainActivity);
            textStatus.setText("connected to " + name);
            connectedThread.start();
        }


    }*/

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BT_MODULE_UUID);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Insecure RFComm Connection", e);
        }
        return device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
    }

    BroadcastReceiver mBluetoothSearchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            switch(action){
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    dataDevice.clear();
                    Toast.makeText(mainActivity.getApplicationContext(),"Start bluetooth searching", Toast.LENGTH_SHORT ).show();
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Map map = new HashMap();
                    if(device.getName() != null) {
                        map.put("name", device.getName());
                        map.put("address", device.getAddress());
                        dataDevice.add(map);
                        adapterDevice.notifyDataSetChanged();
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Toast.makeText(mainActivity.getApplicationContext(),"Stop bluetooth searching", Toast.LENGTH_SHORT ).show();
                    btnSearch.setEnabled(true);
                    btnParied.setEnabled(false);
            }
        }
    };


    }

