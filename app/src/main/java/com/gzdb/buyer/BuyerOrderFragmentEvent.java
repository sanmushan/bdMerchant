package com.gzdb.buyer;

/**
 * Created by zhumg on 4/25.
 */
public class BuyerOrderFragmentEvent {

    //由于 orderFragment 会有多个，所以，fragmentIndex 代表每个的索引
    public int fragmentIndex;

    public BuyerOrderFragmentEvent(int fragmentIndex) {
        this.fragmentIndex = fragmentIndex;
    }

}
