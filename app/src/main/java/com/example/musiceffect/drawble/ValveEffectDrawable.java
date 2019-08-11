package com.example.musiceffect.drawble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import com.example.musiceffect.helper.CircleDrawHelper;
import com.example.musiceffect.utils.SystemUtil;

public class ValveEffectDrawable extends BaseEffectDrawable {

    private PointF o;
    private PointF[] points;
    private float mDrawWidth = 10;

    private CircleDrawHelper mCircleDrawHelper;

    private int mLineWidth = 2;

    private int mAmount = 4;

    public ValveEffectDrawable(Context context) {
        super(context);
        init();
    }

    private void init() {
        mRadius = SystemUtil.dip2px(getContext(), 102);
        mLineWidth = SystemUtil.dip2px(getContext(), mLineWidth);
        mDrawWidth = SystemUtil.dip2px(getContext(), mDrawWidth);

        mCircleDrawHelper = new CircleDrawHelper(mCount / mAmount);

        points = new PointF[mCount / mAmount];
        for (int i = 0; i < mCount / mAmount; i++) {
            points[i] = new PointF();
        }
        o = new PointF();
    }

    @Override
    public void draw(Canvas canvas) {
        mIsDrawing = true;
        drawWare(canvas);
        mIsDrawing = false;
    }

    private void drawWare(Canvas canvas) {
        o.x = canvas.getWidth() / 2;
        o.y = canvas.getHeight() / 2;
        for (int j = 0; j < mAmount; j++) {

            for (int i = 0; i < points.length; i++) {
                int index = i * mCountOffset * mAmount;
                float value = 0;
                if (mData != null) {
                    value = (int) mData[ j + i];
                }
                if (value > 0) {
                    value = SystemUtil.dip2px(getContext(), value * 0.5f);
                }

                float r = mRadius + value;
                float x = (float) Math.sin(Math.toRadians(index)) * r + canvas.getWidth() / 2;
                float y = (float) Math.cos(Math.toRadians(index)) * r + canvas.getHeight() / 2;
                mCircleDrawHelper.setPoint(points[i], x, y);
            }


            mCircleDrawHelper.calculate(points, 0.8);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(getColor(j));
            mPaint.setAlpha(100);
            mCircleDrawHelper.drawBezierCurveFill(canvas, points, mPaint, o);

        }
    }



}
