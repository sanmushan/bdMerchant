package com.gzdb.picking;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzdb.picking.adapter.SearchPickingAdapter;
import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.BackOrder;
import com.gzdb.response.showPick;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.back.RealBackProductActivity;
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
 * Created by liyunbiao on 2017/8/9.
 */

public class SearchPickingActivity extends AfinalActivity implements View.OnClickListener {
    //选中的itemTypeId
    long selectTypeId;
    int pageIndex = 1;
    SearchTitleBar searchTitleBar;
    RefreshLoad refreshLoad;
    List<showPick> items = new ArrayList<>();
    SearchPickingAdapter adapter;
    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;
    @Bind(R.id.fr_listview)
    ListView listView;

    @Bind(R.id.okbtn)
    TextView okbtn;
    @Bind(R.id.title_left)
    RelativeLayout title_left;

    private  String strAll="";

    @Override
    public int getContentViewId() {
        return R.layout.activity_back_product;
    }

    @Override
    public void initView(View view) {
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(SearchPickingActivity.this, RealBackProductActivity.class);
                startActivity(intent);
            }
        });
        searchTitleBar = new SearchTitleBar(view);
        searchTitleBar.setLeftBack(this);
        searchTitleBar.setRightTxt("搜索");
        searchTitleBar.setRightListener(this);
        searchTitleBar.getCenter_search_edit().setHint("输入来源单号查询");
        searchTitleBar.setEditTxtListener(new SearchTitleBar.EditTextListener() {
            @Override
            public void notifyInputTxt(String str) {
                //如果有内容，则搜索
                if (str != null && str.trim().length() > 0) {
                    //搜索
                    strAll=str;
                    refreshLoad.showLoading();
                } else {
                    //清除
                    ptr.setVisibility(View.GONE);
                    refreshLoad.hibe();
                }
            }
        });

        adapter = new SearchPickingAdapter(SearchPickingActivity.this, items);
        listView.setAdapter(adapter);
        refreshLoad = new RefreshLoad(SearchPickingActivity.this, ptr, view, new RefreshLoadListener() {
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

        refreshLoad.getLoadingView().showLoadingTxt("输入来源单号查询");
    }

    void startGetItems() {

        Map map = ApiRequest.showPickList(Cache.passport.getPassportId(),strAll,"",pageIndex,20);
        Http.post(SearchPickingActivity.this, map, Api.showPickList, new HttpCallback<List<showPick>>("datas") {
            @Override
            public void onSuccess(List<showPick> data) {
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
                 //   refreshLoad.showError(msg);
                } else {
                    adapter.removeAll();
                    adapter.notifyDataSetChanged();
//                    ptr.setVisibility(View.GONE);
//                    refreshLoad.showReset(msg);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
