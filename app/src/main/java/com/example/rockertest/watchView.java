package com.example.rockertest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 表盘刻度值
 */
public class watchView extends View {
    private int mWidth;//控件宽度

    private int mHeight;//控件高度

    private int mBigRadius = 100;//大半径

    private int mScale = 20;//刻度长度

    private int mSmallRadius;//里面小圆的半径

    private Paint mPaint;//画笔

    private int index = 50;//刻度数

    private double angle = 360d;//角度

    private double smalAngle;//每个小的刻度角度

    private int indexSmal = 0;//标记刻度

    private boolean boolStart = true;

    public watchView(Context context) {
        this(context, null);
        init();
    }

    public watchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        init();
    }

    public watchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        mSmallRadius = mBigRadius - mScale;

        mPaint = new Paint();

        mPaint.setStrokeWidth(3);

        mPaint.setColor(Color.RED);

        mPaint.setAntiAlias(true);

        smalAngle = angle / index;

        indexSmal = index;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widhSize = getResolveSize(200, widthMeasureSpec);
        int heightSize = getResolveSize(200, heightMeasureSpec);

        if (widhSize > heightSize) {
            heightSize = widhSize;
        }

        mWidth = widhSize;
        mHeight = heightSize;
        setMeasuredDimension(widhSize, heightSize);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        int centerWidth = mWidth / 2;
        int centerheight = mHeight / 2;

        for (int i = 0; i < index; i++) {
            float cosAngle = (float) Math.cos(Math.toRadians(smalAngle * i));
            float sinAngle = (float) Math.sin(Math.toRadians(smalAngle * i));

            if (indexSmal == i) {
                mPaint.setColor(Color.BLACK);
            } else {
                mPaint.setColor(Color.RED);
            }

            canvas.drawLine(centerWidth + mSmallRadius * cosAngle, centerheight - mSmallRadius * sinAngle,
                    centerWidth + mBigRadius * cosAngle, centerheight - mBigRadius * sinAngle, mPaint);

        }

    }


    private int getResolveSize(int size, int measureSec) {
        int result = size;

        int meSize = MeasureSpec.getSize(measureSec);
        int meMode = MeasureSpec.getMode(measureSec);
        switch (meMode) {
            case MeasureSpec.AT_MOST:
                result = Math.min(size, meSize);
                break;
            case MeasureSpec.EXACTLY:
                result = meSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                result = size;
                break;
        }


        return result;
    }

    public void startThread() {
        boolStart = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (boolStart) {
                    try {
                        Thread.sleep(50);

                        if(indexSmal == 0){
                            indexSmal = index;
                        }

                        indexSmal --;
                        handler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            invalidate();
        }
    };

    public void endThread(){
        boolStart = false;
    }
}


