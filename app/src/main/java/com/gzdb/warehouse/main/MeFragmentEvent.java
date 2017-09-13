package com.gzdb.warehouse.main;

/**
 * Created by zhumg on 5/9.
 */
public class MeFragmentEvent {

    public static final int SET_PRINT = 0;

    private int type;
    private String value;

    public MeFragmentEvent(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
