package com.gzdb.buyer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzdb.warehouse.R;
import com.gzdb.response.SupplyItem;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by zhumg on 4/21.
 */
public class BuyerWarehItemAdapter extends AfinalAdapter<SupplyItem> {

    public BuyerWarehItemAdapter(Context context, List<SupplyItem> supplyItems) {
        super(context, supplyItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_warehouse_item, null);
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
        TextView txt_wait;
        TextView txt_stock;
        TextView txt_buy;
        TextView txt_unin;
        TextView txt_state;
        SupplyItem item;

        void init(View view) {

            item_pic = ViewUtils.find(view, R.id.item_pic);
            txt_itemName = ViewUtils.find(view, R.id.txt_itemName);
            txt_wait = ViewUtils.find(view, R.id.txt_wait);
            txt_stock = ViewUtils.find(view, R.id.txt_stock);
            txt_buy = ViewUtils.find(view, R.id.txt_buy);
            txt_unin = ViewUtils.find(view, R.id.txt_unin);
            txt_state = ViewUtils.find(view, R.id.txt_state);
        }

        void refresh() {

            if (item.getImageUrl() != null) {
                ImageLoader.getInstance().displayImage(item.getImageUrl() + "@150w", item_pic);
            }

            txt_itemName.setText(item.getItemName());
            txt_wait.setText("预警库存：" + String.valueOf(item.getWarningQuantity()));
            txt_stock.setText("库存：" + String.valueOf(item.getStock()));
            txt_buy.setText("采购中：" + String.valueOf(item.getPurchaseQuantity()));
            txt_unin.setText("单位：" + item.getUnitName());

            if(item.getStock() < item.getWarningQuantity()) {
                txt_state.setVisibility(View.VISIBLE);
            } else {
                txt_state.setVisibility(View.GONE);
            }
        }

    }
}
