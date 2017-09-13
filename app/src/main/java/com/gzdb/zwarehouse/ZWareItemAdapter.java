package com.gzdb.zwarehouse;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzdb.response.SupplyItem;
import com.gzdb.warehouse.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by zhumg on 4/27.
 */
public class ZWareItemAdapter extends AfinalAdapter<SupplyItem> {

    public ZWareItemAdapter(Context context, List<SupplyItem> supplyItems) {
        super(context, supplyItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_zware_item, null);
            holder = new Holder();
            holder.init(convertView);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        SupplyItem item = getItem(position);
        holder.item = item;
        holder.refresh();

        return convertView;
    }

    class Holder  {

        ImageView item_pic;
        TextView txt_itemName;
        TextView txt_stock;
        TextView txt_unin;
        TextView txt_state;
        SupplyItem item;

        void init(View view) {

            item_pic = ViewUtils.find(view, R.id.item_pic);
            txt_itemName = ViewUtils.find(view, R.id.txt_itemName);
            txt_stock = ViewUtils.find(view, R.id.txt_stock);
            txt_unin = ViewUtils.find(view, R.id.txt_unin);
            txt_state = ViewUtils.find(view, R.id.txt_state);
        }

        void refresh() {

            if (item.getImageUrl() != null) {
                ImageLoader.getInstance().displayImage(item.getImageUrl() + "@150w", item_pic);
            }

            txt_itemName.setText(item.getItemName());
            txt_stock.setText("库存：" + String.valueOf(item.getStock()));
            txt_unin.setText("单位：" + item.getUnitName());

            if(item.getStock() < item.getWarningQuantity()) {
                txt_state.setVisibility(View.VISIBLE);
            } else {
                txt_state.setVisibility(View.GONE);
            }
        }

    }
}
