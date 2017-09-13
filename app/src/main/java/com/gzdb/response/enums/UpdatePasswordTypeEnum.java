package com.gzdb.response.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhumg on 5/5.
 */
public enum UpdatePasswordTypeEnum {

    UPDATE_LOGIN_PASSWORD(1, "修改登录密码"),

    UPDATE_PAY_PASSWORD(2, "修改支付密码"),

    FIND_PASSWORD(3, "忘记密码，找回密码"),
    ;

    private int key;
    private String value;

    UpdatePasswordTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    private static final Map<Integer, UpdatePasswordTypeEnum> UPDATE_PASSWORD_MAP = new HashMap<>();

    static {
        UpdatePasswordTypeEnum[] updatePasswordTypeEnums = UpdatePasswordTypeEnum.values();
        for (UpdatePasswordTypeEnum updatePasswordTypeEnum : updatePasswordTypeEnums) {
            UPDATE_PASSWORD_MAP.put(updatePasswordTypeEnum.getKey(), updatePasswordTypeEnum);
        }
    }

    public static UpdatePasswordTypeEnum getUpdatePasswordTypeEnum(int key) {
        return UPDATE_PASSWORD_MAP.get(key);
    }
}
