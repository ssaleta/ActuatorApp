package com.example.sebastian.actuatorapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;

import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static int REQUEST_ENABLE_BT = 0;

    @BindView(R.id.bargraf_percent) TextView bargrafInPercent;
    @BindView(R.id.toggleBtn4)ToggleButton btn4;
    @BindView(R.id.toggleBtnP)ToggleButton btnP;
    @BindView(R.id.toggleBtnO)ToggleButton btn0;
    @BindView(R.id.toggleBtnPlus)ToggleButton btnPlus;
    @BindView(R.id.toggleBtn20)ToggleButton btn20;
    @BindView(R.id.toggleBtnSO)ToggleButton btnSO;
    @BindView(R.id.toggleBtnSZ)ToggleButton btnSZ;
    @BindView(R.id.toggleBtnMZ)ToggleButton btnMZ;
    @BindView(R.id.btn_unpair)Button btnUnpair;
    @BindView(R.id.btn_blink)Button btnBlink;
    @BindView(R.id.indicator_lamp)ImageView indicatorLamp;

    private BluetoothSPP bt;
    private BluetoothAdapter bluetoothAdapter;
    private Connection connection;
    private Context context;
    private byte[] frameMessage;
    private byte byteBtn4 = 0;
    private byte byteBtnP = 0;
    private byte byteBtn0 = 0;
    private byte byteBtnPlus = 0;
    private byte byteBtn20 = 0;
    private byte byteBtnSZ = 0;
    private byte byteBtnSO = 0;
    private byte byteBtnMZ = 0;

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
        createFrameMessage();
        periodicalSendFrame();
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

    @OnClick(R.id.btn_unpair)
    void unpair(){
        connection.disconnect();
        connection.erasePairedDevices();
        if(connection.erasePairedDevices() == true) {
            makeToast("Disconnected & unpair");
        }else {
            makeToast("Disconnected & unpair FAILED!");
        }
        }
    @OnClick(R.id.toggleBtn4)
    void clickBtn4(){
        sendMessage();
        Log.d(TAG, "wartosc btn4" + btn4.isChecked());
        if(btn4.isChecked()== true){
            byteBtn4 = 1;
        }else{
            byteBtn4 = 0;
        }
        createFrameMessage();
    }
    @OnClick(R.id.toggleBtnP)
    void clickBtnP(){
        Log.d(TAG,"wartosc btnP" +btnP.isChecked());
        if(btnP.isChecked()== true){
            byteBtnP = 1;
        }else{
            byteBtnP = 0;
        }
        createFrameMessage();
    }
    @OnClick(R.id.toggleBtnO)
    void clickBtnO(){
        Log.d(TAG, "wartosc btnO" + btn0.isChecked());
        if(btn0.isChecked()== true){
            byteBtn0 = 1;
        }else{
            byteBtn0 = 0;
        }
        createFrameMessage();
    }
    @OnClick(R.id.toggleBtnPlus)
    void clickBtnPlus(){
        Log.d(TAG,"wartosc btnPlus" +btnPlus.isChecked());
        if(btnPlus.isChecked()== true){
            byteBtnPlus = 1;
        }else{
            byteBtnPlus = 0;
        }
        createFrameMessage();
    }
    @OnClick(R.id.toggleBtn20)
    void clickBtn20(){
        Log.d(TAG,"wartosc btn20" +btn20.isChecked());
        if(btn20.isChecked()== true){
            byteBtn20 = 1;
        }else{
            byteBtn20 = 0;
        }
        createFrameMessage();
    }
    @OnClick(R.id.toggleBtnSO)
    void clickBtnSO(){
        Log.d(TAG,"wartosc btnSO" +btnSO.isChecked());
        if(btnSO.isChecked()== true){
            byteBtnSO = 1;
        }else{
            byteBtnSO = 0;
        }
        createFrameMessage();
    }
    @OnClick(R.id.toggleBtnSZ)
    void clickBtnSZ(){
        Log.d(TAG,"wartosc btnSZ" +btnSZ.isChecked());
        if(btnSZ.isChecked()== true){
            byteBtnSZ = 1;
        }else{
            byteBtnSZ = 0;
        }
        createFrameMessage();
    }
    @OnClick(R.id.toggleBtnMZ)
    void clickBtnMZ(){
        Log.d(TAG,"wartosc btnMZ" +btnMZ.isChecked());
        if(btnMZ.isChecked()== true){
            byteBtnMZ = 1;
        }else{
            byteBtnMZ = 0;
        }
        createFrameMessage();
    }
    @OnClick(R.id.btn_blink)
    void clickBtnBlink(){
    makeToast("Blink");
    }

    private void createFrameMessage() {
        frameMessage = new byte[8];
        frameMessage = new byte[]{byteBtn4, byteBtnP, byteBtn0, byteBtnPlus, byteBtn20, byteBtnSO, byteBtnSZ, byteBtnMZ};
        for(int i = 0;i < frameMessage.length; i++) {
            Log.d(TAG, Arrays.toString(new byte[]{frameMessage[i]}));
        }
    }

    Runnable runnableFrameMessage = new Runnable(){
        @Override
        public void run() {
        Log.d(TAG,Arrays.toString(frameMessage));
        }
    };

    public void periodicalSendFrame() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(runnableFrameMessage,0,3,TimeUnit.DAYS.SECONDS);
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
        bluetoothAdapter = bt.getBluetoothAdapter();
        intentFilter();
    }

    private void connection(){
        connection = new Connection(this, bt, bluetoothAdapter);
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
        connection.sendMessage("Message");
        listOfPairedDevices();
    }

    public void listOfPairedDevices(){
       connection.listOfPairedDevices();
    }
    public void erasePairedDevices(){
        connection.erasePairedDevices();
        if(connection.erasePairedDevices() == false){
            makeToast("Erasing paired devices FAILED ");
        }
        if(connection.erasePairedDevices() == true){
            makeToast("Zero paired devices");
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
