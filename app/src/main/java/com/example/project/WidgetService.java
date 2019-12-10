package com.example.project;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author 潘
 * @Reference https://blog.csdn.net/alvinhuai/article/details/81565714
 */
public class WidgetService extends Service {
    private final String ACTION_UPDATE_ALL = "UPDATE_ALL";
    private static final int UPDATE_TIME = 5000;

    private Timer mTimer;
    private TimerTask mTimerTask;

    @Override
    public void onCreate() {
        super.onCreate();

        // 每经过指定时间，发送一次广播
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Intent updateIntent = new Intent(ACTION_UPDATE_ALL);
                sendBroadcast(updateIntent);
            }
        };
        mTimer.schedule(mTimerTask, 1000, UPDATE_TIME);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mTimerTask.cancel();
        mTimer.cancel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }
}
