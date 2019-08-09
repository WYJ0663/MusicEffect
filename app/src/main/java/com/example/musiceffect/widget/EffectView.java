package com.example.musiceffect.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.example.musiceffect.drawble.AncientEffectDrawable;
import com.example.musiceffect.drawble.AncientEffectDrawable2;
import com.example.musiceffect.drawble.ReverberationEffectDrawable2;
import com.example.musiceffect.drawble.BaseEffectDrawable;
import com.example.musiceffect.drawble.ReverberationEffectDrawable;

import java.util.ArrayList;

public class EffectView extends ImageView {
    public static final int LUMP_OFFSET = 4;
    public static final int LUMP_COUNT = 360 / LUMP_OFFSET;
    private int mPaintColor = Color.parseColor("#CABFA3");
    public EffectView(Context context) {
        super(context);
        init();
    }

    public EffectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EffectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    private BaseEffectDrawable mDrawable;

    public void setAncientEffectDrawable() {
        mDrawable = new AncientEffectDrawable(getContext(), LUMP_COUNT, LUMP_OFFSET);
        setImageDrawable(mDrawable);
        setColor();
    }

    public void setAncientEffectDrawable2() {
        mDrawable = new AncientEffectDrawable2(getContext(), LUMP_COUNT, LUMP_OFFSET);
        setImageDrawable(mDrawable);
        invalidate();
        setColor();
    }
    public void setAncientEffectDrawable3() {
        mDrawable = new ReverberationEffectDrawable(getContext(), LUMP_COUNT, LUMP_OFFSET);
        setImageDrawable(mDrawable);
        invalidate();
        setColor();
    }

    public void setReverberationEffectDrawable() {
        mDrawable = new ReverberationEffectDrawable2(getContext(), LUMP_COUNT, LUMP_OFFSET);
        setImageDrawable(mDrawable);
        setColor();
    }

    public void setColor(){
        if (mDrawable != null) {
            mDrawable.setColor(mPaintColor);
        }
    }

    public void setColor(int color) {
        mPaintColor = color;
        if (mDrawable != null) {
            mDrawable.setColor(color);
        }
    }

    public void setColor(ArrayList<Integer> colors) {
        if (mDrawable != null) {
            mDrawable.setColor(colors);
        }
    }

    public void setData(final byte[] data) {
        if (data != null) {
            Log.e("yijunwu", (int)data[0] + " " + (int)data[1]);
        }
        if (mDrawable != null) {
            mDrawable.setData(readyData(data));
        }
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



}
