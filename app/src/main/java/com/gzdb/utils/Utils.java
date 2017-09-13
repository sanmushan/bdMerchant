package com.gzdb.utils;

import android.content.Context;

import com.zhumg.anlib.ActivityManager;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2017/3/26 0026.
 */

public class Utils {

    public static double toYuan(long price) {
        double v = (double)price / 100;
        return Double.parseDouble(doubleToTowStr(v));
    }

    public static String toYuanStr(long price) {
        double v = (double)price / 100;
        return doubleToTowStr(v);
    }

    public static double doubleToTow(double price) {
        return Double.parseDouble(doubleToTowStr(price));
    }

    public static String doubleToTowStr(double price) {
        //截取2位double
        String vs = String.valueOf(price);
        StringBuilder sb = new StringBuilder();
        boolean point = false;
        int pointCount = 0;
        for (int i = 0; i < vs.length(); i++) {
            char c = vs.charAt(i);
            if (c == '.') {
                point = true;
                sb.append(c);
                continue;
            }
            if (point) {
                pointCount++;
                //只截2位
                if (pointCount == 3) {
                    break;
                }
            }
            sb.append(c);
        }
        //如果未够2位数
        if(pointCount != 2) {
            int c = 2 - pointCount;
            for (int i = 0; i < c; i++) {
                sb.append("0");
            }
        }
        return sb.toString();
    }

    /** 加法 */
    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /** 乘法 */
    public static double mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
}
