package com.charles.demo.test;

import android.util.Log;

import java.util.Random;

public class RandomTest {
    public static void test(){
        Random random = new Random();
        Log.d("William", "RandomTest-test: " +(100+random.nextInt(100)));
    }
}
