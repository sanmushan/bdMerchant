package com.gzdb.response.enums;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public enum CurrencyTypeEnum {

    YUER(-1, "余额"),
    ;

    private int type;
    private String name;

    private CurrencyTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
