package com.gzdb.picking.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzdb.response.Item;
import com.gzdb.response.Passport;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by liyunbiao on 2017/8/24.
 */

public class PickPersonAdapter extends AfinalAdapter<Passport> {


    public PickPersonAdapter(Context context, List<Passport> item) {
        super(context, item);
    }

    OnClickListenerDel onClickListenerDel;

    public interface OnClickListenerDel {
        void setOnClickListener(Passport item);
    }

    public void setOnClickListenerDel(OnClickListenerDel onClickListenerDel) {
        this.onClickListenerDel = onClickListenerDel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_person_items, null);
            holder = new Holder();
            holder.init(convertView, position);
            convertView.setTag(holder);

        } else {
            holder = (Holder) convertView.getTag();
        }

        final Passport item = getItem(position);

        if (item.getRealName() != null && item.getRealName().length() > 0) {
            holder.tv_title.setText(item.getRealName());
        } else if (item.getShowName() != null && item.getShowName().length() > 0) {
            holder.tv_title.setText(item.getShowName());
        } else if (item.getLoginName() != null && item.getLoginName().length() > 0) {
            holder.tv_title.setText(item.getLoginName());
        } else {
            holder.tv_title.setText(item.getPhoneNumber());
        }
        holder.tv_num.setText(item.getDeviceName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerDel.setOnClickListener(item);
            }
        });
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
