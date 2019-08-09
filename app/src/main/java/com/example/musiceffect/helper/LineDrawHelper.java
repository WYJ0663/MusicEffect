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

    private int lineCount = 90;

    private int count;
    private float[] heights;
    private Point[] points;
    private Point[] midPoints;
    private Point[] ratioPoints;
    private Point[] controlPoints;
    private Path path = new Path();
    private Point controlPoint1 = new Point();
    private Point controlPoint2 = new Point();


    public LineDrawHelper(int count) {
        this.count = count;
        heights = new float[lineCount];
        points = new Point[count + 2];//左右各加一个点
        midPoints = new Point[count + 1];
        ratioPoints = new Point[count + 1];
        controlPoints = new Point[count * 2 + 2];

        for (int i = 0, j = 0; i < count + 1; i++) {
            points[i] = new Point();
            midPoints[i] = new Point();
            ratioPoints[i] = new Point();
            controlPoints[j++] = new Point();
            controlPoints[j++] = new Point();
        }
        points[count + 1] = new Point();
    }

    public void setPoints(Point[] ps, int p1X, int p1Y, int p2X, int p2Y) {
        points[0].x = p1X;
        points[0].y = p1Y;
        points[points.length - 1].x = p2X;
        points[points.length - 1].y = p2Y;

        for (int i = 0; i < ps.length; i++) {
            points[i + 1] = ps[i];
        }

    }

    public void calculate(double k) {
        // 计算中点
        for (int i = 0; i < count + 1; i++) {
            Point p1 = points[i];
            Point p2 = points[(i + 1)];
            setPoint(midPoints[i], (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
        }

        // 计算比例点
        for (int i = 0; i < count; i++) {
            Point p1 = points[i];
            Point p2 = points[(i + 1)];
            Point p3 = points[(i + 2)];
            double l1 = distance(p1, p2);
            double l2 = distance(p2, p3);
            double ratio = l1 / (l1 + l2);
            Point mp1 = midPoints[i];
            Point mp2 = midPoints[(i + 1)];
            ratioPointConvert(mp2, mp1, ratio, ratioPoints[i]);
        }

        // 移动线段，计算控制点
        for (int i = 0, j = 0; i < count; i++) {
            Point ratioPoint = ratioPoints[i];
            Point verPoint = points[(i + 1)];
            int dx = ratioPoint.x - verPoint.x;
            int dy = ratioPoint.y - verPoint.y;
            setPoint(controlPoint1, midPoints[i].x - dx, midPoints[i].y - dy);
            setPoint(controlPoint2, midPoints[(i + 1)].x - dx, midPoints[(i + 1)].y - dy);
            ratioPointConvert(controlPoint1, verPoint, k, controlPoints[j++]);
            ratioPointConvert(controlPoint2, verPoint, k, controlPoints[j++]);
        }
    }

    public void calculateHeight(Canvas canvas, Paint paint, int w) {
        int space = w / lineCount;
        int curWidth = 0;
        int s = 0;
        for (int i = 1; i < this.count; i++) {

            PointF startPoint = new PointF(points[i]);
            PointF endPoint = new PointF(points[(i + 1)]);
            PointF controlPoint1 = new PointF(controlPoints[i * 2 - 1]);
            PointF controlPoint2 = new PointF(controlPoints[i * 2]);

//            path.reset();
//            path.moveTo(startPoint.x, startPoint.y);
//            path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, endPoint.x, endPoint.y);
//            canvas.drawPath(path, paint);

            int precision = (int) Math.max(Math.abs(startPoint.x - endPoint.x), Math.abs(startPoint.y - endPoint.y));

//            PointF[] po = BezierCurveUtil.evaluate(precision, startPoint, controlPoint1, controlPoint2, endPoint);
//            for (int j = 1; j < po.length; j++) {
//                float x = po[j].x;
//                float y = po[j].y;
//                canvas.drawPoint(x, y, paint);
//            }


            for (int j = 0; j < precision; j++) {
                float t = (float) j / precision;
                PointF p = BezierCurveUtil.evaluate(t, startPoint, controlPoint1, controlPoint2, endPoint);
//                paint.setColor(Color.BLACK);
//                canvas.drawPoint(p.x, p.y, paint);
                if (p.x >= curWidth && s < heights.length) {
                    heights[s] = p.y;
//                    paint.setColor(Color.RED);
//                    canvas.drawLine(curWidth, 0, curWidth, heights[s], paint);
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

    public void setPoint(Point point, int x, int y) {
        point.x = x;
        point.y = y;
    }

    /**
     * 计算两点之间的距离
     */
    public double distance(Point p1, Point p2) {
        return Math.sqrt(((p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)));
    }

    /**
     * 比例点转换
     */
    public void ratioPointConvert(Point p1, Point p2, double ratio, Point p) {
        p.x = (int) (ratio * (p1.x - p2.x) + p2.x);
        p.y = (int) (ratio * (p1.y - p2.y) + p2.y);
    }

    public void drawCicleLineFromTowPoints(Canvas canvas, Point p1, Point p2, Paint paint) {
        if (p1.equals(p2)) {
            return;
        }
        path.reset();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        canvas.drawPath(path, paint);
    }

}
