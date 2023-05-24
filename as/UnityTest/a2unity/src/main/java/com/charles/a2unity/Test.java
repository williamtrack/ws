package com.charles.a2unity;

import android.util.Log;

public class Test {
    void UA_voidTest() {
        Log.d("Unity", "Test-UA_voidTest: ");
        AU_fromAndroid();
    }

    void UA_intTest(int value1, int value2) {
        Log.d("Unity", "Test-UA_intTest: " + value1 + "：" + value2);
    }

    int UA_returnIntTest() {
        return 100;
    }

    void AU_fromAndroid(){
        UnityHelper.callUnity("GameObject", "FromAndroid", "hello unity i'm android");
    }
}
