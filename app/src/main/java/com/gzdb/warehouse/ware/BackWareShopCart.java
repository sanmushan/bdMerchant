package com.gzdb.warehouse.ware;

import android.util.Log;

import com.gzdb.response.SupplyItem;
import com.gzdb.warehouse.adapter.ItemAdapter;
import com.zhumg.anlib.widget.AdapterModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liyunbiao on 2017/7/13.
 */

public class BackWareShopCart {
    private static Map<Long, CartItem> itemMaps = new HashMap<Long, CartItem>();
    public static final List<CartItem> itemLists = new ArrayList<CartItem>();
    public static int UPDATE_ID;

    public static int getAllCount() {
        int count = 0;
        for (int i = 0; i < itemLists.size(); i++) {
            CartItem item = itemLists.get(i);
            count = count + item.count;
        }
        return count;
    }

    public static void removeAll() {
        itemLists.clear();
        itemMaps.clear();
        UPDATE_ID++;
    }

    public static class CartItem implements AdapterModel {

        public int count;
        public SupplyItem srcItem;
        // 选中标记
        public boolean selected;

        public CartItem(SupplyItem supplyItem) {
            this.srcItem = supplyItem;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        /**
         * 购买
         * 0 成功购买
         * 1 超出库存
         * 2 超出限购
         *
         * @return
         */
        public int add() {
            count++;
            //更新
            UPDATE_ID++;
            return 0;
        }

        //删除
        public boolean del() {
            //每次删除1件
            count = count - 1;
            if (count == 0) {
                delShopCartItem_(srcItem);
                return true;
            }
            return false;
        }

        //直接设置
        public void set(int newv) {
            //更新
            UPDATE_ID++;
            count = newv;
        }

        @Override
        public int getUiType() {
            return ItemAdapter.CART_MODEL_UI_TYPE_CARTITEM;
        }
    }

    public static CartItem createCartItem(SupplyItem supplyItem) {
        CartItem ci = itemMaps.get(supplyItem.getItemId());
        if (ci != null) {
            return ci;
        }
        ci = new CartItem(supplyItem);
        itemMaps.put(supplyItem.getItemId(), ci);
        itemLists.add(ci);
        return ci;
    }

    public static CartItem getCartItem(long itemId) {
        return itemMaps.get(itemId);
    }

    private static void delShopCartItem_(SupplyItem supplyItem) {
        CartItem cartItem = itemMaps.remove(supplyItem.getItemId());
        if (cartItem != null) {
            for (int i = 0; i < itemLists.size(); i++) {
                CartItem sci = itemLists.get(i);
                if (sci.srcItem.getItemId() == cartItem.srcItem.getItemId()) {
                    itemLists.remove(i);
                    return;
                }
            }
        }
        Log.e("zhumg", "购物车列表没有找到该商品 " + supplyItem.getItemId());
    }
}
