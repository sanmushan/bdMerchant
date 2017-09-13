package com.gzdb.response;

import com.zhumg.anlib.widget.AdapterModel;

import java.util.List;

/**
 * Created by Administrator on 2017/3/26 0026.
 */

public class ConsumerOrder implements AdapterModel{

    private String sequenceNumber;//"      : "90820170313103426784044523",
    private String orderSequenceNumber;
    private long orderId;//"             : 100006,
    private String createTime;//"          : "2017-03-13 10:34:37",
    private long totalPrice;
    private int orderStatus;
    private String statusValue;//"         : "已支付",
    private String receiptNickName;
    private String shippingNickName;//"     :　
    private String dailySort;//排号
    private int deliverStatus;

    public List<ConsumerOrderItem> getItemSnapshotArray() {
        return itemSnapshotArray;
    }

    public void setItemSnapshotArray(List<ConsumerOrderItem> itemSnapshotArray) {
        this.itemSnapshotArray = itemSnapshotArray;
    }

    private List<ConsumerOrderItem> itemSnapshotArray;

    public int getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(int deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    public String getDailySort() {
        return dailySort;
    }

    public void setDailySort(String dailySort) {
        this.dailySort = dailySort;
    }

    public String getShippingNickName() {
        return shippingNickName;
    }

    public void setShippingNickName(String shippingNickName) {
        this.shippingNickName = shippingNickName;
    }

    public String getOrderSequenceNumber() {
        return orderSequenceNumber;
    }

    public void setOrderSequenceNumber(String orderSequenceNumber) {
        this.orderSequenceNumber = orderSequenceNumber;
    }

    public String getReceiptNickName() {
        return receiptNickName;
    }

    public void setReceiptNickName(String receiptNickName) {
        this.receiptNickName = receiptNickName;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    @Override
    public int getUiType() {
        return 0;
    }
}
