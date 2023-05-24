package com.charles.util.blue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BluetoothUtil {
    private final String TAG = "William";
    private BluetoothClient bluetoothClient;
    private List<BluetoothDevice> devices = new ArrayList<>();
    private List<BluetoothDevice> bouddevices = new ArrayList<>();
    private boolean isFinishSearch = false;
    private String[] boudedblueName;
    private String[] blueName;
    private String[] mblueName;
    private BluetoothDevice targetDevice;
    private String[] mBlueMac;
    Context context;

    public BluetoothUtil(Context context) {this.context = context;}

    public void start() {
        bluetoothClient = BluetoothClient.newInstance(context);
        bluetoothClient.setOnBluetoothFindDeviceListener(new BluetoothClient.BluetoothFindDeviceListener() {
            @Override
            public void onFindDevice(BluetoothDevice device) {
                if (devices.contains(device) || device.getName() == null) {

                } else { devices.add(device);}
            }
        }).setOnBluetoothSearchFinishedListener(new BluetoothClient.BluetoothSearchFinishedListener() {
            @Override
            public void onFinishedSearch(List<BluetoothDevice> devices) {

                StringBuilder sb = new StringBuilder();
                mblueName = new String[devices.size()];
                mBlueMac = new String[devices.size()];

                for (int i = 0; i < devices.size(); i++) {
                    mblueName[i] = devices.get(i).getName();
                    mBlueMac[i] = devices.get(i).getAddress();
                    Log.d("William", "BluetoothUtil-onFinishedSearch: " +mblueName[i]);

                    if(mblueName[i].equals("Z9")) {
                        Log.d("William", " ==="+bouddevices.get(i).getAddress());
//                        connectBlue(bouddevices.get(i).getAddress());
                    }
                }

                if (mblueName.length != 0) {
                    sb.append(mblueName[0]).append("&").append(mBlueMac[0]);
                    for (int i = 1; i < mblueName.length; i++) {
                        sb.append(";").append(mblueName[i]).append("&").append(mBlueMac[i]);
                        Log.d(TAG, "onFinishedSearch: " + mblueName[i] + "&" + mBlueMac[i]);
                    }
                }
                Log.d(TAG, "搜索完成，共找到" + mblueName.length + "设备");
//                Log.d("William", "BluetoothUtil-onFinishedSearch: " +sb.toString());
            }
        }).setOnBluetoothBondChangeListener(new BluetoothClient.BluetoothBondChangeListener() {
            @Override
            public void onBond() {
                Log.d(TAG, "BOND_NONE 删除配对");
            }

            @Override
            public void onBonding() {
                Log.d(TAG, "BOND_BONDING 正在配对");
            }

            @Override
            public void onBonded() {
                Log.d(TAG, "BOND_BONDED 配对成功");
                bluetoothClient.bondAndConn(true, targetDevice);
            }
        }).setOnBluetoothConnectListener(new BluetoothClient.BluetoothConnectListener() {
            @Override
            public void onConnect(BluetoothDevice device) {
                Log.d(TAG, "蓝牙已连接: " + device.getName());
            }

            @Override
            public void onDisconnect(BluetoothDevice device) {
                Log.d(TAG, "蓝牙已断开: " + device.getName());
            }
        });
    }

    public void onPause() {
        bluetoothClient.unRegiestReceiver();
    }

    public void onResume() {
        bluetoothClient.registerReceiver();
        if(!bluetoothClient.check()){
            Log.d("William", "not connected");
            scanBlue();
        }
    }

    //打开蓝牙
    public void openBlue() {
        bluetoothClient.openBluetooth();
    }

    //关闭蓝牙
    public void closeBlue() {
        bluetoothClient.closeBluetooth();
    }

    //搜索蓝牙
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void scanBlue() {
        devices.clear();
        bouddevices.clear();
        addBound();
        bluetoothClient.startSearch();
//        bluetoothClient.startScan();
    }

    public String[] getBounedDevice() {
        bouddevices.clear();
        addBound();
        blueName = new String[bouddevices.size()];
        for (int i = 0; i < bouddevices.size(); i++) {
            blueName[i] = bouddevices.get(i).getName() + "&" + bouddevices.get(i).getAddress();
        }
        return blueName;
    }

    //连接蓝牙
    public void connectBlue(String targetDeviceName) {
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getAddress().equals(targetDeviceName)) {
                targetDevice = devices.get(i);
                Log.d("William", "connectBlueTooth: " + targetDevice.getName());
            }
        }
        bluetoothClient.bondAndConn(true, targetDevice);
    }

    //断开蓝牙连接
    public void disConnect(String targetDeviceName) {
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getAddress().equals(targetDeviceName)) {

                targetDevice = devices.get(i);
                Log.d("William", "disconnectBlueTooth: " + targetDevice.getName());
            }
        }
        bluetoothClient.mHidConnectUtil.disConnect(targetDevice);
    }

    //取消配对
    public void unPair(String targetDeviceName) {
        for (int i = 0; i < devices.size(); i++) {
            if (devices.get(i).getName().equals(targetDeviceName)) {
                Log.d("William", "unpariBlurTooth: " + devices.get(i).getName());
                targetDevice = devices.get(i);
            }
        }
        bluetoothClient.mHidConnectUtil.unPair(targetDevice);
    }

    //已配对设备
    private void addBound() {
        Set<BluetoothDevice> bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (bondedDevices != null)
            devices.addAll(bondedDevices);
        bouddevices.addAll(bondedDevices);
        for (int i = 0; i < bouddevices.size(); i++) {
            Log.d(TAG, "addBound: " + bouddevices.get(i).getName());
//            Log.d(TAG, "addBound address: " + bouddevices.get(i).getAddress());
        }
    }
}
