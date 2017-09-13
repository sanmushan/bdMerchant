package com.gzdb.utils;

import android.os.Environment;

/**
 * 作   者：liyunbiao
 * 时   间：2017/5/24
 * 修 改 人：
 * 日   期：
 * 描   述：
 */

public class Const {

    public static final String SCRECTKEY = "EsrWZid27X2KJHWFXkwrrebH2YqXvl";
    public static final String ACCESSKEY = "iXpQFs4mbuUvIUWa";
    public static final char  CHAR7 = 7;
    public  static  final  String PAGESIZE="20";
    public  static  final  int PAGESIZE_INT=20;
    //图片存储地址
    public static String getUrl() {
        String url = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            url = Environment.getExternalStorageDirectory() + "/dianba_send/";
        } else {
            url = "\\sdcard\\dianba_send\\";
        }
        return url;
    }
}
