package com.example.customfloatingbutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

/**
 * Created by ASUS on 2017/9/7.
 */

public class FloatButton2 extends ViewGroup {

    private static final String TAG = "FloatButton2";

    private float width;
    private float height;
    //圆半径
    private float center;
    //矩形右边x坐标
    private float x;

    //矩形左边x坐标
    private float left_x;

    private Paint mPaint;

    private final static int IS_SLIDE_DECREASE = 0;  //递减状态
    private static final int IS_SLIDE_INCREASE = 1;  //递增状态

    private boolean isIncrease = true;

    private FoldListener foldListener;  //折叠监听
    private OnClickListener onClickListener;  //点击监听

    private FloatButton2 mFloatButton;

    private float widWidth;

//    private View child;     //textview
//    private int tWidth;     //文本宽度
//    private int tHeight;    //文本高度
//    private float tX;       //文本宽度变化值
//    private float tX_x;     //文本拉伸变化比

    private int bacColor;
    private int innerCircleColor;
    private String text;

    private float y = 20;    //圆环宽度
    private float y_x;      //圆环宽度比

    public FloatButton2(Context context) {
        super(context);
    }

    public FloatButton2(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mFloatButton = this;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray type = context.obtainStyledAttributes(attrs, R.styleable.FloatButton);
        innerCircleColor = type.getColor(R.styleable.FloatButton_inner_circle_color, Color.YELLOW);
        bacColor = type.getColor(R.styleable.FloatButton_inner_circle_color, Color.BLUE);
        text = type.getString(R.styleable.FloatButton_text);

//        TextView textView = new TextView(context);
//        textView.setText(text);
//        textView.setTextSize(16);
//        addView(textView);
    }

    public FloatButton2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (width == 0) {
            setWidthAdnHeight(widthMeasureSpec, heightMeasureSpec);
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = MeasureSpec.getSize(heightMeasureSpec);

//            child = getChildAt(0);
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
//            tWidth = child.getMeasuredWidth();
//            tHeight = child.getMeasuredHeight();

            center = height / 2;
            left_x = center;
            x = width - center;
            y_x = y * left_x;
            Log.d(TAG, "y_x: " + y_x);

            //初始化文本范围右下角 x坐标  +10设置间距
//            tX = tWidth + center * 2 + 10;
            //初始文本伸缩比
//            tX_x = (tWidth + 10) / (width - center * 2);

        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void setWidthAdnHeight(int widthMeasureSpec, int heightMeasureSpec) {
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
//        if (wMode == MeasureSpec.AT_MOST) {
//            if (tWidth < 100){
//                width = tWidth + 150;
//            }else if(tWidth > 100 && tWidth *1.5f<widWidth -100){
//                width = tWidth*1.5f;
//            }else{
//                width = widWidth-100;
//            }
//        } else {
//            width = MeasureSpec.getSize(widthMeasureSpec);
//        }
//        if (hMode == MeasureSpec.AT_MOST) {
//            height = tHeight*2;
//        } else {
//            height = MeasureSpec.getSize(heightMeasureSpec);
//            if (height<tHeight*2){
//                height = tHeight*2;
//            }
//        }
    }

    //ViewGroup 绘制方法
    @Override
    protected void dispatchDraw(Canvas canvas) {
        mPaint.setColor(bacColor);
        //左边圆
        canvas.drawCircle(left_x, center, center, mPaint);
        //矩形
        RectF rectF = new RectF(left_x, 0, x, height);
        mPaint.setColor(bacColor);
        canvas.drawRect(rectF, mPaint);
        //右边圆
        mPaint.setColor(bacColor);
        canvas.drawCircle(x, center, center, mPaint);

        //小圆
        mPaint.setColor(innerCircleColor);
        canvas.drawCircle(x, center, center - y, mPaint);

        canvas.save();
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
//        child.layout((int)center * 2 + 5, (int)center -tHeight / 2, (int) tX,
//                (int)center + tHeight / 2);
    }



    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case IS_SLIDE_DECREASE:
                    left_x += 30;
                    if (left_x <= width - center) {
                        y = y_x / left_x;
                        Log.d(TAG, "y: " + y);
//                        tX = tX_x * left_x;
                        mHandler.sendEmptyMessageDelayed(IS_SLIDE_DECREASE , 1);
                    }else {
                        left_x = x;
                        y = 0;
//                        tX = center * 2 + 5;
                        setEnabled(true);
                        //折叠回调
                        if (foldListener != null) {
                            foldListener.onFold(isIncrease, mFloatButton);
                        }
                    }
                    break;
                case IS_SLIDE_INCREASE:
                    left_x -= 30;
                    if (left_x > center) {
                        y = y_x / left_x;
//                        tX = tX_x * left_x;
                        mHandler.sendEmptyMessageDelayed(IS_SLIDE_INCREASE , 1);
                    } else {
                        y = 20;
                        left_x = center;
//                        tX = tWidth + center * 2 + 10;
                        setEnabled(true);
                        //折叠回调
                        if (foldListener != null) {
                            foldListener.onFold(isIncrease, mFloatButton);
                        }
                    }
                    break;
                default:
                    break;
            }
            requestLayout();
            invalidate();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }




    private boolean canClick;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                canClick = judgeCanClick(event.getX(), event.getY());
                if (!canClick)
                    return super.onTouchEvent(event);
                break;
            case MotionEvent.ACTION_UP:
                if (canClick && onClickListener != null && isEnabled()) {
                    onClickListener.onClick(mFloatButton);
                }
                break;
            default:
                break;
        }
        return true;
    }

    private boolean judgeCanClick(float x, float y) {
        boolean canClick;
        if (isIncrease) {   //伸展状态
            if (left_x < center * 2 && y < height) {
                canClick = true;
            }else {
                canClick = false;
            }
        }else {
            if (x < width && y < center * 2) {  //在圆内
                canClick = true;
            }else {
                canClick = false;
            }
        }

        return canClick;
    }

    //递减状态
    private void startDecrease() {
        setEnabled(false);  //滑动时不给点击事件
        isIncrease = false; //记录递增还是递减状态
        mHandler.sendEmptyMessageDelayed(IS_SLIDE_DECREASE, 40);
    }

    //递增状态
    private void startIncrease() {
        setEnabled(false);  //滑动时不给点击事件
        isIncrease = true;  //记录递增还是递减状态
        mHandler.sendEmptyMessageDelayed(IS_SLIDE_INCREASE, 40);
    }

    //外部调用
    public void startScroll() {
        if (isIncrease) {    //判断是否是递增状态
            startDecrease();
        } else {
            startIncrease();
        }
    }

    public interface FoldListener {
        void onFold(boolean isIncrease, FloatButton2 fb);
    }

    public void setFoldListener(FoldListener foldListener) {
        this.foldListener = foldListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(FloatButton2 sfb);
    }


}
