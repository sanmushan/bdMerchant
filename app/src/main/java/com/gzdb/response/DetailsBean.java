package com.gzdb.response;

/**
 * Created by liyunbiao on 2017/8/11.
 */

public class DetailsBean {
    public String getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(String itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }


    public String getPickListId() {
        return pickListId;
    }

    public void setPickListId(String pickListId) {
        this.pickListId = pickListId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(String itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getPickedQuantity() {
        return pickedQuantity;
    }

    public void setPickedQuantity(String pickedQuantity) {
        this.pickedQuantity = pickedQuantity;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String itemTemplateId;//": 123,
    private String itemId;//": 354200,
    private String itemName;//": "飘柔日常绿茶去油洗发露400g",
    private String quantity;//": 23,

    public String[] getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String[] storageLocation) {
        this.storageLocation = storageLocation;
    }

    private String [] storageLocation;//": "A-1-1-1",
    private String pickListId;//": 17,

    private String id;
    private String itemTypeId;//": 90051,
    private String pickedQuantity;//": 0,
    private String barcode;//": "6903148030479",
    private String status;//": 0

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    private String stock;//": 0
}
