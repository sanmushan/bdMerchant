package com.gzdb.picking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzdb.response.DetailsBean;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.StatusPickingEnum;
import com.gzdb.response.showPick;
import com.gzdb.utils.DateUtil;
import com.gzdb.utils.ProductSort;
import com.gzdb.utils.ProductSorts;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by liyunbiao on 2017/8/8.
 */

public class PickingAdapter extends AfinalAdapter<showPick> {

    private OrderRoleTypeEnum orderRoleTypeEnum;
    private StatusPickingEnum statusPickingEnum;

    public PickingAdapter(Context context, List<showPick> consumerOrders) {
        super(context, consumerOrders);
    }

    public OrderRoleTypeEnum getOrderRoleTypeEnum() {
        return orderRoleTypeEnum;
    }

    public void setOrderRoleTypeEnum(OrderRoleTypeEnum orderRoleTypeEnum) {
        this.orderRoleTypeEnum = orderRoleTypeEnum;
    }

    public void setOrderTypeEnum(StatusPickingEnum statusPickingEnum) {
        this.statusPickingEnum = statusPickingEnum;
    }


    OnClickListenerDel onClickListenerDel;

    public interface OnClickListenerDel {
        void setOnClickListener(showPick item, int i);
    }

    public void setOnClickListenerDel(OnClickListenerDel onClickListenerDel) {
        this.onClickListenerDel = onClickListenerDel;
    }


    OnClickListenerDelItem OnClickListenerDelItem;

    public interface OnClickListenerDelItem {
        void setOnClickListenerItem(DetailsBean item, showPick order, int i);
    }

    public void setOnClickListenerDelItem(OnClickListenerDelItem onClickListenerDelItem) {
        this.OnClickListenerDelItem = onClickListenerDelItem;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_packing_item, null);
            holder = new Holder();
            holder.init(convertView, position);
            convertView.setTag(holder);
            convertView.setOnClickListener(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        showPick order = getItem(position);
        holder.setData(order);

        return convertView;
    }

    class Holder implements View.OnClickListener {


        TextView tv_date;
        TextView tv_submit;

        TextView tv_warehouse_name;
        TextView tv_order_num;
        TextView tv_picking;
        TextView tv_total_num;
        TextView tv_encode;
        TextView tv_location;
        TextView tv_title;
        TextView tv_grade;
        TextView tv_notdata;
        TextView tv_notdata_util;
        TextView tv_num01;
        TextView tv_num02;
        TextView tv_item_finish;
        TextView tv_num03;
        TextView tv_num;
        TextView tv_tmp;
        LinearLayout ll_body;
        LinearLayout ll_item_body;
        LinearLayout ll_run_location;

        int position = 0;


        void init(View view, int position) {
            this.position = position;
            tv_date = ViewUtils.find(view, R.id.tv_date);//下单时间
            ll_item_body = ViewUtils.find(view, R.id.ll_item_body);
            tv_warehouse_name = ViewUtils.find(view, R.id.tv_warehouse_name);//仓库名称
            tv_order_num = ViewUtils.find(view, R.id.tv_order_num);//
            ll_body = ViewUtils.find(view, R.id.ll_body);
            tv_grade = ViewUtils.find(view, R.id.tv_grade);//等级
            tv_num01 = ViewUtils.find(view, R.id.tv_num01);//排号
            tv_num02 = ViewUtils.find(view, R.id.tv_num02);//排号
            tv_num03 = ViewUtils.find(view, R.id.tv_num03);//排号

            tv_item_finish = ViewUtils.find(view, R.id.tv_item_finish);//排号


        }

        void setView(int pos, final int index, final DetailsBean details, final showPick order) {
            try {

                View view_items = LayoutInflater.from(context).inflate(R.layout.packing_child, null, false);
                tv_picking = ViewUtils.find(view_items, R.id.tv_picking);//已拣货
                tv_total_num = ViewUtils.find(view_items, R.id.tv_total_num);//总数
                tv_encode = ViewUtils.find(view_items, R.id.tv_encode);//条码
                tv_location = ViewUtils.find(view_items, R.id.tv_location);//库位
                tv_title = ViewUtils.find(view_items, R.id.tv_title);//商品标题
                tv_submit = ViewUtils.find(view_items, R.id.tv_submit);
                tv_notdata = ViewUtils.find(view_items, R.id.tv_notdata);//排号
                tv_notdata_util = ViewUtils.find(view_items, R.id.tv_notdata_util);
                ll_run_location = ViewUtils.find(view_items, R.id.ll_run_location);
                tv_tmp = ViewUtils.find(view_items, R.id.tv_tmp);
                tv_num = ViewUtils.find(view_items, R.id.tv_num);
                tv_title.setText("");

                if (statusPickingEnum.getType() == StatusPickingEnum.FINISH.getType()) {
                    tv_item_finish.setVisibility(View.GONE);
                } else {
                    tv_item_finish.setVisibility(View.VISIBLE);
                }
                tv_title.setText(details.getItemName());//商品标题
                tv_picking.setText(details.getPickedQuantity());//已拣货
                tv_total_num.setText(details.getQuantity());//总数
                if (details.getStock() != null) {
                    tv_num.setText(details.getStock());//总数
                }
                tv_notdata_util.setVisibility(View.GONE);
                if (details.getStatus().equals("0")) {
                    tv_submit.setText(details.getPickedQuantity() + "");
                    tv_submit.setTextSize(30);
                    ll_run_location.setBackgroundResource(R.drawable.packing_border_high);
                    tv_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (statusPickingEnum.getType() == StatusPickingEnum.FINISH.getType()) {
                                    return;
                                }
                                OnClickListenerDelItem.setOnClickListenerItem(details, order, index);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    tv_tmp.setVisibility(View.GONE);
                } else if (details.getStatus().equals("1")) {

                    tv_submit.setText("已捡完");
                    tv_submit.setTextSize(18);
                    tv_tmp.setVisibility(View.GONE);
                    ll_run_location.setBackgroundResource(R.drawable.packing_border);

                    tv_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                OnClickListenerDelItem.setOnClickListenerItem(details, order, index);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } else if (details.getStatus().equals("2")) {
                    tv_notdata_util.setVisibility(View.VISIBLE);
                    tv_submit.setText(details.getPickedQuantity() + "");
                    tv_submit.setTextSize(30);
                    ll_run_location.setBackgroundResource(R.drawable.packing_border_red);
                    tv_notdata.setVisibility(View.VISIBLE);
                    tv_tmp.setVisibility(View.GONE);
                    tv_notdata.setText("" + (Integer.parseInt(details.getQuantity()) - Integer.parseInt(details.getPickedQuantity())));
                    tv_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (statusPickingEnum.getType() == StatusPickingEnum.FINISH.getType()) {
                                    return;
                                }
                                OnClickListenerDelItem.setOnClickListenerItem(details, order, index);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    tv_notdata.setVisibility(View.GONE);
                }
                tv_encode.setText("条码:" + details.getBarcode());//条码
                String[] d = details.getStorageLocation();
                tv_location.setText(d.toString());//库位
                String ft = "";
                if (d != null) {
                    for (int i = 0; i < d.length; i++) {
                        ft = ft + "   " + d[i];
                    }
                }
                tv_location.setText(ft);//库位
                ll_body.addView(view_items);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        showPick order;

        void setData(showPick order) {
            this.order = order;
            try {
                ll_body.removeAllViews();
                tv_date.setText("下单时间：" + DateUtil.getToDate(Long.parseLong(order.getCreateTime())));
                tv_warehouse_name.setText("下单仓库：" + order.getSrcWarehouseName());//仓库名称
                tv_order_num.setText("来源订单：" + order.getOrderSequenceNumber());//订单号
                tv_grade.setText("");//等级
                tv_num01.setText("排号" + order.getDaySortNumber());
                tv_num02.setText("");
                tv_num03.setText("");
                tv_item_finish.setOnClickListener(this);
                if (!order.getStatus().equals("0")) {
                    tv_item_finish.setVisibility(View.VISIBLE);
                } else {
                    tv_item_finish.setVisibility(View.VISIBLE);
                }

                List<DetailsBean> dlist = order.getDetails();
                ProductSort sort = new ProductSort();
                ProductSorts sorts = new ProductSorts();
                if (statusPickingEnum.getType() == StatusPickingEnum.NODATA.getType()) {
                    Collections.sort(dlist, sorts);
                } else {
                    Collections.sort(dlist, sort);
                }
                if (dlist != null) {
                    for (int i = 0; i < dlist.size(); i++) {
                        if (statusPickingEnum.getType() == StatusPickingEnum.NODATA.getType()) {
                          //  if (dlist.get(i).getStatus().equals("2")) {
                                setView(0, i, dlist.get(i), order);
                          //  }
                        } else if (statusPickingEnum.getType() == StatusPickingEnum.NOPICKING.getType()) {
                            // if (dlist.get(i).getStatus().equals("0")) {
                            setView(0, i, dlist.get(i), order);
                            //}
                        } else {
                            setView(0, i, dlist.get(i), order);
                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            try {

                if (id == R.id.tv_item_finish) {
                    onClickListenerDel.setOnClickListener(order, position);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
