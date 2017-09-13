package com.gzdb.warehouse.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gzdb.response.Api;
import com.gzdb.response.OrderDetail;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.response.enums.StatusEnterEnum;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.order.OrderDetailActivity;
import com.gzdb.warehouse.order.OrderListFragment;
import com.gzdb.warehouse.order.OrderSearchActivity;
import com.gzdb.widget.ScanActivity;
import com.gzdb.widget.TitleViewPager;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.DeviceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 仓管 订单
 * Created by Administrator on 2017/4/16 0016.
 */

public class WareOrderFragment extends AfinalFragment implements View.OnClickListener {

//    @Bind(R.id.title_center_txt1)
//    TextView title_center_txt1;

    @Bind(R.id.title_center_txt2)
    TextView title_center_txt2;

    @Bind(R.id.title_center_txt3)
    TextView title_center_txt3;

    @Bind(R.id.title_left)
    View title_left;

    @Bind(R.id.title_right)
    View title_right;

    TitleViewPager titleViewPager;
    List<Fragment> fragments = new ArrayList<>();

    //当前类型
    int select_index = 0;
    OrderTypeEnum selectOrderTypeNum;
    OrderRoleTypeEnum orderRoleTypeEnum;
    ScanActivity.ScanCallback scanCallback;

    //采购员帮仓库采购的订单状态
    StatusEnterEnum[] purchase_orderStatus = {
            StatusEnterEnum.W_HANDLER, StatusEnterEnum.W_BATCH, StatusEnterEnum.W_DELIVERY, StatusEnterEnum.FINISH
    };

    //子仓，我向其它仓调拨商品的订单状态
    StatusEnterEnum[] allocation_orderStatus = {
            StatusEnterEnum.A_HANDLER, StatusEnterEnum.A_BATCH, StatusEnterEnum.A_DELIVERY, StatusEnterEnum.FINISH
    };

    //主仓，其它仓向我调拨
    StatusEnterEnum[] mwarehouse_orderStatus = {
            StatusEnterEnum.M_HANDLER, StatusEnterEnum.M_BATCH, StatusEnterEnum.M_DELIVERY, StatusEnterEnum.FINISH
    };

    @Override
    public int getContentViewId() {
        return R.layout.fragment_wareh_order;
    }

    @Override
    protected void initViewData(View view) {

        EventBus.getDefault().register(this);

        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);
//        title_center_txt1.setOnClickListener(this);
        title_center_txt2.setOnClickListener(this);
        title_center_txt3.setOnClickListener(this);

        if(Cache.passport.isWarehouse()) {
            //子仓
            orderRoleTypeEnum = OrderRoleTypeEnum.CONSUMER;
            fragments.add(OrderListFragment.create(orderRoleTypeEnum, OrderTypeEnum.ALLOCATION_ORDER_TYPE, StatusEnterEnum.A_HANDLER));
            fragments.add(OrderListFragment.create(orderRoleTypeEnum, OrderTypeEnum.ALLOCATION_ORDER_TYPE, StatusEnterEnum.A_BATCH));
            fragments.add(OrderListFragment.create(orderRoleTypeEnum, OrderTypeEnum.ALLOCATION_ORDER_TYPE, StatusEnterEnum.A_DELIVERY));
            fragments.add(OrderListFragment.create(orderRoleTypeEnum, OrderTypeEnum.ALLOCATION_ORDER_TYPE, StatusEnterEnum.FINISH));
        } else {
            //主仓
            orderRoleTypeEnum = OrderRoleTypeEnum.MERCHANT;
            fragments.add(OrderListFragment.create(orderRoleTypeEnum, OrderTypeEnum.ALLOCATION_ORDER_TYPE, StatusEnterEnum.M_HANDLER));
            fragments.add(OrderListFragment.create(orderRoleTypeEnum, OrderTypeEnum.ALLOCATION_ORDER_TYPE, StatusEnterEnum.M_BATCH));
            fragments.add(OrderListFragment.create(orderRoleTypeEnum, OrderTypeEnum.ALLOCATION_ORDER_TYPE, StatusEnterEnum.M_DELIVERY));
            fragments.add(OrderListFragment.create(orderRoleTypeEnum, OrderTypeEnum.ALLOCATION_ORDER_TYPE, StatusEnterEnum.FINISH));
        }

        setOrderType(OrderTypeEnum.ALLOCATION_ORDER_TYPE);
        titleViewPager = new TitleViewPager(view, getChildFragmentManager(), fragments, DeviceUtils.screenWidth(getContext()));
        selectOrderTypeNum = OrderTypeEnum.ALLOCATION_ORDER_TYPE;

        //扫码
        scanCallback = new ScanActivity.ScanCallback() {
            @Override
            public void onScanSuccess(String code) {
                //进入订单详情
                Map map = new HashMap();
                map.put("passportId", Cache.passport.getPassportIdStr());
                map.put("roleType", String.valueOf(orderRoleTypeEnum.getType()));
                map.put("qrCode", code);
                Http.post(WareOrderFragment.this.getActivity(), map, Api.SCAN_QRCODE, new HttpCallback<OrderDetail>() {
                    @Override
                    public void onSuccess(OrderDetail data) {
                        //跳去详情
                        Intent intent = new Intent(WareOrderFragment.this.getActivity(), OrderDetailActivity.class);
                        intent.putExtra("detail", data);
                        intent.putExtra("orderType", selectOrderTypeNum.getType());
                        intent.putExtra("orderRoleType", orderRoleTypeEnum.getType());
                        WareOrderFragment.this.getActivity().startActivity(intent);
                    }
                });
            }
        };
    }

    void setSelect(TextView txt) {
        txt.setBackgroundResource(R.drawable.btn_white);
        txt.setTextColor(getContext().getResources().getColor(R.color.blue));
    }

    void setNoSelect(TextView txt) {
        txt.setBackgroundResource(R.color.transparent);
        txt.setTextColor(getContext().getResources().getColor(R.color.white));
    }

    void setOrderType(OrderTypeEnum orderType) {
        //刷新面板
        StatusEnterEnum[] status = null;
        //如果是采购单
        if(orderType == OrderTypeEnum.ALLOCATION_ORDER_TYPE) {
            if(Cache.passport.isWarehouse()) {
                status = allocation_orderStatus;
            }else {
                status = mwarehouse_orderStatus;
            }
        } else {
            status = purchase_orderStatus;
        }
        //重新设置所有fragment的订单类型
        for (int i = 0; i < fragments.size(); i++) {
            OrderListFragment f = (OrderListFragment) fragments.get(i);
            f.setNewOrderTypeEnum(orderType);
            f.setNewStatusEnterEnum(status[i]);
            f.setNewOrderRoleTypeEnum(orderRoleTypeEnum);
        }
        selectOrderTypeNum = orderType;
    }

    public void refreshUi() {
        //强制刷新面板
        ((OrderListFragment) titleViewPager.getNowFragment()).qRefresh();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
//        if (id == R.id.title_center_txt1) {
//            //销售单
//            if (select_index == 0) return;
//            select_index = 0;
//            setSelect(title_center_txt1);
//            setNoSelect(title_center_txt2);
//            setNoSelect(title_center_txt3);
//            setOrderType(OrderTypeEnum.SALE_ORDER_TYPE);
//            refreshUi();
//        } else
        if (id == R.id.title_center_txt2) {
            //调拨单
            if (select_index == 1) return;
            select_index = 1;
            setSelect(title_center_txt2);
            //setNoSelect(title_center_txt1);
            setNoSelect(title_center_txt3);
            setOrderType(OrderTypeEnum.ALLOCATION_ORDER_TYPE);
            titleViewPager.setTitle(1, "分批送");
            refreshUi();
        } else if (id == R.id.title_center_txt3) {
            //采购单
            if (select_index == 2) return;
            select_index = 2;
            setSelect(title_center_txt3);
            //setNoSelect(title_center_txt1);
            setNoSelect(title_center_txt2);
            setOrderType(OrderTypeEnum.PURCHASE_ORDER_TYPE);
            titleViewPager.setTitle(1, "分批收");
            refreshUi();
        } else if (id == R.id.title_left) {
            //扫一扫
            ScanActivity.setScanCallback(scanCallback);
            Intent intent = new Intent(WareOrderFragment.this.getActivity(), ScanActivity.class);
            WareOrderFragment.this.getActivity().startActivity(intent);
        } else if (id == R.id.title_right) {
            //搜索
            Intent intent = new Intent(getActivity(), OrderSearchActivity.class);
            intent.putExtra("orderType", selectOrderTypeNum.getType());
            intent.putExtra("orderRoleType", orderRoleTypeEnum.getType());
            getActivity().startActivity(intent);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    /**
     * 执行在主线程。
     * 非常实用，可以在这里将子线程加载到的数据直接设置到界面中。
     *
     * @param msg 事件1
     */
    @Subscribe
    public void onEventMainThread(WareOrderFragmentEvent msg) {
        Log.e("db", "MainActivity onEventMainThread");

        select_index = 1;
        setSelect(title_center_txt2);
        setNoSelect(title_center_txt3);
        setOrderType(OrderTypeEnum.ALLOCATION_ORDER_TYPE);

        if(titleViewPager.switchTab(msg.fragmentIndex)) {
            return;
        }
        ((OrderListFragment)titleViewPager.getNowFragment()).qRefresh();
    }
}
