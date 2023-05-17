package com.charles.demo.activity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.charles.demo.service.MyService;
import com.charles.demo.R;

import java.util.List;

public class Main3Activity extends BaseActivity {

    private Handler handler;
    private MyService myService;
    Intent intent;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            myService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        intent = new Intent(this, MyService.class);
        //startService(intent);
        //bindService(intent,serviceConnection,BIND_AUTO_CREATE);

        findViewById(R.id.btn_main3_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent=new Intent(getBaseContext(),Main3Activity.class);
                //startActivity(intent);
//                if(myService!=null){
//                    myService.print();
//                }
                //System.exit(0);
                //unbindService(serviceConnection);
                //stopService(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Will", "Main2Activity-onStop: " + isAppOnForeground());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("will", "Main2Activity-onDestroy: ");
    }

    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
