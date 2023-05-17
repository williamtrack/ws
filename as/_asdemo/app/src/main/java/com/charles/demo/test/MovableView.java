package com.charles.demo.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

//  Created by Charles on 09/30/2020.
//  mail: zingon@163.com
public class MovableView extends View {
    private int lastX;
    private int lastY;
    private Scroller mScroller;

    public MovableView(Context context) {
        super(context);
    }

    public MovableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mScroller=new Scroller(getContext());
        //ObjectAnimator.ofFloat(this, "translationX", 0, 300).setDuration(1000).start();
        //ObjectAnimator.ofFloat(this, "translationY", 0, 500).setDuration(2000).start();
    }

    public MovableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastX;
                int offsetY = y - lastY;
//                offsetLeftAndRight(offsetX);
//                offsetTopAndBottom(offsetY);
                ((View)getParent()).scrollBy(-offsetX,-offsetY);

                //layout(getLeft()+offsetX,getTop()+offsetY,getRight()+offsetX,getBottom()+offsetY);

                //LinearLayout.LayoutParams layoutParams=(LinearLayout.LayoutParams) getLayoutParams();
                //layoutParams.topMargin=getTop()+offsetY-250;
                //layoutParams.leftMargin=getLeft()+offsetX;
                //layoutParams.weight=50;
                //layoutParams.height=30;
                //setLayoutParams(layoutParams);
                break;
        }
        return true;//消费了此事件，否则继续等待
    }
}
