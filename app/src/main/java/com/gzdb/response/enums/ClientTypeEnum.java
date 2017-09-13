package com.gzdb.response.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chinahuangxc on 2017/4/7.
 */
public enum ClientTypeEnum {

    /**
     * 1、快递员
     */
    COURIER(1, "courier", "快递员", true),
    /**
     * 2、POS
     */
    POS(2, "pos", "POS", true),
    /**
     * 3、消费者
     */
    CONSUMER(3, "consumer", "消费者", false),
    /**
     * 4、商家
     */
    MERCHANT(4, "merchant", "商家", true),
    /**
     * 5、仓管
     */
    WAREHOUSE(5, "warehouse", "仓管", true),
    /**
     * 6、采购
     */
    PURCHASE(6, "purchase", "采购", true),
    PICKING(23, "PICKING", "拣货", true);


    private int key;
    private String code;
    private String name;
    private boolean check;

    ClientTypeEnum(int key, String code, String name, boolean check) {
        this.key = key;
        this.code = code;
        this.name = name;
        this.check = check;
    }

    public int getKey() {
        return key;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public boolean isCheck() {
        return check;
    }

    private static final Map<Integer, ClientTypeEnum> CLIENT_TYPE_ENUM_MAP = new HashMap<>();

    static {
        ClientTypeEnum[] clientTypeEnums = ClientTypeEnum.values();
        for (ClientTypeEnum clientTypeEnum : clientTypeEnums) {
            CLIENT_TYPE_ENUM_MAP.put(clientTypeEnum.getKey(), clientTypeEnum);
        }
    }

    public static ClientTypeEnum getClientTypeEnum(int key) {
        return CLIENT_TYPE_ENUM_MAP.get(key);
    }
}
