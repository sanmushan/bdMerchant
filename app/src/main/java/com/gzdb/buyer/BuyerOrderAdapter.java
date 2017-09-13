package com.gzdb.buyer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzdb.response.Api;
import com.gzdb.response.ConsumerOrder;
import com.gzdb.response.enums.ClientTypeEnum;
import com.gzdb.response.enums.OrderPaymentEnter;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.OrderStatusEnum;
import com.gzdb.response.enums.PaymentTypeEnum;
import com.gzdb.response.enums.UpdatePasswordTypeEnum;
import com.gzdb.utils.Utils;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.adapter.OrderAdapter;
import com.gzdb.warehouse.me.ResetPasswordActivity;
import com.gzdb.warehouse.order.OrderDetailActivity;
import com.gzdb.warehouse.order.ShouItemActivity;
import com.gzdb.widget.InputPayPasswordDialog;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;
import com.zhumg.anlib.widget.RemoveListWidget;
import com.zhumg.anlib.widget.dialog.TipClickListener;
import com.zhumg.anlib.widget.dialog.TipDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class BuyerOrderAdapter extends AfinalAdapter<ConsumerOrder> {

    private OrderRoleTypeEnum orderRoleTypeEnum;

    Holder click_holder;

    TipDialog errorTipDialog;
    //RemoveListWidget removeListWidget;

    public BuyerOrderAdapter(Context context, List<ConsumerOrder> consumerOrders, RemoveListWidget.DelCallback delCallback) {
        super(context, consumerOrders);
        //removeListWidget = new RemoveListWidget(this, delCallback);
    }

    TipDialog getErrorTipDialog() {
        if(errorTipDialog == null) {
            errorTipDialog = new TipDialog(context);
            errorTipDialog.hibeLeftBtn();
        }
        return errorTipDialog;
    }

    public void setOrderRoleTypeEnum(OrderRoleTypeEnum orderRoleTypeEnum) {
        this.orderRoleTypeEnum = orderRoleTypeEnum;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_order_item, null);
            holder = new Holder();
            holder.init(convertView);
            convertView.setTag(holder);
            convertView.setOnClickListener(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        ConsumerOrder order = getItem(position);

        holder.order = order;
        holder.position = position;
        holder.preView = convertView;

        holder.tv_order_pnum.setText("排号：" + position);
        holder.tv_order_id.setText("订单号：" + order.getOrderSequenceNumber());
        holder.tv_order_state.setText(order.getStatusValue());
        if (orderRoleTypeEnum == OrderRoleTypeEnum.CONSUMER) {
            holder.tv_name.setText("发货仓：" + order.getShippingNickName());
        } else if (orderRoleTypeEnum == OrderRoleTypeEnum.MERCHANT) {
            holder.tv_name.setText("采购门店：" + order.getReceiptNickName());
        }
        holder.tv_time.setText("下单时间：" + order.getCreateTime());

        holder.handlerBtn();

        return convertView;
    }


    class Holder implements View.OnClickListener {

        View preView;

        TextView tv_order_pnum;
        TextView tv_order_id;
        TextView tv_order_state;
        TextView tv_name;
        TextView tv_time;

        View tv_gg;
        View btn_ll;
        TextView btn1;
        TextView btn2;
        int btnHandlerType = 0;

        ConsumerOrder order;
        int position;

        void init(View view) {

            tv_order_pnum = ViewUtils.find(view, R.id.tv_order_pnum);
            tv_order_id = ViewUtils.find(view, R.id.tv_order_id);
            tv_order_state = ViewUtils.find(view, R.id.tv_order_state);
            tv_name = ViewUtils.find(view, R.id.tv_name);
            tv_time = ViewUtils.find(view, R.id.tv_time);
            tv_gg = ViewUtils.find(view, R.id.tv_gg);
            btn_ll = ViewUtils.find(view, R.id.btn_ll);
            btn1 = ViewUtils.find(view, R.id.btn1);
            btn2 = ViewUtils.find(view, R.id.btn2);

            btn1.setOnClickListener(this);
            btn2.setOnClickListener(this);
        }

        void handlerBtn() {
            //采购员
            if(Cache.clientTypeEnum == ClientTypeEnum.PURCHASE) {

                tv_order_pnum.setVisibility(View.GONE);

                //未付款
                if (order.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_DEFAULT.getType()) {

                    btn_ll.setVisibility(View.VISIBLE);

                    btn1.setVisibility(View.VISIBLE);
                    btn1.setText("去付款");

                    btnHandlerType = OrderDetailActivity.Btn_HandlerType_Pay;

                    btn2.setVisibility(View.VISIBLE);
                    btn2.setText("取消订单");
                }  else {
                    btn1.setVisibility(View.VISIBLE);
                    btn1.setText("打印小票");
                    btn2.setVisibility(View.GONE);
                    btn_ll.setVisibility(View.VISIBLE);
                    btnHandlerType = OrderDetailActivity.Btn_HandlerType_Print;
//                    btn_ll.setVisibility(View.GONE);
//                    tv_gg.setVisibility(View.GONE);
                }
            } else {
                //仓管
                if (order.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_DEFAULT.getType()) {

                    btn1.setVisibility(View.VISIBLE);
                    btn1.setText("打印小票");
                    btn2.setVisibility(View.GONE);

                    btn_ll.setVisibility(View.VISIBLE);

                    btnHandlerType = OrderDetailActivity.Btn_HandlerType_Print;

                }
                //已完成
                else if (order.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_CONFIRM.getType()) {

                    btn_ll.setVisibility(View.GONE);
                    tv_gg.setVisibility(View.GONE);
                }
                //其它全是确认订单
                else {
                    btn1.setEnabled(true);
                    btn1.setText("确认订单");
                    btn_ll.setVisibility(View.VISIBLE);

                    btnHandlerType = OrderDetailActivity.Btn_HandlerType_Confirm;

                    btn2.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.btn1) {
                //针对按钮类型处理
                if(btnHandlerType == 0) return;

                switch (btnHandlerType) {
                    case OrderDetailActivity.Btn_HandlerType_Pay:
                        click_holder = this;
                        payOrder(String.valueOf(order.getOrderId()), order.getTotalPrice());
                        return;
                    case OrderDetailActivity.Btn_HandlerType_Confirm:
                    {
                        Log.e("db", "当前确认的订单是：" + click_holder.order.getOrderSequenceNumber() + ", position = " + click_holder.position);
                        Map map = new HashMap();
                        map.put("orderId", String.valueOf(order.getOrderId()));
                        map.put("passportId", Cache.passport.getPassportIdStr());
                        Http.post(BuyerOrderAdapter.this.context, map, Api.CONFIRM_ORDER, new HttpCallback() {
                            @Override
                            public void onSuccess(Object data) {
                                btn_ll.setVisibility(View.GONE);
                                ToastUtil.showToast(BuyerOrderAdapter.this.context, msg);
                            }
                        }.setPass());
                    }
                    case OrderDetailActivity.Btn_HandlerType_ConfirmShouhuo: {
                        Intent intent = new Intent();
                        intent.setClass(BuyerOrderAdapter.this.context, ShouItemActivity.class);
                        BuyerOrderAdapter.this.context.startActivity(intent);
                        return;
                    }
                    case OrderDetailActivity.Btn_HandlerType_Print:
                        Map map = new HashMap();
                        map.put("orderId", String.valueOf(order.getOrderId()));
                        map.put("passportId", Cache.passport.getPassportIdStr());
                        Http.post(BuyerOrderAdapter.this.context, map, Api.PRINTERORDERTICKET, new HttpCallback() {
                            @Override
                            public void onSuccess(Object data) {
                                ToastUtil.showToast(BuyerOrderAdapter.this.context, msg);
                            }
                        }.setPass());
                        break;
                }
            } else if(id == R.id.btn2) {
                click_holder = this;
                Log.e("db", "当前取消的订单是：" + click_holder.order.getOrderSequenceNumber() + ", position = " + click_holder.position);
                //取消订单
                Map map = new HashMap();
                map.put("orderId", String.valueOf(order.getOrderId()));
                map.put("passportId", Cache.passport.getPassportIdStr());
                Http.post(BuyerOrderAdapter.this.context, map, Api.CANCEL_ORDER, new HttpCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        //removeListWidget.setDelDatas(click_holder.preView, click_holder.position);
                        //removeListWidget.runDelete();
                        BuyerOrderAdapter.this.remove(position);
                        BuyerOrderAdapter.this.notifyDataSetChanged();
                        ToastUtil.showToast(BuyerOrderAdapter.this.context, msg);
                    }
                }.setPass());

            } else {
                //直接进入详情
                Intent intent = new Intent((Activity) context, BuyerOrderDetailActivity.class);
                intent.putExtra("orderId", order.getOrderId());
                intent.putExtra("orderRoleType", orderRoleTypeEnum.getType());
                ((Activity) context).startActivity(intent);
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
        Http.post(BuyerOrderAdapter.this.context, map, Api.PAYMENT_ORDER, new HttpCallback() {
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
                InputPayPasswordDialog dialog = new InputPayPasswordDialog(BuyerOrderAdapter.this.context,
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
                    TipDialog tipDialog = new TipDialog(BuyerOrderAdapter.this.context);
                    tipDialog.setTipClickListener(new TipClickListener() {
                        @Override
                        public void onClick(boolean left) {
                            if (!left) {
                                //弹出设置密码
                                Intent intent = new Intent(BuyerOrderAdapter.this.context, ResetPasswordActivity.class);
                                intent.putExtra("updateType", UpdatePasswordTypeEnum.UPDATE_PAY_PASSWORD.getKey());
                                BuyerOrderAdapter.this.context.startActivity(intent);
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
        Http.post(BuyerOrderAdapter.this.context, map, Api.BANLANCE_PAYMENT, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                //支付宝付款成功
                ToastUtil.showToast(BuyerOrderAdapter.this.context, "余额付款成功!");
                click_holder.btn_ll.setVisibility(View.GONE);
                click_holder.tv_order_state.setText("已支付");
            }

            @Override
            public void onFailure() {
                getErrorTipDialog().setContentMsg(msg).show();
            }
        }.setPass());
    }
}
