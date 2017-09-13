package com.gzdb.response;

/**
 * Created by liyunbiao on 2017/8/28.
 */

public class PickGroupBean {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSrcWarehouseId() {
        return srcWarehouseId;
    }

    public void setSrcWarehouseId(String srcWarehouseId) {
        this.srcWarehouseId = srcWarehouseId;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    private String id;
    private String groupName;
    private String srcWarehouseId;
    private String itemTypeId;
}
