package com.gzdb.response.enums;

/**
 * Created by zhumg on 3/28.
 */
public enum BeyondControllTypeEnum {

    CAN_NOT_BEYOND(0, "不可超出限制"),
    BATCH(1, "超出时以最高价购买"),
    ;

    private int type;
    private String name;

    private BeyondControllTypeEnum(int type, String name) {
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
