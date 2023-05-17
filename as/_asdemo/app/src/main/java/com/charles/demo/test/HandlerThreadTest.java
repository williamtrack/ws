package com.charles.demo.test;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

/*
 *   Created by Charles on 09/11/2020.
 */
public class HandlerThreadTest {
    static HandlerThread thread1;
    static Handler handler_thread1;
    static Handler handler_main;

    //一个线程只有一个looper，可以有无数各handler
    public static void test(Context context) {
        thread1 = new HandlerThread("handlerThread_thread1");
        thread1.start();

        handler_thread1 = new Handler(thread1.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                System.out.println("William-thread1");
                if (handler_main != null) {
                    for (int i = 0, j = 0; i < 1000000000; i++,j=j+2) { j = i/(j+1); }
                    handler_main.sendEmptyMessageDelayed(0, 1000);
                }
            }
        };

        handler_main = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                //随便发送一个空的消息，延迟3秒发送
                System.out.println("William-main");
                if (handler_thread1 != null) {
                    for (int i = 0, j = 0; i < 1000000000; i++,j=j+2) { j = i/(j+1); }
                    handler_thread1.sendEmptyMessageDelayed(0, 1000);
                }
            }
        };

        handler_thread1.sendEmptyMessageDelayed(0, 1000);

        handler_main.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("William", "HandlerThreadTest-run: ");

            }
        }, 500);

//        handler_thread1.removeCallbacks(myRunnable); //清空消息
    }
}
