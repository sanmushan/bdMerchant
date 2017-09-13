package com.gzdb.warehouse.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzdb.response.Api;
import com.gzdb.response.BackOrder;
import com.gzdb.response.ConsumerOrderItem;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.OrderStatusEnum;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.utils.Utils;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;
import java.util.List;

/**
 * Created by liyunbiao on 2017/7/31.
 */

public class BackAdapter extends AfinalAdapter<BackOrder> {

    private OrderRoleTypeEnum orderRoleTypeEnum;
    private OrderTypeEnum orderTypeEnum;

    public BackAdapter(Context context, List<BackOrder> consumerOrders) {
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
            convertView = View.inflate(context, R.layout.adapter_back_item, null);
            holder = new Holder();
            holder.init(convertView);
            convertView.setTag(holder);
            convertView.setOnClickListener(holder);
        } else {
            holder = ( Holder) convertView.getTag();
        }

        BackOrder order = getItem(position);

        holder.order = order;
        holder.position = position;


        holder.refresh();

      //  holder.handlerBtn();

        return convertView;
    }

    class Holder implements View.OnClickListener {



        TextView order_id_txt;
        TextView down_time_txt;
        TextView send_name_txt;
        TextView send_address_txt;
        TextView send_call_btn;
        TextView send_map_btn;
        TextView revt_name_txt;
        TextView revt_address_txt;
        TextView revt_call_btn;
        TextView revt_map_btn;
        TextView psf_info;
        TextView psf_v;
        TextView all_p;
        View btn_ll_g;
        View btn_ll;
        TextView btn1;
        TextView btn2;
        TextView btn3;

        LinearLayout items_view;
        LinearLayout ll_revt;
        LinearLayout ll_send;

        BackOrder order;
        int position;

        void init(View view) {
            order_id_txt = ViewUtils.find(view, R.id.order_id_txt);
            down_time_txt = ViewUtils.find(view, R.id.down_time_txt);
            send_name_txt = ViewUtils.find(view, R.id.send_name_txt);
            send_address_txt = ViewUtils.find(view, R.id.send_address_txt);
            send_call_btn = ViewUtils.find(view, R.id.send_call_btn);

            send_map_btn = ViewUtils.find(view, R.id.send_map_btn);//发货人导航
            send_map_btn.setOnClickListener(this);
            revt_name_txt = ViewUtils.find(view, R.id.revt_name_txt);
            revt_address_txt = ViewUtils.find(view, R.id.revt_address_txt);
            revt_call_btn = ViewUtils.find(view, R.id.revt_call_btn);

            revt_map_btn = ViewUtils.find(view, R.id.revt_map_btn);//收货人导航
            revt_map_btn.setOnClickListener(this);
            psf_info = ViewUtils.find(view, R.id.psf_info);
            psf_v = ViewUtils.find(view, R.id.psf_v);
            all_p = ViewUtils.find(view, R.id.all_p);
            btn_ll_g = ViewUtils.find(view, R.id.btn_ll_g);
            btn_ll = ViewUtils.find(view, R.id.btn_ll);
            btn1 = ViewUtils.find(view, R.id.btn1);
            btn2 = ViewUtils.find(view, R.id.btn2);
            btn3 = ViewUtils.find(view, R.id.btn3);
            items_view = ViewUtils.find(view, R.id.items_view);
            ll_revt = ViewUtils.find(view, R.id.ll_revt);
            ll_revt.setOnClickListener(this);
            ll_send = ViewUtils.find(view, R.id.ll_send);
            ll_send.setOnClickListener(this);
        }

        void refresh() {

            order_id_txt.setText("订单号：" + order.getOrderSequenceNumber());
            down_time_txt.setText("下单时间：" + order.getCreateTime());
            refreshItemViews(false);


        }
        private void refreshItemViews(boolean all) {
            items_view.removeAllViews();

            int count = 0;

            List<ConsumerOrderItem> items = order.getItemSnapshotArray();

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

                if (order.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_BATCH.getType()) {
                    refreshItemViewInfoTxt(item.getDiscountQuantity(), item.getReceiptQuantity(), item.getUnReceiptQuantity(), 0, item.getUnitName());
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
        private void refreshItemViewTxt(String t1, String t2, String t3, int top) {

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
        }
        void refreshItemViewInfoTxt(int t1, int t2, int t3, int top, String unit) {
            if (unit == null) {
                unit = "";
            }

            // 添加配送费
            View p_view = View.inflate(context, R.layout.widget_supply_buy_item, null);
            if (top != 0) {
                p_view.setPadding(0, top, 0, 0);
            }
            TextView textview1 = (TextView) p_view.findViewById(R.id.item_t1);
            textview1.setText("配送中:" + t1 + unit);

            TextView textview2 = (TextView) p_view.findViewById(R.id.item_t2);
            textview2.setText("已收货:" + t2 + unit);

            TextView textview3 = (TextView) p_view.findViewById(R.id.item_t3);
            if (t3 > 0) {
                textview3.setText("未收货:" + t3 + unit);
                textview3.setTextColor(context.getResources().getColor(R.color.red));
            } else {
                textview3.setText("已收完");
                textview3.setTextColor(context.getResources().getColor(R.color.blue));
            }

            items_view.addView(p_view);
        }
        private void refreshG_down() {
            View p_view = View.inflate(context, R.layout.widget_g, null);
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
            View p_view = View.inflate(context, R.layout.widget_g, null);
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

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.btn1) {

            } else if (id == R.id.btn2) {

            } else {


            }
        }
    }

}
