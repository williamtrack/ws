package com.charles.demo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.charles.demo.R;

public class Main2Activity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        int a=(int)App.getExtra("paraA");
        Log.d("Will", "Main2Activity: "+a);

        findViewById(R.id.btn_main3_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
            }
        });
    }
}
