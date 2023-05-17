package com.charles.demo.activity;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

//  Created by Charles on 11/05/2020.
//  mail: zingon@163.com
public class App extends Application {

    private static Map<String, Object> map = new HashMap<>();
    private static App instance;
    private static Handler handler;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static App getInstance() {
        return instance;
    }

    public static void putExtra(String key, Object object) {
        map.put(key, object);
    }

    public static Object getExtra(String key) {
        return map.get(key);
    }


    public static Context getApp() {
        return instance;
    }

    public static Handler getHandler() {
        return handler;
    }
}
