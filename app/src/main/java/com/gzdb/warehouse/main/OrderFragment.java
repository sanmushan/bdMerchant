package com.gzdb.warehouse.main;

import android.content.Intent;
import android.support.v4.app.Fragment;
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
import com.gzdb.warehouse.order.OrderListFragment;
import com.gzdb.warehouse.order.OrderSearchActivity;
import com.gzdb.widget.ScanActivity;
import com.gzdb.widget.TitleViewPager;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.DeviceUtils;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单
 * Created by zhumg on 4/18.
 */
public class OrderFragment extends AfinalFragment {

    TitleViewPager titleViewPager;
    List<Fragment> fragments = new ArrayList<>();
    BaseTitleBar baseTitleBar;
    ScanActivity.ScanCallback scanCallback;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_order;
    }

    @Override
    protected void initViewData(View view) {

        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setCenterTxt("订单中心");
        baseTitleBar.setRightImg(R.drawable.icon_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //搜索
                Intent intent = new Intent(getActivity(), OrderSearchActivity.class);
                intent.putExtra("orderType", OrderTypeEnum.SALE_ORDER_TYPE.getType());
                intent.putExtra("orderRoleType", OrderRoleTypeEnum.MERCHANT.getType());
                getActivity().startActivity(intent);
            }
        });


        //扫码
        scanCallback = new ScanActivity.ScanCallback() {
            @Override
            public void onScanSuccess(String code) {
                //进入订单详情
                Map map = new HashMap();
                map.put("passportId", Cache.passport.getPassportIdStr());
                map.put("roleType", String.valueOf(RoleTypeEnum.MERCHANT.getType()));
                map.put("qrCode", code);
                Http.post(OrderFragment.this.getActivity(), map, Api.SCAN_QRCODE, new HttpCallback<OrderDetail>() {
                    @Override
                    public void onSuccess(OrderDetail data) {
                       //二叼需求
                        if(msg!=null)
                        {
                            ToastUtil.showToast(getActivity(),msg);

                        }
//                        if(data==null||data.getOrderId()==0){
//                            ToastUtil.showToast(getActivity(),"无效订单");
//                            return;
//                        }
//                        //跳去详情
//                        Intent intent = new Intent(OrderFragment.this.getActivity(), OrderDetailActivity.class);
//                        intent.putExtra("detail", data);
//                        intent.putExtra("orderType", OrderTypeEnum.SALE_ORDER_TYPE.getType());
//                        OrderFragment.this.getActivity().startActivity(intent);
                    }
                });
            }
        };

        baseTitleBar.setLeftImg(R.drawable.icon_scan);
        baseTitleBar.setLeftListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //扫一扫
                ScanActivity.setScanCallback(scanCallback);
                Intent intent = new Intent(OrderFragment.this.getActivity(), ScanActivity.class);
                OrderFragment.this.getActivity().startActivity(intent);
            }
        });

        fragments.add(OrderListFragment.create(OrderRoleTypeEnum.MERCHANT, OrderTypeEnum.SALE_ORDER_TYPE, StatusEnterEnum.HANDLER));
        fragments.add(OrderListFragment.create(OrderRoleTypeEnum.MERCHANT, OrderTypeEnum.SALE_ORDER_TYPE, StatusEnterEnum.BATCH));
        fragments.add(OrderListFragment.create(OrderRoleTypeEnum.MERCHANT, OrderTypeEnum.SALE_ORDER_TYPE, StatusEnterEnum.DELIVERY));
        fragments.add(OrderListFragment.create(OrderRoleTypeEnum.MERCHANT, OrderTypeEnum.SALE_ORDER_TYPE, StatusEnterEnum.FINISH));

        titleViewPager = new TitleViewPager(view, getChildFragmentManager(), fragments, DeviceUtils.screenWidth(getContext()));
    }

}
