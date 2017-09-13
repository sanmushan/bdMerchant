package com.gzdb.warehouse.order;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzdb.response.ConsumerOrderItem;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;
import com.zhumg.anlib.widget.dialog.InputClickListener;
import com.zhumg.anlib.widget.dialog.InputDialog;

import java.util.List;

/**
 * Created by zhumg on 5/2.
 */
public class ShouItemAdapter extends AfinalAdapter<ConsumerOrderItem> implements InputClickListener {

    InputDialog inputDialog = null;
    ConsumerOrderItem click_item = null;

    public ShouItemAdapter(Context context, List<ConsumerOrderItem> consumerOrderItems) {
        super(context, consumerOrderItems);
        inputDialog = new InputDialog(context, InputType.TYPE_CLASS_NUMBER);
        inputDialog.setInputClickListener(this);

//        inputDialog.setTitle("请输入收货数量");
//        inputDialog.setTitle("请输入赠送数量");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_send_item, null);
            holder = new Holder();
            holder.init(convertView);
            convertView.setTag(holder);
            convertView.setOnClickListener(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        ConsumerOrderItem item = getItem(position);
        holder.item = item;
        holder.refresh();

        return convertView;
    }

    @Override
    public void onInput(String txt) {
        if (txt == null || "".equals(txt)) {
            return;
        }
        //判断
        int v = 0;
        try {
            v = Integer.parseInt(txt);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        int q = click_item.getUnReceiptQuantity();
        if (v > q) {
            ToastUtil.showToast(context, "不能大于未收货数量");
            return;
        }
        click_item.setShouCount(v);
        notifyDataSetChanged();
    }

    class Holder implements View.OnClickListener {

        TextView tv_name;
        TextView tv_count;
        TextView tv_out;
        TextView tv_num;
        TextView tv_state;
        TextView tv_num_01;
        TextView tv_state_01;

        ConsumerOrderItem item;

        public void init(View view) {
            tv_name = ViewUtils.find(view, R.id.tv_name);
            tv_count = ViewUtils.find(view, R.id.tv_count);
            tv_out = ViewUtils.find(view, R.id.tv_out);
            tv_num = ViewUtils.find(view, R.id.tv_num);
            tv_state = ViewUtils.find(view, R.id.tv_state);

            tv_num_01 = ViewUtils.find(view, R.id.tv_num_01);
            tv_state_01 = ViewUtils.find(view, R.id.tv_state_01);


           tv_num.setOnClickListener(new View.OnClickListener() {//todo -----------------------------------
                @Override
                public void onClick(View v) {
                    //未完成收货数量输入
                    click_item = item;
                    inputDialog.setTitle("请输入收货数量");
                    inputDialog.setContentMsg(String.valueOf(item.getShouCount()));
                    inputDialog.show();
                }
            });


            tv_num_01.setOnClickListener(new View.OnClickListener() {//todo -----------------------------------
                @Override
                public void onClick(View v) {
                    //赠送数量输入
                    click_item = item;
                    inputDialog.setTitle("请输入赠送数量");
                    inputDialog.setContentMsg(String.valueOf(item.getShouCount()));
                    inputDialog.show();
                }
            });
        }

        void refresh() {
            tv_name.setText(item.getItemName());
            tv_count.setText("订单数：" + String.valueOf(item.getQuantity()));
            tv_out.setText("已收货：" + String.valueOf(item.getReceiptQuantity()));
            tv_num.setText(String.valueOf(item.getShouCount()));
            tv_state.setText("未完成收货");

            tv_num_01.setText(String.valueOf(item.getShouCount()));
            tv_state_01.setText("赠送数量");

        }

        @Override
        public void onClick(View v) {
//            click_item = item;
//            inputDialog.setContentMsg(String.valueOf(click_item.getShouCount()));
//            inputDialog.show();

        }
    }
}
