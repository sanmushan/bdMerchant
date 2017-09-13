package com.gzdb.warehouse.order;

import android.view.View;
import android.widget.ListView;

import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.response.Api;
import com.gzdb.response.ConsumerOrder;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.warehouse.adapter.OrderAdapter;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.widget.bar.SearchTitleBar;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by zhumg on 4/19.
 */
public class OrderSearchActivity extends AfinalActivity implements View.OnClickListener {

    SearchTitleBar searchTitleBar;
    RefreshLoad refreshLoad;

    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;

    @Bind(R.id.fr_listview)
    ListView listView;

    int orderType;
    int orderRoleType;

    int pageIndex = 1;

    OrderAdapter adapter;
    List<ConsumerOrder> orders = new ArrayList<>();

    @Override
    public int getContentViewId() {
        return R.layout.activity_search_order;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();

        orderType = getIntent().getIntExtra("orderType", 0);
        orderRoleType = getIntent().getIntExtra("orderRoleType", 0);

        adapter = new OrderAdapter(this, orders);

        adapter.setOrderTypeEnum(OrderTypeEnum.getOrderTypeEnum(orderType));
        adapter.setOrderRoleTypeEnum(OrderRoleTypeEnum.getOrderRoleTypeEnum(orderRoleType));
        listView.setAdapter(adapter);

        searchTitleBar = new SearchTitleBar(view);
        searchTitleBar.setLeftBack(this);
        searchTitleBar.setRightTxt("搜索");
        searchTitleBar.setRightListener(this);
        searchTitleBar.getCenter_search_edit().setHint("请输入订单号进行搜索");
        searchTitleBar.setEditTxtListener(new SearchTitleBar.EditTextListener() {
            @Override
            public void notifyInputTxt(String str) {
                //如果有内容，则搜索
                if (str != null && str.trim().length() > 0) {
                    //搜索
                    //search(str);
                } else {
                    //清除
                    ptr.setVisibility(View.GONE);
                    refreshLoad.hibe();
                }
            }
        });

        refreshLoad = new RefreshLoad(this, ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (!over) {
                    pageIndex = 1;
                    ptr.setVisibility(View.GONE);
                    startSearch();
                } else {
                    ptr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    pageIndex = 1;
                    startSearch();
                }
            }

            @Override
            public void onLoadmore(boolean over) {
                if (!over) {
                    pageIndex++;
                    startSearch();
                }
            }
        }, listView);

        ptr.setVisibility(View.GONE);
        refreshLoad.getLoadingView().showLoadingTxt("请输入订单号进行搜索");
    }

    void startSearch() {
        String key = searchTitleBar.getCenter_search_edit().getText().toString().trim();
        if (key == null || key.length() < 1) {
            return;
        }
        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("searchKeyValue", key);
        map.put("roleType", String.valueOf(orderRoleType));
        map.put("orderType", String.valueOf(orderType));
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(Api.PAGE_SIZE));

        Http.post(this, map, Api.SEARCH_ORDER, new HttpCallback<List<ConsumerOrder>>("datas") {

            @Override
            public void onSuccess(List<ConsumerOrder> data) {
                if (refreshLoad.isLoadMore()) {
                    orders.addAll(data);
                } else {
                    orders.clear();
                    orders.addAll(data);
                }
                refreshLoad.complete(data.size() >= Api.PAGE_SIZE, adapter.isEmpty());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure() {
                if (refreshLoad.isLoadMore()) {
                    refreshLoad.showError(msg);
                } else {
                    adapter.removeAll();
                    adapter.notifyDataSetChanged();
                    //直接显示重试
                    ptr.setVisibility(View.GONE);
                    refreshLoad.showReset(msg);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_right) {
            refreshLoad.showLoading();
        }
    }
}
