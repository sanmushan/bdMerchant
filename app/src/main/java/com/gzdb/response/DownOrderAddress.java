package com.gzdb.response;

/**
 * 下单时使用的地址对象
 * Created by zhumg on 3/29.
 */
public class DownOrderAddress implements java.io.Serializable {

    private String receiptProvince;// - String 收货省份，必填参数。
    private String receiptCity;// - String 收货城市，必填参数。
    private String receiptDistrict;// - String 收货区域，必填参数
    private String receiptAddress;// - String 具体收货地址(详细地址)，必填参数。
    private String receiptNickName;// - String 收货人昵称，必填参数。
    private String receiptPhone;// - String 收货人联系号码，必填参数。
    private String receiptLocation;// - String 收货地址经纬度，非必填参数；请尽量提供，方便距离跟踪；格式为：latitude,longitude。

    private String address;

    public String getAddress() {
        if (address != null) {
            return address;
        } else {
            refreshAddress();
        }
        return address;
    }

    public void refreshAddress() {
        StringBuilder sb = new StringBuilder();
        if (receiptProvince != null) {
            sb.append(receiptProvince).append(" ");
        }
        if (receiptCity != null) {
            sb.append(receiptCity).append(" ");
        }
        if (receiptDistrict != null) {
            sb.append(receiptDistrict).append(" ");
        }
        if (receiptAddress != null) {
            sb.append(receiptAddress);
        }
        address = sb.toString();
    }

    public String getReceiptProvince() {
        return receiptProvince;
    }

    public void setReceiptProvince(String receiptProvince) {
        this.receiptProvince = receiptProvince;
    }

    public String getReceiptCity() {
        return receiptCity;
    }

    public void setReceiptCity(String receiptCity) {
        this.receiptCity = receiptCity;
    }

    public String getReceiptDistrict() {
        return receiptDistrict;
    }

    public void setReceiptDistrict(String receiptDistrict) {
        this.receiptDistrict = receiptDistrict;
    }

    public String getReceiptAddress() {
        return receiptAddress;
    }

    public void setReceiptAddress(String receiptAddress) {
        this.receiptAddress = receiptAddress;
    }

    public String getReceiptNickName() {
        return receiptNickName;
    }

    public void setReceiptNickName(String receiptNickName) {
        this.receiptNickName = receiptNickName;
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
}
