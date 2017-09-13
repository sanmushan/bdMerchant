package com.gzdb.utils;

import com.gzdb.response.DetailsBean;

import java.util.Comparator;

/**
 * Created by liyunbiao on 2017/8/17.
 */

public class ProductSorts  implements Comparator {

    @Override
    public int compare(Object lhs, Object rhs) {
        DetailsBean b1=(DetailsBean)lhs;
        DetailsBean b2=(DetailsBean)rhs;
        int flag =Integer.parseInt(b2.getStatus())-Integer.parseInt(b1.getStatus());
        return flag;
    }
}
