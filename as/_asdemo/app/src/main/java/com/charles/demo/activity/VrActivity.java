package com.charles.demo.activity;

import android.app.ActivityGroup;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

//  Created by Charles on 2021/2/28.
//  mail: zingon@163.com
public class VrActivity extends ActivityGroup implements SurfaceHolder.Callback {
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("will", "surfaceCreated: ");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("will", "surfaceChanged: ");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("will", "surfaceDestroyed: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout relativeLayout =new RelativeLayout(getBaseContext());
        setContentView(relativeLayout);

        SurfaceView sv = new SurfaceView(this);
        relativeLayout.addView(sv);

        Button button = new Button(getBaseContext());
        button.setText("test");
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("will", "onClick: ");
            }
        });
        relativeLayout.addView(button);

        sv.setBackgroundColor(Color.GREEN);
//        ViewGroup.LayoutParams lp = sv.getLayoutParams();
////        ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(1000,1000);//这么写会报错
//        lp.width = 1000;
//        lp.height = 1000;
//        sv.setLayoutParams(lp);

        sv.getHolder().addCallback(this);
        sv.getHolder().setFixedSize(1000,1000);

        // Force the screen to stay on, rather than letting it dim and shut off
        // while the user is watching a movie.
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Force screen brightness to stay at maximum
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.screenBrightness = 1.0f;
        getWindow().setAttributes(params);
    }
}
