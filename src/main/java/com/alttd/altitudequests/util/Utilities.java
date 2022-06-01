package com.alttd.altitudequests.util;

import java.util.Random;

public class Utilities {
    public static double round(double num, int precision) {
        double scale = Math.pow(10, precision);
        return ((int) (num * scale)) / scale;
    }

    public static int randomOr0(int max) {
        if (max <= 1)
            return 0;
        return new Random().nextInt(0, max - 1);
    }
}
