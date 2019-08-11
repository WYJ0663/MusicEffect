package com.example.musiceffect.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import com.example.musiceffect.utils.BezierCurveUtil;

public class LineDrawHelper {

    private int lineCount = 120;

    private int count;
    private float[] heights;
    private PointF[] points;
    private PointF[] midPoints;
    private PointF[] ratioPoints;
    private PointF[] controlPoints;
    private Path path = new Path();
    private PointF controlPoint1 = new PointF();
    private PointF controlPoint2 = new PointF();


    public LineDrawHelper(int lineCount, int count) {
        this(count);
        this.lineCount = lineCount;
    }

    public LineDrawHelper(int count) {
        this.count = count;
        heights = new float[lineCount];
        points = new PointF[count + 3];//左加1个点，右加2个点，左右各一个辅助计算斜率点，绘制点n+1，距离为n
        midPoints = new PointF[count + 2];
        ratioPoints = new PointF[count + 2];
        controlPoints = new PointF[count * 2 + 4];

        for (int i = 0, j = 0; i < count + 2; i++) {
            points[i] = new PointF();
            midPoints[i] = new PointF();
            ratioPoints[i] = new PointF();
            controlPoints[j++] = new PointF();
            controlPoints[j++] = new PointF();
        }
        points[count + 2] = new PointF();
    }

    public void setPoints(PointF[] ps, float mdrawwidth) {
        float p1X = (int) -mdrawwidth;
        float p1Y = ps[ps.length - 1].y;
        float p2X = (int) (ps[ps.length - 1].x + mdrawwidth);
        float p2Y = ps[0].y;
        float p3X = (int) (ps[ps.length - 1].x + mdrawwidth + mdrawwidth);
        float p3Y = ps[1].y;

        points[0].x = p1X;
        points[0].y = p1Y;
        points[points.length - 2].x = p2X;
        points[points.length - 2].y = p2Y;
        points[points.length - 1].x = p3X;
        points[points.length - 1].y = p3Y;

        for (int i = 0; i < ps.length; i++) {
            points[i + 1] = ps[i];
        }
    }

    public void calculate(double k) {
        // 计算中点
        for (int i = 0; i < count + 2; i++) {
            PointF p1 = points[i];
            PointF p2 = points[(i + 1)];
            setPoint(midPoints[i], (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
        }

        // 计算比例点
        for (int i = 0; i < count + 1; i++) {
            PointF p1 = points[i];
            PointF p2 = points[(i + 1)];
            PointF p3 = points[(i + 2)];
            double l1 = distance(p1, p2);
            double l2 = distance(p2, p3);
            double ratio = l1 / (l1 + l2);
            PointF mp1 = midPoints[i];
            PointF mp2 = midPoints[(i + 1)];
            ratioPointConvert(mp2, mp1, ratio, ratioPoints[i]);
        }

        // 移动线段，计算控制点
        for (int i = 0, j = 0; i < count + 1; i++) {
            PointF ratioPoint = ratioPoints[i];
            PointF verPoint = points[(i + 1)];
            float dx = ratioPoint.x - verPoint.x;
            float dy = ratioPoint.y - verPoint.y;
            setPoint(controlPoint1, midPoints[i].x - dx, midPoints[i].y - dy);
            setPoint(controlPoint2, midPoints[(i + 1)].x - dx, midPoints[(i + 1)].y - dy);
            ratioPointConvert(controlPoint1, verPoint, k, controlPoints[j++]);
            ratioPointConvert(controlPoint2, verPoint, k, controlPoints[j++]);
        }
    }

    /**
     * 计算曲线每个间隔的高度
     */
    public void calculateHeight() {
        float space = points[count + 1].x / lineCount;
        float curWidth = 0;
        int s = 0;
        for (int i = 1; i < this.count + 1; i++) {

            PointF startPoint = points[i];
            PointF endPoint = points[(i + 1)];
            PointF controlPoint1 = controlPoints[i * 2 - 1];
            PointF controlPoint2 = controlPoints[i * 2];

            int precision = (int) Math.max(Math.abs(startPoint.x - endPoint.x), Math.abs(startPoint.y - endPoint.y));

            for (int j = 0; j < precision; j++) {
                float t = (float) j / precision;
                PointF p = BezierCurveUtil.evaluate(t, startPoint, controlPoint1, controlPoint2, endPoint);
                //debug
//                paint.setColor(Color.BLACK);
//                canvas.drawPoint(p.x + 100, p.y, paint);
                if (p.x >= curWidth && s < heights.length) {
                    heights[s] = p.y;
//                    paint.setColor(Color.RED);
//                    canvas.drawLine(curWidth + 100, 0, curWidth + 100, heights[s], paint);
                    s++;
                    curWidth += space;
                }
            }
        }
    }


    public float[] getHeights() {
        return heights;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setPoint(PointF point, float x, float y) {
        point.x = x;
        point.y = y;
    }

    /**
     * 计算两点之间的距离
     */
    public double distance(PointF p1, PointF p2) {
        return Math.sqrt(((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)));
    }

    /**
     * 比例点转换
     */
    public void ratioPointConvert(PointF p1, PointF p2, double ratio, PointF p) {
        p.x = (int) (ratio * (p1.x - p2.x) + p2.x);
        p.y = (int) (ratio * (p1.y - p2.y) + p2.y);
    }


}
