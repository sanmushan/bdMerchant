package com.gzdb.response;

/**
 * Created by zhumg on 4/20.
 */
public class VersionInfo {

    private int upgradeType;// - int 更新的类型，参考：VersionTypeEnum
    private String title;// - String 更新标题
    private String showVersion;// - String 用于展示前端的版本号，如：V1.0

    //当upgradeType为VersionTypeEnum.UN_FORCE_UPGRADE或VersionTypeEnum.FORCE_UPGRADE
    private int versionIndex;// - int 最新内部版本号
    private String introduce;// - String 版本更新提示内容
    private String versionUrl;// - String 新版本的下载地址

    public int getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(int upgradeType) {
        this.upgradeType = upgradeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShowVersion() {
        return showVersion;
    }

    public void setShowVersion(String showVersion) {
        this.showVersion = showVersion;
    }

    public int getVersionIndex() {
        return versionIndex;
    }

    public void setVersionIndex(int versionIndex) {
        this.versionIndex = versionIndex;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }
}
