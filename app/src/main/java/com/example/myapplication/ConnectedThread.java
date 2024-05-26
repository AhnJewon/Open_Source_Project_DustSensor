package com.example.myapplication;


import android.bluetooth.BluetoothSocket;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public ConnectedThread(BluetoothSocket socket) {
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

                    String pm = "", time = "", timeotp = "", mac = "";

                    int index = data.indexOf("!");
                    pm = data.substring(0, index);
                    data = data.substring(index +1);
                    time = data.substring(0, 10);
                    data = data.substring(11);

                    index = data.indexOf("!");
                    timeotp = data.substring(0, index);
                    data = data.substring(index+1);
                    mac = data.substring(0,17);

                    Log.i("PM", pm);
                    Log.i("Time", time);
                    Log.i("TimeOTP", timeotp);
                    Log.i("Mac", mac);
                }

            } catch (IOException e) {
                e.printStackTrace();

                break;
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

    //private String 함수명(byte[] sb) {
//        String pass = sb.toString();
//        Log.i("Pass", pass);
//
//        String time = "", timeotp = "", mac = "";
//
//        int index = pass.indexOf("!");
//        if (index != -1) {
//            pm = pass.substring(0, index-1);
//            pass = pass.substring(index +1);
//            time = pass.substring(0, 10);
//            pass = pass.substring(11);
//            timeotp = pass.substring(0,4);
//            pass = pass.substring(5);
//            mac = pass.substring(0)

//            Log.i("Time", time);
//            Log.i("TimeOTP", timeotp);
//            Log.i("Mac", mac);
//        }
//pm1.0/pm2.5/pm10.0!time!timeotp!mac 주소
//
//Ex. 8/9/10!1798108442!1234!B8:AC:24:55:66:EE
}
