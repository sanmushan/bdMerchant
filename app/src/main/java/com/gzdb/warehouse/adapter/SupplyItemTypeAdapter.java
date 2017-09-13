package com.gzdb.warehouse.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gzdb.warehouse.R;
import com.gzdb.response.HomePageRecommendItemTypes;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/3/24 0024.
 */

public class SupplyItemTypeAdapter extends AfinalAdapter<HomePageRecommendItemTypes> {

    private int defaultColor = 0;
    private int selectColor = 0;
    private int uitype;

    public SupplyItemTypeAdapter(Context context, List<HomePageRecommendItemTypes> itemTypeEntities, int uitype) {
        super(context, itemTypeEntities);
        defaultColor = context.getResources().getColor(R.color.font_6);
        selectColor = context.getResources().getColor(R.color.blue);
        this.uitype = uitype;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_supply_type, null);
        }
        HomePageRecommendItemTypes entity = getItem(position);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_title);

        if (entity.isSelect()) {
            textView.setBackgroundResource(R.color.bg_f4);
            textView.setTextColor(selectColor);
        } else {
            textView.setTextColor(defaultColor);
            if(uitype == 1) {
                textView.setBackgroundResource(R.color.bg_f4);
            }else {
                textView.setBackgroundResource(R.color.white);
            }
        }

        if(entity.isSelect()) {
            textView.setTextColor(selectColor);
        } else {
            textView.setTextColor(defaultColor);
        }
        textView.setText(entity.getItemTypeName());
        return convertView;
    }

}