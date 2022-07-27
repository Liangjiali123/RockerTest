package com.example.rockertest;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RockerView extends View {
    Paint mAreaPaint;
    Paint mUserPaint;

    Point mCenterPoint;
    Point mMovePoint;
    Point myPoint;
    private float downX;
    private float downY;
    private Object[] s;
    private float angle1;

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
     private float startAngle = 0;
    //终点角度
     private float angle = 0;
     //覆盖角度
     private float sweepAngle = 0;

    ArrayList<Float> angles= new ArrayList<Float>();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerWidth = getWidth()/2;
        int centerHeight = getHeight()/2;

        mCenterPoint.set(centerWidth,centerHeight);

        rectRadius = centerWidth;

        mAreaPaint.setStyle(Paint.Style.STROKE);
        mAreaPaint.setStrokeWidth(10);
        mAreaPaint.setColor(Color.RED);
        RectF rectF = new RectF(0,0,getWidth(),getHeight());
        canvas.drawRect(rectF,mAreaPaint);

        mUserPaint.setStyle(Paint.Style.STROKE);
        mUserPaint.setStrokeWidth(20);
        mUserPaint.setColor(Color.YELLOW);
        RectF rectF1 = new RectF(centerWidth-drawRadius,
                centerHeight-drawRadius,
                centerWidth+drawRadius,
                centerHeight+drawRadius);

        if (startAngle<angle1){
            //顺时针
            if (angle < startAngle) {
                this.angle = angle + 360;
            }
        }else if(startAngle>angle1){
            //逆时针
            if (angle>startAngle){
                this.startAngle = startAngle+360;
            }
        }
        sweepAngle = angle - startAngle;

        canvas.drawArc(rectF1, startAngle, sweepAngle, false, mUserPaint);

        /**
         * 顺时针判断: angles.get(i)<angles.get(i+1)
         *        当终点角度angle经过三点钟方向 则 angle+360
         *        扫描角度 sweepAngle = angle - startAngle;
         * 逆时针判断: angles.get(i)<angles.get(i+1)
         *        当终点角度angle经过三点钟方向 则 startAngle+360
         *        扫描角度 sweepAngle = angle - startAngle;
         */


        canvas.save();

        canvas.restore();
    }

    private float drawRadius;
    private float rectRadius;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                //半径
                drawRadius = countRadius(downX, downY);
                startAngle = getAngle(mCenterPoint,new Point((int) downX,(int) downY));
                Log.v("RockerView","startAngle = "+startAngle);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float moveX =event.getX();
                float moveY = event.getY();

                angle = getAngle(mCenterPoint,new Point((int)moveX,(int) moveY));
                angles.add(angle);
                s = angles.toArray();

                Log.v("RockerView","angle = "+angle);

                Log.v("RockerView","sweepAngle = "+ sweepAngle);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                float upY = event.getY();

                angle1 = (float) s[1];
                Log.v("RockerView","angle1 = "+ angle1);
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
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
     * @param touchPoint 触摸点
     * @return 绘制终点
     */
    private float getAngle(Point mCenterPoint,Point touchPoint) {
        float lenX = (float) (touchPoint.x - mCenterPoint.x );
        // 两点在Y轴距离
        float lenY = (float) (touchPoint.y - mCenterPoint.y );
        // 两点距离
        float lenXY = (float) Math.sqrt((double) (lenX * lenX + lenY * lenY));
        // 计算弧度
        double radian = Math.acos(lenX / lenXY)* (touchPoint.y < mCenterPoint.y ? -1 : 1);

        return  (float) radian2Angle(radian);
    }

    private double radian2Angle(double radian) {
        double tmp = Math.round(radian *180/ Math.PI);
        return tmp >= 0 ? tmp : 360 + tmp;
    }

}
