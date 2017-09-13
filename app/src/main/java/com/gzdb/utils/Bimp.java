package com.gzdb.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Bimp {
    public static int max = 0;

    public static ArrayList<ImageItem> tempSelectBitmap = new ArrayList<ImageItem>();   //选择的图片的临时列表

    public static Bitmap revitionImageSize(String path) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(
                new File(path)));
        BitmapFactory.Options options = new BitmapFactory.Options();
        //如果设置为True，那么BitmapFactory.decodeFile()并不会真的返回一个Bitmap回来，而是会把他的宽高
        //取回来给你，这样就不会占用太多的内存而频繁造成OOM
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(in, null, options);
        in.close();
        int i = 0;
        Bitmap bitmap = null;
        while (true) {
            // >>右移运算符 num >> 1 相当于num除以2
            if ((options.outWidth >> i <= 1000)
                    && (options.outHeight >> i <= 1000)) {
                in = new BufferedInputStream(
                        new FileInputStream(new File(path)));
                options.inSampleSize = (int) Math.pow(2.0D, i);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(in, null, options);
                break;
            }
            i += 1;
        }
        return bitmap;
    }

    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {//API 12
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }

}
