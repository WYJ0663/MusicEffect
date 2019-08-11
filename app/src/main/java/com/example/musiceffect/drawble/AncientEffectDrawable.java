package com.example.musiceffect.drawble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.SystemClock;
import com.example.musiceffect.helper.CircleDrawHelper;
import com.example.musiceffect.utils.SystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AncientEffectDrawable extends BaseEffectDrawable {

    private static final int TRIANGLE_SIZE = 5;

    private PointF[] points;
    private PointF[] points2;

    private CircleDrawHelper mCircleDrawHelper;

    private Random mRandom = new Random();
    private List<Triangle> mTriangleList = new ArrayList<>();

    private Matrix mMatrix = new Matrix();
    private Path mPath = new Path();

    public AncientEffectDrawable(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint.setStyle(Paint.Style.STROKE);

        mCircleDrawHelper = new CircleDrawHelper(mCount);

        points = new PointF[mCount];
        points2 = new PointF[mCount];

        for (int i = 0; i < mCount; i++) {
            points[i] = new PointF();
            points2[i] = new PointF();
        }
    }


    @Override
    public void draw(Canvas canvas) {
        mIsDrawing = true;

        drawTriangle(canvas);

        drawWare(canvas);

        mIsDrawing = false;
    }

    private long addTime = -1;

    private void drawTriangle(Canvas canvas) {
        long curTime = SystemClock.elapsedRealtime();
        if (addTime <= 0) {
            addTime = curTime;
        }
        long deltaTime = curTime - addTime;
        if (deltaTime > 500) {
            addTime = curTime;
            for (int i = 0; i < TRIANGLE_SIZE; i++) {
                Triangle triangle = new Triangle(getContext(), mRandom, canvas.getWidth(), canvas.getHeight(), mRadius);
                mTriangleList.add(triangle);
            }
        }

        for (int i = 0; i < mTriangleList.size(); i++) {
            Triangle triangle = mTriangleList.get(i);
            if (triangle.isOut()) {
                mTriangleList.remove(triangle);
                i--;
                continue;
            }
            triangle.draw(canvas, mPaint, mMatrix, mPath);
        }
    }

    private void drawWare(Canvas canvas) {
        if (mData != null) {
            mPaint.setAlpha(255);
            for (int i = 0; i < mData.length; i++) {
                int index = i * mCountOffset;
                float value =  mData[i];
                if (value < 8) {
                    value = 0;
                }
                if (value > 0) {
                    value = SystemUtil.dip2px(getContext(), value * 0.5f);
                }

//                float x2 = (float) Math.sin(Math.toRadians(index)) * mRadius + getWidth() / 2;
//                float y2 = (float) Math.cos(Math.toRadians(index)) * mRadius + getHeight() / 2;
//                setPoint(points2[i], (int) x2, (int) y2);
//                mPaint.setColor(Color.RED);
//                mPaint.setStrokeWidth(10);
//                canvas.drawPoint(x2, y2, mPaint);
//
                float r = mRadius + value;
                float x = (float) Math.sin(Math.toRadians(index)) * r + canvas.getWidth() / 2;
                float y = (float) Math.cos(Math.toRadians(index)) * r + canvas.getHeight() / 2;
                mCircleDrawHelper.setPoint(points[i], x, y);

                float r3 = mRadius - value / 2;
                float x3 = (float) Math.sin(Math.toRadians(index)) * r3 + canvas.getWidth() / 2;
                float y3 = (float) Math.cos(Math.toRadians(index)) * r3 + canvas.getHeight() / 2;
                mCircleDrawHelper.setPoint(points2[i], x3, y3);
            }
            mCircleDrawHelper.calculate(points, 0.8);
            mCircleDrawHelper.drawBezierCurve(canvas, points, mPaint);
            mCircleDrawHelper.calculate(points2, 0.8);
            mCircleDrawHelper.drawBezierCurve(canvas, points2, mPaint);

            for (int i = 0; i < points.length; i++) {
                mCircleDrawHelper.drawCicleLineFromTowPoints(canvas, points[i], points2[i], mPaint);
            }

        } else {
            canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, mRadius, mPaint);
        }
    }

}
