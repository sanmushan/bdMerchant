package com.gzdb.warehouse.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzdb.warehouse.R;
import com.gzdb.response.Warehouse;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by zhumg on 4/21.
 */
public class WarehouseAdapter extends AfinalAdapter<Warehouse> {

    private Warehouse selectWarehouse;

    public WarehouseAdapter(Context context, List<Warehouse> warehouses) {
        super(context, warehouses);
    }

    public Warehouse getSelectWarehouse() {
        return this.selectWarehouse;
    }

    public void setSelectWarehouse(Warehouse warehouse) {
        this.selectWarehouse = warehouse;
        this.selectWarehouse.setSelect(true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_warehouse, null);
            holder = new Holder();
            holder.init(convertView);
            convertView.setTag(holder);
            convertView.setOnClickListener(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Warehouse warehouse = getItem(position);

        holder.warehouse = warehouse;
        holder.tv_name.setText(warehouse.getName());
        holder.tv_address.setText(warehouse.getFormatAddress());

        if (warehouse.isSelect()) {
            holder.simg.setImageResource(R.drawable.choice_click_s);
        } else {
            holder.simg.setImageResource(R.drawable.choice_click);
        }

        return convertView;
    }

    class Holder implements View.OnClickListener {

        ImageView simg;
        TextView tv_name;
        TextView tv_address;
        Warehouse warehouse;

        void init(View view) {
            simg = ViewUtils.find(view, R.id.simg);
            tv_name = ViewUtils.find(view, R.id.tv_name);
            tv_address = ViewUtils.find(view, R.id.tv_address);
        }

        @Override
        public void onClick(View v) {
            if (selectWarehouse != null) {
                selectWarehouse.setSelect(false);
            }
            setSelectWarehouse(warehouse);
            notifyDataSetChanged();
            //关闭
            ((Activity)context).finish();
        }
    }
}
