package com.example.rockertest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class RockerView extends View {
    Paint mAreaPaint;
    Paint mUserPaint;

    Point mCenterPoint;
    Point mMovePoint;
    Point myPoint;

    public RockerView(Context context) {
        super(context);
        init();
    }

    public RockerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RockerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public RockerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        //绘制区域
        mAreaPaint = new Paint();
        mAreaPaint.setAntiAlias(true);

        //自定义
        mUserPaint = new Paint();
        mUserPaint.setAntiAlias(true);

        //中心点
        mCenterPoint = new Point();

        //自定义
        myPoint = new Point();
    }

    //起始角
    private float startAngle;
    //绘制角度
    private float angle;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int measureWidth = getWidth();
        int measureHeight = getHeight();

        mCenterPoint.set(measureWidth/2,measureHeight/2);

        rectRadius = measureWidth/2;

        mAreaPaint.setStyle(Paint.Style.STROKE);
        mAreaPaint.setStrokeWidth(20);
        mAreaPaint.setColor(Color.RED);
        RectF rectF = new RectF(0,0,measureWidth,measureHeight);
        canvas.drawRect(rectF,mAreaPaint);

        mUserPaint.setStyle(Paint.Style.STROKE);
        mUserPaint.setStrokeWidth(20);
        mUserPaint.setColor(Color.YELLOW);
        RectF rectF1 = new RectF(measureWidth-drawRadius,
                measureHeight-drawRadius,
                measureWidth+drawRadius,
                measureHeight+drawRadius);
        //顺时针
//        if (angle>=360+startAngle){
//            canvas.drawArc(rectF,0,360,false,mUserPaint);
//        }else {
            float sweepAngle = angle - startAngle;
            canvas.drawArc(rectF1, startAngle, sweepAngle, false, mUserPaint);
//        }

        canvas.save();

        canvas.restore();
    }

    private float drawRadius;
    private float rectRadius;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX =event.getX();
                float moveY = event.getY();
                //半径
                drawRadius = countRadius(event.getX(0),event.getY(0));

                startAngle = getAngle(mCenterPoint,new Point((int) event.getX(0),(int) event.getY(0)));
                angle = getAngle(mCenterPoint,new Point((int) moveX,(int)moveY));

                mMovePoint = getPoint(new Point((int) moveX,(int)moveY));
                moveTo(mMovePoint.x,mMovePoint.y);
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_CANCEL:
//                callBackFinish();
                float upX = event.getX();
                float upY = event.getY();
                moveTo(mMovePoint.x,mMovePoint.y);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    private Point getPoint(Point touchPoint) {
        //区域判断
        return touchPoint;
    }


    //移动位置
    private void moveTo(int x, int y) {
        mMovePoint.set(x,y);
        invalidate();
    }

    //半径计算
    private float countRadius(float x, float y) {
        float mX = (float) (mCenterPoint.x - x );
        // 两点在Y轴距离
        float mY = (float) (mCenterPoint.y - y );
        // 两点距离
        float mXY = (float) Math.sqrt((double) (mX * mX + mY * mY));

        // must mXY <= rectRadius

        return mXY;
    }

    /**
     *获取绘制终点坐标点
     * @param mCenterPoint 中心点
     * @param touchPoint 触摸点
     * @return 绘制终点
     */
    private float getAngle(Point mCenterPoint,Point touchPoint) {
        float lenX = (float) (mCenterPoint.x - touchPoint.x );
        // 两点在Y轴距离
        float lenY = (float) (mCenterPoint.y - touchPoint.y );
        // 两点距离
        float lenXY = (float) Math.sqrt((double) (lenX * lenX + lenY * lenY));
        // 计算弧度
        double radian = Math.acos(lenX / lenXY) * (touchPoint.y < mCenterPoint.y ? 1 : -1);

        return  (float) radian2Angle(radian);
    }

    private double radian2Angle(double radian) {
        double tmp = Math.round(radian *180/ Math.PI);
        return tmp >= 0 ? tmp : 360 + tmp;
    }

}
