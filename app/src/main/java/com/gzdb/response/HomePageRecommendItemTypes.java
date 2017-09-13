package com.gzdb.response;

import com.zhumg.anlib.widget.AdapterModel;

import java.util.List;

/**
 * Created by zhumg on 3/23.
 */
public class HomePageRecommendItemTypes implements AdapterModel {

    private long itemTypeId;
    private String itemTypeName;
    private String itemTypeIcon;
    private String itemTypeImage;
    private List<HomePageRecommendItemTypes> subItemTypes;

    public long getItemTypeId() {
        return itemTypeId;
    }

    public void setItemTypeId(long itemTypeId) {
        this.itemTypeId = itemTypeId;
    }

    public String getItemTypeName() {
        return itemTypeName;
    }

    public void setItemTypeName(String itemTypeName) {
        this.itemTypeName = itemTypeName;
    }

    public String getItemTypeIcon() {
        return itemTypeIcon;
    }

    public void setItemTypeIcon(String itemTypeIcon) {
        this.itemTypeIcon = itemTypeIcon;
    }

    public String getItemTypeImage() {
        return itemTypeImage;
    }

    public void setItemTypeImage(String itemTypeImage) {
        this.itemTypeImage = itemTypeImage;
    }

    public List<HomePageRecommendItemTypes> getSubItemTypes() {
        return subItemTypes;
    }

    public void setSubItemTypes(List<HomePageRecommendItemTypes> subItemTypes) {
        this.subItemTypes = subItemTypes;
    }

    //选中标志
    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public static HomePageRecommendItemTypes createItemType(long itemTypeId, String name) {
        HomePageRecommendItemTypes types = new HomePageRecommendItemTypes();
        types.itemTypeId = itemTypeId;
        types.itemTypeName = name;
        return types;
    }

    @Override
    public int getUiType() {
        return 0;
    }
}
