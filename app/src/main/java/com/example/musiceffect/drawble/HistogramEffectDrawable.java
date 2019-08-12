package com.example.musiceffect.drawble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import com.example.musiceffect.helper.CircleDrawHelper;
import com.example.musiceffect.utils.SystemUtil;

public class HistogramEffectDrawable extends BaseEffectDrawable {

    private float mDrawWidth = 5f;
    private float mLineWidth = 2f;

    private float mFftHeight = 100;
    private float mWareHeight = 400;

    public HistogramEffectDrawable(Context context) {
        super(context);
        init();
    }

    private void init() {
        mLineWidth = SystemUtil.dip2px(getContext(), mLineWidth);
        mDrawWidth = SystemUtil.dip2px(getContext(), mDrawWidth);
        mFftHeight = SystemUtil.dip2px(getContext(), mFftHeight);
        mWareHeight = SystemUtil.dip2px(getContext(), mWareHeight);
    }

    @Override
    public void draw(Canvas canvas) {
        mIsDrawing = true;
        drawWare(canvas, mFftHeight, mData);
        drawWare(canvas, mWareHeight, mWaveData);
        mIsDrawing = false;
    }

    @Override
    public void onCall(byte[] data) {
        mData = data;
        invalidateSelf();
    }

    byte[] mWaveData;

    @Override
    public void onWaveCall(byte[] data) {
        mWaveData = data;
        invalidateSelf();
    }

    private void drawWare(Canvas canvas, float h, byte[] data) {
        if (data == null) {
            return;
        }
        float w = mLineWidth;
        for (int i = 0; i < data.length; i++) {
            float value = Math.abs((int)data[i]);
            value = SystemUtil.dip2px(getContext(), value * 0.5f);

            float l = w;
            float t = h;
            float r = l + mDrawWidth;
            float b = t + value;
            canvas.drawRect(l, t, r, b, mPaint);

            w += mDrawWidth + mLineWidth;
        }
    }

}
