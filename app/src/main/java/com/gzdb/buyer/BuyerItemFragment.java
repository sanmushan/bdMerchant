package com.gzdb.buyer;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.response.Api;
import com.gzdb.response.SupplyItem;
import com.gzdb.response.Warehouse;
import com.gzdb.warehouse.ware.SupplyTypeActivity;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.widget.AdapterModel;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * 采购员 采购 界面
 *
 * Created by Administrator on 2017/4/22 0022.
 */

public class BuyerItemFragment extends AfinalFragment implements View.OnClickListener, Cache.CacheCallback<List<Warehouse>> {

    @Bind(R.id.title_left)
    View title_left;

    @Bind(R.id.title_right)
    View title_right;

    @Bind(R.id.title_center)
    View title_center;

    @Bind(R.id.title_center_txt)
    TextView title_center_txt;

    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;

    @Bind(R.id.fr_listview)
    ListView listView;

    @Bind(R.id.cart)
    View cart;

    @Bind(R.id.cart_num)
    TextView cart_num;

    BuyerItemAdapter adapter;
    List<AdapterModel> items;

    RefreshLoad refreshLoad;
    int pageIndex = 1;

    //当前选中的仓库
    Warehouse selectWarehouse;
    //当前选中的商品类型
    long selectTypeId;

    //是否获取过仓库列表
    boolean wlist_bool = false;

    //点击显示了仓库activity
    boolean show_warehouse_activity = false;

    //点击显示了分类activity
    boolean show_type_activity = false;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_buyer;
    }

    @Override
    protected void initViewData(View view) {
        title_center_txt.setText("主仓库");

        items = new ArrayList<>();
        adapter = new BuyerItemAdapter(this.getActivity(), items, cart_num, false);
        listView.setAdapter(adapter);

        title_center.setOnClickListener(this);
        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);
        cart.setOnClickListener(this);

        refreshLoad = new RefreshLoad(this.getActivity(), ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (!over) {
                    ptr.setVisibility(View.GONE);
                    if(!wlist_bool) {
                        startGetWarehouses();
                    } else {
                        startGetItems();
                    }
                } else {
                    ptr.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    pageIndex = 1;
                    adapter.removeAll();
                    startGetItems();
                }
            }
            @Override
            public void onLoadmore(boolean over) {
                if (!over) {
                    pageIndex++;
                    startGetItems();
                }
            }
        }, listView);

        refreshLoad.showLoading();
    }

    void startGetItems() {
        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("itemTypeId", String.valueOf(selectTypeId));
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(Api.PAGE_SIZE));

        Http.post(this.getActivity(), map, Api.PURCHASE_ITEMS, new HttpCallback<List<SupplyItem>>("datas") {
            @Override
            public void onSuccess(List<SupplyItem> data) {
                if (refreshLoad.isLoadMore()) {
                    items.addAll(data);
                } else {
                    items.clear();
                    items.addAll(data);
                }
                adapter.notifyDataSetChanged();
                refreshLoad.complete(data.size() >= Api.PAGE_SIZE, items.isEmpty());
            }

            @Override
            public void onFailure() {
                refreshLoad.complete(0>= Api.PAGE_SIZE, items.isEmpty());
                if (refreshLoad.isLoadMore()) {
                    refreshLoad.showError(msg);
                } else {
                    adapter.removeAll();
                    adapter.notifyDataSetChanged();
                    //直接显示重试
                    ptr.setVisibility(View.GONE);
                    refreshLoad.showReset(msg);
                }
            }
        });

    }

    void startGetWarehouses() {
        Cache.getWarehouses(this.getActivity(), this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == com.zhumg.anlib.R.id.title_left) {
            //商品分类
            show_type_activity = true; //标记打开了 分类 列表
            Intent intent = new Intent(this.getActivity(), SupplyTypeActivity.class);
            intent.putExtra("buyer", true);
            getActivity().startActivityForResult(intent, 1);
        } else if (id == R.id.title_right) {
            //搜索商品
            Intent intent = new Intent(this.getActivity(), BuyerItemSearchActivity.class);
            intent.putExtra("targetWarehouseId", selectWarehouse == null ? 0 : selectWarehouse.getWarehouseId());
            this.getActivity().startActivity(intent);
        } else if (id == R.id.title_center) {
            //仓库列表
            show_warehouse_activity = true;//标记打开了 仓库 列表
            Intent intent = new Intent(this.getActivity(), WarehouseListActivity.class);
            intent.putExtra("targetWarehouseId", selectWarehouse == null ? 0 : selectWarehouse.getWarehouseId());
            this.getActivity().startActivity(intent);
        } else if(id == R.id.cart) {
            //去购物车
            Intent intent = new Intent(this.getActivity(), BuyerShopCartActivity.class);
            intent.putExtra("targetWarehouseId", selectWarehouse == null ? 0 : selectWarehouse.getWarehouseId());
            this.getActivity().startActivity(intent);
        }
    }

    @Override
    public void onVisible() {

        cart_num.setText(String.valueOf(BuyerShopCart.getAllCount()));
        adapter.notifyDataSetChanged();

        //如果点击显示了仓库
        if (show_warehouse_activity) {
            //设置值
            show_warehouse_activity = false;
            for (int i = 0; i < Cache.warehouses.size(); i++) {
                Warehouse warehouse = Cache.warehouses.get(i);
                if (warehouse.isSelect()) {
                    selectWarehouse = warehouse;
                    title_center_txt.setText(selectWarehouse.getName());
                    //refreshLoad.showLoading();
                    return;
                }
            }
        }

        //如果点击显示了分类
        if (show_type_activity) {
            show_type_activity = false;
            if (SupplyTypeActivity.click_select_type_id != 0 && selectTypeId != SupplyTypeActivity.click_select_type_id) {
                selectTypeId = SupplyTypeActivity.click_select_type_id;
                refreshLoad.showLoading();
            }
        }
    }

    @Override
    public void onSuccess(List<Warehouse> data) {
        wlist_bool = true;
        //拿第一个
        if (data.size() > 0) {
            selectWarehouse = data.get(0);
            title_center_txt.setText(selectWarehouse.getName());
            //加载商品
            refreshLoad.showLoading("加载" + selectWarehouse.getName() + " 仓库商品");
            startGetItems();
        } else {
            selectWarehouse = null;
            title_center_txt.setText("主仓库");
            refreshLoad.showError("没有仓库列表");
        }
    }

    @Override
    public void onFailure(int code, String msg) {

        refreshLoad.showError(msg);
    }
}
