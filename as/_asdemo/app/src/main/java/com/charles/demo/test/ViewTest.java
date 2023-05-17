package com.charles.demo.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

//  Created by Charles on 2021/2/4.
//  mail: zingon@163.com
public class ViewTest extends View {

    private Canvas canvas;
    int left, top;
    private Rect rect;
    private Paint rectPaint;

    public ViewTest(Context context) {
        super(context);
        initAttribute();
    }

    public ViewTest(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttribute();
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (this.canvas == null) {
            this.canvas = canvas;
        }
        super.onDraw(canvas);
        canvas.drawRect(rect, rectPaint);
        canvas.drawLine(0, 0, 300, 300, rectPaint);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
//        setMeasuredDimension(1000,1000);//这里面是原始的大小，需要重新计算可以修改本行  
//        Log.d("will", "onMeasure: "+height);
    }

    private void initAttribute() {
        left = top = 0;
        rect = new Rect(left, top, left + 300, top + 400);
        rectPaint = new Paint();
        rectPaint.setAlpha(255);
        rectPaint.setStrokeWidth(5);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setColor(Color.RED);
    }

    public void setRect(int left, int top) {
        rect.left = left;
        rect.top = top;
        rect.right = rect.left + 300;
        rect.bottom = rect.top + 300;
    }

    public void letDraw() {
        this.draw(canvas);
        Log.i("Will", "MyView-letDraw: //");
    }

    int width,height;

}
