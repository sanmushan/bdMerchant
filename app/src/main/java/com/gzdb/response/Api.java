package com.gzdb.response;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class Api {

    public static boolean isULR=false;//true:正式版 false:测试版
    /**
     * 微信APP ID
     */
    public static final String WX_APP_ID = "wx31044be9b94adb2a";

    /**
     * 供应链ID
     */
    public static final String partnerId = "xlb908100000";
    /**
     * 供应链订单APP ID
     */
    public static final String SUPPLY_ORDER_APPID = "908100001";
    /**
     * 供应链支付appId
     */
    public static final String SUPPLY_PAY_APPID = "908100000";
    /**
     * 供应链商品appId -- 908100002
     */
    public static final String SUPPLY_ITEM_APPID = "908100002";

    //#### 通行证中心域名地址 ####
    public static final String passportRemoteURL = "http://db.0085.com/";//"http://192.168.0.124:8080/";//;

    //#### 支付中心域名地址 ####
    public static final String paymentRemoteURL = "http://db.0085.com/";

    //#### 订单中心域名地址 ####
    public static final String orderRemoteURL = "http://db.0085.com/";

    //#### 商品中心域名地址 ####
    public static final String itemRemoteURL = "http://db.0085.com/";

    //#### 物流域名地址 ####
    public static final String logisticsRemoteURL = "http://db.0085.com/";

    //#### 供应链域名地址 ####
    public static final String supplychainRemoteURL = "http://sc.0085.com/";//"http://192.168.0.124:8080/";//;
    //#### 供应链开拓域名地址 ####
    public static final String develoingURL = "http://db.0085.com/";//"http://192.168.0.124:8080/";//;

    public  static String basePassportRemoteURL(){
        if(isULR){
            return passportRemoteURL;
        }else {
            return passportRemoteURL_Test;
        }
    }
    public  static  String BasePaymentRemoteURL(){
        if(isULR){
            return paymentRemoteURL;
        }else {
            return  paymentRemoteURL_Test;
        }
    }
    public  static  String BaseOrderRemoteURL(){
        if(isULR){
            return orderRemoteURL;
        }else {
            return orderRemoteURL_Test;
        }
    }
    public  static  String BaseitemRemoteURL(){
        if(isULR){
            return itemRemoteURL;
        }else {
            return itemRemoteURL_Test;
        }
    }
    public  static  String BaselogisticsRemoteURL(){
        if(isULR){
            return logisticsRemoteURL;
        }else {
            return logisticsRemoteURL_Test;
        }
    }
    public  static  String BasesupplychainRemoteURL(){
        if(isULR){
            return supplychainRemoteURL;
        }else {
            return supplychainRemoteURL_Test;
        }
    }
    public  static  String BasedeveloingURL(){
        if(isULR){
            return develoingURL;
        }else {
            return develoingURL_Test;
        }
    }
    public static  String getJpushAlias(){
        if(!isULR){
            return "0085@";
        }else {
            return "";
        }
    }

//
//
//    //#### 通行证中心域名地址 ####
//    public static final String passportRemoteURL_Test = "http://192.168.1.98:8811/";//"http://192.168.0.124:8080/";//;
//
//    //#### 支付中心域名地址 ####
//    public static final String paymentRemoteURL_Test = "http://192.168.1.98:8810/";
//
//    //#### 订单中心域名地址 ####
//    public static final String orderRemoteURL_Test = "http://192.168.1.98:8812/";
//
//    //#### 商品中心域名地址 ####
//    public static final String itemRemoteURL_Test = "http://192.168.1.98:8805/";
//
//    //#### 物流域名地址 ####
//    public static final String logisticsRemoteURL_Test = "http://192.168.1.98:8802/";
//
//    //#### 供应链域名地址 ####
//    public static final String supplychainRemoteURL_Test = "http://192.168.1.98:8099/";//"http://192.168.0.124:8080/";//;
//    //#### 供应链开拓域名地址 ####
//    public static final String develoingURL_Test = "http://192.168.1.241:9040/";//"http://192.168.0.124:8080/";//;


    //#### 通行证中心域名地址 ####
    public static final String passportRemoteURL_Test = "http://120.25.215.131:8080/";//"http://192.168.0.124:8080/";//;

    //#### 支付中心域名地址 ####
    public static final String paymentRemoteURL_Test = "http://120.25.215.131:8081/";

    //#### 订单中心域名地址 ####
    public static final String orderRemoteURL_Test = "http://120.25.199.108:8080/";

    //#### 商品中心域名地址 ####
    public static final String itemRemoteURL_Test = "http://120.25.199.108:8081/";

    //#### 物流域名地址 ####
    public static final String logisticsRemoteURL_Test = "http://120.76.136.24:8080/";

    //#### 供应链域名地址 ####
    public static final String supplychainRemoteURL_Test = "http://120.76.136.24:8081/";//"http://192.168.0.124:8080/";//;
    //#### 供应链开拓域名地址 ####
    public static final String develoingURL_Test = "http://192.168.1.241:9040/";//"http://192.168.0.124:8080/";//;



    public  static  final  String BASEURL="http://db.0085.com/";



    ////////////////////////////////////////////////////////////////////////////////////////////////通行证中心相关 接口public static final String REGISTER_PASSPORT = passportRemoteURL + "passport/registerPassport";//注册
    public static final String LOGIN = basePassportRemoteURL() + "passport/loginPassport";//登陆
    public static final String FORGET_PASSWORD = basePassportRemoteURL() + "passport/forgetPassword";//忘记密码，重置密码
    public static final String MODIFY_PASSWORD = basePassportRemoteURL() + "passport/modifyPassword";//修改登录密码
    public static final String GET_SMS = basePassportRemoteURL() + "sms/requestVerificationCode";//SMS
    public static final String GET_ADDRESS = basePassportRemoteURL() + "address/getAddress";//获得地址
    public static final String GET_ADDRESSES = basePassportRemoteURL() + "address/getAddresses";//获得全部地址
    public static final String NEW_ADDRESS = basePassportRemoteURL() + "address/newAddress";//新建地址
    public static final String REMOVE_ADDRESS = basePassportRemoteURL() + "address/removeAddress";//删除地址
    public static final String SET_DEFAULT_ADDRESS = basePassportRemoteURL() + "address/setDefaultAddress";//设置默认地址
    public static final String VERSION_UPGRADE = basePassportRemoteURL() + "passport/versionUpgrade";//版本检测更新
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////供应链相关
    //supply chain
    public static final String SUPPLY_HOMEPAGE = BasesupplychainRemoteURL() + "item/homepage";//采购首页
    public static final String SHOW_ITEM_TYPES = BasesupplychainRemoteURL() + "item/itemTypes";//仓管商品类型列表
    public static final String SPLIT_ITEMS = BasesupplychainRemoteURL() + "item/splitItems";//购物车分单
    public static final String P_CREATE_ORDER = BasesupplychainRemoteURL() + "order/prepareCreateOrder";//预下单
    public static final String GENERATE_ORDER = BasesupplychainRemoteURL() + "order/generateOrder";//真正的下单
    public static final String PURCHASE_ORDER = BasesupplychainRemoteURL() + "order/purchaseOrder";//采购员真正下单
    public static final String ALLOCATION_ORDER = BasesupplychainRemoteURL() + "order/allocationOrder";//调拨下单订单
    public static final String PAYMENT_ORDER = BasesupplychainRemoteURL() + "order/paymentOrder";//支付订单
    public static final String ORDER_LIST = BasesupplychainRemoteURL() + "order/showOrders";//订单列表
    public static final String showPickList = BasesupplychainRemoteURL() + "warehouse/showPickList";//订单列表
    public static final String eshowPickListByScanQRcode = BasesupplychainRemoteURL() + "warehouse/showPickListByScanQRcode";//订单列表
    public static final String SEARCH_ORDER = BasesupplychainRemoteURL() + "order/searchOrders";//搜索订单
    public static final String ORDER_DETAIL = BasesupplychainRemoteURL() + "order/orderDetail";//订单详情
    public static final String CONFIRM_ORDER = BasesupplychainRemoteURL() + "order/confirmOrder";//确认订单,采购员确认
    public static final String PRINTERORDERTICKET = BasesupplychainRemoteURL() + "order/printerOrderTicket";//打印订单
    public static final String CANCEL_ORDER = BasesupplychainRemoteURL() + "order/cancelOrder";//确认取消，采购员操作
    public static final String DELIVER_ORDER = BasesupplychainRemoteURL() + "order/deliverOrder";//订单发货
    public static final String GET_WAREHOUSE = BasesupplychainRemoteURL() + "warehouse/getSelfWarehouses";
    public static final String WAREHOUSE_ITEMS = BasesupplychainRemoteURL() + "item/warehouseItems";//仓库商品列表，搜索也是这个
    public static final String PURCHASE_ITEMS = BasesupplychainRemoteURL() + "item/purchaseItems";//采购员商品列表，搜索也是这个
    public static final String SEARCH_ITEMS = BasesupplychainRemoteURL() + "item/searchItemsForWarehouse";//我的仓库，搜索商品
    public static final String PURCHASE_ITEM_TYPES = BasesupplychainRemoteURL() + "item/purchaseItemTypes";//采购员商品分类
    public static final String BATCH_RECEIPT = BasesupplychainRemoteURL() + "order/batchReceipt";//分批收货
    public static final String SET_PRINT = BasesupplychainRemoteURL() + "warehouse/setPrinterNumber";//设置打印设备
    public static final String SCAN_QRCODE = BasesupplychainRemoteURL() + "order/scanQRCode";//扫码
    public static final String SUPPLIER_PROPERTIES = BasesupplychainRemoteURL() + "warehouse/getSupplierProperties";//设置
    public static final String ORDER_RECEIPTIMG = BasesupplychainRemoteURL() + "order/createOrderReceiptImg";//添加收货单签名照记录
    public static final String FLAG_ORDER_STATE  = BasesupplychainRemoteURL() + "order/confirmPurchaseOrder";//标记订单完成状态
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////////////////支付中心 相关接口
    public static final String GET_CURRENCYS = BasePaymentRemoteURL() + "paymentController/passportCurrency";//获取用户所有货币信息
    public static final String BANLANCE_PAYMENT = BasePaymentRemoteURL() + "paymentController/balancePayment";//余额支付
    public static final String UPDATE_PAY_PASSWORD = BasePaymentRemoteURL() + "paymentController/setPaymentPassword";//修改密码




    // 超过这个值，则添加一个可以向下拉，向上拉的按钮
    public static final int updown_count = 5;
    public static final int PAGE_SIZE = 10;
    public static final int LIST_REFRESH_TIME = 60;



}
