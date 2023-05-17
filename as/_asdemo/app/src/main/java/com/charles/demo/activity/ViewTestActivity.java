package com.charles.demo.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.charles.demo.test.ViewTest;

//  Created by Charles on 2021/2/28.
//  mail: zingon@163.com
public class ViewTestActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewTest viewTest;
        setContentView(viewTest = new ViewTest(getBaseContext()));

        ViewGroup.LayoutParams lp = viewTest.getLayoutParams();
        lp.height = 1000;
        lp.width = 1000;
        viewTest.setBackgroundColor(Color.GREEN);

        LinearLayout layout = new LinearLayout(this);
    }
}
