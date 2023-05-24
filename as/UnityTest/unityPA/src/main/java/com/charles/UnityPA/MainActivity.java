package com.charles.UnityPA;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.unity3d.player.UnityPlayer;


//2019-unity-jar路径:
//C:/Program Files/Unity/Editor/Data/PlaybackEngines/AndroidPlayer/Variations/mono/Release/Classes
//C:/Program Files/Unity/Editor/Data/PlaybackEngines/AndroidPlayer/Source/unity3d/player

public class MainActivity extends UnityPlayerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Unity", "MainActivity-onCreate: ");//不执行
//        setContentView(R.layout.activity_main);
    }

    //Unity调安卓
    public void showToast(){ //2019这种办法无法获取context？2017可以?
        doJob();
        Toast.makeText(getBaseContext(),"Toast!",Toast.LENGTH_LONG).show();
    }

    public void showToast(Context context){ //unity主动传context可行
        doJob();
        Toast.makeText(context,"Toast!",Toast.LENGTH_LONG).show();
    }

    //安卓调用Unity
    public void doJob(){
        UnityPlayer.UnitySendMessage("GameObject", "FromAndroid", "hello unity i'm android");
    }
}
