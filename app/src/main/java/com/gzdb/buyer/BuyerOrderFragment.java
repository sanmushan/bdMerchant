package com.gzdb.buyer;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import com.gzdb.response.Api;
import com.gzdb.response.OrderDetail;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.response.enums.RoleTypeEnum;
import com.gzdb.response.enums.StatusEnterEnum;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.order.OrderDetailActivity;
import com.gzdb.widget.ScanActivity;
import com.gzdb.widget.TitleViewPager;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.DeviceUtils;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhumg on 4/25.
 */
public class BuyerOrderFragment extends AfinalFragment {

    TitleViewPager titleViewPager;
    List<Fragment> fragments = new ArrayList<>();
    BaseTitleBar baseTitleBar;
    ScanActivity.ScanCallback scanCallback;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_buyer_order;
    }

    @Override
    protected void initViewData(View view) {

        EventBus.getDefault().register(this);

        //扫码
        scanCallback = new ScanActivity.ScanCallback() {
            @Override
            public void onScanSuccess(String code) {
                //进入订单详情
                Map map = new HashMap();
                map.put("passportId", Cache.passport.getPassportIdStr());
                map.put("roleType", String.valueOf(RoleTypeEnum.CONSUMER.getType()));
                map.put("qrCode", code);
                Http.post(BuyerOrderFragment.this.getActivity(), map, Api.SCAN_QRCODE, new HttpCallback<OrderDetail>() {
                    @Override
                    public void onSuccess(OrderDetail data) {
                        //跳去详情
                        Intent intent = new Intent(BuyerOrderFragment.this.getActivity(), OrderDetailActivity.class);
                        intent.putExtra("detail", data);
                        intent.putExtra("orderType", OrderTypeEnum.SALE_ORDER_TYPE.getType());
                        BuyerOrderFragment.this.getActivity().startActivity(intent);
                    }
                });
            }
        };

        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setCenterTxt("订单中心");
        baseTitleBar.setRightImg(R.drawable.icon_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyerOrderListFragment fragment = (BuyerOrderListFragment)titleViewPager.getNowFragment();
                //搜索
                Intent intent = new Intent(getActivity(), BuyerOrderSearchActivity.class);
                intent.putExtra("orderType", fragment.getOrderTypeEnum().getType());
                intent.putExtra("orderRoleType", fragment.getOrderRoleTypeEnum().getType());
                getActivity().startActivity(intent);
            }
        });
        baseTitleBar.setLeftImg(R.drawable.icon_scan);
        baseTitleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //扫一扫
                ScanActivity.setScanCallback(scanCallback);
                Intent intent = new Intent(BuyerOrderFragment.this.getActivity(), ScanActivity.class);
                BuyerOrderFragment.this.getActivity().startActivity(intent);
            }
        });

        fragments.add(BuyerOrderListFragment.create(OrderRoleTypeEnum.CONSUMER, OrderTypeEnum.PURCHASE_ORDER_TYPE, StatusEnterEnum.P_HANDLER, 0));
        fragments.add(BuyerOrderListFragment.create(OrderRoleTypeEnum.CONSUMER, OrderTypeEnum.PURCHASE_ORDER_TYPE, StatusEnterEnum.P_DELIVERY, 1));
        fragments.add(BuyerOrderListFragment.create(OrderRoleTypeEnum.CONSUMER, OrderTypeEnum.PURCHASE_ORDER_TYPE, StatusEnterEnum.FINISH, 2));

        titleViewPager = new TitleViewPager(view, getChildFragmentManager(), fragments, DeviceUtils.screenWidth(getContext()));
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
    public void onEventMainThread(BuyerOrderFragmentEvent msg) {
        Log.e("db", "BuyerOrderFragment onEventMainThread");
        titleViewPager.switchTab(msg.fragmentIndex);
    }
}
