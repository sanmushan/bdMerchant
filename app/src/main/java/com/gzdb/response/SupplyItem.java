package com.gzdb.response;

import com.gzdb.warehouse.adapter.ItemAdapter;
import com.zhumg.anlib.widget.AdapterModel;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class SupplyItem implements AdapterModel {

    private long itemId = 0;// 商品ID
    private long itemTemplateId = 0;// 商品模版ID
    private String itemName = null;//String 商品名
    private String unitName = null;//单位名
    private String imageUrl = null;//商品图片链接
    private int stock = 0;// 库存

    private int currstock = 0;// 购买库存
    private int warningQuantity;//预警库存
    private int pendingQuantity;//挂起库存（待发货）
    private int partnerStock;//上级仓库存
    private int purchaseQuantity;//采购员用，采购中的数量
    private long sellPrice;//商品价格，采购中使用
    private String warehouseName;//采购中供货商名称
    public int getCurrstock() {
        return currstock;
    }

    public void setCurrstock(int currstock) {
        this.currstock = currstock;
    }
    public long getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(long sellPrice) {
        this.sellPrice = sellPrice;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public int getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(int purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(long itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getWarningQuantity() {
        return warningQuantity;
    }

    public void setWarningQuantity(int warningQuantity) {
        this.warningQuantity = warningQuantity;
    }

    public int getPendingQuantity() {
        return pendingQuantity;
    }

    public void setPendingQuantity(int pendingQuantity) {
        this.pendingQuantity = pendingQuantity;
    }

    public int getPartnerStock() {
        return partnerStock;
    }

    public void setPartnerStock(int partnerStock) {
        this.partnerStock = partnerStock;
    }

    @Override
    public int getUiType() {
        return ItemAdapter.CART_MODEL_UI_TYPE_ITEM;
    }
}
