package com.charles.demo.test;

import android.view.MotionEvent;

//  Created by Charles on 2021/2/4.
//  mail: zingon@163.com
public class TouchActHelper {
    private static int pointCount = 0;
    private static float oldDist = 0;
    private static float oldPoint = 0;

    public TouchActHelper() {
    }

    public static void init(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                pointCount = 1;
                break;
            case MotionEvent.ACTION_UP:
                pointCount = 0;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                pointCount -= 1;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);//两点按下时的距离
                pointCount += 1;
                break;
            case MotionEvent.ACTION_MOVE:
                int sensitivity = 10;
                if (pointCount >= 2) {
                    float newDist = spacing(event);
                    if (newDist > oldDist + sensitivity) {
                        zoomOut();
                    } else if (newDist < oldDist - sensitivity) {
                        zoomIn();
                    } else if (Math.abs(event.getX() - oldPoint) > 5) {
//                        LogUtil.d("move");
                    }
                    oldPoint = event.getX();
                    oldDist = newDist;
                    break;
                }
        }
    }

    private static float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private static void zoomOut() {
//        LogUtil.d("zoomOut");
    }

    private static void zoomIn() {
//        LogUtil.d("zoomIn");
    }

}
