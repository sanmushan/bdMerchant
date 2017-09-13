package com.gzdb.picking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzdb.response.BackOrder;
import com.gzdb.response.Item;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.StatusPickingEnum;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AdapterClickListener;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by liyunbiao on 2017/8/9.
 */

public class ProductAdpater extends AfinalAdapter<Item> {

    public ProductAdpater(Context context, List<Item> item) {
        super(context, item);
    }
    OnClickListenerDel onClickListenerDel;
    public interface OnClickListenerDel {
        void setOnClickListener(Item item, int i);
    }
    public void setOnClickListenerDel(OnClickListenerDel onClickListenerDel) {
        this.onClickListenerDel = onClickListenerDel;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_product_item, null);
            holder = new Holder();
            holder.init(convertView,position);
            convertView.setTag(holder);
            convertView.setOnClickListener(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Item item = getItem(position);
        holder.tv_title.setText(item.getName());
        holder.tv_encode.setText("条码："+item.getBarcode());
        if(item.getStock()!=null) {
            String str=item.getStock();
            if(item.getWarningQuantity()!=null&&item.getWarningQuantity().length()>0){
                str=str+"/"+item.getWarningQuantity();
            }
            if(item.getSafetyQuantity()!=null&&item.getSafetyQuantity().length()>0){
                str=str+"/"+item.getWarningQuantity();
            }
            holder.tv_num.setText("库存：" +str);
        }else {
            holder.tv_num.setText("");
        }
        return convertView;
    }

    class Holder implements View.OnClickListener {


        TextView tv_title;
        TextView tv_encode;
        TextView tv_num;
        TextView tv_del;
        int pos=0;

        void init(View view,int pos) {
            tv_title = ViewUtils.find(view, R.id.tv_title);//下单时间
            tv_encode = ViewUtils.find(view, R.id.tv_encode);//下单时间
            tv_del = ViewUtils.find(view, R.id.tv_del);//下单时间
            tv_num = ViewUtils.find(view, R.id.tv_num);//下单时间
            tv_del.setOnClickListener(this);
            this.pos=pos;

        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.tv_del) {
                onClickListenerDel.setOnClickListener(getItem(pos),pos);
            } else {


            }
        }
    }

}
