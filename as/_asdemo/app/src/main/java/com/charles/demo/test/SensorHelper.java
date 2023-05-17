package com.charles.demo.test;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;


//  Created by Charles on 2021/2/4.
//  mail: zingon@163.com
public class SensorHelper implements SensorEventListener {

    public interface IListener{
        void job1(float x,float y,float z);
        void job2();
    }

    private SensorManager sensorManager;
    private long lastUpdate;
    private Context context;
    private static float[] mTmp = new float[16];
    private IListener iListener;

    public SensorHelper(Context baseContext,IListener iListener) {
        this.context = baseContext;
        this.iListener=iListener;
    }


    public void init() {
        //获取SensorManager实例
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        //获取当前设备支持的传感器列表
//        List<Sensor> allSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
//        StringBuilder sb = new StringBuilder();
//        sb.append("当前设备支持传感器数：" + allSensors.size() + "   分别是：\n\n");
//        boolean type = true;
//        for (Sensor s : allSensors) {
//            switch (s.getType()) {
//                case Sensor.TYPE_ACCELEROMETER:
//                    sb.append("加速度传感器(Accelerometer sensor)" + "\n");
//                    break;
//                case Sensor.TYPE_GYROSCOPE:
//                    sb.append("陀螺仪传感器(Gyroscope sensor)" + "\n");
//                    break;
//                case Sensor.TYPE_LIGHT:
//                    sb.append("光线传感器(Light sensor)" + "\n");
//                    break;
//                case Sensor.TYPE_MAGNETIC_FIELD:
//                    sb.append("磁场传感器(Magnetic field sensor)" + "\n");
//                    break;
//                case Sensor.TYPE_ORIENTATION:
//                    sb.append("方向传感器(Orientation sensor)" + "\n");
//                    break;
//                case Sensor.TYPE_PRESSURE:
//                    sb.append("气压传感器(Pressure sensor)" + "\n");
//                    break;
//                case Sensor.TYPE_PROXIMITY:
//                    sb.append("距离传感器(Proximity sensor)" + "\n");
//                    break;
//                case Sensor.TYPE_TEMPERATURE:
//                    sb.append("温度传感器(Temperature sensor)" + "\n");
//                    break;
//                case Sensor.TYPE_ROTATION_VECTOR:
//
//                    break;
//                default:
//                    type = false;
//                    //sb.append("其他传感器" + "\n");
//                    break;
//            }
//            if (type) {
//                sb.append("设备名称：" + s.getName() + "\n 设备版本：" + s.getVersion() + "\n 供应商："
//                        + s.getVendor() + "\n\n");
//            }
//            type = true;
//        }
//        LogUtil.d("SensorHelper: "+sb.toString());


//        sensorManager.registerListener(this,
//                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST);
//        sensorManager.registerListener(this,
//                sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
//                SensorManager.SENSOR_DELAY_FASTEST);

        lastUpdate = System.currentTimeMillis();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
        if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE){
            getGyroscope(event,iListener);
        }
//        if(event.sensor.getType()==Sensor.TYPE_ROTATION_VECTOR){
//            getRotation(event,iListener);
//        }
        
    }

    private void getRotation(SensorEvent event,IListener iListener) {
//        long actualTime = System.currentTimeMillis();
//        if (actualTime - lastUpdate < 1000) {
//            return;
//        }
//        lastUpdate = actualTime;
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];
//        LogUtil.d("SensorHelper: "+x+"=="+y+"==="+z);
        SensorManager.getRotationMatrixFromVector(mTmp, values);

        iListener.job1(x,y,z);

//        if(Math.abs(x)>0.5||Math.abs(y)>0.5||Math.abs(z)>0.5){
//            for(float a:values){
//                LogUtil.d("SensorHelper: "+a);
//            }
//            LogUtil.d("======");
//        }

    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        if (accelationSquareRoot >= 2) //
        {
            long actualTime = System.currentTimeMillis();
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            Toast.makeText(context, "Device was shuffed", Toast.LENGTH_SHORT).show();
//            LogUtil.d("==="+x/SensorManager.GRAVITY_EARTH);
        }
    }

    private void getGyroscope(SensorEvent event,IListener iListener){
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];
        float sens =0.1f;
        if(Math.abs(x)>sens||Math.abs(y)>sens||Math.abs(z)>sens) {
//            LogUtil.d("SensorHelper: " + x+"=="+y+"=="+z);
        }
        iListener.job1(x,y,z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onResume() {
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onPause() {
        // unregister listener
        sensorManager.unregisterListener(this);
    }
}
