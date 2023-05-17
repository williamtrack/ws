package com.charles.demo.test;

import android.util.Log;

//  Created by Charles on 10/14/2020.
//  mail: zingon@163.com
//  内存泄漏测试
public class MemoryLeakTest {
    private static MemoryLeakTest instance;
    private final byte[] bytes;

    private MemoryLeakTest() {
        bytes = new byte[100000000];
        for (int i = 0; i < 100000000; i++) {
            bytes[i] = (byte) i;
        }
    }

    public static MemoryLeakTest getInstance() {
        if (instance == null) {
            synchronized (MemoryLeakTest.class) {
                if (instance == null) {instance = new MemoryLeakTest();}
            }
        }
        return instance;
    }

    public void getValue() {
        Log.d("William", "MemoryLeakTest-getValue: " + bytes[100]);
    }
}
