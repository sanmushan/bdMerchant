package com.gzdb.response;

/**
 * Created by liyunbiao on 2017/5/26.
 */
public class Bank {

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSimpleCode() {
        return simpleCode;
    }

    public void setSimpleCode(String simpleCode) {
        this.simpleCode = simpleCode;
    }

    private String bankCode;//": "102100099996",
    private String name;//": "中国工商银行",
    private String logo;//": "http://oss.0085.com/merchants/2016/0510/1462881874.png",
    private String id;//": 100000,
    private String simpleCode;//": "ICBC"
}
