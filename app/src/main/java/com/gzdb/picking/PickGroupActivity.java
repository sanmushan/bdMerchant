package com.gzdb.picking;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.gzdb.picking.adapter.PickGroupAdapter;
import com.gzdb.response.Api;
import com.gzdb.response.PickGroupBean;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.widget.bar.BaseTitleBar;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by liyunbiao on 2017/8/24.
 */

public class PickGroupActivity extends AfinalActivity implements View.OnClickListener {
    //选中的itemTypeId
    BaseTitleBar baseTitleBar;
    long selectTypeId;
    RefreshLoad refreshLoad;
    List<PickGroupBean> items = new ArrayList<>();
    PickGroupAdapter adapter;
    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;
    @Bind(R.id.fr_listview)
    ListView listView;
    int pageIndex = 1;


    @Override
    public int getContentViewId() {
        return R.layout.activity_pick_group;
    }

    @Override
    public void initView(View view) {

        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("拣货组");
        baseTitleBar.setLeftListener(this);
        baseTitleBar.setRightImg(R.drawable.add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.startActivity(PickGroupActivity.this, PickGroupAddActivity.class);
            }
        });
        adapter = new PickGroupAdapter(PickGroupActivity.this, items);
        listView.setAdapter(adapter);

        adapter.setOnClickListenerDel(new PickGroupAdapter.OnClickListenerDel() {
            @Override
            public void setOnClickListener(PickGroupBean item) {
                Intent myintent = new Intent();
                myintent.setClass(PickGroupActivity.this, PickGroupEditActivity.class);
                myintent.putExtra("id", item.getId());
                myintent.putExtra("groupName", item.getGroupName());
                myintent.putExtra("itemTypeId", item.getItemTypeId());
                startActivity(myintent);
            }
        });

        refreshLoad = new RefreshLoad(PickGroupActivity.this, ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (!over) {
                    pageIndex = 1;
                    ptr.setVisibility(View.GONE);
                    initData();
                } else {
                    ptr.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    pageIndex = 1;
                    initData();
                }
            }

            @Override
            public void onLoadmore(boolean over) {
                if (!over) {
                    pageIndex++;
                    initData();
                }
            }
        }, listView);
        refreshLoad.showLoading();

    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLoad.showLoading();
    }

    public void initData() {
        try {

            Map<String, String> map = new HashMap<>();
            map.put("passportId", Cache.passport.getPassportIdStr());
            httpPostNoShow(map, Api.BasesupplychainRemoteURL() + "warehouse/selGroup", new HttpCallback<List<PickGroupBean>>("datas") {
                @Override
                public void onSuccess(List<PickGroupBean> data) {

                    try {
                        items.clear();
                        if (data == null || data.size() == 0) {
                            //refreshLoad.getLoadingView().showReset(msg);
                            return;
                        }

                        items.addAll(data);
                        refreshLoad.complete(data.size() >= Api.PAGE_SIZE, items.isEmpty());
                        adapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure() {
                    super.onFailure();
                    refreshLoad.complete(false, items.isEmpty());
                    refreshLoad.getLoadingView().showReset(msg);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                ActivityManager.finishActivity(PickGroupActivity.class);
                break;
        }
    }
}
