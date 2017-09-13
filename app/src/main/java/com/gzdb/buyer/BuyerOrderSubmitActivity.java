package com.gzdb.buyer;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzdb.response.Api;
import com.gzdb.response.CartWarehouseIds;
import com.gzdb.response.Currency;
import com.gzdb.response.Warehouse;
import com.gzdb.response.enums.DeviceTypeEnum;
import com.gzdb.response.enums.OrderPaymentEnter;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.response.enums.PaymentTypeEnum;
import com.gzdb.response.enums.UpdatePasswordTypeEnum;
import com.gzdb.utils.Utils;
import com.gzdb.utils.baidu.BaiduLocationManager;
import com.gzdb.utils.baidu.LocationAddress;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.me.ResetPasswordActivity;
import com.gzdb.widget.InputPayPasswordDialog;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.bar.BaseTitleBar;
import com.zhumg.anlib.widget.dialog.TipClickListener;
import com.zhumg.anlib.widget.dialog.TipDialog;
import com.zhumg.anlib.widget.mvc.LoadingView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by zhumg on 4/24.
 */
public class BuyerOrderSubmitActivity extends AfinalActivity implements View.OnClickListener {

    @Bind(R.id.tv_name)
    TextView tv_name;

    @Bind(R.id.tv_phone)
    TextView tv_phone;

    @Bind(R.id.tv_address)
    TextView tv_address;

    @Bind(R.id.tv_z1)
    TextView tv_z1;

    @Bind(R.id.w_all_price)
    TextView w_all_price;

    @Bind(R.id.btn_commit)
    View btn_commit;

    @Bind(R.id.item_ll)
    LinearLayout item_ll;

    @Bind(R.id.p_layout)
    View p_layout;

    @Bind(R.id.btn_ll)
    View btn_ll;

    LoadingView loadingView;

    BaseTitleBar baseTitleBar;
    //预下单
    String sequenceNumber;
    //仓库ID
    long targetWarehouseId;
    //合计价格
    long all_price;
    int all_count;

    int red_color;

    JSONObject paymentParameter;

    //下单的仓库
    Warehouse downOrderWarehouse;

    TipDialog errorTipDialog;
    //异常，关闭窗体，并跳转回订单页
    TipClickListener errorTipListener = new TipClickListener() {
        @Override
        public void onClick(boolean left) {
            closeActivity(0);//异常订单，统一调去 未处理 订单页面
        }
    };

    //采购员余额
    long urrentAmount;

    @Override
    public int getContentViewId() {
        return R.layout.activity_buyer_order_submit;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setCenterTxt("订单付款");
        baseTitleBar.setLeftBack(this);

        red_color = getResources().getColor(R.color.red);

        loadingView = new LoadingView(view);
        errorTipDialog = new TipDialog(this);
        errorTipDialog.setTipClickListener(errorTipListener).hibeLeftBtn().setTitle("温馨提示");

        targetWarehouseId = getIntent().getLongExtra("targetWarehouseId", 0);
        //根据仓库ID,获得仓库信息
        List<Warehouse> warehouses = Cache.warehouses;
        for (int i = 0; i < warehouses.size(); i++) {
            Warehouse w = warehouses.get(i);
            if(w.getWarehouseId() == targetWarehouseId) {
                downOrderWarehouse = w;
                break;
            }
        }
        all_count = BuyerShopCart.getAllCount();
        all_price = BuyerShopCart.getAllPrice();
        //设置
        tv_address.setText(downOrderWarehouse.getFormatAddress());
        w_all_price.setText(Utils.toYuanStr(all_price));

        btn_commit.setOnClickListener(this);

        //隐藏
        p_layout.setVisibility(View.GONE);
        btn_ll.setVisibility(View.GONE);

        //显示加载中
        loadingView.showLoading();

        //调预下单
        prepareCreateOrder();

        //调分单
        startSplitItems();

        //获取余额
        getCurrencys();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_commit) {
            //确定付款
            btn_commit.setEnabled(false);
            commit();
        }
    }
    //获取用户货币
    void getCurrencys() {
        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        Http.post(this, map, Api.GET_CURRENCYS, new HttpCallback<List<Currency>>("datas") {
            @Override
            public void onSuccess(List<Currency> datas) {
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getType() == 1) {
                        btn_commit.setEnabled(true);//默认先不能点击
                        urrentAmount = datas.get(i).getCurrentAmount();
                        tv_z1.setText("所剩余额：¥:" + Utils.toYuanStr(urrentAmount));
                        break;
                    }
                }
            }
        });
    }
    /**
     * 购物车分仓显示
     */
    Map splitItems() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BuyerShopCart.itemLists.size(); i++) {
            BuyerShopCart.BuyerCartItem cart = BuyerShopCart.itemLists.get(i);
            sb.append(cart.srcItem.getItemId());
            if (i < BuyerShopCart.itemLists.size() - 1) {
                sb.append(",");
            }
        }
        Map map = new HashMap<>();
        map.put("items", sb.toString());
        return map;
    }

    //分单
    void startSplitItems() {
        //访问数据
        Http.post(this, splitItems(), Api.SPLIT_ITEMS, new HttpCallback<List<CartWarehouseIds>>("datas") {
            @Override
            public void onSuccess(List<CartWarehouseIds> datas) {
                //循环，设置所有购物车数据
                for (int i = 0; i < datas.size(); i++) {
                    CartWarehouseIds c = datas.get(i);
                    View v = createWItemView(c);
                    item_ll.addView(v);
                }
                p_layout.setVisibility(View.VISIBLE);
                btn_ll.setVisibility(View.VISIBLE);
                loadingView.hibe();
            }

            @Override
            public void onFailure() {
                loadingView.showLoadingTxt(msg);
            }
        });
    }

    View createWItemView(CartWarehouseIds c) {
        long price = 0;
        List<Long> ids = c.getItems();
        View view = View.inflate(this, R.layout.widget_buyer_order_submit_items, null);
        //名称
        ((TextView)view.findViewById(R.id.tv_sname)).setText(c.getWarehouseName());
        LinearLayout layout = ViewUtils.find(view, R.id.items_view);
        for (int i = 0; i < ids.size(); i++) {
            long itemId = ids.get(i);
            //取得该对象
            BuyerShopCart.BuyerCartItem item = BuyerShopCart.getCartItem(itemId);
            if(item == null) {
                continue;
            }
            long iPrice = item.getPrice();
            price = price + iPrice;
            //创建界面
            refreshItemViewTxt(layout, item.srcItem.getItemName(), item.count + item.srcItem.getUnitName(), Utils.toYuanStr(iPrice), i==0?0:10, false);
        }
        refreshItemViewTxt(layout, "合计：", "", Utils.toYuanStr(price), 10, true);
        return view;
    }

    void refreshItemViewTxt(LinearLayout items_view, String t1, String t2, String t3, int top, boolean red) {

        View p_view = View.inflate(this, R.layout.widget_supply_buy_item,
                null);
        if (top != 0) {
            p_view.setPadding(0, top, 0, 0);
        } else {
            p_view.setPadding(0, 0, 0, 0);
        }

        TextView textview1 = (TextView) p_view.findViewById(R.id.item_t1);
        if (t1 != null) {
            textview1.setText(t1);
            if(red) {
                textview1.setTextColor(red_color);
            }
        } else {
            textview1.setVisibility(View.INVISIBLE);
        }

        TextView textview2 = (TextView) p_view.findViewById(R.id.item_t2);
        if (t2 != null) {
            textview2.setText(t2);
            if(red) {
                textview2.setTextColor(red_color);
            }
        } else {
            textview2.setVisibility(View.INVISIBLE);
        }

        TextView textview3 = (TextView) p_view.findViewById(R.id.item_t3);
        if (t3 != null) {
            textview3.setText(t3);
            if(red) {
                textview3.setTextColor(red_color);
            }
        } else {
            textview3.setVisibility(View.INVISIBLE);
        }
        items_view.addView(p_view);
    }

    //预下单
    void prepareCreateOrder() {
        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("orderType", String.valueOf(OrderTypeEnum.PURCHASE_ORDER_TYPE.getType()));
        Http.post(this, map, Api.P_CREATE_ORDER, new HttpCallback<String>("sequenceNumber") {
            @Override
            public void onSuccess(String data) {
                BuyerOrderSubmitActivity.this.sequenceNumber = data;
            }

            @Override
            public void onFailure() {
                //关掉
                errorTipDialog.setContentMsg("预下单失败，错误异常：" + msg).show();
            }
        });
    }


    //真正的下单
    void commit() {

        JSONObject itemSet = new JSONObject();
        for (int i = 0; i < BuyerShopCart.itemLists.size(); i++) {
            BuyerShopCart.BuyerCartItem wCartItem = BuyerShopCart.itemLists.get(i);
            try {
                itemSet.put(wCartItem.srcItem.getItemId() + "", "" + wCartItem.count);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("deviceType", String.valueOf(DeviceTypeEnum.DEVICE_TYPE_ANDROID.getType()));
        map.put("targetWarehouseId", String.valueOf(targetWarehouseId));
        map.put("sequenceNumber", sequenceNumber);
        LocationAddress address = BaiduLocationManager.getInitSelectAddress();
        if (address != null) {
            map.put("currentLocation", address.getLocation());
        } else {
            map.put("currentLocation", "0,0");
        }
        map.put("itemSet", itemSet.toString());

        showLoadingDialog();

        Http.post(this, map, Api.PURCHASE_ORDER, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                //去付款
                payOrder(sequenceNumber, all_price);
            }

            @Override
            public void onFailure() {
                errorTipDialog.setContentMsg("确认下单失败，错误异常：" + msg).show();
            }
        }.setPass());
    }


    //支付订单
    void payOrder(final String sequenceNumber, final long all_price) {
        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("paymentEnter", String.valueOf(OrderPaymentEnter.COMPOSITE.getType()));//合并支付
        map.put("sequenceNumber", sequenceNumber);
        map.put("paymentType", PaymentTypeEnum.BALANCE.getEnName());
        Http.post(BuyerOrderSubmitActivity.this, map, Api.PAYMENT_ORDER, new HttpCallback() {
            @Override
            public void onSuccess(Object obj) {
                closeLoadingDialog();
                JSONObject data = (JSONObject) obj;

                String partnerId = data.optString("partnerId");
                String appId = data.optString("appId");
                String prePaymentId = data.optString("prePaymentId");
                String randomParameter = data.optString("randomParameter");
                long timeStamp = data.optLong("timeStamp");
                String sign = data.optString("sign");
                paymentParameter = new JSONObject();
                try {
                    paymentParameter.put("partnerId", partnerId);
                    paymentParameter.put("appId", appId);
                    paymentParameter.put("prePaymentId", prePaymentId);
                    paymentParameter.put("randomParameter", randomParameter);
                    paymentParameter.put("timeStamp", timeStamp);
                    paymentParameter.put("sign", sign);

                    //显示输入密码
                    showInputDialog();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                //super.onFailure();
                btn_commit.setEnabled(true);
                //没有支付密码
                if (code == 100) {
                    //提示跳去修改支付密码
                    TipDialog tipDialog = new TipDialog(BuyerOrderSubmitActivity.this);
                    tipDialog.setTipClickListener(new TipClickListener() {
                        @Override
                        public void onClick(boolean left) {
                            if (left) {
                                //按了取消按钮，直接关闭界面
                                closeActivity(0);
                            } else {
                                ActivityManager.finishActivity(BuyerShopCartActivity.class);
                                ActivityManager.finishActivity(BuyerItemSearchActivity.class);
                                //弹出设置密码
                                Intent intent = new Intent(BuyerOrderSubmitActivity.this, ResetPasswordActivity.class);
                                intent.putExtra("updateType", UpdatePasswordTypeEnum.UPDATE_PAY_PASSWORD.getKey());
                                BuyerOrderSubmitActivity.this.startActivity(intent);
                                finish();
                            }
                        }
                    }).setContentMsg("你还没有设置支付密码，现在去设置吗？")
                            .setTitle("温馨提示")
                            .setLeftBtn("取消")
                            .setRightBtn("去设置").show();
                } else {
                    errorTipDialog.setContentMsg("支付订单失败，错误异常：" + msg).show();
                }
            }
        });
    }

    InputPayPasswordDialog inputDialog = null;

    void showInputDialog() {
        //弹出输入密码框
        if(inputDialog == null) {
            inputDialog = new InputPayPasswordDialog(BuyerOrderSubmitActivity.this,
                    new InputPayPasswordDialog.InputPasswordCallBack() {
                        @Override
                        public void callback(String password) {
                            if (password != null && password.trim().length() > 0) {
                                balancePay(password);
                            } else {
                                //直接关闭
                                ToastUtil.showToast(BuyerOrderSubmitActivity.this, "你取消了密码输入");
                                closeActivity(0);
                            }
                        }
                    });
        }
        inputDialog.setShowMoney(Utils.toYuanStr(all_price));
        inputDialog.show();
    }

    void closeActivity(int targetIndex) {
        //清除所有购物车内容
        BuyerShopCart.removeAll();
        //关闭购物车activity
        ActivityManager.finishActivity(BuyerShopCartActivity.class);
        ActivityManager.finishActivity(BuyerItemSearchActivity.class);
        //关闭自己
        finish();
        //发布事件
        EventBus.getDefault().post(new BuyerOrderListFragmentEvent(0));//刷新 待处理 界面事件
        EventBus.getDefault().post(new BuyerActivityEvent(0));//首页tab跳转去订单界面
        EventBus.getDefault().post(new BuyerOrderFragmentEvent(targetIndex));//订单界面跳转去 待处理界面
    }

    void balancePay(String password) {
        Map map = new HashMap();
        try {
            map.put("paymentParameter", paymentParameter.toString());
            map.put("passportId", Cache.passport.getPassportIdStr());
            map.put("paymentPassword", password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Http.post(BuyerOrderSubmitActivity.this, map, Api.BANLANCE_PAYMENT, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                //支付宝付款成功
                ToastUtil.showToast(BuyerOrderSubmitActivity.this, "余额付款成功!");
                closeActivity(0);
            }

            @Override
            public void onFailure() {
                Log.e("db", "code="+code+", msg="+msg);
                //如果是密码错误的，过滤掉，重新弹出
                if(code == 1) {
                    ToastUtil.showToast(BuyerOrderSubmitActivity.this, msg);
                    //重新弹出
                    showInputDialog();
                } else {
                    errorTipDialog.setContentMsg("余额支付订单失败，错误异常：" + msg).show();
                }
            }
        }.setPass());
    }
}
