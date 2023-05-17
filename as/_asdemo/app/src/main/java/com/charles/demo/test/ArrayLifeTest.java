package com.charles.demo.test;

import android.util.Log;

class ArrayLifeTest {
    static int[] a1 = new int[]{8, 7, 6};

    static void test() {
//        for(int x:returnA()){
//            Log.d("William", "ArrayLifeTest-test: " + x);
//        }
        change(a1);
        for (int x : a1) {
            Log.d("William", "ArrayLifeTest-test: " + x);
        }
    }

    private static int[] returnA() {
        int[] a1 = new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 0};
        return a1;
    }

    private static void change(int[] a) {
        a[0] = 0;
    }
}
