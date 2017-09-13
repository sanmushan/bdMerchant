package com.gzdb.picking.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzdb.response.BackOrder;
import com.gzdb.response.DetailsBean;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.StatusPickingEnum;
import com.gzdb.response.showPick;
import com.gzdb.utils.CreateQRImage;
import com.gzdb.utils.DateUtil;
import com.gzdb.utils.Utils;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by liyunbiao on 2017/8/9.
 */

public class SearchPickingAdapter extends  AfinalAdapter<showPick> {


    public SearchPickingAdapter(Context context, List<showPick> consumerOrders) {
        super(context, consumerOrders);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            Holder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.adapter_search_packing_item, null);
                holder = new Holder();
                holder.init(convertView);
                convertView.setTag(holder);
                convertView.setOnClickListener(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            showPick order = getItem(position);
            holder.setData(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        TextView tv_num01;
        TextView tv_num02;
        TextView tv_num03;
        LinearLayout ll_body;
        ImageView img_encode;

        void init(View view) {


            tv_date = ViewUtils.find(view, R.id.tv_date);//下单时间
            tv_warehouse_name = ViewUtils.find(view, R.id.tv_warehouse_name);//仓库名称
            tv_order_num = ViewUtils.find(view, R.id.tv_order_num);//
            ll_body = ViewUtils.find(view, R.id.ll_body);
            tv_grade = ViewUtils.find(view, R.id.tv_grade);//等级
            tv_num01 = ViewUtils.find(view, R.id.tv_num01);//排号
            tv_num02 = ViewUtils.find(view, R.id.tv_num02);//排号
            tv_num03 = ViewUtils.find(view, R.id.tv_num03);//排号
            img_encode = ViewUtils.find(view, R.id.img_encode);


        }

        void setView(final int index,DetailsBean detailsBean) {
            try {
                View view_items = LayoutInflater.from(context).inflate(R.layout.picking_search_child, null, false);
                tv_picking = ViewUtils.find(view_items, R.id.tv_picking);//已拣货
                tv_total_num = ViewUtils.find(view_items, R.id.tv_total_num);//总数
                tv_encode = ViewUtils.find(view_items, R.id.tv_encode);//条码
                tv_location = ViewUtils.find(view_items, R.id.tv_location);//库位
                tv_title = ViewUtils.find(view_items, R.id.tv_title);//商品标题
                tv_submit = ViewUtils.find(view_items, R.id.tv_submit);
                tv_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onclickSubmit(index);
                    }
                });
                tv_picking.setText("已拣货："+detailsBean.getPickedQuantity());//已拣货
                tv_total_num.setText("数量："+detailsBean.getQuantity());//总数
                tv_encode.setText(detailsBean.getBarcode());//条码
             //   tv_location.setText(detailsBean.getStorageLocation());//库位
                tv_title.setText(detailsBean.getItemName());//商品标题
                ll_body.addView(view_items);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void setData(showPick order) {

            try {
                tv_date.setText("下单时间："+ DateUtil.getToDate(Long.parseLong(order.getCreateTime()))) ;

                tv_warehouse_name.setText("下单仓库："+order.getSrcWarehouseName());//仓库名称
                tv_order_num.setText("来源订单："+order.getOrderSequenceNumber());//订单号
                tv_grade.setText("");//等级
                tv_num01.setText(""+order.getDaySortNumber());//排号
                tv_num02.setText("");//排号
                tv_num03.setText("");//排号
                CreateQRImage cr = new CreateQRImage();
                Bitmap bm = cr.createQRImage("888", 250, 250);
                img_encode.setImageBitmap(bm);
                ll_body.removeAllViews();
                List<DetailsBean> dlist= order.getDetails();
                if(dlist!=null) {
                    for (int i = 0; i < dlist.size(); i++) {
                        setView(i,dlist.get(i));

                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        void onclickSubmit(int pos) {

        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_finish) {

            } else {


            }
        }
    }

}
