package com.example.myapplication;


import static android.app.PendingIntent.getActivity;

import android.bluetooth.BluetoothSocket;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private postdata postdata = new postdata();
    private MacAddress macAddress = new MacAddress();
    private Retrofit retrofit;

    MainActivity mainActivity;

    public ConnectedThread(BluetoothSocket socket, MainActivity mainActivity) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;

        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()
        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                // Read from the InputStream
                //Log.e("test", String.valueOf(mmInStream));
                bytes = mmInStream.available();

                if (bytes != 0) {
                    buffer = new byte[1024];
                    SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                    bytes = mmInStream.available(); // how many bytes are ready to be read?
                    bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                    String data = new String(buffer, StandardCharsets.UTF_8);
                    Log.e("data", data);

                    String pm , time, timeotp , mac , key;

                    int index = data.indexOf("!");
                    pm = data.substring(0, index);
                    data = data.substring(index +1);

                    index = data.indexOf("!");
                    time = data.substring(0, index);
                    data = data.substring(index+1);

                    index = data.indexOf("!");
                    timeotp = data.substring(0, index);
                    data = data.substring(index+1);
                    mac = data.substring(0,17);

                    mainActivity.start();
                    key = mainActivity.getLocation();
                    postdata.set_data(macAddress.witchJo(mac), "connection", mac, mainActivity.getId(), time, timeotp, "1-2", pm);


                    Log.i("PM", pm);
                    Log.i("Time", time);
                    Log.i("TimeOTP", timeotp);
                    Log.i("Mac", mac);

                    sendData(postdata);
                }

            } catch (IOException e) {
                e.printStackTrace();

                break;
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(String input) {
        byte[] bytes = input.getBytes();           //converts entered String into bytes
        try {
            mmOutStream.write(bytes);
            mmSocket.close();
        } catch (IOException e) {
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
        }
    }

    private Boolean sendData(postdata postjson) {

        {

            retrofit = mainActivity.get_retrofit();

            comm_data service = retrofit.create(comm_data.class);
            Call<String> call = null;
            final String[] callback = new String[1];

            if (postjson.get_data().contains("/")) {
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


            return true;
        }
    }

}
