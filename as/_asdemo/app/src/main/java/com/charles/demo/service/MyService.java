package com.charles.demo.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.charles.demo.R;
import com.charles.demo.activity.Main3Activity;

public class MyService extends Service {
    public MyService() {
    }

    public final IBinder iBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return iBinder;
    }

    private boolean isWorked;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Will", "MyService-onCreate: --");

        //创建通知
        String CHANNEL_ONE_ID = "com.charles.ag";
        String CHANNEL_ONE_NAME = "Channel One";
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID, CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_MIN);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(this, Main3Activity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification = new Notification.Builder(this)
                .setChannelId(CHANNEL_ONE_ID)
                .setContentTitle("This is content title")
                .setContentText("This is content text")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentIntent(pi)
                .build();
        notification.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(1, notification);

//        newThread();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isWorked = false;
        Log.d("Will", "MyService-onDestroy: ---");
    }

    public void print() { Log.d("Will", "MyService-print: ---"); }

    private void newThread() {
        isWorked = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isWorked) {
                    Log.d("Will", "MyService-run: --");

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}
