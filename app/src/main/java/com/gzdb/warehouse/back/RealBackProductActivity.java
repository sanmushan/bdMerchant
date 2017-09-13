package com.gzdb.warehouse.back;

import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.gzdb.response.Api;
import com.gzdb.response.SupplyItem;
import com.gzdb.utils.GlobalData;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.adapter.BackProductAdapter;
import com.gzdb.warehouse.adapter.ItemAdapter;
import com.gzdb.warehouse.ware.BackWareShopCart;
import com.gzdb.warehouse.ware.WareShopCart;
import com.zhumg.anlib.AfinalActivity;
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
 * Created by liyunbiao on 2017/7/13.
 */

public class RealBackProductActivity extends AfinalActivity {
    //选中的itemTypeId

    RefreshLoad refreshLoad;
    List<AdapterModel> items = new ArrayList<>();
    BackProductAdapter adapter;
    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;
    @Bind(R.id.fr_listview)
    ListView listView;

    @Bind(R.id.title_left)
    RelativeLayout title_left;

    @Override
    public int getContentViewId() {
        return R.layout.activity_real_back_product;
    }

    @Override
    public void initView(View view) {
        title_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealBackProductActivity.this.finish();
            }
        });
        adapter = new BackProductAdapter(RealBackProductActivity.this, items, false);
        listView.setAdapter(adapter);
        refreshLoad = new RefreshLoad(RealBackProductActivity.this, ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (!over) {
                    ptr.setVisibility(View.GONE);
                    startGetItems();
                } else {
                    ptr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    startGetItems();
                }
            }

            @Override
            public void onLoadmore(boolean over) {
                if (!over) {
                    startGetItems();
                }
            }
        }, listView);

        refreshLoad.showLoading();
    }

    void startGetItems() {

        try {
            if (BackWareShopCart.itemLists != null) {
                for (int i = 0; i < BackWareShopCart.itemLists.size(); i++) {
                    items.add(BackWareShopCart.itemLists.get(i));

                }
                refreshLoad.complete(BackWareShopCart.itemLists.size() >= Api.PAGE_SIZE, items.isEmpty());
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
