package com.example.musiceffect.drawble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import com.example.musiceffect.helper.LineDrawHelper;
import com.example.musiceffect.utils.ColorUtil;
import com.example.musiceffect.utils.SystemUtil;

public class ElectronicEffectDrawable extends BaseEffectDrawable {

    private PointF[] points;
    private float mDrawWidth = 10;

    private LineDrawHelper mLineDrawHelper;

    private int mLineRectWidth = 4;

    private RectF mRectF = new RectF();

    private Matrix mMatrix = new Matrix();
    private Path mPath = new Path();
    private Paint mLightPaint;
    private int mLightWidth = 30;
    private LinearGradient mLinearGradient;

    public ElectronicEffectDrawable(Context context) {
        super(context);
        init();
    }

    private void init() {

        mLineRectWidth = SystemUtil.dip2px(getContext(), mLineRectWidth);
        mDrawWidth = SystemUtil.dip2px(getContext(), mDrawWidth);

        mLineDrawHelper = new LineDrawHelper(mCount / 4);

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

    @Override
    public void setColor(int color) {
        super.setColor(color);
        setLightColor();
    }

    private void setLightColor() {
        mLinearGradient = new LinearGradient(0, 0, mLightWidth + mLineRectWidth, 0,
                new int[]{getAlphaColor(160), getAlphaColor(70), getAlphaColor(0)},
                new float[]{0.0f, 0.3f, 1.0f}, Shader.TileMode.MIRROR);
        mLightPaint.setShader(mLinearGradient);
    }

    @Override
    public void draw(Canvas canvas) {
        mIsDrawing = true;
        drawWare(canvas);
        mIsDrawing = false;
    }


    private void drawWare(Canvas canvas) {

        for (int i = 0; i < points.length; i++) {
            float value = 0;
            if (mData != null) {
                value = (int) mData[i];
            }
            //debug
//            value = (int) (mRandom.nextFloat()*127);
            if (value > 0) {
                value = SystemUtil.dip2px(getContext(), value * 0.5f + 5);
            }

            float x = i * mDrawWidth;
            float y = value;
            mLineDrawHelper.setPoint(points[i], x, y);
        }

        mLineDrawHelper.setPoints(points, mDrawWidth);

        mLineDrawHelper.calculate(0.8);
        mLineDrawHelper.calculateHeight();

        for (int i = 0; i < mLineDrawHelper.getLineCount(); i++) {
            int index = i * 360 / mLineDrawHelper.getLineCount();

            canvas.save();
            canvas.rotate(index, canvas.getWidth() / 2, canvas.getHeight() / 2);

            float l = canvas.getWidth() / 2 + mRadius;
            float t = canvas.getHeight() / 2;
            float r = l + mLineRectWidth + mLineDrawHelper.getHeights()[i];
            float b = t + mLineRectWidth;
            mRectF.set(l, t, r, b);
            mPaint.setColor(mPaintColor);
            canvas.drawRoundRect(mRectF, mLineRectWidth, mLineRectWidth, mPaint);

            float ll = r - mLineRectWidth/2;
            float lt = t;
            float lr = ll + mLightWidth;
            float lb = lt + mLineRectWidth;
            mRectF.set(ll, lt, lr, lb);
            mPath.reset();
            mPath.moveTo(ll, lt);
            mPath.lineTo(lr, lt + mLineRectWidth / 3);
            mPath.lineTo(lr, lt + mLineRectWidth * 2 / 3);
            mPath.lineTo(ll, lb);
            mMatrix.reset();
            mMatrix.postTranslate(ll, 0);
            mLinearGradient.setLocalMatrix(mMatrix);
            canvas.drawPath(mPath, mLightPaint);

            canvas.restore();
        }
    }


}
