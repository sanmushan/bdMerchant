package com.gzdb.warehouse.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzdb.warehouse.R;
import com.gzdb.response.SupplyItem;
import com.gzdb.warehouse.ware.WareShopCart;
import com.gzdb.widget.BuyCountInputDialog;
import com.gzdb.widget.NumInputView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.utils.ViewUtils;
import com.zhumg.anlib.widget.AdapterModel;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by zhumg on 4/19.
 */
public class ItemAdapter extends AfinalAdapter<AdapterModel> implements NumInputView.NumInputListener {

    public static final int CART_MODEL_UI_TYPE_ITEM = 0;
    public static final int CART_MODEL_UI_TYPE_CARTITEM = 1;

    int zeroColor;
    int defaultColor;

    BuyCountInputDialog buyCountInputDialog;
    Holder click_holder;

    TextView cartCountTxt;
    boolean isCart = false;

    //删除时，回调
    public Runnable delRunnable;

    public ItemAdapter(Context context, List<AdapterModel> supplyItems, TextView allTxt, boolean isCart) {
        super(context, supplyItems);
        zeroColor = context.getResources().getColor(R.color.red);
        defaultColor = context.getResources().getColor(R.color.font_9);
        buyCountInputDialog = new BuyCountInputDialog(context, this, 4);
        cartCountTxt = allTxt;
        this.isCart = isCart;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.adapter_item, null);
            holder = new Holder();
            holder.init(convertView);
            convertView.setTag(holder);
            convertView.setOnClickListener(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        AdapterModel model = getItem(position);

        WareShopCart.CartItem cartItem = null;
        SupplyItem item = null;

        if (model.getUiType() == CART_MODEL_UI_TYPE_ITEM) {
            //模型来源于 采购页面
            item = (SupplyItem) model;
            cartItem = WareShopCart.getCartItem(item.getItemId());
        } else {
            //模型来源于 购物车
            cartItem = (WareShopCart.CartItem) model;
            item = cartItem.srcItem;
        }

        if (item.getImageUrl() != null) {
            ImageLoader.getInstance().displayImage(item.getImageUrl() + "@150w", holder.item_pic);
        }

        holder.item = item;
        holder.cartItem = cartItem;
        holder.position = position;

        holder.txt_itemName.setText(item.getItemName());
        holder.txt_wait.setText("待发:" + item.getPendingQuantity());
        holder.txt_yj.setText("预警库存:" + item.getWarningQuantity());

        holder.txt_kc.setText("库存:" + item.getStock());
        holder.txt_kc.setTextColor(defaultColor);

        if (item.getStock() == 0 || item.getStock() < item.getWarningQuantity()) {
            holder.txt_kc_ti.setVisibility(View.VISIBLE);
        } else {
            holder.txt_kc_ti.setVisibility(View.INVISIBLE);
        }

        if (item.getPartnerStock() > 0) {
            holder.emptyImg.setVisibility(View.GONE);
            holder.addBtn.setVisibility(View.VISIBLE);
            holder.buyNum.setVisibility(View.VISIBLE);
            //如果有买该商品
            if (cartItem != null && cartItem.count > 0) {
                holder.delBtn.setVisibility(View.VISIBLE);
                holder.buyNum.setText(String.valueOf(cartItem.count));
            } else {
                holder.delBtn.setVisibility(View.INVISIBLE);
                holder.buyNum.setText("0");
            }
        } else {
            holder.emptyImg.setVisibility(View.VISIBLE);
            holder.addBtn.setVisibility(View.GONE);
            holder.delBtn.setVisibility(View.GONE);
            holder.buyNum.setVisibility(View.GONE);
        }

        if(cartItem != null) {
            holder.txt_zkc.setText("主仓库存：" + (item.getPartnerStock() - cartItem.count));
        } else {
            holder.txt_zkc.setText("主仓库存：" + item.getPartnerStock());
        }

        return convertView;
    }

    @Override
    public boolean onClickNum(String text) {
        if (click_holder == null) {
            return false;
        }
        try {
            int v = Integer.parseInt(text);
            if (click_holder.cartItem == null) {
                click_holder.cartItem = WareShopCart.createCartItem(click_holder.item);
            }
            //检测 库存
            if (v > click_holder.item.getPartnerStock()) {
                ToastUtil.showToast(context, "已超出主仓库存");
                return false;
            }
            click_holder.cartItem.set(v);
            if (v > 0) {
                click_holder.delBtn.setVisibility(View.VISIBLE);
            } else {
                click_holder.delBtn.setVisibility(View.INVISIBLE);
            }
            click_holder.buyNum.setText(text);
            if (cartCountTxt != null) {
                cartCountTxt.setText(String.valueOf(WareShopCart.getAllCount()));
            }
            click_holder.txt_zkc.setText("主仓库存：" +
                    +(click_holder.item.getPartnerStock() - click_holder.cartItem.count));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void onMax() {
        ToastUtil.showToast(context, "购买数量不能超过 9999 哦！");
    }

    class Holder implements View.OnClickListener {

        ImageView item_pic;
        TextView txt_itemName;
        TextView txt_wait;
        TextView txt_yj;
        TextView txt_kc;
        TextView txt_kc_ti;
        TextView txt_zkc;
        View emptyImg;

        TextView buyNum;
        View addBtn;
        View delBtn;

        int position;
        SupplyItem item;
        WareShopCart.CartItem cartItem;

        void init(View view) {

            item_pic = ViewUtils.find(view, R.id.item_pic);
            txt_itemName = ViewUtils.find(view, R.id.txt_itemName);
            txt_wait = ViewUtils.find(view, R.id.txt_wait);
            txt_yj = ViewUtils.find(view, R.id.txt_yj);
            txt_kc = ViewUtils.find(view, R.id.txt_kc);
            txt_kc_ti = ViewUtils.find(view, R.id.txt_kc_ti);
            txt_zkc = ViewUtils.find(view, R.id.txt_zkc);
            emptyImg = ViewUtils.find(view, R.id.item_pic_no);

            addBtn = ViewUtils.find(view, R.id.ll_add);
            addBtn.setOnClickListener(this);
            buyNum = ViewUtils.find(view, R.id.tv_num);
            buyNum.setOnClickListener(this);
            delBtn = ViewUtils.find(view, R.id.ll_del);
            delBtn.setOnClickListener(this);
            delBtn.setVisibility(View.INVISIBLE);
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
                if (cartItem == null) {
                    buyCountInputDialog.setTxtValue("0", item.getPartnerStock());
                } else {
                    buyCountInputDialog.setTxtValue(cartItem.count + "", item.getPartnerStock());
                }
                buyCountInputDialog.show();
            }
        }


        void add() {
            if (item.getPartnerStock() <= 0) {
                ToastUtil.showToast(addBtn.getContext(), "主仓库存不足");
                return;
            }
            if (cartItem == null) {
                cartItem = WareShopCart.createCartItem(item);
            }
            if (cartItem.count + 1 > item.getPartnerStock()) {
                ToastUtil.showToast(addBtn.getContext(), "已超出主仓库存");
                return;
            }
            cartItem.add();
            buyNum.setText(cartItem.count + "");
            delBtn.setVisibility(View.VISIBLE);
            txt_zkc.setText("主仓库存：" +
                    +(item.getPartnerStock() - cartItem.count));
            //刷新
            if (cartCountTxt != null)
                cartCountTxt.setText(String.valueOf(WareShopCart.getAllCount()));
        }

        void del() {
            if (cartItem == null) {
                cartItem = WareShopCart.getCartItem(item.getItemId());
            }
            if (cartItem == null || cartItem.count < 1) {
                return;
            }
            boolean canRemove = cartItem.del();
            if (cartItem.count > 0) {
                buyNum.setText(cartItem.count + "");
            } else {
                buyNum.setText("0");
                delBtn.setVisibility(View.INVISIBLE);
            }
            //如果需要删除
            if (isCart && canRemove) {
                //删除
                cartItem = null;
                remove(position);
                if (delRunnable != null) {
                    delRunnable.run();
                }
                notifyDataSetChanged();
            } else if(canRemove) {
                cartItem = null;
                notifyDataSetChanged();
            } else {
                txt_zkc.setText("主仓库存：" +
                        +(item.getPartnerStock() - cartItem.count));
            }
            //刷新
            if (cartCountTxt != null)
                cartCountTxt.setText(String.valueOf(WareShopCart.getAllCount()));
        }
    }
}
