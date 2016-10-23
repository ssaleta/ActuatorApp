package com.example.sebastian.actuatorapp;

import android.content.Context;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by Sebastian on 2016-10-15.
 */
public class Connection {

    private BluetoothSPP bt;
    private Context context;

    public Connection(Context context, BluetoothSPP bt){
        this.context = context;
        this.bt = bt;
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


}
