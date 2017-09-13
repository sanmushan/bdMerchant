package com.gzdb.response;

import java.util.List;

/**
 * Created by liyunbiao on 2017/7/31.
 */

public class BackOrder {
    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getOrderSequenceNumber() {
        return orderSequenceNumber;
    }

    public void setOrderSequenceNumber(String orderSequenceNumber) {
        this.orderSequenceNumber = orderSequenceNumber;
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

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public String getShippingNickName() {
        return shippingNickName;
    }

    public void setShippingNickName(String shippingNickName) {
        this.shippingNickName = shippingNickName;
    }

    public String getShippingPhone() {
        return shippingPhone;
    }

    public void setShippingPhone(String shippingPhone) {
        this.shippingPhone = shippingPhone;
    }

    public String getShippingLocation() {
        return shippingLocation;
    }

    public void setShippingLocation(String shippingLocation) {
        this.shippingLocation = shippingLocation;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getReceiptNickName() {
        return receiptNickName;
    }

    public void setReceiptNickName(String receiptNickName) {
        this.receiptNickName = receiptNickName;
    }

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
    }

    public String getReceiptPhone() {
        return receiptPhone;
    }

    public void setReceiptPhone(String receiptPhone) {
        this.receiptPhone = receiptPhone;
    }

    public String getReceiptLocation() {
        return receiptLocation;
    }

    public void setReceiptLocation(String receiptLocation) {
        this.receiptLocation = receiptLocation;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public int getCollectingFees() {
        return collectingFees;
    }

    public void setCollectingFees(int collectingFees) {
        this.collectingFees = collectingFees;
    }

    public long getDistributionFee() {
        return distributionFee;
    }

    public void setDistributionFee(long distributionFee) {
        this.distributionFee = distributionFee;
    }

    public long getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public List<ConsumerOrderItem> getItemSnapshotArray() {
        return itemSnapshotArray;
    }

    public void setItemSnapshotArray(List<ConsumerOrderItem> itemSnapshotArray) {
        this.itemSnapshotArray = itemSnapshotArray;
    }

    public int getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(int deliverStatus) {
        this.deliverStatus = deliverStatus;
    }

    private String sequenceNumber;// - String 公用序号，预下单时生产
    private String orderSequenceNumber;// - String 订单序列号，下单时生成，每个订单独立
    private long orderId;// - long 订单ID
    private String createTime;// - String 下单时间，格式：yyyy-MM-dd HH:mm:ss
    private int orderStatus;// - int 订单状态，具体参考：OrderStatusEnum
    private String statusValue;// - String 状态值展示，前端直接展示即可


    private int orderType;
    private String shippingNickName;
    private String shippingPhone;
    private String shippingLocation;
    private String shippingAddress;

    private String receiptNickName;
    private String receiptAddress;
    private String receiptPhone;
    private String receiptLocation;

    private String totalDistance;
    private int collectingFees;
    private long distributionFee;
    private long totalPrice;
    private String remark;

    private List<ConsumerOrderItem> itemSnapshotArray;

    private int deliverStatus;
}
