package com.gzdb.response;

import java.util.List;

/**
 * 交接任务列表实体类
 * Created by PVer on 2017/9/15.
 */

public class LogisticsListBean implements java.io.Serializable{
    private String id;
    private String batchNo;
    private String orderNum;
    private String packageTotle;
    private String connectTotle;
    private String warehouseId;
    private String warehouseName;
    private String status;
    private String type;
    private String time;
    private List<LogisticsDetailsListBean> logisticsDispatchAreaDetailsList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getPackageTotle() {
        return packageTotle;
    }

    public void setPackageTotle(String packageTotle) {
        this.packageTotle = packageTotle;
    }

    public String getConnectTotle() {
        return connectTotle;
    }

    public void setConnectTotle(String connectTotle) {
        this.connectTotle = connectTotle;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<LogisticsDetailsListBean> getLogisticsDispatchAreaDetailsList() {
        return logisticsDispatchAreaDetailsList;
    }

    public void setLogisticsDispatchAreaDetailsList(List<LogisticsDetailsListBean> logisticsDispatchAreaDetailsList) {
        this.logisticsDispatchAreaDetailsList = logisticsDispatchAreaDetailsList;
    }
}
