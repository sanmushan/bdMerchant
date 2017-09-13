package com.gzdb.utils.baidu;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;

public class BaiduLocationManager {

    private static LocationClient mLocationClient;
    private static BaiduLocationNotify baiduNotify;

    //当前地址
    private static LocationAddress now_locationAddress;
    private static LocationAddress select_locationAddress;

    public static LocationAddress getNowAddress() {
        return now_locationAddress;
    }

    public static LocationAddress getSelectAddress() {
        if (select_locationAddress == null) {
            select_locationAddress = new LocationAddress();
        }
        return select_locationAddress;
    }

    public static LocationAddress getInitSelectAddress() {
        return select_locationAddress;
    }

    public static void init(Context context) {
        //初始化百度SDK
        SDKInitializer.initialize(context);

        mLocationClient = new LocationClient(context);
        mLocationClient.registerLocationListener(new MyLocationListener());
        //startLocation(null);
    }

    public static void startLocation(BaiduLocationNotify notify) {
        try {
            baiduNotify = notify;
            LocationClientOption option = new LocationClientOption();
            option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
            option.setCoorType("bd09ll");//返回的定位结果是百度经纬度，默认值gcj02
            option.setOpenGps(true); // 打开gps
            option.setScanSpan(30000);// 间隔时间为5000ms
            option.setIsNeedAddress(true);
            mLocationClient.setLocOption(option);
            mLocationClient.start();
        } catch (Exception ex) {
            ex.printStackTrace();
            notify.callNotify(null);
            mLocationClient.stop();
        }
    }

    public static interface BaiduLocationNotify {
        public void callNotify(LocationAddress location);
    }

    /**
     * 实现实时位置回调监听
     */
    private static class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null || location.getAddrStr() == null) {
                baiduNotify.callNotify(null);
                mLocationClient.stop();
                return;
            }
            Log.e("zhumg", "定位信息：" + location);
            if (now_locationAddress == null) {
                now_locationAddress = new LocationAddress();
            }
            now_locationAddress.copyBd(location);

            if (baiduNotify != null) {
                baiduNotify.callNotify(now_locationAddress);
            }
            Log.e("zhumg", now_locationAddress.toString());
            mLocationClient.stop();
        }
    }
}
