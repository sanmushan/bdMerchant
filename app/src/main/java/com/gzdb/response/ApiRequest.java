package com.gzdb.response;

import android.content.Context;

import com.gzdb.response.enums.ClientTypeEnum;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.StatusEnterEnum;
import com.gzdb.response.enums.StatusPickingEnum;
import com.gzdb.warehouse.Cache;
import com.zhumg.anlib.utils.ApkUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class ApiRequest {

    /**
     * 登陆逻辑
     *
     * @param name
     * @param password
     */
    public static Map login(Context context, String name, String password, ClientTypeEnum clientTypeEnum) {
        //尝试登陆
        final Map map = new HashMap();
        map.put("username", name);
        map.put("password", password);
        map.put("clientType", String.valueOf(clientTypeEnum.getKey()));
        map.put("versionIndex", String.valueOf(ApkUtils.getVersionCode(context)));
        return map;
    }
    /**
     * 大分类
     *
     */
    public static Map supplierItemTypes(int parentItemTypeId) {
        //尝试登陆
        final Map map = new HashMap();
     //   map.put("parentItemTypeId",parentItemTypeId+"");
        return map;
    }

    /**
     * 拿验证码
     *
     * @param phoneNumber
     */
    public static Map requestVerificationCode(String phoneNumber, String smsType) {
        Map map = new HashMap();
        map.put("phoneNumber", phoneNumber);
        map.put("type", smsType);
        return map;
    }

    /**
     * 获取商品
     */
    public static Map searchItem(long passportId, String longitude, String latitude, long itemTypeId, int sortType, int sortValue, String searchKeyValue, int pageIndex) {
        Map map = new HashMap();
        map.put("passportId", String.valueOf(passportId));
        map.put("longitude", longitude);
        map.put("latitude", latitude);
        map.put("itemTypeId", String.valueOf(itemTypeId));
        map.put("sortType", String.valueOf(sortType));
        map.put("sortValue", String.valueOf(sortValue));
        if (searchKeyValue != null) {
            map.put("searchKeyValue", String.valueOf(searchKeyValue));
        }
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(Api.PAGE_SIZE));
        return map;
    }

    /**
     * 订单列表
     */
    public static Map orders(long passportId, OrderRoleTypeEnum roleType, OrderTypeEnum orderType, StatusEnterEnum statusEnter, int pageIndex) {
        Map map = new HashMap();
        map.put("passportId", String.valueOf(passportId));
        map.put("roleType", String.valueOf(roleType.getType()));
        map.put("orderType", String.valueOf(orderType.getType()));
        map.put("statusEnter", String.valueOf(statusEnter.getType()));
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("clientType", String.valueOf(Cache.clientTypeEnum.getKey()));
        map.put("pageSize", String.valueOf(Api.PAGE_SIZE));
        return map;
    }
    /**
     * 订单列表
     */
    public static Map orders(long passportId, OrderRoleTypeEnum roleType, OrderTypeEnum orderType, StatusPickingEnum statusEnter, int pageIndex,int pageSize) {
        Map map = new HashMap();
        map.put("passportId",passportId);
        map.put("roleType", String.valueOf(roleType.getType()));
        map.put("orderType", String.valueOf(orderType.getType()));
        map.put("statusEnter", String.valueOf(1));
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("clientType", String.valueOf(Cache.clientTypeEnum.getKey()));
        map.put("pageSize", String.valueOf(pageSize));
        return map;
    }

    /**
     * 订单列表
     */
    public static Map  showPickList(long passportId,String orderNum,String status,int
            page,int size){
        Map map = new HashMap();
        map.put("passportId",passportId);
        map.put("orderNum",orderNum);
        map.put("status", status);
        map.put("page", page);
        map.put("size", size);
        return map;
    }


    /**
     * 库位与商品绑定接口
     * @param barcode
     * @param storageLocationNum
     * @return
     */
    public  static  Map itemAndWarehouseStor(String barcode,String storageLocationNum){
        Map map=new HashMap();
        map.put("barcode", barcode);
        map.put("passportId", Cache.passport.getPassportId());
        map.put("storageLocationNum",storageLocationNum);
        return  map;
    }

    /**
     * 盘点
     * @param itemId
     * @param itemTemplateId
     * @param beforeStock
     * @param afterStock
     * @param itemQuantity
     * @return
     */
    public  static  Map changeStock(String itemId,String itemTemplateId,String beforeStock,String afterStock,String itemQuantity){
        Map map=new HashMap();
        map.put("itemId", itemId);
        if(Cache.passport.getShowName()!=null) {
            map.put("roleName", Cache.passport.getShowName());
        }else {
            map.put("roleName", Cache.passport.getLoginName());
        }
        map.put("passportId", Cache.passport.getPassportId());
        map.put("itemTemplateId", itemTemplateId);
        map.put("beforeStock", beforeStock);
        map.put("afterStock", afterStock);
        map.put("itemQuantity",itemQuantity);
        return  map;
    }
    /**
     * 移除库位与商品绑定关系【解除绑定接口】
     * @param barcode
     * @param storageLocationNum
     * @return
     */
    public  static  Map removeBinding(String barcode,String storageLocationNum){
        Map map=new HashMap();
        map.put("barcode", barcode);
        map.put("passportId", Cache.passport.getPassportId());
        map.put("storageLocationNum",storageLocationNum);
        return  map;
    }
    /**
     *根据商品编码查询库位信息接口
     * @param barcode
     * @return
     */
    public  static  Map queryWarehouseStorageLocationByBarcode(String barcode){
        Map map=new HashMap();
        map.put("barcode", barcode);
        map.put("passportId", Cache.passport.getPassportId());
        return  map;
    }
    /**
     *根据库位编码查询该库位下所有商品信息
     * @param storageLocationNum
     * @return
     */
    public  static  Map queryAllItemByStorageLocationNum(String storageLocationNum ){
        Map map=new HashMap();
        map.put("storageLocationNum", storageLocationNum);
        map.put("passportId", Cache.passport.getPassportId());
        return  map;
    }
    /**
     *拣货
     * @param barcode
     * @return
     */
    public  static  Map pickItem(String barcode,String pickListId,int num ){
        Map map=new HashMap();
        map.put("barcode", barcode);
        map.put("pickListId", pickListId);
        map.put("pickNum", num);
        map.put("passportId", Cache.passport.getPassportId());
        return  map;
    }

    /**
     *拣货完成
     * @param pickListId
     * @return
     */
    public  static  Map pickListDone(String pickListId){
        Map map=new HashMap();
        map.put("pickListId", pickListId);
        map.put("passportId", Cache.passport.getPassportId());
        return  map;
    }



}
