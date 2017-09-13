package com.gzdb.zwarehouse;

import android.view.View;
import android.widget.ListView;

import com.gzdb.response.Api;
import com.gzdb.response.SupplyItem;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.widget.bar.SearchTitleBar;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by zhumg on 4/27.
 */
public class ZWareSearchItemActivity extends AfinalActivity implements View.OnClickListener {

    SearchTitleBar searchTitleBar;
    RefreshLoad refreshLoad;

    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;

    @Bind(R.id.fr_listview)
    ListView listView;

    int pageIndex = 1;
    long warehouseId;

    ZWareItemAdapter adapter;
    List<SupplyItem> items = new ArrayList<>();

    @Override
    public int getContentViewId() {
        return R.layout.activity_search_item;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();

        adapter = new ZWareItemAdapter(this, items);
        listView.setAdapter(adapter);

        warehouseId = getIntent().getLongExtra("warehouseId", 0);
        searchTitleBar = new SearchTitleBar(view);
        searchTitleBar.setLeftBack(this);
        searchTitleBar.setRightTxt("搜索");
        searchTitleBar.setRightListener(this);
        searchTitleBar.getCenter_search_edit().setHint("请输入商品名称进行搜索");

        refreshLoad = new RefreshLoad(this, ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (!over) {
                    pageIndex = 1;
                    ptr.setVisibility(View.GONE);
                    //搜索
                    startSearch();
                } else {
                    ptr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    pageIndex = 1;
                    startSearch();
                }
            }

            @Override
            public void onLoadmore(boolean over) {
                if (!over) {
                    pageIndex++;
                    startSearch();
                }
            }
        }, listView);

        ptr.setVisibility(View.GONE);
        refreshLoad.getLoadingView().showLoadingTxt("请输入商品名称进行搜索");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_right) {
            //搜索
            refreshLoad.showLoading();
        }
    }

    void startSearch() {
        String key = searchTitleBar.getCenter_search_edit().getText().toString().trim();
        if (key == null || key.length() < 1) {
            return;
        }
        Map map = new HashMap();
        map.put("passportId", String.valueOf(Cache.passport.getPassportIdStr()));//采购员的passportId;
        map.put("targetWarehouseId", String.valueOf(warehouseId));//仓库的passportId
        map.put("itemTypeId", "0");
        map.put("roleType", String.valueOf(Cache.clientTypeEnum.getKey()));
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(Api.PAGE_SIZE));
        map.put("searchKeyValue", key);//这里不需要搜索

        Http.post(this, map, Api.WAREHOUSE_ITEMS, new HttpCallback<List<SupplyItem>>("datas") {

            @Override
            public void onSuccess(List<SupplyItem> data) {
                if (refreshLoad.isLoadMore()) {
                    items.addAll(data);
                } else {
                    items.clear();
                    items.addAll(data);
                }
                refreshLoad.complete(data.size() >= Api.PAGE_SIZE, adapter.isEmpty());
                adapter.notifyDataSetChanged();
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
}
