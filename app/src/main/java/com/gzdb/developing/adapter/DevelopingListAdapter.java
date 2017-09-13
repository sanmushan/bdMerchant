package com.gzdb.developing.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzdb.developing.DevelopingBean;
import com.gzdb.response.ConsumerOrderItem;
import com.gzdb.response.Warehouse;
import com.gzdb.utils.DateUtil;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.adapter.SendItemAdapter;
import com.gzdb.warehouse.adapter.WarehouseAdapter;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AdapterModel;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * 作   者：liyunbiao
 * 时   间：2017/5/23
 * 修 改 人：
 * 日   期：
 * 描   述：
 */

public class DevelopingListAdapter  extends AfinalAdapter<DevelopingBean> {
    public DevelopingListAdapter(Context context, List<DevelopingBean> strings) {
        super(context, strings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         Holder holder = null;
        try {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_developing, null);
                holder = new  Holder();
                holder.init(convertView);
                convertView.setTag(holder);
            } else {
                holder = ( Holder) convertView.getTag();
            }
            DevelopingBean model = getItem(position);

            holder.tv_shop_name.setText(model.getShowName());
            holder.tv_phone.setText("联系电话:"+model.getPhoneNumber());
            holder.ll_auditOpinion.setVisibility(View.GONE);
            holder.tv_auditOpinion.setText("");
            if(model.getCreateDate()!=null){
                holder.tv_date.setText("开拓时间："+DateUtil.getToDate(Long.parseLong(model.getCreateDate())));
            }

            //审核类型： 0.待审核，1.审核通过，2.审核不通过
            if(model.getAuditorType().equals("0")){
                holder.tv_state.setText("待审核");
                holder.tv_state.setTextColor(context.getResources().getColor(R.color.font_6));
            }else  if(model.getAuditorType().equals("1")){
                holder.tv_state.setText("审核通过");
                holder.tv_state.setTextColor(context.getResources().getColor(R.color.green));
            } else  if(model.getAuditorType().equals("2")){
                holder.tv_state.setText("审核不通过");
                holder.tv_state.setTextColor(context.getResources().getColor(R.color.red));
                holder.tv_auditOpinion.setText(model.getAuditOpinion());
                holder.ll_auditOpinion.setVisibility(View.VISIBLE);
            }else  if(model.getAuditorType().equals("3")) {
                 holder.tv_state.setText("资料不完善");
                holder.tv_state.setTextColor(context.getResources().getColor(R.color.red));
                holder.tv_auditOpinion.setText(model.getAuditOpinion());
                holder.ll_auditOpinion.setVisibility(View.GONE);
            }else {
                holder.tv_state.setTextColor(context.getResources().getColor(R.color.font_6));
                holder.tv_auditOpinion.setText(model.getAuditOpinion());
                holder.ll_auditOpinion.setVisibility(View.GONE);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

        return convertView;
    }
    class Holder  {

        TextView tv_shop_name;
        TextView tv_phone;
        TextView tv_name;
        TextView tv_address;
        TextView tv_state;
        TextView tv_date;
        TextView tv_auditOpinion;
        LinearLayout ll_auditOpinion;

        void init(View view) {
            tv_shop_name = ViewUtils.find(view, R.id.tv_shop_name);
            tv_phone = ViewUtils.find(view, R.id.tv_phone);
            tv_name = ViewUtils.find(view, R.id.tv_name);
            tv_state = ViewUtils.find(view, R.id.tv_state);
            tv_date = ViewUtils.find(view, R.id.tv_date);
            tv_address = ViewUtils.find(view, R.id.tv_address);
            tv_auditOpinion = ViewUtils.find(view, R.id.tv_auditOpinion);
            ll_auditOpinion = ViewUtils.find(view, R.id.ll_auditOpinion);
        }
    }
}
