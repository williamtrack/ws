package com.charles.demo;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

public class DeviceIdUtil {
    private static final String INVALID_ANDROID_ID = "1234567890123456";

    public static String getAndroidID(Context context) {
        String deviceId;
        //如果sdk版本大于等于29, 则无法获取IMEI
        deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(deviceId)) {
            return deviceId;
        } else {
            return INVALID_ANDROID_ID;
        }
    }
}
