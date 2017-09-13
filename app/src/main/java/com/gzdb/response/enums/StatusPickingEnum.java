package com.gzdb.response.enums;

/**
 * Created by liyunbiao on 2017/8/8.
 */

public enum  StatusPickingEnum {

    NOPICKING(0, "未拣货"),
    MERGEORDER(1, "合单"),
    NODATA(2, "缺货"),
    FINISH(3, "已完成"),
    ;
    private int type;
    private String name;

    private StatusPickingEnum(int type, String name) {
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
