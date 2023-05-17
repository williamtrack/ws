package com.charles.util;

import android.util.Log;

//  Created by Charles on 2021/3/19.
//  mail: zingon@163.com
public class CountUtil {
    private static long firstTime = System.nanoTime();
    private static long lastTime = System.nanoTime();

    public static long intervalTime(boolean isLog) {
        long interval = (System.nanoTime() - lastTime) / 1000000;
        lastTime = System.nanoTime();
        if (isLog) Log.d("William-TimeInterval", interval + " ms");
        return interval;
    }

    public static long intervalMicroTime(boolean isLog) {
        long interval = (System.nanoTime() - lastTime) / 1000;
        lastTime = System.nanoTime();
        if (isLog) Log.d("William-TimeInterval", interval + " us");
        return interval;
    }

    public static long consumingTime(boolean isLog) {
        if (isLog) Log.d("William-TimeConsuming", (System.nanoTime() - firstTime) / 1000000 + " ms");
        return (System.nanoTime() - firstTime) / 1000000;
    }

    public static void reset() {
        firstTime = System.nanoTime();
        lastTime = System.nanoTime();
    }

}