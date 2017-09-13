package com.gzdb.response;

/**
 * Created by liyunbiao on 2017/8/12.
 */

public class NumPickingBean {
    public String getLack() {
        return lack;
    }

    public void setLack(String lack) {
        this.lack = lack;
    }

    public String getMerge() {
        return merge;
    }

    public void setMerge(String merge) {
        this.merge = merge;
    }

    public String getUnTouch() {
        return unTouch;
    }

    public void setUnTouch(String unTouch) {
        this.unTouch = unTouch;
    }

    private  String lack;
    private  String merge;
    private  String unTouch;
}
