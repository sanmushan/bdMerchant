package com.gzdb.response.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/26 0026.
 */

public enum OrderTypeEnum {

    /** 1 - 销售订单 */
    SALE_ORDER_TYPE(1, "销售订单"),
    /** 2 - 调拨订单 */
    ALLOCATION_ORDER_TYPE(2, "调拨订单"),
    /** 3 - 采购订单 */
    PURCHASE_ORDER_TYPE(3, "采购订单"),
    /** 4 - 扫码订单 */
    SCAN_ORDER_TYPE(4, "扫码订单"),
    /** 5 - 渠道订单 */
    CHANNEL_ORDER_TYPE(5, "渠道订单"),
    /** 6 - 合作订单 */
    PARTNER_ORDER_TYPE(6, "合作订单"),
    ;

    private int type;
    private String name;

    private OrderTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, OrderTypeEnum> ORDER_TYPE_ENUM_MAP = new HashMap<>();

    static {
        OrderTypeEnum[] orderTypeEnums = OrderTypeEnum.values();
        for (OrderTypeEnum orderTypeEnum : orderTypeEnums) {
            ORDER_TYPE_ENUM_MAP.put(orderTypeEnum.getType(), orderTypeEnum);
        }
    }

    public static OrderTypeEnum getOrderTypeEnum(int key) {
        return ORDER_TYPE_ENUM_MAP.get(key);
    }
}
