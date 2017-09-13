package com.gzdb.buyer;

/**
 * Created by zhumg on 4/25.
 */
public class BuyerActivityEvent {
    //切换fragment 索引
    public int fragmentIndex;

    public BuyerActivityEvent() {
    }

    public BuyerActivityEvent(int index) {
        this.fragmentIndex = index;
    }
}
