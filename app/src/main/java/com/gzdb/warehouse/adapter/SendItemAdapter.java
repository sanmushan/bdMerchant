package com.gzdb.warehouse.adapter;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzdb.warehouse.R;
import com.gzdb.response.ConsumerOrderItem;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;
import com.zhumg.anlib.widget.dialog.InputClickListener;
import com.zhumg.anlib.widget.dialog.InputDialog;

import java.util.List;

/**
 * Created by zhumg on 4/21.
 */
public class SendItemAdapter extends AfinalAdapter<ConsumerOrderItem> implements InputClickListener {

    InputDialog inputDialog = null;
    ConsumerOrderItem click_item = null;

    public SendItemAdapter(Context context, List<ConsumerOrderItem> consumerOrderItems) {
        super(context, consumerOrderItems);
        inputDialog = new InputDialog(context, InputType.TYPE_CLASS_NUMBER);
        inputDialog.setInputClickListener(this);
        inputDialog.setTitle("请输入发货数量");
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
        int q = click_item.getUnShippingQuantity();
        if (v > q) {
            ToastUtil.showToast(context, "不能大于未发货数量");
            return;
        }
        click_item.setSendCount(v);
        notifyDataSetChanged();
    }

    class Holder implements View.OnClickListener {

        TextView tv_name;
        TextView tv_count;
        TextView tv_out;
        TextView tv_num;
        TextView tv_state;

        ConsumerOrderItem item;

        public void init(View view) {
            tv_name = ViewUtils.find(view, R.id.tv_name);
            tv_count = ViewUtils.find(view, R.id.tv_count);
            tv_out = ViewUtils.find(view, R.id.tv_out);
            tv_num = ViewUtils.find(view, R.id.tv_num);
            tv_state = ViewUtils.find(view, R.id.tv_state);
        }

        void refresh() {
            tv_name.setText(item.getItemName());
            tv_count.setText("订单数：" + String.valueOf(item.getQuantity()));
            tv_out.setText("已发货：" + String.valueOf(item.getQuantity() - item.getUnShippingQuantity()));//已发货=问题-未发货
            tv_num.setText(String.valueOf(item.getSendCount()));
            tv_state.setText("未完成发货");
        }

        @Override
        public void onClick(View v) {
            click_item = item;
            inputDialog.setContentMsg(String.valueOf(click_item.getSendCount()));
            inputDialog.show();
        }
    }
}
