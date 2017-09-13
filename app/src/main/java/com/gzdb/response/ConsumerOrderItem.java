package com.gzdb.response;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public class ConsumerOrderItem implements java.io.Serializable {

    private long itemTemplateId;//"      : 100000,
    private long itemId;//"              : 100000,
    private long itemSnapshotId;            //快照ID，发货时专用
    private String itemName;//"            : "iPhone7",
    private long quantity;//"            : 购买总量
    private String unitName;//"            : "部",
    private long normalPrice;//"         : 638800,
    ////////////////////////////////////////////////
    private int unReceiptQuantity;//"   : 未收货数量,
    private int receiptQuantity;//"     : 收货数量,
    private int distributionQuantity;//"    : 配送中的数量0
    private int unShippingQuantity;//"  : 未发货数量

    public int getDiscountQuantity() {
        return discountQuantity;
    }

    public void setDiscountQuantity(int discountQuantity) {
        this.discountQuantity = discountQuantity;
    }

    private int discountQuantity;//"  : 库存

    public int getUnShippingQuantity() {
        return unShippingQuantity;
    }

    public void setUnShippingQuantity(int unShippingQuantity) {
        this.unShippingQuantity = unShippingQuantity;
    }

    public long getItemSnapshotId() {
        return itemSnapshotId;
    }

    public void setItemSnapshotId(long itemSnapshotId) {
        this.itemSnapshotId = itemSnapshotId;
    }

    public long getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(long itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getUnReceiptQuantity() {
        return unReceiptQuantity;
    }

    public void setUnReceiptQuantity(int unReceiptQuantity) {
        this.unReceiptQuantity = unReceiptQuantity;
    }

    public long getNormalPrice() {
        return normalPrice;
    }

    public void setNormalPrice(long normalPrice) {
        this.normalPrice = normalPrice;
    }

    public int getReceiptQuantity() {
        return receiptQuantity;
    }

    public void setReceiptQuantity(int receiptQuantity) {
        this.receiptQuantity = receiptQuantity;
    }

    public int getDistributionQuantity() {
        return distributionQuantity;
    }

    public void setDistributionQuantity(int distributionQuantity) {
        this.distributionQuantity = distributionQuantity;
    }

    //用于发货填入的数字
    private int sendCount;

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    //用于收货填入的数字
    public int shouCount;

    public int getShouCount() {
        return shouCount;
    }

    public void setShouCount(int shouCount) {
        this.shouCount = shouCount;
    }
}
