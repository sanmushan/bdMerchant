package com.gzdb.response.enums;

/**
 * Created by Administrator on 2017/3/26 0026.
 * 订单状态 枚举
 */

public enum OrderStateEnum {

    HANDLER(1, "已处理"),
    BATCH(2, "待处理"),
    DELIVERY(3, "配送中"),
    FINISH(4, "已完成"),
    ;

    private int type;
    private String name;

    private OrderStateEnum(int type, String name) {
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
