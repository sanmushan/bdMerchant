package com.gzdb.warehouse.main;

import android.view.View;
import android.widget.ListView;

import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.BackOrder;
import com.gzdb.response.ConsumerOrder;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.response.enums.StatusEnterEnum;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.adapter.BackAdapter;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by liyunbiao on 2017/7/31.
 */

public class BackListFragment extends AfinalFragment {

    StatusEnterEnum statusEnterEnum;
    OrderRoleTypeEnum orderRoleTypeEnum;
    OrderTypeEnum orderTypeEnum;

    StatusEnterEnum new_statusEnterEnum;
    OrderRoleTypeEnum new_orderRoleTypeEnum;
    OrderTypeEnum new_orderTypeEnum;

    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;

    @Bind(R.id.fr_listview)
    ListView listView;

    BackAdapter orderAdapter;
    List<BackOrder> orders = new ArrayList<>();

    RefreshLoad refreshLoad;

    int pageIndex = 1;
    long oldTime = 0;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_ptr;
    }

    @Override
    protected void initViewData(View view) {

        orderAdapter = new BackAdapter(getActivity(), orders);
        orderAdapter.setOrderRoleTypeEnum(this.orderRoleTypeEnum);
        orderAdapter.setOrderTypeEnum(orderTypeEnum);

        listView.setAdapter(orderAdapter);
        listView.setBackgroundResource(R.color.bg_e8);

        refreshLoad = new RefreshLoad(this.getActivity(), ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (over) {
                    ptr.setVisibility(View.VISIBLE);
                } else {
                    pageIndex = 1;
                    ptr.setVisibility(View.GONE);
                    //加载订单数据
                    startGetOrders();
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    pageIndex = 1;
                    startGetOrders();
                }
            }

            @Override
            public void onLoadmore(boolean over) {
                if (!over) {
                    pageIndex++;
                    startGetOrders();
                }
            }

        }, listView);
    }

    @Override
    public void onVisible() {

        //如果类型不一致，则重置
        if (orderTypeEnum != new_orderTypeEnum || statusEnterEnum != new_statusEnterEnum || orderRoleTypeEnum != new_orderRoleTypeEnum) {
            orderTypeEnum = new_orderTypeEnum;
            statusEnterEnum = new_statusEnterEnum;
            orderRoleTypeEnum = new_orderRoleTypeEnum;
            //清除数据
            if (orderAdapter != null) {
                orderAdapter.setOrderTypeEnum(orderTypeEnum);
                orderAdapter.setOrderRoleTypeEnum(orderRoleTypeEnum);
                orderAdapter.removeAll();
            }
        }

        //如果为空，或者 刷新时间够1分钟，则刷新
        long time = System.currentTimeMillis();
        if (orderAdapter != null && !orderAdapter.isEmpty()) {
            if (time - oldTime > 1000 * Api.LIST_REFRESH_TIME) {
                oldTime = time;
                refreshLoad.showRefresh();
            }
            return;
        }
        oldTime = time;
        refreshLoad.showLoading();
    }

    void startGetOrders() {
        try {
            List<BackOrder> data =new ArrayList<>();
            for(int i=0;i<10;i++) {
                BackOrder b = new BackOrder();
                data.add(b);
            }
            if (refreshLoad.isLoadMore()) {
                orderAdapter.addMore(data);
            } else {
                orderAdapter.refresh(data);
            }
            orderAdapter.notifyDataSetChanged();
            refreshLoad.complete(data.size() >= Api.PAGE_SIZE, orderAdapter.isEmpty());
//            Map map = ApiRequest.orders(Cache.passport.getPassportId(), orderRoleTypeEnum, orderTypeEnum, statusEnterEnum, pageIndex);
//            Http.post(getActivity(), map, Api.ORDER_LIST, new HttpCallback<List<BackOrder>>("datas") {
//                @Override
//                public void onSuccess(List<BackOrder> data) {
//                    if (refreshLoad.isLoadMore()) {
//                        orderAdapter.addMore(data);
//                    } else {
//                        orderAdapter.refresh(data);
//                    }
//                    orderAdapter.notifyDataSetChanged();
//                    refreshLoad.complete(data.size() >= Api.PAGE_SIZE, orderAdapter.isEmpty());
//                }
//
//                @Override
//                public void onFailure() {
//                    if (refreshLoad.isLoadMore()) {
//                        refreshLoad.showError(msg);
//                    } else {
//                        orderAdapter.removeAll();
//                        orderAdapter.notifyDataSetChanged();
//                        ptr.setVisibility(View.GONE);
//                        refreshLoad.showReset(msg);
//                    }
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BackListFragment create(OrderRoleTypeEnum orderRoleTypeEnum, OrderTypeEnum orderTypeEnum, StatusEnterEnum statusEnterEnum) {
        BackListFragment orderFragment = new BackListFragment();

        //默认2个一致

        orderFragment.statusEnterEnum = statusEnterEnum;
        orderFragment.orderRoleTypeEnum = orderRoleTypeEnum;
        orderFragment.orderTypeEnum = orderTypeEnum;

        orderFragment.new_statusEnterEnum = statusEnterEnum;
        orderFragment.new_orderRoleTypeEnum = orderRoleTypeEnum;
        orderFragment.new_orderTypeEnum = orderTypeEnum;

        return orderFragment;
    }
}
