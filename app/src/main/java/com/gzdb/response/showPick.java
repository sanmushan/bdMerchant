package com.gzdb.response;

import java.util.List;

/**
 * Created by liyunbiao on 2017/8/11.
 */

public class showPick {

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getSrcReceiptNickName() {
        return srcReceiptNickName;
    }

    public void setSrcReceiptNickName(String srcReceiptNickName) {
        this.srcReceiptNickName = srcReceiptNickName;
    }

    public String getSrcWarehouseId() {
        return srcWarehouseId;
    }

    public void setSrcWarehouseId(String srcWarehouseId) {
        this.srcWarehouseId = srcWarehouseId;
    }

    public String getSrcWarehouseName() {
        return srcWarehouseName;
    }

    public void setSrcWarehouseName(String srcWarehouseName) {
        this.srcWarehouseName = srcWarehouseName;
    }

    public String getDaySortNumber() {
        return daySortNumber;
    }

    public void setDaySortNumber(String daySortNumber) {
        this.daySortNumber = daySortNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderSequenceNumber() {
        return orderSequenceNumber;
    }

    public void setOrderSequenceNumber(String orderSequenceNumber) {
        this.orderSequenceNumber = orderSequenceNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String  createTime;
    private String warehouseId;
    private String srcReceiptNickName;
    private String srcWarehouseId;
    private String srcWarehouseName;
    private String daySortNumber;
    private String id;
    private String orderSequenceNumber;
    private String status;

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getSrcReceiptAddress() {
        return srcReceiptAddress;
    }
    public void setSrcReceiptAddress(String srcReceiptAddress) {
        this.srcReceiptAddress = srcReceiptAddress;
    }

    private String qrCode;
    private String srcReceiptAddress;

    public List<DetailsBean> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsBean> details) {
        this.details = details;
    }

    private List<DetailsBean> details;//": 0
}
