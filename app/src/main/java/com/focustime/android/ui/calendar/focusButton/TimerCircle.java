package com.focustime.android.ui.calendar.focusButton;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.focustime.android.R;

import java.util.Random;

/**
 * A timer circle with animation
 */
public class TimerCircle extends View {
    // ring brush
    private Paint mPaint;
    // circle brush
    private Paint nPaint;

    // circle color
    private int circle_color;

    // ring color
    private int ring_color;

    // The size of the ring corresponds to the width of the brush 圆环大小对应着画笔的宽度
    private int ring_width;

    // Specify the width and length
    private int width;
    private int height;

    // font color
    private int text_color;
    // font size
    private int text_size;
    // path color
    private int path_color;
    // radius
    private int radius;

    // Specify the current value of the animation,
    // for example, specify the animation from 0 to 10000.
    private int current_value;

    // When x seconds are reached, the corresponding arc arc is x/ * 360.
    // f x can be derived from currentValue
    // currentValue/1000 for seconds
    private float  angle_value;

    // With the valueAnimator we can get the currentValue
    private ValueAnimator animator;

    // indicates the duration of the valueAnimator,
    // set here to the same as the maximum countdown time
    private float duration;

    // Maximum countdown time, if 1 minute timing, here there are 600000ms,
    // the unit is ms
    private int maxTime=500000;

    // The current time is the time remaining in the countdown and needs to be displayed in the circle
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
        canvas.drawCircle(center, center, radius, nPaint);

        // Draw a circle, set it as a hollow circle,
        // specify the radius as the radius of the inner circle,
        // and the width of the brush is the width of the circle
        mPaint.setStyle(Paint.Style.STROKE);//26.设置空心圆
        mPaint.setStrokeWidth(ring_width);//27.画笔宽度即圆环宽度
        mPaint.setColor(ring_color);//28. 圆环的颜色
        canvas.drawCircle(center, center, radius, mPaint);


        // Draws a circle based on an outer rectangle
        RectF rectF = new RectF(center-radius,center-radius,center+radius,center+radius);

        // Calculate the radian, by passing the current value of currentValue's worth to
        angle_value = current_value * 360.0f / maxTime * 1.0f;

        // Set the shadow size and color
        //mPaint.setShadowLayer(10, 10, 10, Color.BLUE);

        //  Specify the line cap style,
        //  which can be understood as a straight line with width at both ends with curvature
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        // The color of the arc
        mPaint.setColor(path_color);

        // Drawing arcs
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
                current_value = (int) animation.getAnimatedValue();
                if(current_value==maxTime){
                    finishListenter.onFinish();
                }
                // The invalidate() method will automatically call the View's onDraw() method.
                invalidate();
            }
        });
        // Linear interpolator, uniform variation
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

    public interface onFinishListener{
        void onFinish();
    }

    public void setFinishListenter(onFinishListener listenter){
        this.finishListenter = listenter;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void reset() {
        this.resetPivot();
    }

    /**
     * Returns some string for display in timeCircle
     * @return
     */
    public String getRandomWords() {
        // an interesting little function
        /*Random random = new Random();
        int temp = random.nextInt(6);

        if (temp <= 1)
            return "Focused";
        else if (temp <= 2)
            return "Effort";
        else if (temp <= 3)
            return "Strive";
        else if (temp <= 4)
            return "Happy:)";
        else return "Best";*/
        return "Focus Time";
    }

}