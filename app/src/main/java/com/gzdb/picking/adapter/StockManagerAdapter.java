package com.gzdb.picking.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzdb.buyer.BuyerItemAdapter;
import com.gzdb.buyer.BuyerShopCart;
import com.gzdb.response.Item;
import com.gzdb.response.ItemStock;
import com.gzdb.warehouse.R;
import com.gzdb.widget.BuyCountInputDialog;
import com.gzdb.widget.NumInputView;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by liyunbiao on 2017/8/23.
 */

public class StockManagerAdapter extends AfinalAdapter<ItemStock> implements NumInputView.NumInputListener {
    BuyCountInputDialog buyCountInputDialog;
    StockManagerAdapter.Holder click_holder;

    public StockManagerAdapter(Context context, List<ItemStock> item) {
        super(context, item);
        buyCountInputDialog = new BuyCountInputDialog(context, this, 4);
    }

    OnClickListenerDel onClickListenerDel;

    public interface OnClickListenerDel {
        void setOnClickListener(ItemStock item, String i);
    }

    public void setOnClickListenerDel(OnClickListenerDel onClickListenerDel) {
        this.onClickListenerDel = onClickListenerDel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_stock_item, null);
            holder = new Holder();
            holder.init(convertView, position);
            convertView.setTag(holder);
            convertView.setOnClickListener(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final ItemStock item = getItem(position);
        holder.tv_title.setText(item.getItemName());
        holder.tv_stock.setText("库存：" + item.getStock() + item.getItemUnitName());
        holder.tv_encode.setText("条码：" + item.getBarcode());

        if (item.getStock() != null) {

            holder.tv_num.setText(item.getStock());
        } else {
            holder.tv_num.setText("");
        }
        final TextView tv = holder.tv_num;
        holder.tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListenerDel.setOnClickListener(item, tv.getText().toString());
            }
        });
        return convertView;
    }

    class Holder implements View.OnClickListener {


        TextView tv_title;
        TextView tv_encode;
        TextView tv_num;
        TextView tv_stock;
        TextView tv_save;
        ImageView ll_add;
        ImageView ll_del;
        int pos = 0;

        void init(View view, int pos) {
            tv_title = ViewUtils.find(view, R.id.tv_title);
            tv_encode = ViewUtils.find(view, R.id.tv_encode);
            tv_num = ViewUtils.find(view, R.id.tv_num);
            tv_stock = ViewUtils.find(view, R.id.tv_stock);
            tv_save = ViewUtils.find(view, R.id.tv_save);
            ll_add = ViewUtils.find(view, R.id.ll_add);
            ll_del = ViewUtils.find(view, R.id.ll_del);
            tv_num.setOnClickListener(this);


            ll_add.setOnClickListener(this);
            ll_del.setOnClickListener(this);
            this.pos = pos;

        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.ll_add) {
                add();
            } else if (id == R.id.ll_del) {
                del();
            } else if (id == R.id.tv_num) {
                click_holder = this;
                //弹出窗体
                ItemStock item = getItem(pos);
                if (item != null && item.getStock() != null) {
                    buyCountInputDialog.setTxtValue(item.getStock(), 1000);
                } else {
                    buyCountInputDialog.setTxtValue("0", 0);
                }
                buyCountInputDialog.show();

            }

        }

        void add() {

            int num = Integer.parseInt(tv_num.getText().toString());
            num++;
            //刷新
            if (tv_num != null) {
                tv_num.setText(String.valueOf(num));
            }
        }

        void del() {
            int num = Integer.parseInt(tv_num.getText().toString());
            num--;
            //刷新
            if (tv_num != null)
                tv_num.setText(String.valueOf(num));
        }
    }


    @Override
    public boolean onClickNum(String text) {
        if (click_holder == null) {
            return false;
        }
        try {
            click_holder.tv_num.setText(text);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onMax() {

    }
}
