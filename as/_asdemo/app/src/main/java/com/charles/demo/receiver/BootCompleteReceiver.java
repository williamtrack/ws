package com.charles.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.charles.demo.MainActivity;

//  Created by Charles on 11/01/2020.
//  mail: zingon@163.com
public class BootCompleteReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent StartIntent = new Intent(context, MainActivity.class);
            StartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(StartIntent);
        }
    }
}
