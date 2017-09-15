package com.gzdb.warehouse.me;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.gzdb.response.Api;
import com.gzdb.response.LogisticsListInfo;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.adapter.LogisticsListDetailAdapter;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
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
 * 交接明细列表
 */
public class HandoverDetailActivity extends AfinalActivity implements View.OnClickListener{
    //选中的itemTypeId
    BaseTitleBar baseTitleBar;
    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;
    @Bind(R.id.fr_listview)
    ListView listView;
    LogisticsListDetailAdapter adapter;
    RefreshLoad refreshLoad;
    //当前页面索引
    int pageIndex = 1;
    List<LogisticsListInfo.ResponseEntity> list = new ArrayList<>();


    @Override
    public int getContentViewId() {
        return R.layout.activity_handover_detail;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();
        Intent intent = getIntent();
        String position = intent.getStringExtra("position");
        String batchNo = intent.getStringExtra("batchNo");
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt(batchNo+"交接明细");
        baseTitleBar.setLeftListener(this);

        adapter = new LogisticsListDetailAdapter(HandoverDetailActivity.this,list,position);
        listView.setAdapter(adapter);
        listView.setBackgroundResource(R.color.bg_e8);

        refreshLoad = new RefreshLoad(this, ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (over) {
                    ptr.setVisibility(View.VISIBLE);
                } else {
                    ptr.setVisibility(View.GONE);
                    //加载订单数据
                    pageIndex = 1;
                    getData();
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    pageIndex = 1;
                    getData();
                }
            }

            @Override
            public void onLoadmore(boolean over) {
                if (!over) {
                    pageIndex++;
                    getData();
                }
            }

        }, listView);
        refreshLoad.showLoading();

    }
    private void getData() {
        Map map = new HashMap();
//        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("passportId", "647800");
        Http.post(this,map, Api.LOGISTICS_LIST_URL, new HttpCallback<List<LogisticsListInfo.ResponseEntity>>("datas") {
            @Override
            public void onSuccess(List<LogisticsListInfo.ResponseEntity> data) {

                try {
                    if (data.size() < 1) {
                        //直接显示重试
                        ptr.setVisibility(View.GONE);
                        refreshLoad.showReset("没有数据");
                    } else {
                        if (refreshLoad.isLoadMore()) {
                            adapter.addMore(data);
                        } else {
                            adapter.refresh(data);
                            adapter.notifyDataSetChanged();
                        }
                        adapter.notifyDataSetChanged();
                    }
                    refreshLoad.complete(false, adapter.isEmpty());
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
