package com.charles.a2unity;

import android.app.Activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//  Created by Charles on 2021/2/26.
//  mail: zingon@163.com
public class UnityHelper {

    public static Activity mActivity;
    public static Class<?> mClass;

    static {
        mActivity = getActivity();
        try {
            mClass = Class.forName("com.unity3d.player.UnityPlayer");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 安卓获取unity的上下文
     *
     * @return Unity上下文
     */
    public static Activity getActivity() {
        Activity unityActivity = null;
        try {
            Class<?> className = Class.forName("com.unity3d.player.UnityPlayer");
            unityActivity = (Activity) className.getDeclaredField("currentActivity").get(className);
        } catch (ClassNotFoundException ignored) {
        } catch (IllegalAccessException ignored) {
        } catch (NoSuchFieldException ignored) {
        }
        return unityActivity;
    }

    /**
     * 安卓调用Unity的方法
     *
     * @param gameObjectName 调用的GameObject的名称
     * @param functionName   方法名
     * @param args           参数
     * @return 调用是否成功
     */
    //UnityHelper.callUnity("GameObject", "FromAndroid", "hello unity i'm android");
    public static boolean callUnity(String gameObjectName, String functionName, String args) {
        try {
            Class<?> classtype = Class.forName("com.unity3d.player.UnityPlayer");
            Method method = classtype.getMethod("UnitySendMessage", String.class, String.class, String.class);
            method.invoke(classtype, gameObjectName, functionName, args);
            return true;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }


}
