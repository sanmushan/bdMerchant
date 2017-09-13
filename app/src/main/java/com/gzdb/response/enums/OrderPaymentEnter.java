package com.gzdb.response.enums;

/**
 * Created by zhumg on 4/24.
 */
public enum OrderPaymentEnter {

    COMPOSITE(1, "合并支付"),//合并时,传 sequenceNumber
    INDEPENDENT(2, "单独支付"),;//单独时,传 orderId

    private int type;
    private String name;

    private OrderPaymentEnter(int type, String name) {
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
