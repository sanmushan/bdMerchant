package com.gzdb.response.enums;

/**
 * Created by Administrator on 2017/3/26 0026.
 * 订单状态 枚举
 */

public enum StatusEnterEnum {
// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 仓库对商家 使用 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    /** 1 -- 待处理(对应快递员时为：已接单) */
    HANDLER(1, "待处理"),
    /** 2 -- 分批送 */
    BATCH(2, "分批送"),
    /** 3 -- 配送中 */
    DELIVERY(3, "配送中"),
// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 仓库对商家 使用 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 商家使用 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    /** 11 -- 未付款 */
    UN_PAYMENT(11, "未付款"),
    /** 12 -- 配送中 */
    C_DELIVERY(12, "配送中"),
    /** 13 -- 待收货 */
    C_RECEIPT(13, "待收货"),
// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 商家使用 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 仓库对上级 使用 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 我向上线调拨的订单
    /** 21 -- 待处理 */
    A_HANDLER(21, "待处理"),
    /** 22 -- 分批送 */
    A_BATCH(22, "分批送"),
    /** 22 -- 配送中 */
    A_DELIVERY(23, "配送中"),
// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 仓库对上级 使用 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 仓库对下级 使用 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 其它他向我调拨的订单
    /** 31 -- 待处理 */
    M_HANDLER(31, "待处理"),
    /** 32 -- 分批送 */
    M_BATCH(32, "分批送"),
    /** 33 -- 配送中 */
    M_DELIVERY(33, "配送中"),
// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 仓库对下级 使用 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 采购对仓库 使用 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 采购员向供应商采购商品给仓库
    /** 41 -- 待处理 */
    W_HANDLER(41, "待处理"),
    /** 42 -- 分批送 */
    W_BATCH(42, "分批送"),
    /** 43 -- 配送中 */
    W_DELIVERY(43, "配送中"),
// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 采购对仓库 使用 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 快递员使用 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    /** 51 -- 已接单 */
    C_ACCEPTED(51, "已接单"),
    /** 52 -- 配送中 */
    C_DISTRIBUTION(52, "配送中"),
    /** 53 -- 分批送 */
    C_BATCH(53, "分批送"),
// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 快递员使用 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 采购员使用 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    /** 61 -- 处理中 */
    P_HANDLER(61, "处理中"),
    /** 62 -- 分批送 */
    P_BATCH(62, "分批送"),
    /** 63 -- 配送中 */
    P_DELIVERY(63, "配送中"),
// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 采购员使用 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 共用 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    /** 4 -- 已完成 */
    FINISH(4, "已完成"),
// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 共用 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    ;


    private int type;
    private String name;

    private StatusEnterEnum(int type, String name) {
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
