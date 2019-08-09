package com.example.musiceffect.drawble;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;

public class BaseEffectDrawable extends Drawable {

    protected byte[] mData;
    protected boolean mIsDrawing = false;

    public void setColor(int color) {
    }

    public void setColor(ArrayList<Integer> colors) {

    }

    public void setData(final byte[] data) {
        if (mIsDrawing) {
            return;
        }
        Log.e("yijunwu", data[0] + " data.length=" + data.length);
        mData = data;
        invalidateSelf();
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
