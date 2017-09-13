package com.gzdb.buyer;

import android.content.Intent;
import android.view.View;
import com.gzdb.response.Api;
import com.gzdb.response.OrderDetail;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.response.enums.RoleTypeEnum;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.order.OrderDetailActivity;
import com.gzdb.widget.ScanActivity;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.widget.bar.BaseTitleBar;
import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liyunbiao on 2017/7/1.
 */
public class BuyerWarehouseFragment extends AfinalFragment {
    BaseTitleBar baseTitleBar;
    @Override
    public int getContentViewId() {
        return R.layout.fragment_warehouse_destory;
    }

    @Override
    protected void initViewData(View view) {

        try {



            baseTitleBar = new BaseTitleBar(view);
            baseTitleBar.setCenterTxt("仓库概要");



        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
