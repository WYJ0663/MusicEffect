package com.example.musiceffect.drawble;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.SystemClock;
import com.example.musiceffect.utils.SystemUtil;

import java.util.Random;

public class Circle {

    private Path path;
    private float cx;
    private float cy;

    private int r;

    //旋转
    private float degrees;

    //透明度
    private int alpha = 150;

    private long startTime = -1;

    private Context mContext;

    private float pointR;

    private int w;
    private int h;

    public Circle(Context context, Random random, int w, int h, int r) {
        mContext = context;
        this.w = w;
        this.h = h;
        this.r = r;

        cx = w / 2;
        cy = h / 2;

        pointR = SystemUtil.dip2px(mContext, random.nextFloat() * 5 + 1);
        degrees = random.nextFloat() * 360;

    }

    public void draw(Canvas canvas, Paint paint) {
        long curTime = SystemClock.elapsedRealtime();
        if (startTime <= 0) {
            startTime = curTime;
        }
        long deltaTime = curTime - startTime;
        r = r + (SystemUtil.dip2px(mContext, deltaTime * 1.0f / 1000));
        degrees = degrees + (SystemUtil.dip2px(mContext, deltaTime * 0.3f / 1000));
        if (alpha > 0) {
            alpha = alpha - (SystemUtil.dip2px(mContext, deltaTime * 0.3f / 1000));
        }
        if (alpha < 0) {
            alpha = 0;
        }
        canvas.save();
        canvas.rotate(degrees, w / 2, h / 2);
        paint.setAlpha(alpha);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cx, cy, r, paint);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(cx+r, cy, pointR, paint);
        canvas.restore();
    }

    public boolean isOut() {
        if (alpha <= 0) {
            return true;
        }
        return false;
    }

}
