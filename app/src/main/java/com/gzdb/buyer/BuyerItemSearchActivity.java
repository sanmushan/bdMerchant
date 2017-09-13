package com.gzdb.buyer;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.response.Api;
import com.gzdb.response.SupplyItem;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.widget.AdapterModel;
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
 * Created by zhumg on 4/25.
 */
public class BuyerItemSearchActivity extends AfinalActivity implements View.OnClickListener {

    SearchTitleBar searchTitleBar;

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

    long targetWarehouseId;

    @Override
    public int getContentViewId() {
        return R.layout.activity_search_item_cart;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();

        items = new ArrayList<>();
        adapter = new BuyerItemAdapter(this, items, cart_num, false);
        listView.setAdapter(adapter);

        cart_num.setText(String.valueOf(BuyerShopCart.getAllCount()));
        cart.setOnClickListener(this);

        targetWarehouseId = getIntent().getLongExtra("targetWarehouseId", 0);

        searchTitleBar = new SearchTitleBar(view);
        searchTitleBar.setLeftBack(this);
        searchTitleBar.setRightTxt("搜索");
        searchTitleBar.setRightListener(this);
        searchTitleBar.getCenter_search_edit().setHint("请输入商品名称进行搜索");
        refreshLoad = new RefreshLoad(this, ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (!over) {
                    pageIndex=1;
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
                    pageIndex=1;
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
        }else if(id == R.id.cart) {
            //去购物车
            Intent intent = new Intent(this, BuyerShopCartActivity.class);
            intent.putExtra("targetWarehouseId", targetWarehouseId);
            this.startActivity(intent);
        }
    }

    void startSearch() {
        String key = searchTitleBar.getCenter_search_edit().getText().toString().trim();
        if (key == null || key.length() < 1) {
            return;
        }
        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("itemTypeId", "0");
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(Api.PAGE_SIZE));
        map.put("searchKeyValue", key);//这里不需要搜索

        Http.post(this, map, Api.PURCHASE_ITEMS, new HttpCallback<List<SupplyItem>>("datas") {

            @Override
            public void onSuccess(List<SupplyItem> data) {
                if (refreshLoad.isLoadMore()) {
                    items.addAll(data);
                } else {
                    items.clear();
                    items.addAll(data);
                }
                refreshLoad.complete(data.size() >= Api.PAGE_SIZE, items.isEmpty());
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
