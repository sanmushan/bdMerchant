package com.gzdb.developing.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzdb.response.ItemType;
import com.gzdb.response.Warehouse;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * 作   者：liyunbiao
 * 时   间：2017/5/24
 * 修 改 人：
 * 日   期：
 * 描   述：
 */

public class TypeAdapter extends AfinalAdapter<ItemType> {
    public TypeAdapter(Context context, List<ItemType> strings) {
        super(context, strings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        try {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.items_type, null);
                holder = new Holder();
                holder.init(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            ItemType itemType = getItem(position);
            holder.tv_title.setText(itemType.getTitle());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    class Holder {

        TextView tv_title;

        void init(View view) {

            tv_title = ViewUtils.find(view, R.id.tv_title);

        }
    }
}
