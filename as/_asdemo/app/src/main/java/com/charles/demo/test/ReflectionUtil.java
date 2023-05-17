package com.charles.demo.test;

import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ReflectionUtil {

    public static void callMethodA() {
        try {
            // ===获取类名===
            Class<?> mClass = Class.forName("android.util.Size");

            // ===获取构造函数===
            Constructor con = mClass.getDeclaredConstructor(int.class, int.class);
            if (!con.isAccessible()) { con.setAccessible(true); }

            // ===构造===
            Object mObject = con.newInstance(20, 10);
//            Object mObject = context.getSystemService("camera3d");

            // ===获取方法===
            Method[] methods = mClass.getDeclaredMethods();
            Method method = null;
            for (Method m : methods) {
                Log.d("William", "ReflectionUtil-name: " + m.getName());
                if (m.getName().equalsIgnoreCase("getWidth")) {
                    method = m;
                    break;
                }
            }
//            if (!method.isAccessible()) { method.setAccessible(true); }

            // ===执行方法===
            Object res = method.invoke(mObject);
            Log.d("William", res.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
