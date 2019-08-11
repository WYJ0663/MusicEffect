package com.example.musiceffect.drawble;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.SystemClock;
import com.example.musiceffect.utils.SystemUtil;

import java.util.Random;

public class Triangle {

    private Path path;
    private float cx;
    private float cy;

    //平移
    private float tx;
    private float ty;
    private float txDirection;

    //缩放
    private float sx = 1;
    private float sy = 1;

    //旋转
    private float degrees;
    private float canvasDegrees;

    //透明度
    private int alpha = 255;

    private int w;
    private int h;

    private long startTime = -1;

    private Context mContext;

    public Triangle(Context context, Random random, int w, int h, int r) {
        mContext = context;
        this.w = w;
        this.h = h;
        tx = w / 2 + r * 2 / 3;
        ty = h / 2;
        sx = SystemUtil.dip2px(context, random.nextFloat() + 2.0f);
        sy = SystemUtil.dip2px(context, random.nextFloat() + 1.0f);

        degrees = random.nextFloat() * 360;
        canvasDegrees = random.nextFloat() * 360;

        createTriangle();
    }

    public void createTriangle() {
        path = new Path();
        path.moveTo(0, 0);
        path.lineTo(10, 0);
        path.lineTo(4, 8.6f);
        path.lineTo(0, 0);
        cx = 5;
        cy = 4.3f;
    }

    public void getMatrix(Matrix matrix) {
        matrix.reset();
        matrix.preTranslate(tx, ty);
        matrix.preRotate(degrees, cx, cy);
        matrix.preScale(sx, sy, cx, cy);
    }

    public void draw(Canvas canvas, Paint paint, Matrix matrix, Path p) {
        long curTime = SystemClock.elapsedRealtime();
        if (startTime <= 0) {
            startTime = curTime;
        }
        long deltaTime = curTime - startTime;
        tx = tx + (SystemUtil.dip2px(mContext, deltaTime * 2.0f / 1000));
        degrees = degrees + (SystemUtil.dip2px(mContext, deltaTime * 0.3f / 1000));
        if (alpha > 0) {
            alpha = alpha - (SystemUtil.dip2px(mContext, deltaTime * 1.0f / 1000));
        }
        if (alpha < 0) {
            alpha = 0;
        }
        canvas.save();
        canvas.rotate(canvasDegrees, w / 2, h / 2);
        p.reset();
        paint.setAlpha(alpha);
        p.addPath(path);
        getMatrix(matrix);
        p.transform(matrix);
        canvas.drawPath(p, paint);
        canvas.restore();
    }

    public boolean isOut() {
        if (cx <= 0 || cy <= 0 || cx >= w || cy >= h || alpha <= 0) {
            return true;
        }
        return false;
    }

}
