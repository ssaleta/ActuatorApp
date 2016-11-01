package com.example.sebastian.actuatorapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Method;
import java.util.Set;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

/**
 * Created by Sebastian on 2016-10-15.
 */
public class Connection {

    private static String TAG = "Connection";
    private static String MACADRESSHC06 = "98:D3:31:90:32:EE";
    private BluetoothSPP bt;
    private BluetoothAdapter bluetoothAdapter;
    private Context context;
    private byte[] steeringFrame;

    public Connection(Context context, BluetoothSPP bt, BluetoothAdapter bluetoothAdapter){
        this.context = context;
        this.bt = bt;
        this.bluetoothAdapter = bluetoothAdapter;
    }
    public boolean availableBluetooth(){
        boolean checkAvailableBluetooth = true;
        if(!(bt.isBluetoothAvailable())){
            checkAvailableBluetooth = false;
        }
        return checkAvailableBluetooth;
    }
    public boolean enableBluetooth(){
        boolean checkEnableBluetooth = true;
        if(!(bt.isBluetoothEnabled())){
            checkEnableBluetooth = false;
        }
        return checkEnableBluetooth;
    }
    public void sendMessage(String message){
        bt.setupService();
        bt.startService(BluetoothState.DEVICE_OTHER);
        bt.connect(MACADRESSHC06);
        bt.send(message, true);
    }
    public void sendFrame(byte[] steeringFrame){
        this.steeringFrame = steeringFrame;
        bt.send(steeringFrame, true);
    }
    public void listOfPairedDevices(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
// If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices

            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListViewe
                Log.d(TAG, "paired devices" + device.getName());
                Log.d(TAG,"paired devices"+ device.getAddress());
            }
            if (pairedDevices.size() > 1) {
                erasePairedDevices();
            }

        }
    }
    public boolean erasePairedDevices(){
        boolean erasePairedDevices = true;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            for (BluetoothDevice device : pairedDevices){
                try{
                    Method m = device.getClass().getMethod("removeBond", (Class[]) null);
                    m.invoke(device, (Object[])null);
                }catch (Exception e){
                    erasePairedDevices = false;
                    Log.e("fail", e.getMessage());
                }
            }
        }
        return erasePairedDevices;
    }


}
