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

public class ReverberationEffectDrawable extends BaseEffectDrawable {

    private static final int TRIANGLE_SIZE = 5;
    private int mCount;
    private int mCountOffset;
    private Context mContext;

    private Paint mPaint;
    private int mPaintColor = Color.parseColor("#CABFA3");
    private int mRadius;
    private int mLineWidth;

    private RectF[] mRectFS;

    private Matrix mMatrix = new Matrix();
    private Path mPath = new Path();


    public ReverberationEffectDrawable(Context context, int count, int countOffset) {
        mContext = context;
        mCount = count;
        mCountOffset = countOffset;
        init();
    }

    public Context getContext() {
        return mContext;
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPaintColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(8);

        mRadius = SystemUtil.dip2px(getContext(), 113);
        mLineWidth = SystemUtil.dip2px(getContext(), 5);

        mRectFS = new RectF[mCount];

        for (int i = 0; i < mCount; i++) {
            mRectFS[i] = new RectF();
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

        drawWare(canvas);

        mIsDrawing = false;
    }

    private Random mRandom = new Random();

    private void drawWare(Canvas canvas) {
        mPaint.setAlpha(255);
        for (int i = 0; i < mRectFS.length; i++) {
            int index = i * mCountOffset;
            int value = 0;
            if (mData != null) {
                value = (int) mData[i];
            }else {
                value = (int) (mRandom.nextFloat() * 127);
            }

            if (value < 0) {
                value = 0;
            }
            if (value > 0) {
                value = SystemUtil.dip2px(getContext(), value  * 1.0f);
            }



            float l = canvas.getWidth() / 2 + mRadius;
            float t = canvas.getWidth() / 2 + mRadius;
//
            float rd = value;
//            float rd = 0;
            float r = l + mLineWidth + rd;
            float b = t + mLineWidth;
            mRectFS[i].set(l, t, r, b);

            canvas.save();
            canvas.rotate(index, canvas.getWidth() / 2, canvas.getHeight() / 2);
            canvas.drawRoundRect(mRectFS[i], mLineWidth, mLineWidth, mPaint);
            canvas.restore();
        }
    }

}
