package com.gzdb.buyer;

import android.util.Log;

import com.gzdb.response.SupplyItem;
import com.gzdb.warehouse.adapter.ItemAdapter;
import com.zhumg.anlib.widget.AdapterModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/4/22 0022.
 */

public class BuyerShopCart {

    public static final int CART_MODEL_UI_TYPE_TITLE = 0;
    public static final int CART_MODEL_UI_TYPE_SUPPLYITEM = 1;
    public static final int CART_MODEL_UI_TYPE_CARTITEM = 2;

    private static Map<Long, BuyerCartItem> itemMaps = new HashMap<Long, BuyerCartItem>();
    public static final List<BuyerCartItem> itemLists = new ArrayList<BuyerCartItem>();
    public static int UPDATE_ID;

    public static int getAllCount() {
        int count = 0;
        for (int i = 0; i < itemLists.size(); i++) {
            BuyerCartItem item = itemLists.get(i);
            count = count + item.count;
        }
        return count;
    }


    public static long getAllPrice() {
        long price = 0;
        for (int i = 0; i < itemLists.size(); i++) {
            BuyerCartItem item = itemLists.get(i);
            price = price + (item.srcItem.getSellPrice() * item.count);
        }
        return price;
    }

    public static void removeAll() {
        itemLists.clear();
        itemMaps.clear();
        UPDATE_ID++;
    }

    public static class BuyerCartItem implements AdapterModel {

        public int count;
        public SupplyItem srcItem;
        // 选中标记
        public boolean selected;
        //当为仓库时，该值表示仓库标题
        public String title;
        //当为仓库时，该值是仓库ID
        public long wid;
        //UI
        public int uiType = CART_MODEL_UI_TYPE_CARTITEM;

        public BuyerCartItem(SupplyItem supplyItem) {
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

        public long getPrice() {
            return count * srcItem.getSellPrice();
        }

        @Override
        public int getUiType() {
            return ItemAdapter.CART_MODEL_UI_TYPE_CARTITEM;
        }
    }

    public static BuyerCartItem createCartItem(SupplyItem supplyItem) {
        BuyerCartItem ci = itemMaps.get(supplyItem.getItemId());
        if (ci != null) {
            return ci;
        }
        ci = new BuyerCartItem(supplyItem);
        itemMaps.put(supplyItem.getItemId(), ci);
        itemLists.add(ci);
        return ci;
    }

    public static BuyerCartItem getCartItem(long itemId) {
        return itemMaps.get(itemId);
    }

    private static void delShopCartItem_(SupplyItem supplyItem) {
        BuyerCartItem cartItem = itemMaps.remove(supplyItem.getItemId());
        if (cartItem != null) {
            for (int i = 0; i < itemLists.size(); i++) {
                BuyerCartItem sci = itemLists.get(i);
                if (sci.srcItem.getItemId() == cartItem.srcItem.getItemId()) {
                    itemLists.remove(i);
                    return;
                }
            }
        }
        Log.e("zhumg", "购物车列表没有找到该商品 " + supplyItem.getItemId());
    }


    /////////////////////下单相关数据
    public static List<WCartItem> wCartItems = new ArrayList<>();

    public static class WCartItem {
        public long wid;//仓库ID
        public String name;//仓库名称
        public long allprice;//合计总价
        public long allcount;//合计数量
        public List<BuyerCartItem> cartItems = new ArrayList<>();//相关商品
    }

    public static void toWCartItem(List<AdapterModel> cartItems) {
        wCartItems.clear();
        WCartItem wci = null;
        long aprice = 0;
        long acount = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            AdapterModel model = cartItems.get(i);
            BuyerCartItem ci = (BuyerCartItem)model;
            if (model.getUiType() == CART_MODEL_UI_TYPE_TITLE) {
                //记录老的内容
                if (wci != null) {
                    wci.allprice = aprice;
                    wci.allcount = acount;
                }
                //新建并添加
                wci = new WCartItem();
                wci.wid = ci.wid;
                wci.name = ci.title;
                wCartItems.add(wci);
            } else {
                wci.cartItems.add(ci);
                aprice = aprice + ci.getPrice();
                acount = acount + ci.count;
            }
        }
        if(wci != null) {
            wci.allprice = aprice;
            wci.allcount = acount;
        }
    }
}
