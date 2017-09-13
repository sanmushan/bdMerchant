package com.gzdb.response.enums;

/**
 * Created by zhumg on 4/14.
 */

/**
 * @author chinahuangxc on 2017/2/8.
 */
public enum SmsCodeTypeEnum {

    /** 1 -- 登录验证码 */
    LOGIN(1, "(登录验证码，五分钟内有效)", ""),
    /** 2 -- 注册验证码 */
    REGISTER(2, "(注册验证码，五分钟内有效)", ""),
    /** 3 -- 重置密码验证码 */
    MODIFY_PASSWORD(3, "(修改密码验证码，五分钟内有效)", ""),
    /** 4 -- 支付验证码 */
    PAYMENT_WORKS(4, "(支付验证码，五分钟内有效)", ""),;

    private int key;
    private String value;
    // 接入阿里云专用
    private String templateCode;

    SmsCodeTypeEnum(int key, String value, String templateCode) {
        this.key = key;
        this.value = value;
        this.templateCode = templateCode;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    @Override
    public String toString() {
        return value;
    }
}
