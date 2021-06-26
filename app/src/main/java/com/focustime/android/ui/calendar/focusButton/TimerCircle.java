package com.focustime.android.ui.calendar.focusButton;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.focustime.android.R;

import java.util.Random;

public class TimerCircle extends View {
    // ring brush 圆环画笔
    private Paint mPaint;
    // circle brush 圆画笔
    private Paint nPaint;

    // circle color 圆的颜色
    private int circle_color;

    // ring color 圆环颜色
    private int ring_color;

    // The size of the ring corresponds to the width of the brush 圆环大小对应着画笔的宽度
    private int ring_width;

    // Specify the width and length 指定控件宽度和长度
    private int width;
    private int height;

    // font color 字体颜色
    private int text_color;
    // font size 字体大小
    private int text_size;
    // path color 路径颜色
    private int path_color;
    // radius
    private int radius;

    // Specify the current value of the animation,
    // for example, specify the animation from 0 to 10000.
    // 指定动画的当前值，比如指定动画从0-10000。
    private int current_value;

    // When x seconds are reached, the corresponding arc arc is x/ * 360.
    // f x can be derived from currentValue
    // currentValue/1000 for seconds
    // 进行到x秒时，对应的圆弧弧度为 x/ * 360.f x可以currentValue得出 currentValue/1000代表秒
    private float  angle_value;

    // With the valueAnimator we can get the currentValue
    // 通过valueAnimator我们可以获得currentValue
    private ValueAnimator animator;

    // indicates the duration of the valueAnimator,
    // set here to the same as the maximum countdown time
    // 表示valueAnimator的持续时间，这里设置为和最大倒计时时间相同
    private float duration;

    // Maximum countdown time, if 1 minute timing, here there are 600000ms,
    // the unit is ms
    // 最大倒计时时间，如果1分钟计时，这里就有600000ms，单位是ms
    private int maxTime=500000;

    // The current time is the time remaining in the countdown and needs to be displayed in the circle
    // 当前的时间是指倒计时剩余时间，需要显示在圆中
    private int currentTime;

    private onFinishListener finishListenter;

    private String text;

    public TimerCircle(Context context) {
        this(context,null);
    }

    public TimerCircle(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TimerCircle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Get Properties
        // 获取属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TimerCircle);
        circle_color = typedArray.getColor(R.styleable.TimerCircle_circleColor, Color.BLUE);
        ring_color = typedArray.getInteger(R.styleable.TimerCircle_ringColor, 0);
        ring_width = (int) typedArray.getDimension(R.styleable.TimerCircle_width, 20);
        text_color = typedArray.getColor(R.styleable.TimerCircle_path, Color.RED);
        text_size = (int) typedArray.getDimension(R.styleable.TimerCircle_textSize,10);
        path_color = typedArray.getColor(R.styleable.TimerCircle_path, Color.RED);
        typedArray.recycle();
        InitPaint();

    }

    private void InitPaint() {
        mPaint = new Paint();
        mPaint.setColor(ring_color);
        mPaint.setAntiAlias(true);
        nPaint = new Paint();
        nPaint.setAntiAlias(true);
        nPaint.setColor(circle_color);

        text = getRandomWords();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthPixels = this.getResources().getDisplayMetrics().widthPixels;
        int heightPixels = this.getResources().getDisplayMetrics().heightPixels;

        // Measurement, the purpose is to finalize the radius of the circle based on the specified width and height
        // the width and height of the screen, the smallest of the four is the radius
        // 测量，目的是为了根据指定的宽高和屏幕的宽高最终确定圆的半径，四个中最小的即为半径
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int minWidth = Math.min(width, widthPixels);
        int minHeight = Math.min(height, heightPixels);

        setMeasuredDimension(Math.min(minHeight, minWidth), Math.min(minHeight, minWidth));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int in_radius = (this.getWidth()-2*ring_width)/2;

        int center = this.getWidth()-in_radius-ring_width;

        drawInner(canvas, in_radius,center);

        drawText(canvas,center);
    }

    private void drawInner(Canvas canvas, int radius, int center) {
        // Draw the inner circle first
        // 先画出内圆
        canvas.drawCircle(center, center, radius, nPaint);

        // Draw a circle, set it as a hollow circle,
        // specify the radius as the radius of the inner circle,
        // and the width of the brush is the width of the circle
        // 画圆环，设置为空心圆，指定半径为内圆的半径，画笔的宽度就是圆环的宽度
        mPaint.setStyle(Paint.Style.STROKE);//26.设置空心圆
        mPaint.setStrokeWidth(ring_width);//27.画笔宽度即圆环宽度
        mPaint.setColor(ring_color);//28. 圆环的颜色
        canvas.drawCircle(center, center, radius, mPaint);


        // Draws a circle based on an outer rectangle
        // 内圆的外接矩形，有什么作用？绘制圆弧时根据外接矩形绘制
        RectF rectF = new RectF(center-radius,center-radius,center+radius,center+radius);

        // Calculate the radian, by passing the current value of currentValue's worth to
        // 计算弧度，通过当前的currentValue的值得到
        angle_value = current_value * 360.0f / maxTime * 1.0f;

        // Set the shadow size and color
        // 设置阴影大小和颜色
        //mPaint.setShadowLayer(10, 10, 10, Color.BLUE);

        //  Specify the line cap style,
        //  which can be understood as a straight line with width at both ends with curvature
        // 指定线帽样式，可以理解为一条有宽度的直线的两端是带有弧度的
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        // The color of the arc
        // 圆弧的颜色
        mPaint.setColor(path_color);

        // Drawing arcs
        // 绘制圆弧
        canvas.drawArc(rectF, -90,angle_value , false, mPaint);
    }

    public void setDuration(int duration, final int maxTime, int currentTime) {
        this.maxTime=maxTime;

        this.duration=duration;

        animator = ValueAnimator.ofInt(currentTime, maxTime);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // Get the progress at this point
                // 获取此时的进度
                current_value = (int) animation.getAnimatedValue();
                if(current_value==maxTime){
                    finishListenter.onFinish();
                }
                // The invalidate() method will automatically call the View's onDraw() method.
                // invalidate()方法系统会自动调用 View的onDraw()方法。
                invalidate();
            }
        });
        // Linear interpolator, uniform variation
        // 线性插值器，匀速变化
        animator.setInterpolator(new LinearInterpolator());

        animator.setDuration(duration);
        animator.start();
    }

    private void drawText(Canvas canvas,int center) {
        mPaint.setTextAlign(Paint.Align.CENTER);

        mPaint.setColor(text_color);
        mPaint.setTextSize(text_size);
        mPaint.setStrokeWidth(0);//clear the width of the brush
        mPaint.clearShadowLayer();//clear shadow

        Rect bounds = new Rect();

        mPaint.getTextBounds(text, 0, text.length(), bounds);

        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();

        int baseline = center - (fontMetrics.bottom+fontMetrics.top)/2;

        canvas.drawText(text, center, baseline, mPaint);

    }
//    private void drawText(Canvas canvas,int center) {
//        //41.计算当前的剩余的时间,单位s
//        currentTime=(maxTime-current_value)/1000;
//
//        //42.显示的倒计时字符串
//        String Text=null;
//
//        if(currentTime<10){
//            Text="00:0"+currentTime;
//        }else if(currentTime>=10&&currentTime<=60){
//            Text="00:"+currentTime;
//        }else if(currentTime>60&&currentTime<600){
//            int min = currentTime/60;
//            int sen = currentTime%60;
//            if (sen<10) {
//                Text="0"+min+":0"+sen;
//            }else{
//                Text="0"+min+":"+sen;
//            }
//
//        }else{
//            int min = currentTime/60;
//            int sen = currentTime%60;
//            if (sen<10) {
//                Text=min+":0"+sen;
//            }else{
//                Text=min+":"+sen;
//            }
//        }
//
//        // 43.设置文字居中，以左下角为基准的（x，y）就是这里的center baseline。具体的关于drawText需要查看https://blog.csdn.net/harvic880925/article/details/50423762/
//        mPaint.setTextAlign(Paint.Align.CENTER);
//        // 44.设置文字颜色
//        mPaint.setColor(text_color);
//        mPaint.setTextSize(text_size);
//        mPaint.setStrokeWidth(0);//清除画笔宽度
//        mPaint.clearShadowLayer();//清除阴影
//        // 45.文字边框
//        Rect bounds = new Rect();
//        // 46.获得绘制文字的边界矩形
//        mPaint.getTextBounds(Text, 0, Text.length(), bounds);
//        // 47.获取绘制Text时的四条线
//        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
//        // 48.计算文字的基线
//        int baseline = center - (fontMetrics.bottom+fontMetrics.top)/2;
//        // 49.绘制表示进度的文字
//        canvas.drawText(Text, center, baseline, mPaint);
//    }

    public interface onFinishListener{
        void onFinish();
    }

    public void setFinishListenter(onFinishListener listenter){
        this.finishListenter = listenter;
    }

    public String getRandomWords() {
        // an interesting little function
        Random random = new Random();
        int temp = random.nextInt(6);

        if (temp <= 1)
            return "Focuesd";
        else if (temp <= 2)
            return "Effort";
        else if (temp <= 3)
            return "Strive";
        else if (temp <= 4)
            return "Happy:)";
        else return "Best";
    }
}