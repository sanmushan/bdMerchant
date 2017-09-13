package com.gzdb.response;


import com.gzdb.utils.baidu.BaiduAddressActivity;

/**
 * 编辑时使用的地址对象
 * Created by Administrator on 2017/3/30 0030.
 */

public class EditAddress implements java.io.Serializable {

    //地址要发个ID回来
    private long id;//地址ID
    private String name;// - String 收货人姓名，必填参数。
    private String alias;//- String 地址别名，必填参数。
    private String phoneNumber;// - String 联系人号码，必填参数。
    private String country;//- String 归属国家，非必填参数；默认为：中华人民共和国
    private String province;// - String 所在省份，必填参数；直辖市时填充为直辖市名称，如：北京市、重庆市等
    private String city;// - String 所在城市，必填参数。
    private String district;// - String 所在行政区域，必填参数。
    private String street;// - String 街道，非必填参数。
    private String streetNum;// - String 街道号，非必填参数。
    private String adcode;// - String 归属城市编码，必填参数。
    private String detailAddress;// - String 具体地址，必填参数。
    private double longitude;// - double 经度，必填参数；主要用于导航、距离计算等。
    private double latitude;// - double 纬度，必填参数；主要用于导航、距离计算等。
    private int type;// - int 地址类型
    private byte status;// - byte 地址状态，具体参考：AddressStatusEnum

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
        if (province != null) {
            sb.append(province).append(" ");
        }
        if (city != null) {
            sb.append(city).append(" ");
        }
        if (district != null) {
            sb.append(district).append(" ");
        }
        if (street != null) {
            sb.append(street);
        }
        if (streetNum != null) {
            sb.append(streetNum);
        }
        if (detailAddress != null) {
            sb.append(detailAddress);
        }
        address = sb.toString();
    }

    public boolean isDefault() {
        return status == 2;
    }

    public boolean isInvalid() {
        return status == 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        if(district == null) {
            return "pp";
        }
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        if(district == null) {
            return "dd";
        }
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNum() {
        return streetNum;
    }

    public void setStreetNum(String streetNum) {
        this.streetNum = streetNum;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    //选中标志
    private boolean select = false;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void inPoi(BaiduAddressActivity.BaiduPoiInfo poi) {

        this.latitude = Double.parseDouble(poi.latitude);
        this.longitude = Double.parseDouble(poi.longtitude);
        this.district = poi.district;
        this.city = poi.city;
        this.street = poi.name;
    }
}
