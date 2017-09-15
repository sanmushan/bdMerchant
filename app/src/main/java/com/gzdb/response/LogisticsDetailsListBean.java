package com.gzdb.response;

/**
 * Created by PVer on 2017/9/15.
 */

public class LogisticsDetailsListBean  implements java.io.Serializable {
    private String id ;
    private String quickMark;
    private String batchNo;
    private String orderSequenceNumber ;
    private String packageTotle;
    private String connect_totle;
    private String warehouseId;
    private String warehouseName;
    private String pickPassportId;
    private String pickName;
    private String status;
    private String type;
    private String amount;
    private String time;
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuickMark() {
        return quickMark;
    }

    public void setQuickMark(String quickMark) {
        this.quickMark = quickMark;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getOrderSequenceNumber() {
        return orderSequenceNumber;
    }

    public void setOrderSequenceNumber(String orderSequenceNumber) {
        this.orderSequenceNumber = orderSequenceNumber;
    }

    public String getPackageTotle() {
        return packageTotle;
    }

    public void setPackageTotle(String packageTotle) {
        this.packageTotle = packageTotle;
    }

    public String getConnect_totle() {
        return connect_totle;
    }

    public void setConnect_totle(String connect_totle) {
        this.connect_totle = connect_totle;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getPickPassportId() {
        return pickPassportId;
    }

    public void setPickPassportId(String pickPassportId) {
        this.pickPassportId = pickPassportId;
    }

    public String getPickName() {
        return pickName;
    }

    public void setPickName(String pickName) {
        this.pickName = pickName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
