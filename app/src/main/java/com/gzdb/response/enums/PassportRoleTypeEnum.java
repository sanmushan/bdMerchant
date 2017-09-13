package com.gzdb.response.enums;

/**
 * <pre>
 *     <b>对应通行证的角色类型</b>
 *     <b>注意：</b>
 *     1、一个通行证可能存在多个角色，但对同一体系下的，只能存在一种角色；如：普通商家、供应商只能存在一个
 *     2、类型值1--10 为保留值，请不要使用
 * </pre>
 * @author chinahuangxc on 2017/4/24.
 */
public enum PassportRoleTypeEnum {

    /** 1 -- 普通商家 */
    MERCHANT(11, "普通商家"),
    /** 2 -- 供应商 */
    SUPPLIER(12, "供应商"),

    /** 11 -- 仓库 */
    WAREHOUSE(21, "仓库"),
    /** 12 -- 主仓 */
    MAIN_WAREHOUSE(22, "主仓"),

    ;
    private int key;
    private String value;

    PassportRoleTypeEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}