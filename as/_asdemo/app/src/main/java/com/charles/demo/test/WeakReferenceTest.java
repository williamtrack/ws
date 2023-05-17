package com.charles.demo.test;

import java.lang.ref.WeakReference;

//  Created by Charles on 10/19/2020.
// 只有在电脑上测试可以
public class WeakReferenceTest {
    public static void test() {
        WeakReference<String> st = new WeakReference<String>(new String("car"));
//        WeakReference<String> st = new WeakReference<String>("car"); //这种写法无法回收 why
        int i = 0;
        while (st.get() != null) {
            if (st.get() != null) {
                i++;
                System.out.println("Object is alive for " + i + " loops - " + st);
                if (i % 10000 == 0) {
                    System.gc();
                }
            } else {
                System.out.println("Object has been collected.");
                break;
            }
        }
        System.out.println("end");

//        WeakReference<String> st = new WeakReference<String>(new String("hello"));
//        System.out.println(st.get());
//        System.gc();
////        Runtime.getRuntime().gc();
//        System.out.println(st.get());
    }
}
