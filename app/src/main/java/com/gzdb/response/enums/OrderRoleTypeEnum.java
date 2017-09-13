package com.gzdb.response.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/26 0026.
 */

public enum OrderRoleTypeEnum {

    CONSUMER(1, "消费者"),
    MERCHANT(2, "商家"),
    COURIER(3, "快递员"),
    ;

    private int type;
    private String name;

    private OrderRoleTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, OrderRoleTypeEnum> ORDER_ROLE_TYPE_ENUM_MAP = new HashMap<>();

    static {
        OrderRoleTypeEnum[] orderRoleTypeEnums = OrderRoleTypeEnum.values();
        for (OrderRoleTypeEnum orderRoleTypeEnum : orderRoleTypeEnums) {
            ORDER_ROLE_TYPE_ENUM_MAP.put(orderRoleTypeEnum.getType(), orderRoleTypeEnum);
        }
    }

    public static OrderRoleTypeEnum getOrderRoleTypeEnum(int key) {
        return ORDER_ROLE_TYPE_ENUM_MAP.get(key);
    }
}
