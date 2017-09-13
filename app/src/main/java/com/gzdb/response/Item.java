package com.gzdb.response;

/**
 * Created by liyunbiao on 2017/8/10.
 */

public class Item {
    public String getAscriptionType() {
        return ascriptionType;
    }

    public void setAscriptionType(String ascriptionType) {
        this.ascriptionType = ascriptionType;
    }

    public String getBannerUrl() {
        return bannerUrl;
    }

    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(String costPrice) {
        this.costPrice = costPrice;
    }

    public String getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(String defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public String getDefineCode() {
        return defineCode;
    }

    public void setDefineCode(String defineCode) {
        this.defineCode = defineCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUploadersPassportId() {
        return uploadersPassportId;
    }

    public void setUploadersPassportId(String uploadersPassportId) {
        this.uploadersPassportId = uploadersPassportId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getSafetyQuantity() {
        return safetyQuantity;
    }

    public void setSafetyQuantity(String safetyQuantity) {
        this.safetyQuantity = safetyQuantity;
    }

    public String getWarningQuantity() {
        return warningQuantity;
    }

    public void setWarningQuantity(String warningQuantity) {
        this.warningQuantity = warningQuantity;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    private String templateId;
    private String safetyQuantity;
    private String warningQuantity;
    private String stock;
    private String ascriptionType;
    private String bannerUrl;
    private String barcode;
    private String costPrice;
    private String defaultPrice;
    private String defineCode;
    private String description;
    private String id;
    private String imageUrl;
    private String name;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    private String itemName;
    private String status;
    private String typeId;
    private String unitId;
    private String uploadTime;
    private String uploadersPassportId;
}
