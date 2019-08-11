package com.example.musiceffect.drawble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.SystemClock;
import com.example.musiceffect.utils.SystemUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class LonelyEffectDrawable extends BaseEffectDrawable {

    private static final int TRIANGLE_SIZE = 5;

    private Random mRandom = new Random();
    private List<Circle> mCircleList = new ArrayList<>();

    private int mBGRadius;

    public LonelyEffectDrawable(Context context) {
        super(context);
        init();
    }

    private void init() {
        mRadius = SystemUtil.dip2px(getContext(), 100);
        mBGRadius = SystemUtil.dip2px(getContext(), 105);
    }


    @Override
    public void draw(Canvas canvas) {
        mIsDrawing = true;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAlpha(150);
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, mBGRadius, mPaint);
        drawCircle(canvas);
        mIsDrawing = false;
    }

    private long addTime = -1;

    private void drawCircle(Canvas canvas) {
        if (mData == null) {
            return;
        }

        long curTime = SystemClock.elapsedRealtime();
        if (addTime <= 0) {
            addTime = curTime;
        }
        long deltaTime = curTime - addTime;
        float value = mData[0];
        if (deltaTime > 400 && value > 8) {
            addTime = curTime;
            Circle circle = new Circle(getContext(), mRandom, canvas.getWidth(), canvas.getHeight(), mRadius);
            mCircleList.add(circle);
        }

        for (int i = 0; i < mCircleList.size(); i++) {
            Circle circle = mCircleList.get(i);
            if (circle.isOut()) {
                mCircleList.remove(circle);
                i--;
                continue;
            }
            circle.draw(canvas, mPaint);
        }
    }

}
