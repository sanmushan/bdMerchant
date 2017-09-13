package com.gzdb.warehouse.order;

import android.view.View;
import android.widget.ListView;

import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.ConsumerOrder;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.StatusEnterEnum;
import com.gzdb.warehouse.adapter.OrderAdapter;
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
 * Created by Administrator on 2017/4/16 0016.
 */

public class OrderListFragment extends AfinalFragment {

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

    OrderAdapter orderAdapter;
    List<ConsumerOrder> orders = new ArrayList<>();

    RefreshLoad refreshLoad;

    int pageIndex = 1;
    long oldTime = 0;
    int fragmentIndex = 0;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_ptr;
    }

    public void setNewOrderTypeEnum(OrderTypeEnum orderTypeEnum) {
        this.new_orderTypeEnum = orderTypeEnum;
        if (this.orderAdapter != null) {
            this.orderAdapter.setOrderTypeEnum(orderTypeEnum);
        }
    }

    public void setNewOrderRoleTypeEnum(OrderRoleTypeEnum orderRoleTypeEnum) {
        this.new_orderRoleTypeEnum = orderRoleTypeEnum;
        if (this.orderAdapter != null) {
            this.orderAdapter.setOrderRoleTypeEnum(orderRoleTypeEnum);
        }
    }

    public void setNewStatusEnterEnum(StatusEnterEnum statusEnterEnum) {
        this.new_statusEnterEnum = statusEnterEnum;
    }

    @Override
    protected void initViewData(View view) {

        orderAdapter = new OrderAdapter(getActivity(), orders);
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
        Map map = ApiRequest.orders(Cache.passport.getPassportId(), orderRoleTypeEnum, orderTypeEnum, statusEnterEnum, pageIndex);
        Http.post(getActivity(), map, Api.ORDER_LIST, new HttpCallback<List<ConsumerOrder>>("datas") {
            @Override
            public void onSuccess(List<ConsumerOrder> data) {
                if (refreshLoad.isLoadMore()) {
                    orderAdapter.addMore(data);
                } else {
                    orderAdapter.refresh(data);
                }
                orderAdapter.notifyDataSetChanged();
                refreshLoad.complete(data.size() >= Api.PAGE_SIZE, orderAdapter.isEmpty());
            }

            @Override
            public void onFailure() {
                if (refreshLoad.isLoadMore()) {
                    refreshLoad.showError(msg);
                } else {
                    orderAdapter.removeAll();
                    orderAdapter.notifyDataSetChanged();
                    ptr.setVisibility(View.GONE);
                    refreshLoad.showReset(msg);
                }
            }
        });
    }

    public void qRefresh() {
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
        refreshLoad.showLoading();
    }

    public static OrderListFragment create(OrderRoleTypeEnum orderRoleTypeEnum, OrderTypeEnum orderTypeEnum, StatusEnterEnum statusEnterEnum) {
        OrderListFragment orderFragment = new OrderListFragment();

        //默认2个一致

        orderFragment.statusEnterEnum = statusEnterEnum;
        orderFragment.orderRoleTypeEnum = orderRoleTypeEnum;
        orderFragment.orderTypeEnum = orderTypeEnum;

        orderFragment.new_statusEnterEnum = statusEnterEnum;
        orderFragment.new_orderRoleTypeEnum = orderRoleTypeEnum;
        orderFragment.new_orderTypeEnum = orderTypeEnum;

        return orderFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        startGetOrders();
    }
}
