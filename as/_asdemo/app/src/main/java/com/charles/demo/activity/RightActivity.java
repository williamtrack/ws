package com.charles.demo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class RightActivity extends BaseActivity {
    protected static final String WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    protected static final String READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    String[] permission = {RightActivity.WRITE_EXTERNAL_STORAGE, RightActivity.READ_PHONE_STATE};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkP(permission)) requestP(permission);
    }

    protected boolean checkP(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {     //异步进行
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }

    protected void requestP(String[] neededPermissions) {
        int requestCode = 0;
        ActivityCompat.requestPermissions(this, neededPermissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isAllGranted = true;
        for (int grantResult : grantResults) {
            isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
        }
        afterP(isAllGranted);
    }

    protected void afterP(boolean isAllGranted) { if (!isAllGranted) finish(); }

//    protected void checkFloatRight() {
//        FloatWindowManager.checkFloatRight(this, new FloatWindowManager.IFloatPermissionCallback() {
//            @Override
//            public void success() { }
//        });
//    }
}
