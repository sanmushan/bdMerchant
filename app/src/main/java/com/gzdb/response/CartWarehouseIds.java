package com.gzdb.response;

import java.util.List;

/**
 * Created by zhumg on 3/28.
 */
public class CartWarehouseIds {

    private long warehouseId;//仓库ID
    private String warehouseName;//仓库名字
    private List<Long> items;//商品ID

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public List<Long> getItems() {
        return items;
    }

    public void setItems(List<Long> items) {
        this.items = items;
    }
}
