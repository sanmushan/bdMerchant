package com.gzdb.utils.baidu;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.baidu.location.BDLocation;

/**
 * Created by zhumg on 3/23.
 */
public class LocationAddress {

    private String district;//区
    private String street;//街道
    private String city;//城市
    private String address;
    private String latitude;
    private String longtitude;

    public String getDistrict() {
        return district;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public String getLocation() {
        return latitude + "," + longtitude;
    }

    public void copyBd(BDLocation bdLocation) {
        Log.e("zhumg", "BDLocation=" + bdLocation.getAddress().city);
        StringBuilder sb = new StringBuilder();
        this.city = bdLocation.getCity();
        if (this.city != null) {
            sb.append(city);
        }
        this.district = bdLocation.getAddress().district;
        if (this.district != null) {
            sb.append(district);
        }
        this.street = bdLocation.getAddress().street + bdLocation.getAddress().streetNumber;
        if (this.street != null) {
            sb.append(street);
        }
        this.address = sb.toString();
//        double[] dt = BDTransUtil.gcj2bd(bdLocation.getLatitude(), bdLocation.getLongitude());
//        this.latitude = dt[0] + "";//精度
//        this.longtitude = dt[1] + "";//纬度
        this.latitude = bdLocation.getLatitude() + "";
        this.longtitude = bdLocation.getLongitude() + "";
    }

    public void copy(LocationAddress locationAddress) {
        this.address = locationAddress.address;
        this.city = locationAddress.city;
        this.latitude = locationAddress.latitude;
        this.longtitude = locationAddress.longtitude;
        this.district = locationAddress.district;
        this.street = locationAddress.street;
    }


    public void copyPoi(BaiduAddressActivity.BaiduPoiInfo poiInfo) {
        this.address = poiInfo.address;
        this.city = poiInfo.city;
        this.latitude = poiInfo.latitude;
        this.longtitude = poiInfo.longtitude;
        this.district = poiInfo.district;
        this.street = "";
    }

    public void save(Context context) {
        SharedPreferences sp = context.getSharedPreferences("location", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("now_latitude", latitude);
        editor.putString("now_lontitude", longtitude);
        editor.putString("now_address", address);
        editor.putString("now_city", city);
        editor.putString("now_district", district);
        editor.putString("now_street", street);
        editor.commit();
    }

    public void load(Context context) {
        SharedPreferences sp = context.getSharedPreferences("location", 0);
        this.latitude = sp.getString("now_latitude", "0");
        this.longtitude = sp.getString("now_lontitude", "0");
        this.address = sp.getString("now_address", "");
        this.city = sp.getString("now_city", "");
        this.district = sp.getString("now_district", "");
        this.street = sp.getString("now_street", "");
    }

    @Override
    public String toString() {
        return "LocationAddress{" +
                "city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longtitude='" + longtitude + '\'' +
                '}';
    }
}
