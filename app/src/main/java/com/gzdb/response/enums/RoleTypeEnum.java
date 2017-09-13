package com.gzdb.response.enums;

/**
 * Created by Administrator on 2017/3/26 0026.
 */

public enum RoleTypeEnum {

    CONSUMER(1, "消费者"),
    MERCHANT(2, "商家"),
    COURIER(3, "快递员"),
    ;

    private int type;
    private String name;

    private RoleTypeEnum(int type, String name) {
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
