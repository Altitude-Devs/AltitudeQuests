package com.alttd.altitudequests.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Utilities {
    public static double round(double num, int precision) {
        double scale = Math.pow(10, precision);
        return ((int) (num * scale)) / scale;
    }

    public static int randomOr0(int max) {
        if (max <= 1)
            return 0;
        return new Random().nextInt(0, max);
    }

    public static int getYearDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return (calendar.get(Calendar.YEAR) * 1000) + calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static String formatName(String name) {
        name = name.toLowerCase().replaceAll("_", " ");
        return name.replace(name.charAt(0), Character.toUpperCase(name.charAt(0)));
    }
}
