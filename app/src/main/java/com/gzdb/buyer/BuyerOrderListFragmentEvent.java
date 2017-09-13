package com.gzdb.buyer;

/**
 * Created by zhumg on 4/25.
 */
public class BuyerOrderListFragmentEvent {
    public static final int HTTP_UPDATE_DATA = 0;//http刷新数据
    public static final int CHECK_LIST_REFRESH = 1;//判断本地数据，并刷新界面

    public int eventType;
    //由于 orderFragment 会有多个，所以，fragmentIndex 代表每个的索引
    public int fragmentIndex;

    public BuyerOrderListFragmentEvent(int fragmentIndex) {
        this.fragmentIndex = fragmentIndex;
    }
    public BuyerOrderListFragmentEvent(int fragmentIndex, int eventType) {
        this.fragmentIndex = fragmentIndex;
        this.eventType = eventType;
    }
}
