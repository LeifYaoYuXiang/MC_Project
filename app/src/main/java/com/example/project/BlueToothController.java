package com.example.project;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Leif(Yuxiang Yao)
 * @Reference: https://blog.csdn.net/xialong_927/article/details/80463120
 */
public class BlueToothController {

    private BluetoothAdapter mAdapter;
    /*BluetoothAdapter类让用户能执行基本的蓝牙任务。例如： 初始化设备的搜索，查询可匹配的设备集，
    使用一个已知的MAC地址来初始化一个BluetoothDevice类，创建一个 BluetoothServerSocket类以监听其它设备对本机的连接请求等*/

    public BlueToothController(){
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }
    /*为了得到这个代表本地蓝牙适配器的 BluetoothAdapter类，调用getDefaultAdapter()这一静态方法。
    这是所有蓝牙动作使用的第一步。当拥有本地适配器以后， 用户可以获得一系列的BluetoothDevice对象*/

    public BluetoothAdapter getAdapter() {
        return mAdapter;
    }

    /**
     * 打开蓝牙
     */
    public void turnOnBlueTooth(Activity activity,int requestCode) {
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开蓝牙可见性
     */
    public void enableVisibily(Context context){
        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        context.startActivity(intent);
    }

    /**
     * 查找设备
     */
    public void findDevice() {
        assert (mAdapter != null);
        mAdapter.startDiscovery();
    }

    /**
     * 获取已绑定设备
     */
    public List<BluetoothDevice> getBondedDeviceList(){
        return new ArrayList<>(mAdapter.getBondedDevices());
    }
}
