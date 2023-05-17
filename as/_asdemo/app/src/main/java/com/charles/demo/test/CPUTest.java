package com.charles.demo.test;

import android.util.Log;

import com.charles.demo.util.CountUtil;

public class CPUTest {
    static byte[] data = new byte[1000000];

    static {
        for (int k = 0; k < 1000000; k++) {
            data[k] = (byte) (k * 3);
        }
    }


    public static void plus() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sum = 0;
                CountUtil.intervalMicroTime(false);
//                for (int i = 0; i < 100000000; i++) {
//                    sum = 1 + 1;
//                }
//                for (int i = 0; i < 1; i++) {
//                    for (int j = 0; j < 100000000; j++)
//                        sum = 1 + 1;
//                }
                for (int i = 0; i < 1000000000; i++) {
                    for (int j = 0; j < 1; j++)
                        sum = 1 + 1;
                }
                CountUtil.intervalMicroTime(true);
                Log.d("William", "CPUTest-plus: " + sum);
            }
        }).start();
    }

    static void multiply() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sum = 1;
                CountUtil.intervalMicroTime(false);
                for (int i = 0; i < 100; i++) {
                    for (int k = 0; k < 1000000; k++) {
                        sum = data[k] * 2;
                    }
                }
                CountUtil.intervalMicroTime(true);
                Log.d("William", "CPUTest-multiply: " + sum);
            }
        }).start();
    }

    static void threadStart() {
        for (int i = 0; i < 1; i++) {
            threadTask(i);
        }
    }

    static void threadStart8() {
        for (int i = 0; i < 8; i++) {
            threadTask(i);
        }
    }

    private static void threadTask(int n) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sum = 0;
                for (int i = 0; ; i++) {
                    sum++;
                }
            }
        }).start();
    }
}
