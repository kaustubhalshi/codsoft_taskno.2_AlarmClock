package com.example.alarmclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

public class CustomClockView extends View {
    private Paint circlePaint, hourPaint, minutePaint, secondPaint, markerPaint, centerPaint;
    private int width, height;
    private float radius;

    public CustomClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.parseColor("#3D405B"));
        circlePaint.setStyle(Paint.Style.FILL);

        hourPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        hourPaint.setColor(Color.parseColor("#FFA726"));
        hourPaint.setStrokeWidth(16f);
        hourPaint.setStrokeCap(Paint.Cap.ROUND);

        minutePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        minutePaint.setColor(Color.parseColor("#29B6F6"));
        minutePaint.setStrokeWidth(12f);
        minutePaint.setStrokeCap(Paint.Cap.ROUND);

        secondPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        secondPaint.setColor(Color.parseColor("#AB47BC"));
        secondPaint.setStrokeWidth(6f);
        secondPaint.setStrokeCap(Paint.Cap.ROUND);

        markerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        markerPaint.setColor(Color.parseColor("#B0B0B0"));
        markerPaint.setStrokeWidth(6f);

        centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        centerPaint.setColor(Color.WHITE);
        centerPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        radius = Math.min(width, height) / 2f - 40;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(width / 2f, height / 2f, radius, circlePaint);

        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) { // Only for major intervals
                float angle = (float) (i * Math.PI / 30);
                float startX = (float) (width / 2f + (radius - 20) * Math.cos(angle));
                float startY = (float) (height / 2f + (radius - 20) * Math.sin(angle));
                float endX = (float) (width / 2f + radius * Math.cos(angle));
                float endY = (float) (height / 2f + radius * Math.sin(angle));
                canvas.drawLine(startX, startY, endX, endY, markerPaint);
            }
        }

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);
        int milliseconds = calendar.get(Calendar.MILLISECOND);

        float hourAngle = (float) (Math.PI / 6 * hours + Math.PI / 360 * minutes);
        float minuteAngle = (float) (Math.PI / 30 * minutes + Math.PI / 1800 * seconds);
        float secondAngle = (float) (Math.PI / 30 * (seconds + milliseconds / 1000.0)); // Smooth second hand

        float hourX = (float) (width / 2f + (radius - 80) * Math.cos(hourAngle - Math.PI / 2));
        float hourY = (float) (height / 2f + (radius - 80) * Math.sin(hourAngle - Math.PI / 2));
        canvas.drawLine(width / 2f, height / 2f, hourX, hourY, hourPaint);

        float minuteX = (float) (width / 2f + (radius - 60) * Math.cos(minuteAngle - Math.PI / 2));
        float minuteY = (float) (height / 2f + (radius - 60) * Math.sin(minuteAngle - Math.PI / 2));
        canvas.drawLine(width / 2f, height / 2f, minuteX, minuteY, minutePaint);

        float secondX = (float) (width / 2f + (radius - 40) * Math.cos(secondAngle - Math.PI / 2));
        float secondY = (float) (height / 2f + (radius - 40) * Math.sin(secondAngle - Math.PI / 2));
        canvas.drawLine(width / 2f, height / 2f, secondX, secondY, secondPaint);

        canvas.drawCircle(width / 2f, height / 2f, 12f, centerPaint);

        postInvalidateOnAnimation();
    }
}
