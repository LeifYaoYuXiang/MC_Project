package com.example.project;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.service.autofill.RegexValidator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PKActivity extends AppCompatActivity {
    private BlueToothController blueToothController=new BlueToothController();;
    public static int REQUEST_CODE=0;
    private DeviceAdapter deviceAdapter;
    private TextView status;
    private ListView listView;
    private Button  button;
    private Button sayHi;
    private List<BluetoothDevice> deviceList=new ArrayList<>();
    private List<BluetoothDevice> bondedDeviceList=new ArrayList<>();
    private AcceptThread mAcceptThread;
    private ConnectThread mConnectThread;
    private Handler mUIHandler = new MyHandler();

    private boolean askWaiting=false;
    private boolean answerWaiting=false;
    private boolean askTakingOn=false;
    private boolean answerTakingOn=false;

    private BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                deviceList.clear();
                deviceAdapter.notifyDataSetChanged();
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){

            }
            else if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceList.add(device);
                deviceAdapter.notifyDataSetChanged();
                Toast.makeText(context, "Find One", Toast.LENGTH_SHORT).show();

            } else if(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0);
                if(scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                    Toast.makeText(context, "Discoverable", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Not Discoverable", Toast.LENGTH_SHORT).show();
                }

            } else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(remoteDevice == null) {
                    Toast.makeText(context, "No Equipment", Toast.LENGTH_SHORT).show();
                    return;
                }
                int status = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
                if(status == BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(context, "Bonded:"+remoteDevice.getName(), Toast.LENGTH_SHORT).show();
                } else if(status == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(context, "Bonding"+remoteDevice.getName(), Toast.LENGTH_SHORT).show();
                } else if(status == BluetoothDevice.BOND_NONE) {
                    Toast.makeText(context, "Not Bond with "+remoteDevice.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        int choose=bundle.getInt("choose");
        if(choose==0){
            askWaiting=true;
        }else{
            answerWaiting = true;
        }
        setContentView(R.layout.activity_pk);
        registerBluetoothReceiver();
        blueToothController.turnOnBlueTooth(PKActivity.this,REQUEST_CODE);
        initUI();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blueToothController.enableVisibily(PKActivity.this);
                bondedDeviceList = blueToothController.getBondedDeviceList();
                deviceAdapter.refresh(bondedDeviceList);
                listView.setOnItemClickListener(bondedDeviceClick);
                if( mAcceptThread != null) {
                    mAcceptThread.cancel();
                }
                mAcceptThread = new AcceptThread(blueToothController.getAdapter(), mUIHandler);
                mAcceptThread.start();
                button.setVisibility(View.GONE);
            }
        });

        sayHi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                say("Hello");
            }
        });


    }


    private void initUI(){
        listView=findViewById(R.id.device_list);
        button=findViewById(R.id.startPK);
        sayHi=findViewById(R.id.sayHi);
        deviceAdapter=new DeviceAdapter(deviceList,this);
        listView.setAdapter(deviceAdapter);
        listView.setOnItemClickListener(bondedDeviceClick);
        status=findViewById(R.id.showStatus);
    }

    private void registerBluetoothReceiver(){
        IntentFilter intentFilter=new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(broadcastReceiver,intentFilter);
    }

//    private AdapterView.OnItemClickListener bondDeviceClick = new AdapterView.OnItemClickListener() {
//        @TargetApi(Build.VERSION_CODES.KITKAT)
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//            BluetoothDevice device = deviceList.get(i);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                device.createBond();
//            }
//        }
//    };

    private AdapterView.OnItemClickListener bondedDeviceClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            BluetoothDevice device = bondedDeviceList.get(i);
            if (mConnectThread != null) {
                mConnectThread.cancel();
            }
            mConnectThread = new ConnectThread(device, blueToothController.getAdapter(), mUIHandler);
            mConnectThread.start();
            listView.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Toast.makeText(this, "OK! Open Blue Tooth", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No!", Toast.LENGTH_SHORT).show();
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case Constant.MSG_GOT_DATA:
                    Toast.makeText(PKActivity.this, "data:" + String.valueOf(message.obj), Toast.LENGTH_SHORT).show();
                    break;
                case Constant.MSG_ERROR:
                    Toast.makeText(PKActivity.this, "error:" + String.valueOf(message.obj), Toast.LENGTH_SHORT).show();
                    break;
                case Constant.MSG_CONNECTED_TO_SERVER:
                    Toast.makeText(PKActivity.this, "连接到服务端", Toast.LENGTH_SHORT).show();
                    askTakingOn=true;
                    break;
                case Constant.MSG_GOT_A_CLINET:
                    Toast.makeText(PKActivity.this, "找到服务端", Toast.LENGTH_SHORT).show();
                    answerTakingOn=true;
                    break;
            }
        }
    }

    private void say(String word) {
        if (mAcceptThread != null) {
            try {
                mAcceptThread.sendData(word.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        else if( mConnectThread != null) {
            try {
                mConnectThread.sendData(word.getBytes("utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }


    private void listen(String string){
        String[] feedback=string.split("%");
        if(feedback.length==2){
            String type=feedback[0];
            String result=feedback[1];
            if("QUESTION".equals(type)){
            //听到的是一个问题，需要自己作出回答
            }else if ("ANSWER".equals(type)){
            //听到的是一个回答，需要自己做出评分（判断对错）
            }else if("REPLY".equals(type)){
            //听到的是一个评分，需要展示出来
            }
        }else{
            Toast.makeText(this, "Some Thing Wrong!", Toast.LENGTH_SHORT).show();
        }
    }









}