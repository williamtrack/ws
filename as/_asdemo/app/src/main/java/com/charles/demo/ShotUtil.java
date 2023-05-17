package com.charles.demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.nio.ByteBuffer;

public class ShotUtil {

    static int width = 768;
    static int height = 1024;
    public static byte[] pixels = new byte[width * height * 4];

    public static Bitmap takeShot() {
        try {
//            long startTimeMillis = System.currentTimeMillis();
            Process sh = Runtime.getRuntime().exec("/system/bin/screencap -p");
            Bitmap bitmap;
            byte[] byteArray = new byte[768 * 1024 * 4]; // 此处的数组大小依据最终的图片大小而定，此处定义为360x640，测试设备的
            try (BufferedInputStream bis = new BufferedInputStream(sh.getInputStream())) {
                int length = bis.read(byteArray);
                bitmap = BitmapFactory.decodeByteArray(byteArray, 0, length);
                pixels = bmp2bytes(bitmap);
//                long endTimeMillis = System.currentTimeMillis();
//                Log.d("William", "shotTime：" + (endTimeMillis - startTimeMillis) + "ms");
            }
            sh.destroy();
            return bitmap;
        } catch (Exception e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] bmp2bytes(Bitmap bmp) {
        int bytes = bmp.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bmp.copyPixelsToBuffer(buf);
        return buf.array();
    }

    public static String adbShot() {
        try {
            // image naming and path  to include download cache appending name you choose for file
            String dirPath = Environment.getExternalStorageDirectory().getPath();
            String imagePath = dirPath + "/aa.png";
            Process sh = Runtime.getRuntime().exec("/system/bin/screencap -p " + imagePath, null, null);
            long startTimeMillis = System.currentTimeMillis();
            sh.waitFor();
            long endTimeMillis = System.currentTimeMillis();
            Log.d("william", "ShotUtil-adbShot: " );
//            Log.d("William", "shotTime：" + (endTimeMillis - startTimeMillis) + "ms");
            return null;
        } catch (Exception e) {
            // exception
        }
        return null;
    }
}
