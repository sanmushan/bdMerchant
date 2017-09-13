package com.gzdb.picking;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gzdb.picking.adapter.PickPersonAdapter;
import com.gzdb.picking.adapter.StockManagerAdapter;
import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.Item;
import com.gzdb.response.Passport;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
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

public class PickPersonActivity extends AfinalActivity implements View.OnClickListener {
    //选中的itemTypeId
    BaseTitleBar baseTitleBar;
    long selectTypeId;
    RefreshLoad refreshLoad;
    List<Passport> items = new ArrayList<>();
    PickPersonAdapter adapter;
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
        baseTitleBar.setCenterTxt("拣货员");
        baseTitleBar.setLeftListener(this);
        baseTitleBar.setRightImg(R.drawable.add, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager.startActivity(PickPersonActivity.this, PickPersonAddActivity.class);

            }
        });

        adapter = new PickPersonAdapter(PickPersonActivity.this, items);
        listView.setAdapter(adapter);
        adapter.setOnClickListenerDel(new PickPersonAdapter.OnClickListenerDel() {
            @Override
            public void setOnClickListener(Passport item) {
                Intent myintent = new Intent();
                myintent.setClass(PickPersonActivity.this, PickPersonEditActivity.class);
                myintent.putExtra("realName", item.getRealName());
                myintent.putExtra("phoneNumber", item.getPhoneNumber());
                myintent.putExtra("passportId", item.getPassportId());
                myintent.putExtra("loginName", item.getLoginName());
                myintent.putExtra("deviceName", item.getDeviceName());
                myintent.putExtra("idNumber", item.getIdNumber());
                startActivity(myintent);
            }
        });

        refreshLoad = new RefreshLoad(PickPersonActivity.this, ptr, view, new RefreshLoadListener() {
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
            map.put("passportId",Cache.passport.getPassportIdStr());
            httpPost(map, Api.BasesupplychainRemoteURL() + "warehouse/getWarehousePassport", new HttpCallback<List<Passport>>("datas") {
                @Override
                public void onSuccess(List<Passport> data) {
                    try {
                        closeLoadingDialog();
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
                ActivityManager.finishActivity(PickPersonActivity.class);
                break;
        }
    }
}
