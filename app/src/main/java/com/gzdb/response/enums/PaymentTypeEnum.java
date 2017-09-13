package com.gzdb.response.enums;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public enum PaymentTypeEnum {

    ALIPAY(1, "支付宝", "ALIPAY"),
    BALANCE(2, "余额", "BALANCE"),
    UNKNOWN(3, "未知", "UNKNOWN"),
    WEIXIN_JS(4, "微信公众号", "WEIXIN_JS"),
    WEIXIN_NATIVE(5, "微信", "WEIXIN_NATIVE"),
    PARTNER_ORDER_TYPE(6, "合作订单", "PARTNER_ORDER_TYPE"),
    ;

    private int type;
    private String name;
    private String enName;

    private PaymentTypeEnum(int type, String name, String enName) {
        this.type = type;
        this.name = name;
        this.enName = enName;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getEnName() {
        return enName;
    }
}
