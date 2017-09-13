package com.gzdb.zwarehouse;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.gzdb.response.Api;
import com.gzdb.response.SupplyItem;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.ware.SupplyTypeActivity;
import com.gzdb.warehouse.ware.WareShopCartActivity;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalFragment;
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
 * Created by zhumg on 4/24.
 */
public class ZWarehouseItemFragment extends AfinalFragment implements View.OnClickListener {

    BaseTitleBar baseTitleBar;
    RefreshLoad refreshLoad;

    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;

    @Bind(R.id.fr_listview)
    ListView listView;

    ZWareItemAdapter adapter;
    List<SupplyItem> items;

    int pageIndex = 1;
    //选中的itemTypeId
    long selectTypeId;

    //点击显示了分类activity
    boolean show_type_activity = false;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_zware_item;
    }

    @Override
    protected void initViewData(View view) {

        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftImg(R.drawable.icon_classification);
        baseTitleBar.setLeftListener(this);
        baseTitleBar.setCenterTxt("我的仓库");
        baseTitleBar.setRightImg(R.drawable.icon_search, this);

        items = new ArrayList<>();
        adapter = new ZWareItemAdapter(this.getActivity(), items);
        listView.setAdapter(adapter);

        refreshLoad = new RefreshLoad(getActivity(), ptr, view, new RefreshLoadListener() {
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

    @Override
    public void onVisible() {
        //如果点击显示了分类
        if (show_type_activity) {
            show_type_activity = false;
            if (SupplyTypeActivity.click_select_type_id != 0 && selectTypeId != SupplyTypeActivity.click_select_type_id) {
                selectTypeId = SupplyTypeActivity.click_select_type_id;
                refreshLoad.showLoading();
            }
        }
    }

    void startGetItems() {

        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("itemTypeId", String.valueOf(selectTypeId));
        map.put("roleType", String.valueOf(Cache.clientTypeEnum.getKey()));
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", String.valueOf(10));

        Http.post(this.getActivity(), map, Api.WAREHOUSE_ITEMS, new HttpCallback<List<SupplyItem>>("datas") {

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == com.zhumg.anlib.R.id.title_left) {
            //标记打开了 分类 列表
            show_type_activity = true;
            Intent intent = new Intent(this.getActivity(), SupplyTypeActivity.class);
            intent.putExtra("buyer", false);
            getActivity().startActivityForResult(intent, 1);
        } else if (id == R.id.title_right) {
            ActivityManager.startActivity(this.getActivity(), ZWareSearchItemActivity.class);
        } else if (id == R.id.cart) {
            ActivityManager.startActivity(this.getActivity(), WareShopCartActivity.class);
        }
    }
}
