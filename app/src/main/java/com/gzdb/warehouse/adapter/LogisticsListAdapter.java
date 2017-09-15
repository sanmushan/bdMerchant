package com.gzdb.warehouse.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzdb.response.LogisticsListBean;
import com.gzdb.utils.DateUtil;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.me.LogisticsHandoverActivity;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by PVer on 2017/9/15.
 */

public class LogisticsListAdapter extends AfinalAdapter<LogisticsListBean> {
    LogisticsHandoverActivity activity;

    public LogisticsListAdapter(Context context, List<LogisticsListBean> logisticsListBeen) {
        super(context, logisticsListBeen);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_handover, null);
            holder = new Holder();
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        LogisticsListBean entity = getItem(position);


        if (entity.getTime() != null){
            holder.txt_time.setText(DateUtil.getToDateUtilLong(Long.parseLong(entity.getTime())));
        }
        holder.txt_orderNumber.setText(entity.getBatchNo());

        if (entity.getStatus().equals("1")){
            holder.txt_state.setText("装车中");
        }else if (entity.getStatus().equals("2")){
            holder.txt_state.setText("运送中");
        }else if(entity.getStatus().equals("3")){
            holder.txt_state.setText("已完成交接");
        }
        holder.txt_total_orders.setText("总订单数："+entity.getOrderNum());
        holder.txt_totalNum.setText("总件数："+entity.getPackageTotle());
        holder.txt_receivedNum.setText("已交接数："+entity.getConnectTotle());



        return convertView;
    }

    class Holder {
        TextView txt_time;//时间
        TextView txt_orderNumber;//单号
        TextView txt_state;//判断装车状态
        TextView txt_total_orders;//总订单数
        TextView txt_totalNum;//总件数
        TextView txt_receivedNum;//已收件数

        void init(View view) {
            txt_time = ViewUtils.find(view, R.id.txt_time);
            txt_orderNumber = ViewUtils.find(view, R.id.txt_orderNumber);
            txt_state = ViewUtils.find(view, R.id.txt_state);
            txt_total_orders = ViewUtils.find(view, R.id.txt_total_orders);
            txt_totalNum = ViewUtils.find(view, R.id.txt_totalNum);
            txt_receivedNum = ViewUtils.find(view, R.id.txt_receivedNum);
        }
    }
}
