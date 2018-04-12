package com.blk.common;

/**
 * Created by asus on 2018/2/4.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class PinChart extends View {

    static Canvas c;
    private Paint[] mPaints;
    private RectF mBigOval;
    float[] mSweep = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private int preWidth;
    private mAnimation ani;
    private int flag;
    private int centerX;
    private int centerY;
    int valueX;
    int valueY;

    public static float[] humidity = { 110, 60, 50, 50, 40, 30, 10, 10 };
    private String str[] = { "数据24%", "数据19%", "数据21%", "其他18%", "数据3%",
            "数据3%", "数据4%", "数据6%" };

    private final String color[] = { "#2cbae7", "#ffa500", "#ff5b3b",
            "#9fa0a4", "#6a71e5", "#f83f5d", "#64a300", "#64ef85" };

    public PinChart(Context context) {
        super(context);
        initView();
    }

    public PinChart(Context context, AttributeSet atr) {
        super(context, atr);
        initView();
    }

    private void initView() {
        ani = new mAnimation();
        ani.setDuration(2000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);// 设置背景颜色(透明)
        mPaints = new Paint[humidity.length];

        for (int i = 0; i < humidity.length; i++) {
            mPaints[i] = new Paint();
            mPaints[i].setAntiAlias(true);
            mPaints[i].setStyle(Paint.Style.FILL);
            mPaints[i].setColor(Color.parseColor(color[i]));
        }
        int cicleWidth = getWidth() - dp2px(60);
        centerX = getWidth() / 2;
        centerY = dp2px(10) + cicleWidth / 2;
        preWidth = (getWidth() - dp2px(40)) / 4;
        int half = getWidth() / 2;

        mBigOval = new RectF();// 饼图的四周边界
        mBigOval.top = dp2px(10);
        mBigOval.left = half - cicleWidth / 2;
        mBigOval.bottom = dp2px(10) + cicleWidth;
        mBigOval.right = half + cicleWidth / 2;

        float start = -180;
        Rect bounds = new Rect();
        for (int i = 0; i < humidity.length; i++) {
            canvas.drawArc(mBigOval, start, mSweep[i], true, mPaints[i]);
            if (humidity[i] > 45) {
                mPaints[i].setXfermode(new PorterDuffXfermode(
                        PorterDuff.Mode.SRC_OVER));
                mPaints[i].setAntiAlias(true);
                mPaints[i].setColor(Color.WHITE);
                mPaints[i].getTextBounds(str[i], 0, str[i].length(), bounds);
                mPaints[i].setTextSize(sp2px(15));
                measureText(start + 180, humidity[i], cicleWidth / 3, i);
                canvas.drawText(str[i], valueX - mPaints[i].measureText(str[i])
                        / 2, valueY + bounds.height() / 2, mPaints[i]);
            }
            start += humidity[i];
            int j = 1;
            int k;
            if (i < 4) {
                j = 0;
                k = i;
            } else {
                j = 1;
                k = i - 4;
            }
            mPaints[i] = new Paint();
            mPaints[i].setAntiAlias(true);
            mPaints[i].setStyle(Paint.Style.FILL);
            mPaints[i].setColor(Color.parseColor(color[i]));
            canvas.drawRect(new RectF(dp2px(20) + preWidth * k, cicleWidth
                    + dp2px(j * 30 + 20), dp2px(20) + preWidth * (k + 1),
                    cicleWidth + dp2px(50 + j * 30)), mPaints[i]);
            mPaints[i].setXfermode(new PorterDuffXfermode(
                    PorterDuff.Mode.SRC_OVER));
            mPaints[i].setAntiAlias(true);
            mPaints[i].setColor(Color.WHITE);
            mPaints[i].getTextBounds(str[i], 0, str[i].length(), bounds);
            mPaints[i].setTextSize(sp2px(15));
            canvas.drawText(str[i], dp2px(20) + preWidth * k + preWidth / 2
                    - mPaints[i].measureText(str[i]) / 2, cicleWidth
                    + dp2px(j * 30 + 20)
                    + (dp2px(30) / 2 + bounds.height() / 2), mPaints[i]);
        }
    }

    /**
     * 显示相应区域字开始的x,y坐标
     *
     * @param start
     * @param angle
     * @param radius
     * @param i
     */
    private void measureText(float start, float angle, int radius, int i) {
        float temp = start + (angle / 2);

        if (temp < 90) {
            valueX = (int) (centerX - Math.abs(radius
                    * Math.sin((temp / 180) * Math.PI)));
            valueY = (int) (centerY - Math.abs(radius
                    * Math.cos((temp / 180) * Math.PI)));
        } else if (temp > 90 && temp < 180) {
            temp = 180 - temp;
            valueX = centerX
                    + (int) Math
                    .abs((radius * Math.cos((temp / 180) * Math.PI)));
            valueY = centerY
                    - (int) Math
                    .abs((radius * Math.sin((temp / 180) * Math.PI)));
        } else if (temp > 180 && temp < 270) {
            temp = temp - 180;
            valueX = centerX
                    + (int) Math
                    .abs((radius * Math.cos((temp / 180) * Math.PI)));
            valueY = centerY
                    + (int) Math
                    .abs((radius * Math.sin((temp / 180) * Math.PI)));
        } else {
            temp = 360 - temp;
            valueX = centerX
                    - (int) Math
                    .abs((radius * Math.cos((temp / 180) * Math.PI)));
            valueY = centerY
                    + (int) Math
                    .abs((radius * Math.sin((temp / 180) * Math.PI)));
        }

    }

    private int sp2px(int value) {
        float v = getResources().getDisplayMetrics().scaledDensity;
        return (int) (value * v + 0.5f);
    }

    private int dp2px(int value) {
        float v = getResources().getDisplayMetrics().density;
        return (int) (value * v + 0.5f);
    }

    public void start(int flag) {
        startAnimation(ani);
        this.flag = flag;
    }

    class mAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime,
                                           Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            if (interpolatedTime < 1.0f && flag == 2) {
                for (int i = 0; i < humidity.length; i++) {
                    mSweep[i] = humidity[i] * interpolatedTime;
                }
            } else if (flag == 1) {
                for (int i = 0; i < humidity.length; i++) {
                    mSweep[i] = 0;
                }
            } else {
                for (int i = 0; i < humidity.length; i++) {
                    mSweep[i] = humidity[i];
                }
            }
            invalidate();
        }
    }

}