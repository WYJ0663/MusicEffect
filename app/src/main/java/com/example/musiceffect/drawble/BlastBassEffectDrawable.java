package com.example.musiceffect.drawble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import com.example.musiceffect.helper.CircleDrawHelper;
import com.example.musiceffect.helper.LineDrawHelper;
import com.example.musiceffect.utils.SystemUtil;

public class BlastBassEffectDrawable extends BaseEffectDrawable {

    private PointF[] points;
    private float mDrawWidth = 10;

    private CircleDrawHelper mCircleDrawHelper;
    private LineDrawHelper mLineDrawHelper;

    private int mLineWidth = 2;

    private int mPointRadius = 113;

    private RectF mRectF = new RectF();
    private Matrix mMatrix = new Matrix();
    private Path mPath = new Path();
    private Paint mLightPaint;
    private int mLightWidth = 10;
    private LinearGradient mLinearGradient;

    public BlastBassEffectDrawable(Context context) {
        super(context);
        init();
    }

    private void init() {
        mLineWidth = SystemUtil.dip2px(getContext(), mLineWidth);
        mDrawWidth = SystemUtil.dip2px(getContext(), mDrawWidth);
        mPointRadius = mRadius + SystemUtil.dip2px(getContext(), 10);

        mCircleDrawHelper = new CircleDrawHelper(mCount / 4);
        mLineDrawHelper = new LineDrawHelper(90, mCount / 4);

        points = new PointF[mCount / 4];
        for (int i = 0; i < mCount / 4; i++) {
            points[i] = new PointF();
        }

        mLightWidth = SystemUtil.dip2px(getContext(), mLightWidth);
        mLightPaint = new Paint();
        mLightPaint.setAntiAlias(true);
        mLightPaint.setStyle(Paint.Style.FILL);
        setLightColor();
    }

    private void setLightColor() {
        mLinearGradient = new LinearGradient(0, 0, mLightWidth, 0,
                new int[]{getAlphaColor(0), getAlphaColor(60)},
                new float[]{0.0f, 1.0f}, Shader.TileMode.CLAMP);
        mLightPaint.setShader(mLinearGradient);
    }

    @Override
    public void setColor(int color) {
        super.setColor(color);
        setLightColor();
    }

    @Override
    public void draw(Canvas canvas) {
        mIsDrawing = true;
        drawWarePoint(canvas);
        drawWareLine(canvas);
        mIsDrawing = false;
    }

    private void drawWarePoint(Canvas canvas) {

        for (int i = 0; i < points.length; i++) {
            float value = 0;
            if (mData != null) {
                value = (int) mData[i];
            }
            //debug
//            value = (int) (mRandom.nextFloat()*127);
            if (value > 0) {
                value = SystemUtil.dip2px(getContext(), value * 0.5f);
            }

            float x = i * mDrawWidth;
            float y = value;
            mLineDrawHelper.setPoint(points[i], x, y);
        }

        mLineDrawHelper.setPoints(points, mDrawWidth);

        mLineDrawHelper.calculate(0.8);
        mLineDrawHelper.calculateHeight();

        mPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < mLineDrawHelper.getLineCount(); i++) {
            int index = i * 360 / mLineDrawHelper.getLineCount();

            float x = canvas.getWidth() / 2 + mPointRadius + mLineDrawHelper.getHeights()[i];
            float y = canvas.getHeight() / 2;

            canvas.save();
            canvas.rotate(index, canvas.getWidth() / 2, canvas.getHeight() / 2);

            mPaint.setColor(mPaintColor);
            canvas.drawCircle(x, y, mLineWidth, mPaint);

            float ll = canvas.getWidth() / 2 + mPointRadius;
            float lt = y - mLineWidth / 2;
            float lr = x;
            float lb = lt + mLineWidth;
            mRectF.set(ll, lt, lr, lb);
            mPath.reset();
            mPath.moveTo(lr, lt);
            mPath.lineTo(ll, y-mLineWidth/4);
            mPath.lineTo(ll,  y+mLineWidth/4);
            mPath.lineTo(lr, lb);
            mMatrix.reset();
            mMatrix.postTranslate(mPointRadius, 0);
            mLinearGradient.setLocalMatrix(mMatrix);
            canvas.drawPath(mPath, mLightPaint);
//            canvas.drawRect(mRectF, mLightPaint);

            canvas.restore();
        }
    }

    private void drawWareLine(Canvas canvas) {

        for (int i = 0; i < points.length; i++) {
            int index = i * mCountOffset * 4;
            float value = 0;
            if (mData != null) {
                value = (int) mData[points.length * 3 + i];
            }
            //debug
//            value = (int) (mRandom.nextFloat()*127);
            if (value > 0) {
                value = SystemUtil.dip2px(getContext(), value * 0.5f);
            }

            float r = mRadius + value;
            float x = (float) Math.sin(Math.toRadians(index)) * r + canvas.getWidth() / 2;
            float y = (float) Math.cos(Math.toRadians(index)) * r + canvas.getHeight() / 2;
            mCircleDrawHelper.setPoint(points[i], x, y);


        }

        mCircleDrawHelper.calculate(points, 0.8);
        mPaint.setStyle(Paint.Style.STROKE);
        mCircleDrawHelper.drawBezierCurve(canvas, points, mPaint);
    }


}
