package com.gzdb.response;

import com.gzdb.response.enums.PassportRoleTypeEnum;

import java.io.Serializable;

/**
 * Created by zhumg on 3/17.
 */
public class Passport implements Serializable {

    private long passportId;
    private String accessToken;
    private String showName;
    private String phoneNumber;
    private String headImage;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    private String realName;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    private String deviceName;

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    private String idNumber;

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    private VersionInfo versionMessage;
    private int roleValue;//登陆后，角色类型，是仓库，还是采购。
    private String loginName;//登陆后，客户端自己注入

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public int getRoleValue() {
        return roleValue;
    }

    public void setRoleValue(int roleValue) {
        this.roleValue = roleValue;
    }

    public VersionInfo getVersionMessage() {
        return versionMessage;
    }

    public void setVersionMessage(VersionInfo versionMessage) {
        this.versionMessage = versionMessage;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassportIdStr() {
        return String.valueOf(passportId);
    }

    public long getPassportId() {
        return passportId;
    }

    public void setPassportId(long passportId) {
        this.passportId = passportId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    /**
     * 判断是否是 主仓
     */
    public boolean isMainWarehouse() {
        return roleValue == PassportRoleTypeEnum.MAIN_WAREHOUSE.getKey();
    }

    /**
     * 判断是否是仓库
     */
    public boolean isWarehouse() {
        return roleValue == PassportRoleTypeEnum.WAREHOUSE.getKey();
    }
}

