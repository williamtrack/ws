package com.charles.demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.charles.demo.activity.BaseActivity;
import com.charles.demo.test.TouchActHelper;
import com.charles.util.blue.BluetoothUtil;

public class MainActivity extends BaseActivity {

    //UI
    ImageView imageView;
    EditText editText;

    //other
    long startTime = System.currentTimeMillis() - 2000;

    HandlerThread thread_1;
    Handler handler_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("William", "MainActivity-onCreate: ");

        imageView = findViewById(R.id.main_iv);
        editText = findViewById(R.id.main_edit);
        findViewById(R.id.btn_main_test1).setOnClickListener(onClickListener);
        findViewById(R.id.btn_main_test2).setOnClickListener(onClickListener);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {requestPermission();}

        thread_1 = new HandlerThread("Thread_1");
        thread_1.start();
        handler_1 = new Handler(thread_1.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        Log.d("William", "MainActivity-handleMessage: 0" );
                        break;
                    case 1:
                        Log.d("William", "MainActivity-handleMessage: 1" );
                        break;
                }

            }
        };
        handler_1.sendEmptyMessageDelayed(0, 10);
//        handler_1.post(new Runnable() {});

//        ObservableTest observableTest = new ObservableTest();
//        observableTest.test();

        bluetoothUtil = new BluetoothUtil(getApplicationContext());
        bluetoothUtil.start();
        bluetoothUtil.onResume();
    }

    BluetoothUtil bluetoothUtil;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_main_test1:
                    bluetoothUtil.scanBlue();
                    break;
                case R.id.btn_main_test2:
                    bluetoothUtil.openBlue();
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Log.d("William", "MainActivity-onKeyDown: " + event.getKeyCode());
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        Log.d("William", "MainActivity-onKeyUp: " + event.getKeyCode());
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        TouchActHelper.init(event);
        return super.onTouchEvent(event);
    }

    @Override
    public void onBackPressed() {
        long endTime = System.currentTimeMillis();
        if (endTime - startTime > 2000) {
            Toast.makeText(MainActivity.this, "Press again to exit.", Toast.LENGTH_SHORT).show();
            startTime = endTime;
        } else {
            System.exit(0);
        }
    }


    private void requestPermission() {
//        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        String[] permissions = new String[]{Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.BLUETOOTH};
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this, permissions, i);
        }
    }

}