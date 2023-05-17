package com.charles.demo.test;

import android.graphics.Bitmap;
import android.graphics.Color;

class HeapStackTest {
    static int num = 30;
    static Bitmap[] bitmaps = new Bitmap[num];
    static byte[] bytes;

    static void createByte() {//使用dalvik内存有256m大小限制
        bytes = new byte[200000000];
    }

    static Bitmap createBitmap() {//用Bitmap函数使用native内存 不是dalvik内存
        for (int i = 0; i < num; i++) {
            Bitmap bitmap = Bitmap.createBitmap(6000, 6000, Bitmap.Config.ARGB_8888);//144M
            bitmap.eraseColor(Color.parseColor("#ff0000")); // 填充颜色
            bitmaps[i] = bitmap;
        }
        return bitmaps[0];
    }
}
