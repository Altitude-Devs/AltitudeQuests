package com.alttd.altitudequests.util;

public class Utilities {
    public static double round(double num, int precision) {
        double scale = Math.pow(10, precision);
        return ((int) (num * scale)) / scale;
    }
}
