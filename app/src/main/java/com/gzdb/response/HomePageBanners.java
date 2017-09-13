package com.gzdb.response;

/**
 * Created by zhumg on 3/23.
 */
public class HomePageBanners {

    private long bannerId;
    private String imageURL;
    private String clickURL;

    public long getBannerId() {
        return bannerId;
    }

    public void setBannerId(long bannerId) {
        this.bannerId = bannerId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getClickURL() {
        return clickURL;
    }

    public void setClickURL(String clickURL) {
        this.clickURL = clickURL;
    }
}
