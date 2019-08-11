package com.example.musiceffect.drawble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.example.musiceffect.utils.ColorUtil;
import com.example.musiceffect.utils.SystemUtil;

import java.util.ArrayList;

public class BaseEffectDrawable extends Drawable {
    public static final int LUMP_OFFSET = 4;
    public static final int LUMP_COUNT = 360 / LUMP_OFFSET;

    protected Context mContext;

    protected int mCount = LUMP_COUNT;
    protected int mCountOffset = LUMP_OFFSET;
    protected Paint mPaint;
    protected int mPaintColor = Color.parseColor("#CABFA3");
    protected int[] mPaintColors ;
    protected int mRadius = 113;

    protected byte[] mData;
    protected boolean mIsDrawing = false;

    public BaseEffectDrawable(Context context) {
        mContext = context;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPaintColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5);

        mRadius = SystemUtil.dip2px(getContext(), mRadius);
    }

    protected Context getContext() {
        return mContext;
    }


    public void setColor(int color) {
        mPaintColor = color;
        mPaint.setColor(mPaintColor);

        mPaintColors = ColorUtil.getColors(color,4,30);
        invalidateSelf();
    }

    public void setColors(int[] colors) {
//        mPaintColors = colors;
//        mPaint.setColor(getColor(0));
//        invalidateSelf();
    }


    protected int getColor(int i) {
        if (mPaintColors != null && i < mPaintColors.length) {
            return mPaintColors[i];
        } else {
            return mPaintColor;
        }
    }

    protected int getAlphaColor(int i) {
        return ColorUtil.setAlpha(mPaintColor, i);
    }


    public void setData(final byte[] data) {
        if (mIsDrawing) {
            return;
        }
        Log.e("yijunwu", data[0] + " data.length=" + data.length);
        mData = readyData(data);
        invalidateSelf();
    }

    /**
     * 预处理数据
     *
     * @return
     */
    private byte[] readyData(byte[] fft) {
        byte[] newData = new byte[LUMP_COUNT];
        byte abs;
        for (int i = 0; i < LUMP_COUNT; i++) {
            abs = (byte) Math.abs(fft[i]);
            //描述：Math.abs -128时越界
            newData[i] = abs < 0 ? 127 : abs;
        }
        return newData;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.UNKNOWN;
    }


}
