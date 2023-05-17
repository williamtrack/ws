package com.charles.demo;

/*
 *   Created by Charles on 08/25/2020.
 *   单例模式-懒汉式(饿汉式是在定义的时候就实例化)
 */
public class Singleton {
    private Singleton() {}

    private static Singleton instance;

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void print() {
        System.out.println("abc");
    }

    public int value() {
        return 1;
    }
}
