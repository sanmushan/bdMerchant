package com.gzdb.response;

/**
 * Created by zhumg on 5/8.
 */
public class SupplierProperties {

    private byte status;// - byte 营业状态，参考：WarehouseStatusEnum
    private int deliveryDistance;// - int 配送距离，单位：米
    private String printerNumber;// - String 打印机编号
    private String contactNumber;// - String 联系号码
    private String address;// - String 地址

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public int getDeliveryDistance() {
        return deliveryDistance;
    }

    public void setDeliveryDistance(int deliveryDistance) {
        this.deliveryDistance = deliveryDistance;
    }

    public String getPrinterNumber() {
        return printerNumber;
    }

    public void setPrinterNumber(String printerNumber) {
        this.printerNumber = printerNumber;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
