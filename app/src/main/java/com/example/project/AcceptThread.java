package com.example.project;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.util.UUID;

public class AcceptThread extends Thread {
    /*
    * 该线程用于服务器端，用于实时监听来自客户端的消息
    * */

    private static final String NAME = "BlueToothClass";
    private static final UUID MY_UUID = UUID.fromString(Constant.CONNECTTION_UUID);

    private final BluetoothServerSocket mmServerSocket;
    private final BluetoothAdapter mBluetoothAdapter;

    private final Handler mHandler;
    //通过Handler处理UI，因为在不同的线程之间

    private ConnectedThread mConnectedThread;
    //当有新的客户端向服务器端发送消息的时候，我们调用该线程，与客户端通话
    //必须新启一个线程进行通信，否则会阻塞该AcceptThread的正常运行

    //handler负责UI
    public AcceptThread(BluetoothAdapter adapter, Handler handler) {
        mBluetoothAdapter = adapter;
        mHandler = handler;
        BluetoothServerSocket tmp = null;
        try {
            // MY_UUID是应用程序的UUID，客户端代码使用相同的UUID
            //下面的语句创建了一个新的服务器端
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
        } catch (IOException e) { }
        mmServerSocket = tmp;
    }

    @Override
    public void run() {
        BluetoothSocket socket;
        //while true: 持续监听
        while (true) {
            try {
                mHandler.sendEmptyMessage(Constant.MSG_START_LISTENING);
                socket = mmServerSocket.accept();
                //调用accept，线程被阻塞了，直到获取到最新的Socket，获取之后进入第58行
            } catch (IOException e) {
                mHandler.sendMessage(mHandler.obtainMessage(Constant.MSG_ERROR, e));
                break;//线程的退出入口：accept失败的时候退出
            }
            // 如果一个连接被接受
            if (socket != null) {
                // 在单独的线程中完成管理连接的工作
                manageConnectedSocket(socket);
                try {
                    mmServerSocket.close();
                    mHandler.sendEmptyMessage(Constant.MSG_FINISH_LISTENING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;//线程的退出入口：获取socket对象出错的时候退出
            }
        }
    }

    private void manageConnectedSocket(BluetoothSocket socket) {
        //只支持同时处理一个连接，否则的话会需要一个连接池，使得代码复杂化
        if( mConnectedThread != null) {
            mConnectedThread.cancel();//如果现有的ConnectedThread不为空，会把之前的客户端踢掉
        }
        mHandler.sendEmptyMessage(Constant.MSG_GOT_A_CLINET);
        //向UI界面汇报：自己获取了一个客户端
        mConnectedThread = new ConnectedThread(socket, mHandler);
        //新启ConnectedThread
        mConnectedThread.start();
    }

    /**
     * 取消监听socket，使此线程关闭
     */
    public void cancel() {
        try {
            mmServerSocket.close();
            mHandler.sendEmptyMessage(Constant.MSG_FINISH_LISTENING);
        } catch (IOException e) { }
    }

    public void sendData(byte[] data) {
        if( mConnectedThread!=null){
            mConnectedThread.write(data);
        }
    }
}
