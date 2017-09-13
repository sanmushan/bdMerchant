package com.gzdb.warehouse.ware;

import android.view.View;
import android.widget.ListView;

import com.gzdb.response.Api;
import com.gzdb.response.enums.DeviceTypeEnum;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.utils.baidu.BaiduLocationManager;
import com.gzdb.utils.baidu.LocationAddress;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.MainActivityEvent;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.adapter.ItemAdapter;
import com.gzdb.warehouse.main.WareOrderFragmentEvent;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.DialogUtils;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.AdapterModel;
import com.zhumg.anlib.widget.dialog.TipClickListener;
import com.zhumg.anlib.widget.dialog.TipDialog;
import com.zhumg.anlib.widget.mvc.LoadingView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by zhumg on 4/20.
 */
public class WareShopCartActivity extends AfinalActivity implements View.OnClickListener, Runnable {

    @Bind(R.id.title_left)
    View title_left;

    @Bind(R.id.title_right)
    View title_right;

    @Bind(R.id.supply_cartlist)
    ListView listView;

    @Bind(R.id.okbtn)
    View okbtn;

    LoadingView loadingView;

    List<AdapterModel> items = new ArrayList<>();
    ItemAdapter adapter = null;

    //提示窗
    TipDialog tipDialog;
    //预下单
    String sequenceNumber;

    @Override
    public int getContentViewId() {
        return R.layout.activity_warehouse_cart;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();

        loadingView = new LoadingView(view);

        title_right.setOnClickListener(this);
        title_left.setOnClickListener(this);

        tipDialog = DialogUtils.createTipDialog(this, "确定要清空购物车的商品吗?", new TipClickListener() {
            @Override
            public void onClick(boolean left) {
                if (!left) {
                    //清空
                    WareShopCart.removeAll();
                    clearAll();
                }
            }
        });

        loadingView.hibe();

        refreshDatas();

        adapter = new ItemAdapter(this, items, null, true);
        adapter.delRunnable = this;

        listView.setAdapter(adapter);

        if (adapter.isEmpty()) {
            clearAll();
        } else {
            adapter.notifyDataSetChanged();
        }

        okbtn.setOnClickListener(this);
        prepareCreateOrder();
    }

    void clearAll() {
        title_right.setVisibility(View.INVISIBLE);
        adapter.getDatas().clear();
        listView.setVisibility(View.GONE);
        okbtn.setVisibility(View.GONE);
        loadingView.setResetListener(WareShopCartActivity.this);
        loadingView.showLoadingTxtBtn("购物车是空的", "去逛逛");
    }

    void refreshDatas() {
        for (int i = 0; i < WareShopCart.itemLists.size(); i++) {
            items.add(WareShopCart.itemLists.get(i));
        }
        if (items.size() < 1) {
            loadingView.showLoadingTxtBtn("购物车为空", "去逛逛");
            loadingView.setResetListener(this);
            listView.setVisibility(View.GONE);
        } else {
            loadingView.hibe();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_left) {
            finish();
        } else if (id == R.id.wlv_btn) {
            //去逛逛标志
            finish();
        } else if (id == R.id.title_right) {
            //清空
            if (adapter.isEmpty()) {
                return;
            }
            tipDialog.show();
        } else if(id == R.id.okbtn) {
            commit();
        }
    }

    @Override
    public void run() {
        if (WareShopCart.getAllCount() <= 0) {
            //清空
            clearAll();
        }
    }

    //预下单
    void prepareCreateOrder() {
        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("orderType", String.valueOf(OrderTypeEnum.ALLOCATION_ORDER_TYPE.getType()));
        Http.post(this, map, Api.P_CREATE_ORDER, new HttpCallback<String>("sequenceNumber") {
            @Override
            public void onSuccess(String data) {
                //closeLoading();
                WareShopCartActivity.this.sequenceNumber = data;
            }

            @Override
            public void onFailure() {
                //关掉
                if (msg != null) {
                    ToastUtil.showToast(WareShopCartActivity.this, "预下单失败，" + msg);
                    //直接关闭
                    finish();
                }
            }
        });
    }

    //真正的下单
    void commit() {

        JSONObject itemSet = new JSONObject();
        for (int i = 0; i < WareShopCart.itemLists.size(); i++) {
            WareShopCart.CartItem wCartItem = WareShopCart.itemLists.get(i);
            try {
                itemSet.put(wCartItem.srcItem.getItemTemplateId() + "", "" + wCartItem.count);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("deviceType", String.valueOf(DeviceTypeEnum.DEVICE_TYPE_ANDROID.getType()));

        map.put("sequenceNumber", sequenceNumber);
        LocationAddress address = BaiduLocationManager.getInitSelectAddress();
        if (address != null) {
            map.put("currentLocation", address.getLocation());
        } else {
            map.put("currentLocation", "0,0");
        }
        map.put("itemTemplateSet", itemSet.toString());

        showLoadingDialog();

        Http.post(this, map, Api.ALLOCATION_ORDER, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                //去付款
                closeLoadingDialog();
                WareShopCart.removeAll();
                //由于可能是搜索页面进入，需要关掉搜索页面
                ActivityManager.finishActivity(WareSearchItemActivity.class);
                ToastUtil.showToast(WareShopCartActivity.this, msg);
                //跳转去订单页面
                EventBus.getDefault().post(new MainActivityEvent(2));
                //跳转去订单页面的处理中页面
                EventBus.getDefault().post(new WareOrderFragmentEvent(0));
                finish();
            }

            @Override
            public void onFailure() {
                super.onFailure();
                closeLoadingDialog();
            }
        }.setPass());
    }
}
