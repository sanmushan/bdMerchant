package com.gzdb.warehouse.back;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzdb.response.Api;
import com.gzdb.response.SupplyItem;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.adapter.BackProductAdapter;
import com.gzdb.warehouse.adapter.ItemAdapter;
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
 * Created by liyunbiao on 2017/7/12.
 */

public class BackProductActivity extends AfinalActivity implements View.OnClickListener {
    //选中的itemTypeId
    long selectTypeId;
    int pageIndex = 1;
    SearchTitleBar searchTitleBar;
    RefreshLoad refreshLoad;
    List<AdapterModel> items = new ArrayList<>();
    BackProductAdapter adapter;
    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;
    @Bind(R.id.fr_listview)
    ListView listView;

    @Bind(R.id.okbtn)
    TextView okbtn;
    @Bind(R.id.title_left)
    RelativeLayout title_left;
    @Override
    public int getContentViewId() {
        return R.layout.activity_back_product;
    }

    @Override
    public void initView(View view) {
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.setClass(BackProductActivity.this,RealBackProductActivity.class);
                startActivity(intent);
            }
        });
        searchTitleBar = new SearchTitleBar(view);
        searchTitleBar.setLeftBack(this);
        searchTitleBar.setRightTxt("搜索");
        searchTitleBar.setRightListener(this);
        searchTitleBar.getCenter_search_edit().setHint("请输入订单号进行搜索");
        searchTitleBar.setEditTxtListener(new SearchTitleBar.EditTextListener() {
            @Override
            public void notifyInputTxt(String str) {
                //如果有内容，则搜索
                if (str != null && str.trim().length() > 0) {
                    //搜索
                    //search(str);
                } else {
                    //清除
                    ptr.setVisibility(View.GONE);
                    refreshLoad.hibe();
                }
            }
        });
        adapter = new BackProductAdapter(BackProductActivity.this, items, false);
        listView.setAdapter(adapter);
        refreshLoad = new RefreshLoad(BackProductActivity.this, ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (!over) {
                    pageIndex = 1;
                    ptr.setVisibility(View.GONE);
                    startGetItems();
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

        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("itemTypeId", String.valueOf(selectTypeId));
        map.put("roleType", String.valueOf(Cache.clientTypeEnum.getKey()));
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(10));

        Http.post(BackProductActivity.this, map, Api.WAREHOUSE_ITEMS, new HttpCallback<List<SupplyItem>>("datas") {

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

    @Override
    public void onClick(View v) {

    }
}
