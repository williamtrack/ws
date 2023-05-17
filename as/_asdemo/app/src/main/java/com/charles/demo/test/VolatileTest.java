package com.charles.demo.test;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VolatileTest {
    volatile int num = 0;

    public void doJob() {
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 1000; i++) {
            executor.execute(() -> {
                num++;
//                synchronized (this){
//                    num++;
//                }
            });
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                executor.shutdown();
                Log.d("William", "VolatileTest-run: " + num);
            }
        }).start();
    }

    public static Boolean stop = false;

    public void doJob2(){
        for(int i=0;i<8;i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int j = 1;
                    while (!stop) {
                        j++;
                    }
                    Log.d("William", "VolatileTest-run: " + j);
                }
            }).start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stop = true;
        Log.d("William", "VolatileTest-doJob2: ===" );
    }
}
