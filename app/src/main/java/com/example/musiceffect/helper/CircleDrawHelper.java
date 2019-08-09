package com.example.musiceffect.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import com.example.musiceffect.utils.BezierCurveUtil;

public class CircleDrawHelper {

    private Point[] midPoints;
    private Point[] ratioPoints;
    private Point[] controlPoints;
    private Path path = new Path();
    private Point controlPoint1 = new Point();
    private Point controlPoint2 = new Point();

    public CircleDrawHelper(int count) {
        midPoints = new Point[count];
        ratioPoints = new Point[count];
        controlPoints = new Point[count * 2];

        for (int i = 0, j = 0; i < count; i++) {
            midPoints[i] = new Point();
            ratioPoints[i] = new Point();
            controlPoints[j++] = new Point();
            controlPoints[j++] = new Point();
        }
    }

    public void calculate(Point[] points, double k) {
        int size = points.length;
        // 计算中点
        for (int i = 0; i < size; i++) {
            Point p1 = points[i];
            Point p2 = points[(i + 1) % size];
            setPoint(midPoints[i], (p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
        }

        // 计算比例点
        for (int i = 0; i < size; i++) {
            Point p1 = points[i];
            Point p2 = points[(i + 1) % size];
            Point p3 = points[(i + 2) % size];
            double l1 = distance(p1, p2);
            double l2 = distance(p2, p3);
            double ratio = l1 / (l1 + l2);
            Point mp1 = midPoints[i];
            Point mp2 = midPoints[(i + 1) % size];
            ratioPointConvert(mp2, mp1, ratio, ratioPoints[i]);
        }

        // 移动线段，计算控制点
        for (int i = 0, j = 0; i < size; i++) {
            Point ratioPoint = ratioPoints[i];
            Point verPoint = points[(i + 1) % size];
            int dx = ratioPoint.x - verPoint.x;
            int dy = ratioPoint.y - verPoint.y;
            setPoint(controlPoint1, midPoints[i].x - dx, midPoints[i].y - dy);
            setPoint(controlPoint2, midPoints[(i + 1) % size].x - dx, midPoints[(i + 1) % size].y - dy);
            ratioPointConvert(controlPoint1, verPoint, k, controlPoints[j++]);
            ratioPointConvert(controlPoint2, verPoint, k, controlPoints[j++]);
        }
    }

    public void drawBezierCurve(Canvas canvas, Point[] points, Paint paint) {
        int size = points.length;
        // 用三阶贝塞尔曲线连接顶点
        for (int i = 0; i < size; i++) {
            Point startPoint = points[i];
            Point endPoint = points[(i + 1) % size];
            Point controlPoint1 = controlPoints[(i * 2 + controlPoints.length - 1) % controlPoints.length];
            Point controlPoint2 = controlPoints[(i * 2) % controlPoints.length];
            path.reset();
            path.moveTo(startPoint.x, startPoint.y);
            path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, endPoint.x, endPoint.y);
            canvas.drawPath(path, paint);
        }
    }

    public void drawBezierCurve2(Canvas canvas, Point[] points, Paint paint) {

        int size = points.length;
        // 用三阶贝塞尔曲线连接顶点
        for (int i = 0; i < size -1; i++) {

            PointF startPoint = new PointF(points[i]);
            PointF endPoint = new PointF(points[(i + 1) % size]);
            PointF controlPoint1 = new PointF(controlPoints[(i * 2 + controlPoints.length - 1) % controlPoints.length]);
            PointF controlPoint2 = new PointF(controlPoints[(i * 2) % controlPoints.length]);

            int precision = (int) Math.max(Math.abs(startPoint.x - endPoint.x), Math.abs(startPoint.y - endPoint.y));

            PointF[] po = BezierCurveUtil.evaluate(precision, startPoint, controlPoint1, controlPoint2, endPoint);
//            float[][] po = BezierCurveUtil.calculate(poss,100);
//            path.reset();
//            path.moveTo(po[0].x, po[0].y);
//            for (int j = 1; j < po.length; j++) {
//                path.lineTo(po[j].x, po[j].y);
//            }
//            paint.setColor(Color.BLUE);
//            canvas.drawPath(path, paint);

            float x0 = po[0].x;
            float y0 = po[0].y;
            for (int j = 1; j < po.length; j++) {
                Log.e("yijunwu", po[j].toString());
                float x = po[j].x;
                float y = po[j].y;
//                canvas.drawLine(x0, y0, x, y, paint);
                canvas.drawPoint(x, y, paint);
                x0 = x;
                y0 = y;
            }

            /////////////
//            Point startPoint = points[i];
//            Point endPoint = points[(i + 1) % size];
//            Point controlPoint1 = controlPoints[(i * 2 + controlPoints.length - 1) % controlPoints.length];
//            Point controlPoint2 = controlPoints[(i * 2) % controlPoints.length];
//            path.reset();
//            path.moveTo(startPoint.x, startPoint.y);
//            path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, endPoint.x, endPoint.y);
//            canvas.drawPath(path, paint);
        }

        for (int i = 0; i < size; i++) {
            PointF startPoint = new PointF(points[i]);
            paint.setColor(Color.BLACK);
            canvas.drawPoint(startPoint.x, startPoint.y, paint);
        }
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
