package com.example.musiceffect.drawble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import com.example.musiceffect.helper.CircleDrawHelper;
import com.example.musiceffect.helper.LineDrawHelper;
import com.example.musiceffect.utils.BezierCurveUtil;
import com.example.musiceffect.utils.SystemUtil;

import java.util.Random;

public class ReverberationEffectDrawable2 extends BaseEffectDrawable {

    private static final int TRIANGLE_SIZE = 5;
    private int mCount;
    private int mCountOffset;
    private Context mContext;

    private Paint mPaint;
    private int mPaintColor = Color.parseColor("#CABFA3");
    private int mRadius = 113;

    private Point[] points;
    private int[] mHeight;
    private int mDrawWidth = 5;

    private LineDrawHelper mLineDrawHelper;

    private Random mRandom = new Random();

    private Matrix mMatrix = new Matrix();
    private Path mPath = new Path();

    private int mLineWidth = 4;

    public ReverberationEffectDrawable2(Context context, int count, int countOffset) {
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
        mPaint.setStrokeWidth(5);

        mRadius = SystemUtil.dip2px(getContext(), mRadius);
        mLineWidth = SystemUtil.dip2px(getContext(), mLineWidth);
        mDrawWidth = SystemUtil.dip2px(getContext(), mDrawWidth);

        mLineDrawHelper = new LineDrawHelper(mCount / 4);

        mHeight = new int[mCount];

        points = new Point[mCount / 4];
        for (int i = 0; i < mCount / 4; i++) {
            points[i] = new Point();
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


    private void drawWare(Canvas canvas) {
        mPath.reset();

        for (int i = 0; i < points.length; i++) {
            int index = i * mCountOffset;
            int value = 0;
            if (mData != null) {
                value = (int) mData[i];
            }
            if (value < 8) {
                value = 0;
            }
            if (value > 0) {
                value = SystemUtil.dip2px(getContext(), value * 0.5f);
            }

            float x = i * mDrawWidth;
            float y = value;
            mLineDrawHelper.setPoint(points[i], (int) x, (int) y);

//            mPaint.setColor(Color.BLUE);
//            canvas.drawPoint(x, y, mPaint);
        }

        int p1X = -mDrawWidth;
        int p1Y = points[points.length - 1].y;
        int p2X = points[points.length - 1].x + mDrawWidth;
        int p2Y = points[0].y;
        mLineDrawHelper.setPoints(points, p1X, p1Y, p2X, p2Y);

        mLineDrawHelper.calculate(0.8);
        mLineDrawHelper.calculateHeight(canvas, mPaint, (points.length)*mDrawWidth);

        for (int i = 0; i < mLineDrawHelper.getLineCount(); i++) {
            int index = i * 360 / mLineDrawHelper.getLineCount();

            float l = canvas.getWidth() / 2 + mRadius;
            float t = canvas.getWidth() / 2 + mRadius;

            float r = l + mLineWidth + mLineDrawHelper.getHeights()[i];
            float b = t + mLineWidth;
            mRectFS.set(l, t, r, b);

            canvas.save();
            canvas.rotate(index, canvas.getWidth() / 2, canvas.getHeight() / 2);
            mPaint.setColor(mPaintColor);
            canvas.drawRoundRect(mRectFS, mLineWidth, mLineWidth, mPaint);
            canvas.restore();
        }
    }

    RectF mRectFS = new RectF();
}
