package com.gzdb.warehouse.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzdb.response.Api;
import com.gzdb.response.ConsumerOrder;
import com.gzdb.response.ConsumerOrderItem;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.OrderStatusEnum;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.response.enums.StatusEnterEnum;
import com.gzdb.utils.Utils;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.order.OrderDetailActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.StringUtils;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class OrderAdapter extends AfinalAdapter<ConsumerOrder> {

    private OrderRoleTypeEnum orderRoleTypeEnum;
    private OrderTypeEnum orderTypeEnum;

    public OrderAdapter(Context context, List<ConsumerOrder> consumerOrders) {
        super(context, consumerOrders);
    }

    public OrderRoleTypeEnum getOrderRoleTypeEnum() {
        return orderRoleTypeEnum;
    }

    public void setOrderRoleTypeEnum(OrderRoleTypeEnum orderRoleTypeEnum) {
        this.orderRoleTypeEnum = orderRoleTypeEnum;
    }

    public void setOrderTypeEnum(OrderTypeEnum orderTypeEnum) {
        this.orderTypeEnum = orderTypeEnum;
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

        String sort_id = order.getDailySort();
        if(!StringUtils.isEmpty(sort_id)) {
            holder.tv_order_pnum.setText("排号：" + sort_id);
        }

        holder.tv_order_id.setText("订单号：" + order.getOrderSequenceNumber());
        holder.tv_order_state.setText(order.getStatusValue());

        if (orderTypeEnum == OrderTypeEnum.ALLOCATION_ORDER_TYPE) {//调拨
            if (Cache.passport.isMainWarehouse()) {
                holder.tv_name.setText("调货仓库：" + order.getReceiptNickName());
            } else {
                holder.tv_name.setText("发货仓库：" + order.getShippingNickName());
            }

        } else if (orderTypeEnum == OrderTypeEnum.PURCHASE_ORDER_TYPE) {//采购单
            holder.tv_name.setText("供应商：" + order.getShippingNickName());
        } else {
            holder.tv_name.setText("采购门店：" + order.getReceiptNickName());//销售单
        }

        holder.tv_time.setText("下单时间：" + order.getCreateTime());

        holder.handlerBtn();
        List<ConsumerOrderItem> items =order.getItemSnapshotArray();
        int count=items.size();
        holder.ll_mbody.removeAllViews();
        for(int i=0;i<count;i++) {

            holder.refreshItemViewTxt(holder.ll_mbody, items.get(i).getItemName(), items.get(i).getQuantity()+items.get(i).getUnitName(), "¥ " + Utils.toYuanStr(items.get(i).getNormalPrice()), 10);

            if (items.get(i).getUnShippingQuantity() > 0&&(items.get(i).getQuantity() - items.get(i).getUnShippingQuantity())>0) {


                View vvvvv = holder.refreshItemViewTxt(holder.ll_mbody, "", "已发货：" + (items.get(i).getQuantity() - items.get(i).getUnShippingQuantity()), "缺货", 2);
                ((TextView) vvvvv.findViewById(R.id.item_t3)).setTextColor(context.getResources().getColor(R.color.red));
            }
        }

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
        LinearLayout ll_mbody;
        int btnHandlerType = 0;

        ConsumerOrder order;
        int position;

        void init(View view) {
            preView = ViewUtils.find(view, R.id.item_ll);
            tv_order_pnum = ViewUtils.find(view, R.id.tv_order_pnum);
            tv_order_id = ViewUtils.find(view, R.id.tv_order_id);
            tv_order_state = ViewUtils.find(view, R.id.tv_order_state);
            tv_name = ViewUtils.find(view, R.id.tv_name);
            tv_time = ViewUtils.find(view, R.id.tv_time);
            tv_gg = ViewUtils.find(view, R.id.tv_gg);
            btn_ll = ViewUtils.find(view, R.id.btn_ll);
            btn1 = ViewUtils.find(view, R.id.btn1);
            btn2 = ViewUtils.find(view, R.id.btn2);
            ll_mbody = ViewUtils.find(view, R.id.ll_mbody);

            btn1.setOnClickListener(this);
            btn2.setOnClickListener(this);
        }
        View refreshItemViewTxt(LinearLayout items_view, String t1, String t2, String t3, int top) {

            View p_view = View.inflate(context, R.layout.widget_supply_buy_item,
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
        void handlerBtn() {
//            btn_ll.setVisibility(View.GONE);
//            //仓库的调拨订单，即向上线仓调拨
            if (orderTypeEnum == OrderTypeEnum.ALLOCATION_ORDER_TYPE) {
//                //主仓
                if (Cache.passport.isMainWarehouse()) {
//                    //已接单
                    if (order.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_ACCEPT.getType()) {
                        //已接单，在这里只是 打印小票
                        btn_ll.setVisibility(View.VISIBLE);
                        btn1.setText("打印小票");

                        btnHandlerType = OrderDetailActivity.Btn_HandlerType_Print;
                        btn2.setVisibility(View.GONE);
                        return;
                    }
                    btn_ll.setVisibility(View.GONE);
                } else {
//                    //已送达，调拨订单，是确认订单
//                    if (order.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_ARRIVE.getType()) {
//                        btn_ll.setVisibility(View.VISIBLE);
//                        btn1.setText("确认收货");
//                        btnHandlerType = OrderDetailActivity.Btn_HandlerType_Confirm;
//                        btn2.setVisibility(View.GONE);
//                        return;
//                    }
                    btn_ll.setVisibility(View.GONE);
                }
            } else if(orderTypeEnum == OrderTypeEnum.PURCHASE_ORDER_TYPE) {
                    if(order.getOrderStatus()==OrderStatusEnum.ORDER_STATUS_CONFIRM.getType()) {
                        btn_ll.setVisibility(View.VISIBLE);
                        btn1.setText("打印小票");
                        btnHandlerType = OrderDetailActivity.Btn_HandlerType_Print;
                        btn2.setVisibility(View.GONE);
                    }else {
                        btn_ll.setVisibility(View.GONE);
                    }
            } else {
                //仓库的销售订单，即商家下单给仓库
               // if (order.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_ACCEPT.getType()) {
                    //已接单，在这里只是 打印小票
                    btn_ll.setVisibility(View.VISIBLE);
                    btn1.setText("打印小票");
                    btnHandlerType = OrderDetailActivity.Btn_HandlerType_Print;
                    btn2.setVisibility(View.GONE);
                    return;
//                }
//                btn_ll.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn1) {
                //针对按钮类型处理
                if (btnHandlerType == 0) return;

                switch (btnHandlerType) {
                    case OrderDetailActivity.Btn_HandlerType_Confirm://仓管，调拨单，确认收货
                    {
                        Map map = new HashMap();
                        map.put("orderId", String.valueOf(order.getOrderId()));
                        map.put("passportId", Cache.passport.getPassportIdStr());
                        Http.post(OrderAdapter.this.context, map, Api.CONFIRM_ORDER, new HttpCallback() {
                            @Override
                            public void onSuccess(Object data) {
                                btn_ll.setVisibility(View.GONE);
                                ToastUtil.showToast(OrderAdapter.this.context, msg);
                            }
                        }.setPass());
                    }
                    return;
                    case OrderDetailActivity.Btn_HandlerType_Print://仓管，打印小票
                    {
                        Map map = new HashMap();
                        map.put("orderId", String.valueOf(order.getOrderId()));
                        map.put("passportId", Cache.passport.getPassportIdStr());
                        Http.post(OrderAdapter.this.context, map, Api.PRINTERORDERTICKET, new HttpCallback() {
                            @Override
                            public void onSuccess(Object data) {
                                ToastUtil.showToast(OrderAdapter.this.context, msg);
                            }
                        }.setPass());
                    }
                    return;
                }
            } else if (id == R.id.btn2) {

            } else {

                //直接进入详情
                Intent intent = new Intent((Activity) context, OrderDetailActivity.class);
                intent.putExtra("orderId", order.getOrderId());
                intent.putExtra("orderType", orderTypeEnum.getType());
                intent.putExtra("orderRoleType", orderRoleTypeEnum.getType());
                ((Activity) context).startActivity(intent);
            }
        }
    }

}
