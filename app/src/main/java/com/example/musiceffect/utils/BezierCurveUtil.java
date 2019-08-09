package com.example.musiceffect.utils;

import android.graphics.Point;
import android.graphics.PointF;

public class BezierCurveUtil {
    /**
     * @param poss      贝塞尔曲线控制点坐标
     * @param precision 精度，需要计算的该条贝塞尔曲线上的点的数目
     * @return 该条贝塞尔曲线上的点（二维坐标）
     */
    public static float[][] calculate(float[][] poss, int precision) {

        //维度，坐标轴数（二维坐标，三维坐标...）
        int dimersion = poss[0].length;

        //贝塞尔曲线控制点数（阶数）
        int number = poss.length;

        //控制点数不小于 2 ，至少为二维坐标系
        if (number < 2 || dimersion < 2)
            return null;

        float[][] result = new float[precision][dimersion];

        //计算杨辉三角
        int[] mi = new int[number];
        mi[0] = mi[1] = 1;
        for (int i = 3; i <= number; i++) {

            int[] t = new int[i - 1];
            for (int j = 0; j < t.length; j++) {
                t[j] = mi[j];
            }

            mi[0] = mi[i - 1] = 1;
            for (int j = 0; j < i - 2; j++) {
                mi[j + 1] = t[j] + t[j + 1];
            }
        }

        //计算坐标点
        for (int i = 0; i < precision; i++) {
            float t = (float) i / precision;
            for (int j = 0; j < dimersion; j++) {
                float temp = 0.0f;
                for (int k = 0; k < number; k++) {
                    temp += Math.pow(1 - t, number - k - 1) * poss[k][j] * Math.pow(t, k) * mi[k];
                }
                result[i][j] = temp;
            }
        }

        return result;
    }

    /**
     * @param fraction 变量
     * @param point0   贝塞尔曲线起点
     * @param point3   贝塞尔曲线终点
     * @return 因为需要的点是从下到上....所以p0, p1, p2, p3的点是从下打上的
     */
    public static float[] evaluate(float fraction, float[] point0, float[] point1, float[] point2, float[] point3) {
        float[] currentPosition = new float[2];
        float f = 1f - fraction;
        //贝塞尔公式计算X点
        currentPosition[0] = point0[0] * (f) * (f) * (f)
                + point1[0] * 3 * fraction * (f) * (f)
                + point2[0] * 3 * (f) * fraction * fraction
                + point3[0] * fraction * fraction * fraction;
        //贝塞尔公式计算Y点
        currentPosition[1] = point0[1] * (f) * (f) * (f)
                + point1[1] * 3 * fraction * (f) * (f)
                + point2[1] * 3 * (f) * fraction * fraction
                + point3[1] * fraction * fraction * fraction;
        return currentPosition;
    }

    public static float[][] evaluate(int precision, float[] point0, float[] point1, float[] point2, float[] point3) {
        float[][] currentPosition = new float[precision][2];
        for (int i = 0; i < precision; i++) {
            float t = (float) i / precision;
            currentPosition[i] = evaluate(t, point0, point1, point2, point3);
        }

        return currentPosition;
    }

    public static Point[] evaluate(int precision, Point point0, Point point1, Point point2, Point point3) {
        Point[] currentPosition = new Point[precision];
        for (int i = 0; i < precision; i++) {
            float t = (float) i / precision;
            currentPosition[i] = evaluate(t, point0, point1, point2, point3);
        }

        return currentPosition;
    }

    public static Point evaluate(float fraction, Point point0, Point point1, Point point2, Point point3) {
        Point currentPosition = new Point();
        float f = 1f - fraction;
        //贝塞尔公式计算X点
        currentPosition.x = (int) (point0.x * (f) * (f) * (f)
                + point1.x * 3 * fraction * (f) * (f)
                + point2.x * 3 * (f) * fraction * fraction
                + point3.x * fraction * fraction * fraction);
        //贝塞尔公式计算Y点
        currentPosition.y = (int) (point0.y * (f) * (f) * (f)
                + point1.y * 3 * fraction * (f) * (f)
                + point2.y * 3 * (f) * fraction * fraction
                + point3.y * fraction * fraction * fraction);
        return currentPosition;
    }

    public static PointF[] evaluate(int precision, PointF point0, PointF point1, PointF point2, PointF point3) {
        PointF[] currentPosition = new PointF[precision];
        for (int i = 0; i < precision; i++) {
            float t = (float) i / precision;
            currentPosition[i] = evaluate(t, point0, point1, point2, point3);
        }

        return currentPosition;
    }

    public static PointF evaluate(float fraction, PointF point0, PointF point1, PointF point2, PointF point3) {
        PointF currentPosition = new PointF();
        float f = 1f - fraction;
        //贝塞尔公式计算X点
        currentPosition.x = point0.x * (f) * (f) * (f)
                + point1.x * 3 * fraction * (f) * (f)
                + point2.x * 3 * (f) * fraction * fraction
                + point3.x * fraction * fraction * fraction;
        //贝塞尔公式计算Y点
        currentPosition.y = point0.y * (f) * (f) * (f)
                + point1.y * 3 * fraction * (f) * (f)
                + point2.y * 3 * (f) * fraction * fraction
                + point3.y * fraction * fraction * fraction;
        return currentPosition;
    }
}
