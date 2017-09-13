package com.gzdb.warehouse.back;

import android.view.View;

import com.gzdb.base.BaseListActivity;
import com.gzdb.response.Api;
import com.gzdb.response.SupplyItem;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.http.HttpCallback;

import java.util.List;
import java.util.Map;

/**
 * Created by liyunbiao on 2017/7/14.
 */

public class BackWareHouseProductActivity extends BaseListActivity<SupplyItem> {
    @Override
    public Map getParam() {
        return null;
    }

    @Override
    public String getUrl() {
        return Api.BASEURL + "";
    }

    @Override
    public int getContentLayoutViewId() {
        return R.layout.activity_back_warehouse_product;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
    }

    @Override
    public HttpCallback httpCallback() {
        return new HttpCallback<List<SupplyItem>>() {
            @Override
            public void onSuccess(List<SupplyItem> data) {
                Result(data);
            }
        };
    }
}
