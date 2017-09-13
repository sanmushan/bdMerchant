package com.gzdb.response.enums;

/**
 * Created by zhumg on 3/29.
 */
public enum  DeviceTypeEnum {

    /** -1 - 设备类型 -- 未知 */
    DEVICE_TYPE_UNKNOW(-1, "未知设备"),
    /** 1 - 设备类型 -- Android */
    DEVICE_TYPE_ANDROID(1, "Android"),
    /** 2 - 设备类型 -- IOS */
    DEVICE_TYPE_IOS(2, "IOS"),
    /** 3 - 设备类型 -- H5 */
    DEVICE_TYPE_H5(3, "H5"),
    ;

    private int type;
    private String name;

    private DeviceTypeEnum(int type, String name) {
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
