package com.dl.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.dl.d2ia.R;

/**
 * 自定义弧形进度条
 */

public class ProgerssCircle extends View {

    private final static String TAG = ProgerssCircle.class.getSimpleName();
    private Paint mPaint;
    private RectF oval;
    private int mStartAngle ;
    private int mSweepAngle ;
    private int mColor = Color.GRAY;
    private int mStrokWidth = 30;


    public ProgerssCircle(Context context) {
        super(context);
        init();
    }

    public ProgerssCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ProgerssCircle);//TypedArray是一个数组容器
        mStartAngle = a.getInteger(R.styleable.ProgerssCircle_startAngle,0);
        mSweepAngle = a.getInteger(R.styleable.ProgerssCircle_sweepAngle,0);
        init();
    }

    public ProgerssCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 设置进度起始
     * @param value 0~360度
     */
    public void setStartAngle(int value){
        mStartAngle = value;
        invalidate();
    }

    /**
     * 设置进度
     * @param value  0~360度
     */
    public void setSweepAngle(int value){
        mSweepAngle = value;
        invalidate();
    }

    /**
     * 设置弧颜色
     * @param value 0xaarrggbb
     */
    public void setColor(int value){
        mColor = value;
        invalidate();
    }

    /**
     * 设置弧宽度
     * @param value
     */
    public void setStrokeWidth(int value){
        mStrokWidth = value;
        invalidate();
    }




    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        oval=new RectF();
    }


//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        Log.e(TAG, "onLayout");
//    }


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        switch (widthMode) {
//            case MeasureSpec.EXACTLY:
//
//                break;
//            case MeasureSpec.AT_MOST:
//
//                break;
//            case MeasureSpec.UNSPECIFIED:
//
//                break;
//        }
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // FILL填充, STROKE描边,FILL_AND_STROKE填充和描边
        mPaint.setStyle(Paint.Style.STROKE);//描边
        mPaint.setStrokeWidth(mStrokWidth);//边的宽度
        int with = getWidth();
        int height = getHeight();
        Log.e(TAG, "onDraw---->" + with + "*" + height);
        float radius =  with < height ?  with / 4 : height / 4;

        oval.set(with / 2 - radius, height / 2 - radius, with / 2
                + radius, height / 2 + radius);//用于定义的圆弧的形状和大小的界限
        mPaint.setColor( 0xffe6e6e6);
        canvas.drawArc(oval, 0, 360, false, mPaint);//画个背景
        mPaint.setColor(mColor);
        canvas.drawArc(oval, mStartAngle, mSweepAngle, false, mPaint);  //根据进度画圆弧
    }
}
