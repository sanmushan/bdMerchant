package com.gzdb.buyer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzdb.response.Api;
import com.gzdb.response.ConsumerOrderItem;
import com.gzdb.response.OrderDetail;
import com.gzdb.response.enums.ClientTypeEnum;
import com.gzdb.response.enums.OrderPaymentEnter;
import com.gzdb.response.enums.OrderStatusEnum;
import com.gzdb.response.enums.PaymentTypeEnum;
import com.gzdb.response.enums.UpdatePasswordTypeEnum;
import com.gzdb.utils.CreateQRImage;
import com.gzdb.utils.Utils;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.me.ResetPasswordActivity;
import com.gzdb.warehouse.order.OrderDetailActivity;
import com.gzdb.widget.InputPayPasswordDialog;
import com.gzdb.zoom.SpaceImageDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;
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
 * Created by zhumg on 4/25.
 */
public class BuyerOrderDetailActivity extends AfinalActivity implements View.OnClickListener {

    int btnHandlerType;

    OrderDetail detail;
    long orderId;

    @Bind(R.id.p_layout)
    View p_layout;

    @Bind(R.id.tv_order_pnum)
    TextView tv_order_pnum;

    @Bind(R.id.tv_name)
    TextView tv_name;

    @Bind(R.id.tv_order_id)
    TextView tv_order_id;

    @Bind(R.id.tv_time)
    TextView tv_time;

    @Bind(R.id.tv_state)
    TextView tv_state;

    @Bind(R.id.items_view)
    LinearLayout items_view;

    @Bind(R.id.tv_allcount)
    TextView tv_allcount;

    @Bind(R.id.tv_shou_user)
    TextView tv_shou_user;

    @Bind(R.id.tv_shou_phone)
    TextView tv_shou_phone;

    @Bind(R.id.tv_shou_address)
    TextView tv_shou_address;

    @Bind(R.id.btn_ll)
    View btn_ll;

    @Bind(R.id.btn1)
    TextView btn1;

    @Bind(R.id.btn2)
    TextView btn2;

    @Bind(R.id.img_qr_code)
    ImageView img_qr_code;

    BaseTitleBar baseTitleBar;
    LoadingView loadingView;

    int orderRoleType;

    TipDialog errorTipDialog;

    @Bind(R.id.ll00)
    LinearLayout ll00;
    @Bind(R.id.ll01)
    LinearLayout ll01;
    @Bind(R.id.ll02)
    LinearLayout ll02;
    @Bind(R.id.ll05)
    LinearLayout ll05;

    //拍照收货单签名照
    @Bind(R.id.img01)
    ImageView img01;
    @Bind(R.id.img02)
    ImageView img02;
    @Bind(R.id.img03)
    ImageView img03;
    @Bind(R.id.img04)
    ImageView img04;
    @Bind(R.id.img05)
    ImageView img05;
    List<String> datas = new ArrayList<String>();

    @Override
    public int getContentViewId() {
        return R.layout.activity_buyer_order_detail;
    }

    @Override
    public void initView(View view) {

        setTranslucentStatus();

        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setCenterTxt("订单详情");
        baseTitleBar.setLeftBack(this);

        orderRoleType = getIntent().getIntExtra("orderRoleType", 0);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        img01.setOnClickListener(this);
        img02.setOnClickListener(this);
        img03.setOnClickListener(this);
        img04.setOnClickListener(this);
        img05.setOnClickListener(this);

        loadingView = new LoadingView(view);
        loadingView.setResetListener(this);

        orderId = getIntent().getLongExtra("orderId", 0);

        p_layout.setVisibility(View.GONE);
        btn_ll.setVisibility(View.GONE);

        loadingView.showLoading();
        startGetOrderDetail();
    }

    TipDialog getErrorTipDialog() {
        if (errorTipDialog == null) {
            errorTipDialog = new TipDialog(this);
            errorTipDialog.hibeLeftBtn();
        }
        return errorTipDialog;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn1) {

            //针对按钮类型处理
            if (btnHandlerType == 0) return;

            switch (btnHandlerType) {
                case OrderDetailActivity.Btn_HandlerType_Pay:
                    payOrder(String.valueOf(detail.getOrderId()), detail.getTotalPrice());
                    return;
                case OrderDetailActivity.Btn_HandlerType_Confirm: {
                    //确认订单
                    Map map = new HashMap();
                    map.put("orderId", String.valueOf(detail.getOrderId()));
                    map.put("passportId", Cache.passport.getPassportIdStr());
                    Http.post(BuyerOrderDetailActivity.this, map, Api.CONFIRM_ORDER, new HttpCallback() {
                        @Override
                        public void onSuccess(Object data) {
                            btn_ll.setVisibility(View.GONE);
                            getErrorTipDialog().setContentMsg(msg).setTipClickListener(new TipClickListener() {
                                @Override
                                public void onClick(boolean left) {
                                    //发消息通知刷新
                                    EventBus.getDefault().post(new BuyerOrderListFragmentEvent(0));
                                    //关闭自己
                                    BuyerOrderDetailActivity.this.finish();
                                }
                            }).show();
                        }
                    }.setPass());
                    return;
                }
            }
        } else if (id == R.id.btn2) {
            //取消订单
            Map map = new HashMap();
            map.put("orderId", String.valueOf(detail.getOrderId()));
            map.put("passportId", Cache.passport.getPassportIdStr());
            Http.post(BuyerOrderDetailActivity.this, map, Api.CANCEL_ORDER, new HttpCallback() {
                @Override
                public void onSuccess(Object data) {
                    ToastUtil.showToast(BuyerOrderDetailActivity.this, msg);
                    //通知刷新
                    EventBus.getDefault().post(new BuyerOrderListFragmentEvent(0));
                    finish();
                }
            }.setPass());
        } else if (id == com.zhumg.anlib.R.id.wlv_btn) {
            //重置
            loadingView.showLoading();
            startGetOrderDetail();
        } else if (id == R.id.img01) {
            zoom(img01, 0);
        } else if (id == R.id.img02) {
            zoom(img02, 1);
        } else if (id == R.id.img03) {
            zoom(img03, 2);
        } else if (id == R.id.img04) {
            zoom(img04, 3);
        } else if (id == R.id.img05) {
            zoom(img05, 4);
        }

    }

    //点击订单照片图片放大
    private void zoom(final ImageView img, final int position) {
        datas.addAll(detail.getOrderReceiptImgs());
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyerOrderDetailActivity.this, SpaceImageDetailActivity.class);
                intent.putExtra("images", (ArrayList<String>) datas);
                int[] location = new int[2];
                img.getLocationOnScreen(location);
                intent.putExtra("position", position);
                intent.putExtra("locationX", location[0]);
                intent.putExtra("locationY", location[1]);
                intent.putExtra("width", img.getWidth());
                intent.putExtra("height", img.getHeight());
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

    }

    void startGetOrderDetail() {
        Map map = new HashMap();
        map.put("orderId", String.valueOf(orderId));
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("roleType", String.valueOf(orderRoleType));
        Http.post(this, map, Api.ORDER_DETAIL, new HttpCallback<OrderDetail>() {
            @Override
            public void onSuccess(OrderDetail data) {
                loadingView.hibe();
                //设置内容
                refreshOrder(data);
            }

            @Override
            public void onFailure() {
                loadingView.showLoadingTxtBtn(msg, "重试");
                loadingView.setResetListener(BuyerOrderDetailActivity.this);
            }
        });
    }

    void refreshOrder(OrderDetail detail) {

        this.detail = detail;

        refreshItemViews(false);

        tv_name.setText(detail.getShippingNickName());
        tv_order_id.setText(String.valueOf(detail.getOrderSequenceNumber()));
        tv_time.setText(detail.getCreateTime());
        tv_state.setText(detail.getStatusValue());

        tv_allcount.setText("¥" + Utils.toYuanStr(detail.getTotalPrice()));
        tv_shou_user.setText(detail.getReceiptNickName());
        tv_shou_phone.setText(detail.getReceiptPhone());
        tv_shou_address.setText(detail.getReceiptAddress());
        CreateQRImage qr = new CreateQRImage();
        Bitmap bm = qr.createQRImage(detail.getQrCode(), 500, 500);
        img_qr_code.setImageBitmap(bm);
        p_layout.setVisibility(View.VISIBLE);

        btn_ll.setVisibility(View.VISIBLE);

        handlerBtn();


        //显示收货照片
        if (detail.getOrderReceiptImgs().size() > 0) {

            ll00.setVisibility(View.VISIBLE);
            ll05.setVisibility(View.VISIBLE);

            if (detail.getOrderReceiptImgs().get(0) != null) {
                ImageLoader.getInstance().displayImage(detail.getOrderReceiptImgs().get(0), img01);
            }
            if (detail.getOrderReceiptImgs().get(1) != null) {
                img02.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(detail.getOrderReceiptImgs().get(1), img02);
            }
            if (detail.getOrderReceiptImgs().get(2) != null) {
                ll01.setVisibility(View.VISIBLE);
                img03.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(detail.getOrderReceiptImgs().get(2), img03);
            }
            if (detail.getOrderReceiptImgs().get(3) != null) {
                img04.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(detail.getOrderReceiptImgs().get(3), img04);
            }
            if (detail.getOrderReceiptImgs().get(4) != null) {
                ll02.setVisibility(View.VISIBLE);
                img05.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(detail.getOrderReceiptImgs().get(4), img05);
            }

        } else {
            return;
        }

    }

    void handlerBtn() {
        //采购员
        if (Cache.clientTypeEnum == ClientTypeEnum.PURCHASE) {
            tv_order_pnum.setVisibility(View.GONE);

            //未付款
            if (detail.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_DEFAULT.getType()) {

                btn1.setVisibility(View.VISIBLE);
                btn1.setText("去付款");

                btnHandlerType = OrderDetailActivity.Btn_HandlerType_Pay;

                btn2.setVisibility(View.VISIBLE);
                btn2.setText("取消订单");
            }
            //已完成
            else if (detail.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_CONFIRM.getType()) {
                btn_ll.setVisibility(View.GONE);
            }
            //已取消
            else if (detail.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_CANCEL.getType()) {
                btn_ll.setVisibility(View.GONE);
            }
            //其它全是确认订单
            else {
                btn1.setEnabled(true);
                btn1.setText("确认订单");
                btnHandlerType = OrderDetailActivity.Btn_HandlerType_Confirm;

                btn2.setVisibility(View.GONE);
            }
        }
    }


    JSONObject paymentParameter;

    //支付订单
    void payOrder(final String orderId, final long all_price) {
        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("paymentEnter", String.valueOf(OrderPaymentEnter.INDEPENDENT.getType()));//单独支付
        map.put("orderId", orderId);
        map.put("paymentType", PaymentTypeEnum.BALANCE.getEnName());
        Http.post(BuyerOrderDetailActivity.this, map, Api.PAYMENT_ORDER, new HttpCallback() {
            @Override
            public void onSuccess(Object obj) {
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //弹出输入密码框
                InputPayPasswordDialog dialog = new InputPayPasswordDialog(BuyerOrderDetailActivity.this,
                        new InputPayPasswordDialog.InputPasswordCallBack() {
                            @Override
                            public void callback(String password) {
                                if (password != null && password.trim().length() > 0) {
                                    balancePay(password);
                                }
                            }
                        });
                dialog.setShowMoney(Utils.toYuanStr(all_price));
                dialog.show();
            }

            @Override
            public void onFailure() {
                //没有支付密码
                if (code == 100) {
                    //提示跳去修改支付密码
                    TipDialog tipDialog = new TipDialog(BuyerOrderDetailActivity.this);
                    tipDialog.setTipClickListener(new TipClickListener() {
                        @Override
                        public void onClick(boolean left) {
                            if (!left) {
                                //弹出设置密码
                                Intent intent = new Intent(BuyerOrderDetailActivity.this, ResetPasswordActivity.class);
                                intent.putExtra("updateType", UpdatePasswordTypeEnum.UPDATE_PAY_PASSWORD.getKey());
                                BuyerOrderDetailActivity.this.startActivity(intent);
                            }
                        }
                    }).setContentMsg("你还没有设置支付密码，现在去设置吗？")
                            .setTitle("温馨提示")
                            .setLeftBtn("取消")
                            .setRightBtn("去设置").show();
                } else {
                    getErrorTipDialog().setContentMsg(msg).show();
                }
            }
        });
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
        Http.post(BuyerOrderDetailActivity.this, map, Api.BANLANCE_PAYMENT, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                //支付宝付款成功
                btn_ll.setVisibility(View.GONE);
                ToastUtil.showToast(BuyerOrderDetailActivity.this, "余额付款成功!");
            }

            @Override
            public void onFailure() {
                getErrorTipDialog().setContentMsg(msg).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        if (requestCode == 123) {
            boolean remove = data.getBooleanExtra("remove", false);
            if (remove) {
                btn1.setEnabled(false);
                btn1.setBackgroundResource(R.drawable.btn_grey);
            }
        }
    }

    private void refreshItemViews(boolean all) {
        items_view.removeAllViews();

        int count = 0;

        List<ConsumerOrderItem> items = detail.getItemSnapshotArray();

        if (all) {
            count = items.size();
        } else {
            count = items.size() > Api.updown_count ? Api.updown_count
                    : items.size();
        }
        for (int i = 0; i < count; i++) {
            ConsumerOrderItem item = items.get(i);

            refreshItemViewTxt(item.getItemName(), item.getQuantity()
                    + item.getUnitName(), "¥" + Utils.toYuanStr(item.getNormalPrice()), 2);

            if (item.getUnReceiptQuantity() > 0) {
                View vvv = refreshItemViewTxt("", "已收货：" + item.getReceiptQuantity(), "未收货：" + item.getUnReceiptQuantity(), 2);
                ((TextView) vvv.findViewById(R.id.item_t3)).setTextColor(getResources().getColor(R.color.red));
            } else {
                View vvv = refreshItemViewTxt("", "已收货：" + item.getReceiptQuantity(), "已收完货", 2);
                ((TextView) vvv.findViewById(R.id.item_t3)).setTextColor(getResources().getColor(R.color.orange));
            }

        }
        if (items.size() > count) {
            // 添加一个下拉条
            refreshG_down();
        } else if (count > Api.updown_count) {
            // 添加上拉条
            refreshG_up();
        } else {

        }

        // 配送费
        //refreshItemViewTxt("配送费", null, wCartItem.splitBean.getWarehouseDeliveryFee(), 10);
        //refreshItemViewTxt("付款金额", null, "¥:" + wCartItem.getAllPrice(), 5);

        items_view.requestLayout();
    }

    private View refreshItemViewTxt(String t1, String t2, String t3, int top) {

        View p_view = View.inflate(this, R.layout.widget_supply_buy_item,
                null);
        if (top != 0) {
            p_view.setPadding(0, top, 0, 0);
        }

        TextView textview1 = (TextView) p_view.findViewById(R.id.item_t1);
        if (t1 != null) {
            textview1.setText(t1);
        } else {
            textview1.setVisibility(View.INVISIBLE);
        }

        TextView textview2 = (TextView) p_view.findViewById(R.id.item_t2);
        if (t2 != null) {
            textview2.setText(t2);
        } else {
            textview2.setVisibility(View.INVISIBLE);
        }

        TextView textview3 = (TextView) p_view.findViewById(R.id.item_t3);
        if (t3 != null) {
            textview3.setText(t3);
        } else {
            textview3.setVisibility(View.INVISIBLE);
        }
        items_view.addView(p_view);

        return p_view;
    }

    private void refreshItemViewTxtCu(String t1, int top) {

        View p_view = View.inflate(this, R.layout.widget_supply_buy_item,
                null);
        if (top != 0) {
            p_view.setPadding(0, top, 0, 0);
        }

        TextView textview1 = (TextView) p_view.findViewById(R.id.item_t1);
        if (t1 != null) {
            textview1.setText(t1);
            textview1.setTextColor(0xffF04848);
        } else {
            textview1.setVisibility(View.INVISIBLE);
        }

        TextView textview2 = (TextView) p_view.findViewById(R.id.item_t2);
        textview2.setVisibility(View.INVISIBLE);

        TextView textview3 = (TextView) p_view.findViewById(R.id.item_t3);
        textview3.setVisibility(View.INVISIBLE);

        items_view.addView(p_view);
    }

    private void refreshG_down() {
        View p_view = View.inflate(this, R.layout.widget_g, null);
        p_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                refreshItemViews(true);
            }
        });
        ViewGroup vg = (ViewGroup) p_view;
        ((ImageView) (vg.findViewById(R.id.widget_g_img)))
                .setImageResource(R.drawable.down_icon);
        items_view.addView(p_view);
    }

    private void refreshG_up() {
        View p_view = View.inflate(this, R.layout.widget_g, null);
        p_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                refreshItemViews(false);
            }
        });
        ViewGroup vg = (ViewGroup) p_view;
        ((ImageView) (vg.findViewById(R.id.widget_g_img)))
                .setImageResource(R.drawable.up_icon);
        items_view.addView(p_view);
    }
}
