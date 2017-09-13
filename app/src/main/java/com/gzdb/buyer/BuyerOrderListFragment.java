package com.gzdb.buyer;

import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.ConsumerOrder;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.response.enums.StatusEnterEnum;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.widget.RemoveListWidget;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by zhumg on 4/25.
 */
public class BuyerOrderListFragment extends AfinalFragment implements RemoveListWidget.DelCallback{

    StatusEnterEnum statusEnterEnum;
    OrderTypeEnum orderTypeEnum;
    OrderRoleTypeEnum orderRoleTypeEnum;

    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;

    @Bind(R.id.fr_listview)
    ListView listView;

    BuyerOrderAdapter orderAdapter;
    List<ConsumerOrder> orders = new ArrayList<>();

    RefreshLoad refreshLoad;

    int pageIndex = 1;
    int oldLoadCount = 0;
    long oldTime = 0;

    //该类在viewpage里面使用，所以会new 多个，该值代表该类处于第几个索引
    int fragmentIndex;


    public int getFragmentIndex() {
        return fragmentIndex;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_ptr;
    }

    @Override
    protected void initViewData(View view) {

        EventBus.getDefault().register(this);

        orderAdapter = new BuyerOrderAdapter(getActivity(), orders, this);
        orderAdapter.setOrderRoleTypeEnum(this.orderRoleTypeEnum);

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
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onVisible() {
        //如果为空，或者 刷新时间够1分钟，则刷新
        long time = System.currentTimeMillis();
        if(orderAdapter != null && !orderAdapter.isEmpty()) {
            if(time - oldTime > 1000 * Api.LIST_REFRESH_TIME) {
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
                    //直接显示重试
                    ptr.setVisibility(View.GONE);
                    refreshLoad.showReset(msg);
                }
            }
        });
    }

    public OrderTypeEnum getOrderTypeEnum() {
        return orderTypeEnum;
    }

    public StatusEnterEnum getStatusEnterEnum() {
        return statusEnterEnum;
    }

    public OrderRoleTypeEnum getOrderRoleTypeEnum() {
        return orderRoleTypeEnum;
    }

    /**
     * 执行在主线程。
     * 非常实用，可以在这里将子线程加载到的数据直接设置到界面中。
     *
     * @param msg 事件1
     */
    @Subscribe
    public void onEventMainThread(BuyerOrderListFragmentEvent msg) {
        Log.e("db", "BuyerOrderListFragment onEventMainThread");
        if (this.fragmentIndex != msg.fragmentIndex) {
            return;
        }
        Log.e("db", "OrderFragment onEventMainThread " + this.fragmentIndex);
        //如果是重新获取数据
        if (msg.eventType == BuyerOrderListFragmentEvent.HTTP_UPDATE_DATA) {
            oldTime = System.currentTimeMillis();
            //如果未有数据
            if (orderAdapter.isEmpty()) {
                refreshLoad.showLoading();
            } else {
                refreshLoad.showRefresh();
            }
        }
    }

    public static BuyerOrderListFragment create(OrderRoleTypeEnum orderRoleTypeEnum, OrderTypeEnum orderTypeEnum, StatusEnterEnum statusEnterEnum, int fIndex) {
        BuyerOrderListFragment orderFragment = new BuyerOrderListFragment();
        orderFragment.statusEnterEnum = statusEnterEnum;
        orderFragment.orderRoleTypeEnum = orderRoleTypeEnum;
        orderFragment.orderTypeEnum = orderTypeEnum;
        orderFragment.fragmentIndex = fIndex;
        return orderFragment;
    }

    @Override
    public void onDelSuccess() {
        if (orderAdapter.isEmpty()) {
            //显示空内容
            ptr.setVisibility(View.GONE);
            refreshLoad.getLoadingView().showReset("该订单列表为空");
        }
    }
}
