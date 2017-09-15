package com.gzdb.warehouse.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzdb.response.LogisticsListInfo;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by PVer on 2017/9/15.
 */

public class LogisticsListDetailAdapter extends AfinalAdapter<LogisticsListInfo.ResponseEntity> {

    String mPosition;
    List<LogisticsListInfo.ResponseEntity> list;

    public LogisticsListDetailAdapter(Context context, List<LogisticsListInfo.ResponseEntity> list) {
        super(context, list);
        this.mPosition = mPosition;
        this.list=list;
    }
    public LogisticsListDetailAdapter(Context context, List<LogisticsListInfo.ResponseEntity> list, String mPosition) {
        super(context, list);
        this.mPosition = mPosition;
        this.list=list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_logistics_details, null);
            holder = new Holder();
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        LogisticsListInfo.ResponseEntity entity = getItem(position);

        int i = Integer.valueOf(mPosition).intValue();

//        if (!StringUtils.isEmpty(entity.getDatas().get(i).getLogisticsDispatchAreaDetailsList().get(0).getBatchNo())){
//            holder.txt_totalNum.setText("总件数"+entity.getDatas().get(i).getLogisticsDispatchAreaDetailsList().get(0).getBatchNo());
//        }

//        if (!StringUtils.isEmpty(entity.getDatas().get(i).getLogisticsDispatchAreaDetailsList().get(0).getTime())){
//            holder.txt_time.setText(DateUtil.getToDateUtilLong(Long.parseLong(entity.getDatas().get(i).getLogisticsDispatchAreaDetailsList().get(0).getTime())));
//        }

        Log.e("linbin","=="+entity.getDatas().get(i).getBatchNo());
//        Log.e("linbin","=="+entity.getDatas().get(i).getLogisticsDispatchAreaDetailsList().get(0).getBatchNo());

//        holder.txt_orderNumber.setText("单号："+entity.getDatas().get(i).getBatchNo());
//        if (entity.getDatas().get(i).getStatus().equals("3")){
//            holder.txt_state.setText("仓管已交接");
//        }else if(entity.getDatas().get(i).getStatus().equals("2")){
//            holder.txt_state.setText("司机已交接");
//            holder.txt_btn.setVisibility(View.VISIBLE);
//        }
//        holder.txt_totalNum.setText("总件数："+entity.getDatas().get(i).getPackageTotle());


        /*if (entity.getTime() != null){
            holder.txt_time.setText(DateUtil.getToDateUtilLong(Long.parseLong(entity.getTime())));
        }
        holder.txt_orderNumber.setText("单号"+entity.getBatchNo());
        if (entity.getStatus().equals("3")){
            holder.txt_state.setText("仓管已交接");
        }else if(entity.getStatus().equals("2")){
            holder.txt_state.setText("司机已交接");
            holder.txt_btn.setVisibility(View.VISIBLE);
        }
        holder.txt_totalNum.setText("总件数："+entity.getPackageTotle());*/


        return convertView;
    }

    class Holder {
        TextView txt_time;//时间
        TextView txt_orderNumber;//单号
        TextView txt_state;//判断状态
        TextView txt_totalNum;//总件数
        TextView txt_btn;//确定按钮

        void init(View view) {
            txt_time = ViewUtils.find(view, R.id.txt02_time);
            txt_orderNumber = ViewUtils.find(view, R.id.txt02_orderNum);
            txt_state = ViewUtils.find(view, R.id.txt_state);
            txt_totalNum = ViewUtils.find(view, R.id.txt02_totality);
            txt_btn = ViewUtils.find(view, R.id.btn);
        }
    }
}
