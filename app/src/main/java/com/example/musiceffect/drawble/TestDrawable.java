package com.example.musiceffect.drawble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.SystemClock;
import com.example.musiceffect.utils.BezierCurveUtil;
import com.example.musiceffect.helper.CircleDrawHelper;
import com.example.musiceffect.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Deprecated
public class TestDrawable extends BaseEffectDrawable {


    private PointF[] points;

    private int mDrawWidth = 500;

    private CircleDrawHelper mCircleDrawHelper;

    private Random mRandom = new Random();

    private Path mPath = new Path();


    public TestDrawable(Context context) {
        super(context);
        init();
    }

    public Context getContext() {
        return mContext;
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPaintColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);

        mRadius = SystemUtil.dip2px(getContext(), 113);

        mCircleDrawHelper = new CircleDrawHelper(mCount / 2);

        mDrawWidth = SystemUtil.dip2px(getContext(), mDrawWidth);
        points = new PointF[mCount / 2];
        for (int i = 0; i < mCount; i++) {
            points[i] = new PointF();
        }
    }

    public void setColor(int color) {
        mPaintColor = color;
        mPaint.setColor(mPaintColor);
        invalidateSelf();
    }


    @Override
    public void draw(Canvas canvas) {
        mIsDrawing = true;
        test(canvas);
        drawWare2(canvas);
        drawWare(canvas);
        mIsDrawing = false;
    }

    private void test(Canvas canvas) {
        float[][] poss = {
                {353.0f, 383.0f},
                {403.0f, 128.0f},
                {670.0f, 266.0f},

                {648.0f, 669.0f}
        };

//        float[][] po = BezierCurveUtil.evaluate(100, poss[0], poss[1], poss[2], poss[3]);
        float[][] po = BezierCurveUtil.calculate(poss, 100);

        float x0 = po[0][0];
        float y0 = po[0][1];
        for (int i = 1; i < po.length; i++) {
            float x = po[i][0];
            float y = po[i][1];
            mPaint.setColor(Color.YELLOW);
            canvas.drawLine(x0, y0, x, y, mPaint);
            x0 = x;
            y0 = y;
        }

        for (int i = 0; i < poss.length; i++) {
            float x = poss[i][0];
            float y = poss[i][1];
            mPaint.setColor(Color.RED);
            canvas.drawPoint(x, y, mPaint);
        }
    }

    private void drawWare(Canvas canvas) {
        mPath.reset();

        for (int i = 0; i < points.length; i++) {
            int index = i * mCountOffset;
            int value = 0;
            if (mData != null) {
                value = (int) mData[i];
            } else {
                value = (int) (mRandom.nextFloat() * 50);
            }
//            if (value < 8) {
//                value = 0;
//            }
            if (value >= 0) {
                value = SystemUtil.dip2px(getContext(), value * 1f + 100);
            }

            float r = mRadius + value;
//            float x = (float) Math.sin(Math.toRadians(index)) * r + canvas.getWidth() / 2;
//            float y = (float) Math.cos(Math.toRadians(index)) * r + canvas.getHeight() / 2;
            float x = 100 + i * SystemUtil.dip2px(getContext(), 30);
            float y = value;
            mCircleDrawHelper.setPoint(points[i], (int) x, (int) y);
            if (i == 0) {
                mPath.moveTo(x, y);
            } else {
                mPath.lineTo(x, y);
            }
        }
    }

    private void drawWare2(Canvas canvas) {
        mPath.reset();

        for (int i = 0; i < points.length; i++) {
            int value = 0;
            if (mData != null) {
                value = (int) mData[i];
            } else {
                value = (int) (mRandom.nextFloat() * 50);
            }
//            if (value < 8) {
//                value = 0;
//            }
            if (value >= 0) {
                value = SystemUtil.dip2px(getContext(), value * 1f + 100);
            }

            float r = mRadius + value;
//            float x = (float) Math.sin(Math.toRadians(index)) * r + canvas.getWidth() / 2;
//            float y = (float) Math.cos(Math.toRadians(index)) * r + canvas.getHeight() / 2;
            float x = 100 + i * SystemUtil.dip2px(getContext(), 30);
            float y = value;
            mCircleDrawHelper.setPoint(points[i], (int) x, (int) y);
            if (i == 0) {
                mPath.moveTo(x, y);
            } else {
                mPath.lineTo(x, y);
            }
        }
//        mPaint.setColor(Color.RED);
//        canvas.drawPath(mPath,mPaint);

        mCircleDrawHelper.calculate(points, 0.8);
        mCircleDrawHelper.drawBezierCurve2(canvas, points, mPaint);
    }
}
