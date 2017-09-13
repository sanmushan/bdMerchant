package com.gzdb.developing.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gzdb.response.DevelopingType;
import com.gzdb.response.Warehouse;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by liyunbiao on 2017/5/26.
 */
public class DevelopingTypeAdapter extends AfinalAdapter<DevelopingType> {
    public DevelopingTypeAdapter(Context context, List<DevelopingType> strings) {
        super(context, strings);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.items_type, null);
            holder = new Holder();
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        DevelopingType d = getItem(position);
        holder.tv_title.setText(d.getAchieveTypeName());
        return convertView;
    }

    class Holder {

        TextView tv_title;

        void init(View view) {

            tv_title = ViewUtils.find(view, R.id.tv_title);
        }
    }
}
