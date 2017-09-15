package com.gzdb.warehouse.me;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gzdb.response.Api;
import com.gzdb.response.LogisticsListBean;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.adapter.LogisticsListAdapter;
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
 * 物流交接 - 交接任务列表
 */
public class LogisticsHandoverActivity extends AfinalActivity implements View.OnClickListener{
    //选中的itemTypeId
    BaseTitleBar baseTitleBar;
    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;
    @Bind(R.id.fr_listview)
    ListView listView;
    LogisticsListAdapter adapter;
    List<LogisticsListBean>list = new ArrayList<>();
    RefreshLoad refreshLoad;
    //当前页面索引
    int pageIndex = 1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_logistics_handover;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("交接任务列表");
        baseTitleBar.setLeftListener(this);

        adapter = new LogisticsListAdapter(LogisticsHandoverActivity.this,list);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                List<LogisticsDetailsListBean> data = list.get(position).getLogisticsDispatchAreaDetailsList();
                Intent intent = new Intent(LogisticsHandoverActivity.this, HandoverDetailActivity.class);
                intent.putExtra("position", String.valueOf(position));
                intent.putExtra("batchNo", list.get(position).getBatchNo());
                startActivity(intent);

            }
        });
    }
    private void getData() {
        Map map = new HashMap();
//        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("passportId", "647800");
        Http.post(this,map, Api.LOGISTICS_LIST_URL, new HttpCallback<List<LogisticsListBean>>("datas") {
            @Override
            public void onSuccess(List<LogisticsListBean> data) {

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
