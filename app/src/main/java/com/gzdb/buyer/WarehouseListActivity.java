package com.gzdb.buyer;

import android.view.View;
import android.widget.ListView;

import com.gzdb.response.Api;
import com.gzdb.response.Warehouse;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.adapter.WarehouseAdapter;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.widget.bar.BaseTitleBar;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * 仓库列表
 * <p>
 * Created by zhumg on 4/21.
 */
public class WarehouseListActivity extends AfinalActivity implements Cache.CacheCallback<List<Warehouse>> {

    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;

    @Bind(R.id.fr_listview)
    ListView listView;

    BaseTitleBar baseTitleBar;
    List<Warehouse> warehouse = new ArrayList<>();
    WarehouseAdapter adapter = null;
    RefreshLoad refreshLoad = null;

    //选中的仓库ID值
    long selectWarehouseId = 0;

    @Override
    public int getContentViewId() {
        return R.layout.activity_warehouse_list;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("仓库列表");

        adapter = new WarehouseAdapter(this, warehouse);
        listView.setAdapter(adapter);

        selectWarehouseId = getIntent().getLongExtra("targetWarehouseId", 0);

        refreshLoad = new RefreshLoad(this, ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (!over) {
                    ptr.setVisibility(View.GONE);
                    Cache.getWarehouses(WarehouseListActivity.this, WarehouseListActivity.this);
                } else {
                    ptr.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    Cache.getWarehouses(WarehouseListActivity.this, WarehouseListActivity.this);
                }
            }

        }, listView);

        refreshLoad.showLoading();
    }

    @Override
    public void onSuccess(final List<Warehouse> data) {
        //拿第一个
        warehouse.clear();
        for (int i = 0; i < data.size(); i++) {
            Warehouse w = data.get(i);
            w.setSelect(false);
            if (selectWarehouseId == w.getWarehouseId()) {
                adapter.setSelectWarehouse(w);
            }
        }
        warehouse.addAll(data);
        adapter.notifyDataSetChanged();

        //下一帧完成
        listView.post(new Runnable() {
            @Override
            public void run() {
                refreshLoad.complete(false, adapter.isEmpty());
            }
        });
    }

    @Override
    public void onFailure(int code, String msg) {
        refreshLoad.showError(msg);
    }
}
