package com.gzdb.response;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public class Currency {

    private long id;// - long 货币的ID
    private String name;//- long 货币名字
    private long type;// - long 类型值
    private long channelId;// - long 所属渠道(用户进件时决定，不能修改)
    private long currentAmount;// - long 可用余额
    private long freezeAmount;// - long 冻结余额

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public long getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(long currentAmount) {
        this.currentAmount = currentAmount;
    }

    public long getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(long freezeAmount) {
        this.freezeAmount = freezeAmount;
    }
}
