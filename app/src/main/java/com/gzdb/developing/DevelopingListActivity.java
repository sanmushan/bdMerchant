package com.gzdb.developing;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.gzdb.developing.adapter.DevelopingListAdapter;
import com.gzdb.response.Api;
import com.gzdb.utils.Const;
import com.gzdb.utils.GlobalData;
import com.gzdb.warehouse.App;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.widget.ScrollerListView;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 作   者：liyunbiao
 * 时   间：2017/5/23
 * 修 改 人：
 * 日   期：
 * 描   述：
 */

public class DevelopingListActivity extends AfinalActivity implements ScrollerListView.IXListViewListener, View.OnClickListener {
    BaseTitleBar baseTitleBar;
    @Bind(R.id.lv_developing)
    ScrollerListView lv_developing;
    private List<DevelopingBean> list;
    DevelopingListAdapter adapter;
    private  int pageNo=0;
    @Override
    public int getContentViewId() {
        return R.layout.activity_developing_list;
    }

    @Override
    public void initView(View view) {
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("开拓供应商");
        baseTitleBar.setRightImg(R.drawable.add, this);
        lv_developing.setPullLoadEnable(true);
        lv_developing.setXListViewListener(this);
        list = new ArrayList<>();

        adapter = new DevelopingListAdapter(DevelopingListActivity.this, list);
        lv_developing.setAdapter(adapter);
        lv_developing.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DevelopingBean db=(DevelopingBean)parent.getAdapter().getItem(position);
              if(db.getAuditorType().equals("2")){
                  GlobalData.evelopingBean=db;
                  Intent intent =new Intent();
                  intent.setClass(DevelopingListActivity.this,DevelopingChangeActivity.class);
                  startActivity(intent);
              }
            }
        });
        onRefresh();
    }

    @Override
    public void onRefresh() {
        pageNo=1;
        initData();
    }

    @Override
    public void onLoadMore() {
        pageNo++;
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    private void initData() {
        try {
            Map map = new HashMap();
            map.put("pageNo", ""+pageNo);
            map.put("pageSize", Const.PAGESIZE);
            if( Cache.passport!=null) {
                map.put("passportId", Cache.passport.getPassportId() + "");
            }
            httpGet(map, Api.BasedeveloingURL() + "json/cdn/achieve/get_achieve_list", new HttpCallback<List<DevelopingBean>>("list") {
                @Override
                public void onSuccess(List<DevelopingBean> data) {
                     if(pageNo==1){
                        list.clear();
                    }
                    if(data!=null){
                        list.addAll(data);
                    }

                    adapter.notifyDataSetChanged();
                    lv_developing.stopLoadMore();
                    lv_developing.stopRefresh();
                    lv_developing.hideFoort();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
                startActivity(new Intent(DevelopingListActivity.this, DevelopingAddActivity.class));
                break;
        }
    }
}
