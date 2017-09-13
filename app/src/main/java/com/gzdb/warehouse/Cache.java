package com.gzdb.warehouse;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.gzdb.response.Api;
import com.gzdb.response.HomePageRecommendItemTypes;
import com.gzdb.response.Passport;
import com.gzdb.response.Warehouse;
import com.gzdb.response.enums.ClientTypeEnum;
import com.gzdb.response.enums.PassportRoleTypeEnum;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class Cache {

    /**
     * sdcard
     */
    public static final String SDCARD_FOLDER = Environment.getExternalStorageDirectory().toString();

    /**
     * 根目录
     */
    public static final String ROOT_FOLDER = SDCARD_FOLDER + "/db_merchant/";

    /**
     * 图片目录
     */
    public static final String IMAGE_FOLDER = ROOT_FOLDER + "img/";

    //登陆的帐号
    public static Passport passport;
    //登陆帐号的类型
    public static ClientTypeEnum clientTypeEnum;
    //角色类型
    public static PassportRoleTypeEnum passportRoleTypeEnum;

    //仓管商品类型缓存
    private static List<HomePageRecommendItemTypes> types = new ArrayList<>();
    //仓管商品类型上一次刷新加载时间
    private static long old_load_itemtype_time;

    public static void getTypes(final Context context, long passportId, String location,
                                final CacheCallback callback) {

        Log.e("getTypes", "getTypes....................1 " + passportId);

        long time = System.currentTimeMillis();
        if (types.size() > 0 && time - old_load_itemtype_time < 60000) {// 1分钟更新1次
            callback.onSuccess(types);
            Log.e("getTypes", "getTypes....................2 " + passportId);
            return;
        }

        //清除原来的
        types.clear();

        old_load_itemtype_time = time;

        Map map = new HashMap();
        map.put("passportId", String.valueOf(passportId));
        map.put("location", location);

        Log.e("getTypes", "getTypes....................3 " + location);
        //加载 商品类型 列表
        Http.post(context, map, Api.SHOW_ITEM_TYPES, new HttpCallback<List<HomePageRecommendItemTypes>>("itemTypes") {
            @Override
            public void onSuccess(List<HomePageRecommendItemTypes> data) {
                types.addAll(data);
                callback.onSuccess(types);
            }

            @Override
            public void onFailure() {
                callback.onFailure(code, msg);
            }
        });
    }

    public static interface CacheCallback<T> {
        public void onSuccess(T data);

        public void onFailure(int code, String msg);
    }


    //仓库列表
    public static List<Warehouse> warehouses = new ArrayList<>();
    //仓库上一次刷新加载时间
    private static long old_load_warehouses_time;

    public static void getWarehouses(final Context context, final CacheCallback callback) {

        long time = System.currentTimeMillis();
        if (warehouses.size() > 0 && time - old_load_warehouses_time < 60000) {// 1分钟更新1次
            callback.onSuccess(warehouses);
            return;
        }

        //清除原来的
        warehouses.clear();

        old_load_warehouses_time = time;

        Http.post(context, new HashMap(), Api.GET_WAREHOUSE, new HttpCallback<List<Warehouse>>("datas") {
            @Override
            public void onSuccess(List<Warehouse> data) {
                warehouses.addAll(data);
                callback.onSuccess(warehouses);
            }
            @Override
            public void onFailure() {
                callback.onFailure(code, msg);
            }
        });
    }


    //仓管商品类型缓存
    private static List<HomePageRecommendItemTypes> buyer_types = new ArrayList<>();
    //仓管商品类型上一次刷新加载时间
    private static long old_load_buyer_types_time;

    public static void getBuyerTypes(final Context context, long passportId,
                                final CacheCallback callback) {

        Log.e("getTypes", "getTypes....................1 " + passportId);

        long time = System.currentTimeMillis();
        if (buyer_types.size() > 0 && time - old_load_buyer_types_time < 60000) {// 1分钟更新1次
            callback.onSuccess(buyer_types);
            return;
        }

        //清除原来的
        buyer_types.clear();

        old_load_buyer_types_time = time;

        Map map = new HashMap();
        map.put("passportId", String.valueOf(passportId));
        //加载 商品类型 列表
        Http.post(context, map, Api.PURCHASE_ITEM_TYPES, new HttpCallback<List<HomePageRecommendItemTypes>>("datas") {
            @Override
            public void onSuccess(List<HomePageRecommendItemTypes> data) {
                buyer_types.addAll(data);
                callback.onSuccess(buyer_types);
            }

            @Override
            public void onFailure() {
                callback.onFailure(code, msg);
            }
        });
    }
}
