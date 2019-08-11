package com.example.musiceffect.drawble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import com.example.musiceffect.utils.SystemUtil;

import java.util.Random;

@Deprecated
public class ReverberationEffectDrawableOld extends BaseEffectDrawable {


    private int mLineWidth;

    private RectF mRectF = new RectF();

    public ReverberationEffectDrawableOld(Context context) {
        super(context);
        init();
    }

    public Context getContext() {
        return mContext;
    }

    private void init() {
        mLineWidth = SystemUtil.dip2px(getContext(), 5);

    }

    public void setColor(int color) {
        mPaintColor = color;
        mPaint.setColor(mPaintColor);
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        mIsDrawing = true;

        drawWare(canvas);

        mIsDrawing = false;
    }

    private Random mRandom = new Random();

    private void drawWare(Canvas canvas) {
        mPaint.setAlpha(255);
        for (int i = 0; i < mCount; i++) {
            int index = i * mCountOffset;
            int value = 0;
            if (mData != null) {
                value = (int) mData[i];
            } else {
                value = (int) (mRandom.nextFloat() * 127);
            }

            if (value < 0) {
                value = 0;
            }
            if (value > 0) {
                value = SystemUtil.dip2px(getContext(), value * 1.0f);
            }


            float l = canvas.getWidth() / 2 + mRadius;
            float t = canvas.getWidth() / 2 + mRadius;
//
            float rd = value;
//            float rd = 0;
            float r = l + mLineWidth + rd;
            float b = t + mLineWidth;
            mRectF.set(l, t, r, b);

            canvas.save();
            canvas.rotate(index, canvas.getWidth() / 2, canvas.getHeight() / 2);
            canvas.drawRoundRect(mRectF, mLineWidth, mLineWidth, mPaint);
            canvas.restore();
        }
    }

}
