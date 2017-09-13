package com.gzdb.picking.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzdb.response.PickGroupBean;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by liyunbiao on 2017/8/24.
 */

public class PickGroupAdapter extends AfinalAdapter<PickGroupBean> {


    public PickGroupAdapter(Context context, List<PickGroupBean> item) {
        super(context, item);
    }

    OnClickListenerDel onClickListenerDel;

    public interface OnClickListenerDel {
        void setOnClickListener(PickGroupBean item);
    }

    public void setOnClickListenerDel(OnClickListenerDel onClickListenerDel) {
        this.onClickListenerDel = onClickListenerDel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_group_items, null);
            holder = new Holder();
            holder.init(convertView, position);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        final PickGroupBean item = getItem(position);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerDel.setOnClickListener(item);
            }
        });
        holder.tv_title.setText(item.getGroupName());

        return convertView;
    }

    class Holder implements View.OnClickListener {


        TextView tv_title;
        TextView tv_num;
        int pos = 0;
        void init(View view, int pos) {
            tv_title = ViewUtils.find(view, R.id.tv_title);
            tv_num = ViewUtils.find(view, R.id.tv_num);
            this.pos = pos;

        }

        @Override
        public void onClick(View v) {
            int id = v.getId();



        }


    }

}
