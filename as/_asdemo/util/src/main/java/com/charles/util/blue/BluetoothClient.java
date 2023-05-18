package com.charles.util.blue;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class BluetoothClient {
    private static final String TAG = "William";
    private static Context mContext;
    // 广播接收者，搜索设备和配对状态改变广播
    private BluetoothReceiver mBluetoothReceiver;
    // 连接类型 安全/受保护的连接(Secure)或者不安全的连接/不受保护的连接(Insecure)
    private static boolean mSecure;
    // 进行配对和连接的设备
    private static BluetoothDevice mDevice;
    // 广播接收者，搜索设备和配对状态改变广播

    // 连接工具类

    // 蓝牙适配器
    private BluetoothAdapter mBluetoothAdapter;
    // 找到设备监听，每找到一个设备回调一次方法

    // 连接监听，方法执行在子线程
    private ClientConnListener mClientConnListener;
    // 保存所有搜索到的设备
    public static List<BluetoothDevice> mDevices = new ArrayList<>();
    // 使用单例
    private static BluetoothClient mBluetoothClient;

    // 是否自动连接
    public boolean isAutoConn = false;

    public BluetoothFindDeviceListener mFindDeviceListener;
    // 搜索完成监听，搜索完成时回调
    public BluetoothSearchFinishedListener mFinishedListener;
    // 进行配对时，配对状态发生改变时回调相关方法
    public BluetoothBondChangeListener mBondChangeListener;
    //连接状态改变监听
    public BluetoothConnectListener mConnectListener;

    public final int MSG_WHAT_FOUND_DEVICE = 0XFF04; // 找到设备 what
    public final int MSG_WHAT_FINISHED_SEARCH = 0XFF05; // 搜索完成 what
    public final int MSG_WHAT_BOND_NONE = 0XFF06; // 没有配对 what
    public final int MSG_WHAT_BOND_BONDING = 0XFF07; // 正在配对 what
    public final int MSG_WHAT_BOND_BONDED = 0XFF08; // 配对成功 what

    private static boolean mReceiverTag = false;
    public HidConnectUtil mHidConnectUtil;

    private ArrayList<String> data = null;
    public List<BluetoothDevice> mBlueDevices = new ArrayList<>();


    /**
     * 创建 BluetoothClient 对象，使用单例模式
     *
     * @param context
     * @return
     */
    public static BluetoothClient newInstance(Context context) {
        if (mBluetoothClient == null) {
            synchronized (BluetoothClient.class) {            //用synchronized加锁
                if (mBluetoothClient == null) {
                    mBluetoothClient = new BluetoothClient(context);
                }
            }
        }
        return mBluetoothClient;
    }

    public BluetoothClient(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mHidConnectUtil = new HidConnectUtil(context);
        //regiestReceiver();

    }

    /**
     * 注册接受搜索到的蓝牙设备广播
     */
    public BluetoothClient registerReceiver() {
        if (!mReceiverTag) {
            mReceiverTag = true;
            mBluetoothReceiver = new BluetoothReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
            intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

            mContext.registerReceiver(mBluetoothReceiver, intentFilter);
        }
        return mBluetoothClient;
    }


    /**
     * 设置找到设备监听，每找到一个设备会调用一次回调方法
     *
     * @param findDeviceListener
     * @return
     */
    public BluetoothClient setOnBluetoothFindDeviceListener(BluetoothFindDeviceListener findDeviceListener) {
        this.mFindDeviceListener = findDeviceListener;
        return mBluetoothClient;
    }

    /**
     * 设置搜索完成监听，搜索结束之后回调方法
     *
     * @param finishedListener
     * @return
     */
    public BluetoothClient setOnBluetoothSearchFinishedListener(BluetoothSearchFinishedListener finishedListener) {
        this.mFinishedListener = finishedListener;
        return mBluetoothClient;
    }

    /**
     * 设置配对状态改变监听
     *
     * @param bondChangeListener
     * @return
     */
    public BluetoothClient setOnBluetoothBondChangeListener(BluetoothBondChangeListener bondChangeListener) {
        this.mBondChangeListener = bondChangeListener;
        return mBluetoothClient;
    }

    //设置连接状态改变监听
    public BluetoothClient setOnBluetoothConnectListener(BluetoothConnectListener connectListener) {
        this.mConnectListener = connectListener;
        return mBluetoothClient;
    }

    /**
     * 设置连接监听
     *
     * @param clientConnListener
     * @return
     */
    public BluetoothClient setOnClientConnListener(ClientConnListener clientConnListener) {
        this.mClientConnListener = clientConnListener;
        return mBluetoothClient;
    }


    /**
     * 打开设备蓝牙
     *
     * @return
     */
    public BluetoothClient openBluetooth() {
        if (!hasBluetooth())
            return mBluetoothClient;
        if (!mBluetoothAdapter.isEnabled()) {
            boolean enable = mBluetoothAdapter.enable();

            //regiestReceiver();
            if (enable)
                //Toast.makeText(mContext, "蓝牙打开成功", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "蓝牙打开成功 ");
            else
                //Toast.makeText(mContext, "蓝牙打开失败", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "蓝牙打开失败 ");
            //Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //mContext.startActivity(intent);
        } else {
            //Toast.makeText(mContext, "蓝牙已打开", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "蓝牙已打开");
        }
        return mBluetoothClient;
    }

    //关闭蓝牙
    public void closeBluetooth() {
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            //unRegiestReceiver();
        } else {
            Log.d(TAG, "蓝牙已关闭");
        }
    }

    //检查是否有设备连接
    public boolean check(){
        int state = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
        Log.d("William", "BluetoothClient-check: " +state);
        if (state == BluetoothAdapter.STATE_CONNECTED){
            return true;
        }else return false;
    }

    /**
     * 设备是否支持蓝牙
     *
     * @return true：支持 false：不支持
     */
    public boolean hasBluetooth() {
        if (mBluetoothAdapter == null) {
            //Toast.makeText(mContext, "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "该设备不支持蓝牙 ");
            return false;
        }
        return true;
    }

    /**
     * 开始搜索设备
     *
     * @return
     */
    public BluetoothClient startSearch() {
        if (!hasBluetooth())
            return mBluetoothClient;
        // 判断蓝牙是否已打开
        if (!mBluetoothAdapter.isEnabled()) {
            //Toast.makeText(mContext, "请先打开蓝牙", Toast.LENGTH_SHORT).show();
            return mBluetoothClient;
        }

        //如果没有注册广播，就先注册
        if (mBluetoothReceiver == null)
            registerReceiver();

        // 清除原来保存的设备信息
        mDevices.clear();

        // 开始搜索
        if (!mBluetoothAdapter.isDiscovering())
            mBluetoothAdapter.startDiscovery();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mBluetoothAdapter.cancelDiscovery();
            }
        }, 5000);
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        mBluetoothAdapter.cancelDiscovery();
        return mBluetoothClient;
    }

    /**
     * 扫描蓝牙
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public List<BluetoothDevice> startScan() {

        mBluetoothAdapter.getBluetoothLeScanner().startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                //信号强度，是负的，数值越大代表信号强度越大
                result.getRssi();
                BluetoothDevice device = result.getDevice();
                mBlueDevices.add(device);
                Log.d(TAG, "onScanResult: " + device.getName());
                super.onScanResult(callbackType, result);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                super.onBatchScanResults(results);
            }

            @Override
            public void onScanFailed(int errorCode) {
                super.onScanFailed(errorCode);
            }
        });
        return mDevices;

    }

    /**
     * 取消搜索
     *
     * @return
     */
    public BluetoothClient cancelDiscovery() {
        if (!hasBluetooth())
            return mBluetoothClient;

        mBluetoothAdapter.cancelDiscovery();
        return mBluetoothClient;
    }

    public BluetoothClient unRegiestReceiver() {
        if (mReceiverTag) {
            if (mBluetoothReceiver != null) {
                mReceiverTag = false;
                mContext.getApplicationContext().unregisterReceiver(mBluetoothReceiver);
                mBluetoothReceiver = null;
            }
        }
        return mBluetoothClient;
    }

    /**
     * 配对蓝牙设备
     *
     * @param device BluetoothDevice对象
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public BluetoothClient bondDevice(BluetoothDevice device) {
        if (!hasBluetooth())
            return mBluetoothClient;

        isAutoConn = false;
        int bondState = device.getBondState();
        if (BluetoothDevice.BOND_NONE == bondState) {
            device.createBond();
        } else if (BluetoothDevice.BOND_BONDING == bondState) {
            Log.d(TAG, "正在配对 ...");
        } else if (BluetoothDevice.BOND_BONDED == bondState) {
            // 已经配对
            Log.d(TAG, "已经配对 ...");
        }
        return mBluetoothClient;
    }

    /**
     * 配对并且连接到服务器
     *
     * @param secure 是否建立安全连接
     * @param device BluetoothDevice对象
     * @return
     */
    public BluetoothClient bondAndConn(boolean secure, BluetoothDevice device) {
        if (!hasBluetooth())
            return mBluetoothClient;

        isAutoConn = true;
        this.mSecure = secure;
        this.mDevice = device;
        int bondState = device.getBondState();
        if (BluetoothDevice.BOND_NONE == bondState) {
            //device.createBond();

            mHidConnectUtil.pair(device);
            //mHidConnectUtil.connect(device);
        } else if (BluetoothDevice.BOND_BONDING == bondState) {
            Log.d(TAG, "正在配对 ...");
        } else if (BluetoothDevice.BOND_BONDED == bondState) {
            // 建立连接
            //createConn(secure, device);
            //connectBlue(device);
            mHidConnectUtil.connect(device);
           /* if (device.getBluetoothClass().getMajorDeviceClass() != BluetoothClass.Device.Major.AUDIO_VIDEO){
                mHidConnectUtil.connect(device);}else{
               connectBlue(device);
            }*/

        }
        return mBluetoothClient;
    }


    /**
     * 找到设备监听
     */
    public interface BluetoothFindDeviceListener {
        /**
         * 每找到一个设备回调一次
         *
         * @param device 找到的设备
         */
        void onFindDevice(BluetoothDevice device);
    }

    /**
     * 搜索完成监听
     */
    public interface BluetoothSearchFinishedListener {
        /**
         * 搜索结束时回调
         *
         * @param devices 找到的所有设备
         */
        void onFinishedSearch(List<BluetoothDevice> devices);
    }

    /**
     * 配对状态发生改变监听
     */
    public interface BluetoothBondChangeListener {
        /**
         * 未配对
         */
        void onBond();

        /**
         * 正在配对
         */
        void onBonding();

        /**
         * 已经配对
         */
        void onBonded();
    }

    /**
     * 建立连接监听，<b>方法运行在子线程</b>
     */
    public interface ClientConnListener {
        /**
         * 连接建立成功，<b>方法运行在子线程</b>
         *
         * @param secure          是否为安全的连接 true：是 false：不是
         * @param bluetoothSocket 建立连接成功之后的 BluetoothSocket 对象
         */
        void onSucceed(boolean secure, BluetoothSocket bluetoothSocket);

        /**
         * 连接建立失败，<b>方法运行在子线程</b>
         *
         * @param e 异常信息
         */
        void onFialed(Exception e);
    }

    /**
     * 连接状态监听
     */
    public interface BluetoothConnectListener {
        //连接成功
        void onConnect(BluetoothDevice device);

        //断开连接
        void onDisconnect(BluetoothDevice device);
    }

    /**
     * 搜索设备和配对状态改变广播
     */
    public class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
               /* Message message = Message.obtain();     //通过obtain方法获取Message对象使得Message到了重复的利用，
                // 减少了每次获取Message时去申请空间的时间。
                message.what = MSG_WHAT_FOUND_DEVICE;
                message.obj = device;
                mHandler.sendMessage(message);*/
                //Log.d(TAG, "onReceive: 设备"+device.getName());
                if (mDevices.contains(device) || device.getName() == null) {} else {
                    mDevices.add(device);
                    if (mFindDeviceListener != null) {
                        mFindDeviceListener.onFindDevice(device);
                    }
                }


            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //mHandler.sendEmptyMessage(MSG_WHAT_FINISHED_SEARCH);
                Log.d("William", "BluetoothReceiver-finish" );

                if (mFinishedListener != null) {
                    mFinishedListener.onFinishedSearch(mDevices);
                }


            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
                switch (state) {
                    case BluetoothDevice.BOND_NONE:
                        //mHandler.sendEmptyMessage(MSG_WHAT_BOND_NONE);
                        //Log.d(TAG, "BOND_NONE 删除配对");
                        if (mBondChangeListener != null) {
                            mBondChangeListener.onBond();
                        }
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        if (mBondChangeListener != null)
                            mBondChangeListener.onBonding();
                        // mHandler.sendEmptyMessage(MSG_WHAT_BOND_BONDING);
                        //Log.d(TAG, "BOND_BONDING 正在配对");
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        // mHandler.sendEmptyMessage(MSG_WHAT_BOND_BONDED);
                        if (mBondChangeListener != null)
                            mBondChangeListener.onBonded();
                        // Log.d(TAG, "BOND_BONDED 配对成功");
                        break;
                }
            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {

                //Log.d(TAG, "蓝牙已连接: " + device.getName());
                if (mConnectListener != null)
                    mConnectListener.onConnect(device);

            } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {

                if (mConnectListener != null)
                    mConnectListener.onDisconnect(device);

                //Log.d(TAG, "蓝牙已断开: " + device.getName());

            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 1000);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "蓝牙已关闭");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "蓝牙已打开 ");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "正在打开蓝牙 ");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "正在关闭蓝牙");
                        break;
                    default:
                        Log.d(TAG, "未知状态 ");
                }
            }
        }

    }

    //蓝牙耳机连接方法
    public void connectBlue(final BluetoothDevice targetDevice) {
        if (targetDevice.getBluetoothClass().getMajorDeviceClass() != BluetoothClass.Device.Major.AUDIO_VIDEO) {
            return;
        }
        mBluetoothAdapter.getProfileProxy(mContext, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) proxy;
                Class btHeadsetCls = BluetoothHeadset.class;
                try {
                    Method connect = btHeadsetCls.getMethod("connect", BluetoothDevice.class);
                    connect.setAccessible(true);
                    connect.invoke(bluetoothHeadset, targetDevice);
                } catch (Exception e) {
                    Log.d(TAG, e + "");
                }
            }

            @Override
            public void onServiceDisconnected(int profile) {

            }
        }, BluetoothProfile.HEADSET);
    }

    //断开蓝牙耳机连接
    public BluetoothClient disConnect(final BluetoothDevice device) {
        if (!hasBluetooth())
            return mBluetoothClient;
        if (device.getBluetoothClass().getMajorDeviceClass() != BluetoothClass.Device.Major.AUDIO_VIDEO) {
            return mBluetoothClient;
        }
        mBluetoothAdapter.getProfileProxy(mContext, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) proxy;
                Class btHeadsetCls = BluetoothHeadset.class;
                try {
                    Method connect = btHeadsetCls.getMethod("disconnect", BluetoothDevice.class);
                    connect.setAccessible(true);
                    connect.invoke(bluetoothHeadset, device);
                } catch (Exception e) {
                    Log.d(TAG, e + "");
                }
            }

            @Override
            public void onServiceDisconnected(int profile) {

            }
        }, BluetoothProfile.HEADSET);
        return mBluetoothClient;
    }


}
