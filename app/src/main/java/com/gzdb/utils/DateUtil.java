package com.gzdb.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by PVer on 2017/6/10.
 */

public class DateUtil {
    /**
     *时间转换
     * @param time
     * @return
     */
    public static  String getToDate(long time){
        Date date=new Date(time);
        java.text.SimpleDateFormat f=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String timeString=f.format(date);
        return timeString;
    }

}
