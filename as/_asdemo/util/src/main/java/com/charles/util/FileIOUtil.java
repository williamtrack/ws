package com.charles.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileIOUtil {
    public static boolean writeFileFromString(String string, String path) {
        try {
            // 判断当前的手机是否有sd卡
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                return false;
            }
            File file = new File(path);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(string.getBytes());
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String readFileToString(String filePath) {
        String str = "";
        try {
            FileInputStream fin = new FileInputStream(filePath);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            str = new String(buffer);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void getHardwareInfo() {
        String file_path = "/dev/bus/usb/001";
        File file = new File(file_path);
        String strMode = null;
        String mInfo;
        if (!file.exists()) {
            Log.d("William", "/dev/video0 not exist!");
        }
        FileInputStream fis;
        try {
            fis = new FileInputStream(file);
            byte[] buf = new byte[64];
            try {
                int len = fis.read(buf, 0, buf.length);
                fis.close();
                Log.d("William", "fis.read len = " + len);
                strMode = new String(buf, 0, 64).trim();
                // strMode = 1~7, 1--min, 7--max
                mInfo = strMode;
                Log.d("William", "getHardwareInfo = " + strMode);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static boolean writeFileFromBytes(Context context, byte[] bytes) {
        try {
            // 判断当前的手机是否有sd卡
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                return false;
            }
            File sdCardFile = Environment.getExternalStorageDirectory();
            File file = new File(sdCardFile, "data");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public byte[] readFileToBytes(String filePath) {
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE) {
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = null;
        byte[] buffer = new byte[(int) fileSize];
        try {
            fi = new FileInputStream(file);
            int offset = 0;
            int numRead = 0;
            while (offset < buffer.length
                    && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }
            // 确保所有数据均被读取
            if (offset != buffer.length) {
                throw new IOException("Could not completely read file "
                        + file.getName());
            }
            fi.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
