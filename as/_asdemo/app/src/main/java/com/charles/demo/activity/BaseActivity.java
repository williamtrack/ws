package com.charles.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseActivity.addActivity(this);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);    //Activity去除标题栏
        //if (getSupportActionBar() != null){ getSupportActionBar().hide(); }  //AppCompatActivity去除标题栏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);   //全屏，去除状态栏
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseActivity.removeActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void showToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    protected void showLongToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
    }

    private static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
            activities.clear();
        }
    }
}
