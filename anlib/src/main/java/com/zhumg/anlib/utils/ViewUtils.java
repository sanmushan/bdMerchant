package com.zhumg.anlib.utils;

import android.view.View;

/**
 * Created by zhumg on 2017/3/24 0024.
 */

public class ViewUtils {

    public static <T> T find(View view, int id) {
        return (T)view.findViewById(id);
    }
}
