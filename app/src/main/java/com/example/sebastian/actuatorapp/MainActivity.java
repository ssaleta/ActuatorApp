package com.example.sebastian.actuatorapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static int REQUEST_ENABLE_BT = 0;
    private static String MACADRESSHC06 = "98:D3:31:90:32:EE";

    private @BindView(R.id.bargraf_percent) TextView bargrafInPercent;
    private @BindView(R.id.toggleBtn4)ToggleButton btn4;
    private @BindView(R.id.toggleBtnP)ToggleButton btnP;
    private @BindView(R.id.toggleBtnO)ToggleButton btn0;
    private @BindView(R.id.toggleBtnPlus)ToggleButton btnPlus;
    private @BindView(R.id.toggleBtn20)ToggleButton btn20;
    private @BindView(R.id.toggleBtnSO)ToggleButton btnSO;
    private @BindView(R.id.toggleBtnSZ)ToggleButton btnSZ;
    private @BindView(R.id.toggleBtnMZ)ToggleButton btnMZ;
    private @BindView(R.id.btn_unpair)Button btnUnpair;
    private @BindView(R.id.btn_blink)Button btnBlink;
    private @BindView(R.id.indicator_lamp)ImageView indicatorLamp;


    private BluetoothSPP bt;
    private BluetoothAdapter bluetoothAdapter;
    private Connection connection;
    private Context context;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        indicatorLamp = (ImageView)findViewById(R.id.indicator_lamp);
        indicatorLamp.setImageResource(R.drawable.button_round_red);
        initializeBluetoothSPP();
        connection();
        setProgressBar();
        btn4 = (ToggleButton) findViewById(R.id.toggleBtn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Log.d(TAG, "CONNECTED MEEN");
            }

            public void onDeviceDisconnected() {
                // Do something when connection was disconnected
                Log.d(TAG, " DISCONECTED MEEN");
            }

            public void onDeviceConnectionFailed() {
                // Do something when connection failed
            }
        });

    }
    public void setProgressBar(){
        RoundCornerProgressBar progress1 = (RoundCornerProgressBar) findViewById(R.id.progress_1);
        progress1.setProgressColor(Color.parseColor("#FFD740"));
        progress1.setProgressBackgroundColor(Color.parseColor("#E0E0E0"));
        progress1.setMax(100);
        progress1.setProgress(50);

    }


    private void initializeBluetoothSPP(){
        bt = new BluetoothSPP(this);
        BluetoothAdapter bluetoothAdapter = bt.getBluetoothAdapter();
        intentFilter();

    }
    private void connection(){
        connection = new Connection(this, bt);
        if(connection.availableBluetooth() == false){
           makeToast(Integer.toString(R.string.bluetoothAvailable));
        }
        if(connection.enableBluetooth() == false){
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private void makeToast(String text){
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public void intentFilter(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(String.valueOf(BluetoothAdapter.STATE_ON));
        filter.addAction(String.valueOf(BluetoothAdapter.STATE_CONNECTED));
        filter.addAction(String.valueOf(BluetoothAdapter.STATE_DISCONNECTED));
        registerReceiver(mReceiver, filter);
    }

    public void sendMessage(){
        bt.setupService();
        bt.startService(BluetoothState.DEVICE_OTHER);
        bt.connect(MACADRESSHC06);
        bt.send("Message", true);
        listOfPairedDevices();
    }

    public void listOfPairedDevices(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
// If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListViewe
                Log.d(TAG,"paired devices"+ device.getName());
                Log.d(TAG,"paired devices"+ device.getAddress());
            }
        }
    }


    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:

                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_ON:

                        makeToast(context.getString(R.string.bluetoothEnable));
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        break;
                    case BluetoothAdapter.STATE_CONNECTED:
                        indicatorLamp.setImageResource(R.drawable.button_round_green);
                        Log.d(TAG, "STATE CONECTED");

                        break;
                    case BluetoothAdapter.STATE_DISCONNECTED:
                        Log.d(TAG, "STATE DISCONECTED");
                        indicatorLamp.setImageResource(R.drawable.button_round_red);

                        break;
                }
            }
        }
    };


}
