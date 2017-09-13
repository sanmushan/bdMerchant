package com.gzdb.buyer;

import android.content.Intent;
import android.util.Log;
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
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * 采购员 -> 仓库 界面
 * <p>
 * 显示仓库商品库存情况
 * <p>
 * <p/>
 * Created by zhumg on 4/21.
 */
public class BuyerWareItemFragment extends AfinalFragment implements View.OnClickListener, Cache.CacheCallback<List<Warehouse>> {

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

    BuyerWarehItemAdapter adapter;
    List<SupplyItem> items;

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
        return R.layout.fragment_buyer_warehouse;
    }

    @Override
    protected void initViewData(View view) {
        title_center_txt.setText("我的仓库");

        items = new ArrayList<>();
        adapter = new BuyerWarehItemAdapter(this.getActivity(), items);
        listView.setAdapter(adapter);

        title_center.setOnClickListener(this);
        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);

        refreshLoad = new RefreshLoad(this.getActivity(), ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (!over) {
                    ptr.setVisibility(View.GONE);
                    if (!wlist_bool) {
                        startGetWarehouses();
                    } else {
                        pageIndex = 1;
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
        Log.e("db", "当前选中仓库：" + selectWarehouse.getName() + ", " + selectWarehouse.getWarehouseId());
        Map map = new HashMap();
        map.put("passportId", String.valueOf(Cache.passport.getPassportIdStr()));//采购员的passportId;
        map.put("targetWarehouseId", selectWarehouse == null ? "0" : String.valueOf(selectWarehouse.getWarehouseId()));//采购员的passportId
        map.put("itemTypeId", String.valueOf(selectTypeId));
        map.put("roleType", String.valueOf(Cache.clientTypeEnum.getKey()));
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(Api.PAGE_SIZE));
        //map.put("searchKeyValue", String.valueOf(Api.PAGE_SIZE));//这里不需要搜索
        Http.post(this.getActivity(), map, Api.WAREHOUSE_ITEMS, new HttpCallback<List<SupplyItem>>("datas") {
            @Override
            public void onSuccess(List<SupplyItem> data) {
                if (refreshLoad.isLoadMore()) {
                    adapter.addMore(data);
                } else {
                    adapter.refresh(data);
                }
                adapter.notifyDataSetChanged();
                refreshLoad.complete(data.size() >= Api.PAGE_SIZE, adapter.isEmpty());
            }

            @Override
            public void onFailure() {
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
            //标记打开了 分类 列表
            show_type_activity = true;
            Intent intent = new Intent(this.getActivity(), SupplyTypeActivity.class);
            intent.putExtra("buyer", false);
            startActivity(intent);
        } else if (id == R.id.title_right) {
            Intent intent = new Intent(this.getActivity(), BuyerWareItemSearchActivity.class);
            intent.putExtra("warehouseId", selectWarehouse == null ? 0 : selectWarehouse.getWarehouseId());
            startActivity(intent);
        } else if (id == R.id.title_center) {
            //标记打开了 仓库 列表
            show_warehouse_activity = true;
            //弹出仓库列表
            Intent intent = new Intent(this.getActivity(), WarehouseListActivity.class);
            intent.putExtra("selectId", selectWarehouse == null ? 0 : selectWarehouse.getPassportId());
            startActivity(intent);
        }
    }

    @Override
    public void onVisible() {
        //如果点击显示了仓库
        if (show_warehouse_activity) {
            //设置值
            show_warehouse_activity = false;
            for (int i = 0; i < Cache.warehouses.size(); i++) {
                Warehouse warehouse = Cache.warehouses.get(i);
                if (warehouse.isSelect()) {
                    selectWarehouse = warehouse;
                    title_center_txt.setText(selectWarehouse.getName());
                    refreshLoad.showLoading();
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
            }else {
                selectTypeId =0;
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
            refreshLoad.getLoadingView().showLoadingTxt("加载" + selectWarehouse.getName() + " 仓库商品");
            title_center.post(new Runnable() {
                @Override
                public void run() {
                    startGetItems();
                }
            });
        } else {
            selectWarehouse = null;
            title_center_txt.setText("我的仓库");
            refreshLoad.showError("没有仓库列表");
        }
    }

    @Override
    public void onFailure(int code, String msg) {
        refreshLoad.showError(msg);
    }

}
