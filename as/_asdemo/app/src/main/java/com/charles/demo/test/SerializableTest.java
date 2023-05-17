package com.charles.demo.test;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/*
 *  Created by Charles on 09/16/2020.
 *  Serializable 序列化
 */
public class SerializableTest implements Serializable {
    private String userName;
    private static File file;
    private String path = Environment.getExternalStorageDirectory().getPath() + "/myData";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public SerializableTest() throws IOException {
        file = new File(path);
        if (file.exists()) {
            file.createNewFile();
        }
    }

    public static void writeUser() {
        try {
            SerializableTest user = new SerializableTest();
            user.setUserName("Mike");
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
            objectOutputStream.writeObject(user);
            objectOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("will", "SerializableTest-writeUser: succeed!");
    }

    public static void readUser() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file));
            SerializableTest user = (SerializableTest) objectInputStream.readObject();
            objectInputStream.close();
            Log.d("will", "SerializableTest-readUser: " + user.getUserName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
