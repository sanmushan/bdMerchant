package com.gzdb.response;

import java.util.List;

/**
 * Created by zhumg on 3/23.
 */
public class HomePage {

    private long roleId;
    private List<HomePageBanners> banners;
    private List<HomePageRecommendItemTypes> specialButtons;
    private List<HomePageRecommendItemTypes> recommendItemTypes;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public List<HomePageBanners> getBanners() {
        return banners;
    }

    public void setBanners(List<HomePageBanners> banners) {
        this.banners = banners;
    }

    public List<HomePageRecommendItemTypes> getSpecialButtons() {
        return specialButtons;
    }

    public void setSpecialButtons(List<HomePageRecommendItemTypes> specialButtons) {
        this.specialButtons = specialButtons;
    }

    public List<HomePageRecommendItemTypes> getRecommendItemTypes() {
        return recommendItemTypes;
    }

    public void setRecommendItemTypes(List<HomePageRecommendItemTypes> recommendItemTypes) {
        this.recommendItemTypes = recommendItemTypes;
    }
}
