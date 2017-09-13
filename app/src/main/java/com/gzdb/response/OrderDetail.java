package com.gzdb.response;

import java.util.List;

/**
 * Created by Administrator on 2017/3/31 0031.
 */

public class OrderDetail implements java.io.Serializable {

    private String sequenceNumber;// - String 公用序号，预下单时生产
    private String orderSequenceNumber;// - String 订单序列号，下单时生成，每个订单独立
    private long orderId;// - long 订单ID
    private String createTime;// - String 下单时间，格式：yyyy-MM-dd HH:mm:ss
    private int orderStatus;// - int 订单状态，具体参考：OrderStatusEnum
    private String statusValue;// - String 状态值展示，前端直接展示即可

    private String dailySort;//排号

    private List<String> orderReceiptImgs;

    public String getDailySort() {
        return dailySort;
    }

    public void setDailySort(String dailySort) {
        this.dailySort = dailySort;
    }

    private List<ConsumerOrderItem> itemSnapshotArray;

    private byte collectingFees;// - byte 是否为代收订单，参考CollectingFeesEnum
    private long distributionFee;// - long 配送费，单位：分
    private long totalPrice;// - long 订单价格，此处为原价，单位：分
    private long actualPrice;// - long 实收费用，单位：分
    private long discountPrice;// - long 实收费用，单位：分

    private long timeout;// - long 剩余失效时长，单位：毫秒
    private byte isInvalid;// - byte 是否有效，失效后可发起重新购买功能；参考GlobalAppointmentOptEnum.LOGIC_TRUE 有效、GlobalAppointmentOptEnum.LOGIC_FALSE 无效

    private String courierNickName;// - String 快递员姓名
    private String courierPhone;// - String 快递员联系号码

    private String shippingNickName;// - String 发货人姓名
    private String shippingAddress;// - String 发货地址，注意：地址已格式化，如：广东省广州市天河区员村一横路广纺联
    private String shippingPhone;// - String 发货人联系号码
    private String shippingLocation;// - String 发货地址经纬度，格式：latitude,longitude

    private String receiptNickName;// - String 收货人姓名
    private String receiptAddress;// - String 收货地址
    private String receiptPhone;// - String 收货人联系号码
    private String receiptLocation;// - String 收货地址经纬度，格式：latitude,longitude

    private String totalDistance;// - String 配送总距离，已格式化，前端直接展示即可

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    private String qrCode;// - String 配送总距离，已格式化，前端直接展示即可

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

    public String getShippingNickName() {
        return shippingNickName;
    }

    public void setShippingNickName(String shippingNickName) {
        this.shippingNickName = shippingNickName;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
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

    public List<ConsumerOrderItem> getItemSnapshotArray() {
        return itemSnapshotArray;
    }

    public void setItemSnapshotArray(List<ConsumerOrderItem> itemSnapshotArray) {
        this.itemSnapshotArray = itemSnapshotArray;
    }

    public byte getCollectingFees() {
        return collectingFees;
    }

    public void setCollectingFees(byte collectingFees) {
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

    public long getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(long actualPrice) {
        this.actualPrice = actualPrice;
    }

    public long getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(long discountPrice) {
        this.discountPrice = discountPrice;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public byte getIsInvalid() {
        return isInvalid;
    }

    public void setIsInvalid(byte isInvalid) {
        this.isInvalid = isInvalid;
    }

    public String getCourierNickName() {
        return courierNickName;
    }

    public void setCourierNickName(String courierNickName) {
        this.courierNickName = courierNickName;
    }

    public String getCourierPhone() {
        return courierPhone;
    }

    public void setCourierPhone(String courierPhone) {
        this.courierPhone = courierPhone;
    }

    public List<String> getOrderReceiptImgs() {
        return orderReceiptImgs;
    }

    public void setOrderReceiptImgs(List<String> orderReceiptImgs) {
        this.orderReceiptImgs = orderReceiptImgs;
    }
}
