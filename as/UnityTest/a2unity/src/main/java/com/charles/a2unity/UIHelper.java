package com.charles.a2unity;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class UIHelper {

    //创建自定义视图
    void viewTest() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        ViewGroup decorView = (ViewGroup)getActivity().getWindow().getDecorView();
        Button button = new Button(UnityHelper.getActivity());
        button.setBackgroundColor(Color.argb(255, 255, 0, 0));
//        decorView.addView(button, layoutParams);

        lp.gravity = Gravity.TOP;
        lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; //不设置屏幕上弹窗之外的地方不能点击
        lp.width = 200;
        lp.height = 100;

        UnityHelper.getActivity().getWindowManager().addView(button, lp);
    }

    //双屏异显
    void renderSecondLayout() {
        DisplayManager mDisplayManager = (DisplayManager) UnityHelper.getActivity().getSystemService(Context.DISPLAY_SERVICE);
        assert mDisplayManager != null;
        Display[] displays = mDisplayManager.getDisplays();
        Log.d("William", "UIHelper-renderSecondLayout: " +displays.length);
        
        if (displays.length == 2) {
            SecondDisplay mPresentation = new SecondDisplay(UnityHelper.getActivity(), displays[1], 1);
            //mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY); //安卓12这句话有问题
            mPresentation.show();
        }
    }

    public static class SecondDisplay extends Presentation {

        public SecondDisplay(Context outerContext, Display display, int theme) {
            super(outerContext, display, theme);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            //去掉标题栏，dialog、activity有效，AppCompatActivity无效
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //去掉Activity上面的状态栏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.second_layout);
        }

    }

}
