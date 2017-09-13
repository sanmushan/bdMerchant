package com.gzdb.response;

/**
 * Created by PVer on 2017/5/31.
 */

public class ItemType {

    private String image;//": "http://supplymc.0085.com/static/img/homepage/type1.png",
    private String top;//": 1,
    private String icon;//": "http://supplymc.0085.com/static/img/homepage/type1.png",
    private String id;//": 11000,

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTop() {
        return top;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String sort;//": 99,
    private String title;//": "零食糖果",
    private String parentId;//": 0,
    private String status;//": 0

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    private boolean isSelect = false;
    private  int pos;

}
