package com.charles.demo.test;

import android.util.Log;

public class ThreadTest {

    public static void job() {
        Thread thread1 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("William", "ThreadTest-run: 1" );

        });
        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("William", "ThreadTest-run: 2");

        });
        thread1.start();
        thread2.start();

        try {
            thread1.join(); //并行变为串行,等待thread1完成后进行
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("William", "Main thread is finished" );
    }
}
