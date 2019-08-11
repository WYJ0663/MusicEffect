package com.example.musiceffect.utils;

public class ColorUtil {
    public static int[] getColors(int color, int count, int offset) {
        int[] colors = new int[count];

        int a = (color >> 24) & 0xff;
        int r = (color >> 16) & 0xff;
        int g = (color >> 8) & 0xff;
        int b = (color) & 0xff;

        int index;
        int c = r;
        if (g > b) {
            if (r > g) {
                index = 0;
                c = r;
            } else {
                index = 1;
                c = g;
            }
        } else {
            if (r > b) {
                index = 0;
                c = r;
            } else {
                c = b;
                index = 2;
            }
        }

        for (int i = 0; i < count; i++) {
            int temp = (c + offset * (i - count / 2)) % 0XFF;

            if (i == 1) {
                colors[i] = createColor(a, r, temp, b);
            } else if (i == 2) {
                colors[i] = createColor(a, r, g, temp);
            } else {
                colors[i] = createColor(a, temp, g, b);
            }
        }

        return colors;
    }

    private static int createColor(int a, int r, int g, int b) {
        return (a << 24) |
                (r << 16) |
                (g << 8) |
                b;
    }

    public static int setAlpha(int color, int alpha) {
//        int a = (color >> 24) & 0xff;
        int r = (color >> 16) & 0xff;
        int g = (color >> 8) & 0xff;
        int b = (color) & 0xff;
        return createColor(alpha, r, g, b);
    }
}
