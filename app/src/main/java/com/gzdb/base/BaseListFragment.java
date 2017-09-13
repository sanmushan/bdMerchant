package com.gzdb.base;

import android.view.View;
import android.widget.ListView;

import com.gzdb.utils.Const;
import com.gzdb.utils.GlobalData;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by liyunbiao on 2017/7/13.
 */

public abstract class BaseListFragment<T>  extends AfinalFragment {



    public abstract Map getParam();

    public abstract String getUrl();

    public abstract int getContentLayoutViewId();

    public abstract HttpCallback httpCallback();


    int pageIndex = 1;
    public ListView listView;
    public RefreshLoad refreshLoad;
    PtrClassicFrameLayout ptr;
    public List<T> list;
    private BaseGenericAdapter<T> adapter;

    public void setAdapter(BaseGenericAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getContentViewId() {
        return getContentLayoutViewId();
    }

    @Override
    protected void initViewData(View view) {
        list = new ArrayList<>();
    }

    public void initListView(View view) {
        listView = (ListView) view.findViewById(R.id.fr_listview);
        ptr =(PtrClassicFrameLayout) view.findViewById(R.id.fr_ptr);

        if (listView != null) {

            listView.setAdapter(adapter);
            refreshLoad = new RefreshLoad(getActivity(), ptr, view, new RefreshLoadListener() {
                @Override
                public void onLoading(boolean over) {
                    if (!over) {
                        ptr.setVisibility(View.GONE);
                        initDataList();
                    } else {
                        ptr.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onRefresh(boolean over) {
                    if (!over) {
                        pageIndex = 1;
                        initDataList();
                    }
                }

                @Override
                public void onLoadmore(boolean over) {
                    if (!over) {
                        pageIndex++;
                        initDataList();
                    }
                }
            }, listView);
            refreshLoad.showLoading();
        }
    }

    private void initDataList() {
        Map map = getParam();
        map.put("pageIndex", String.valueOf(pageIndex));
        map.put("pageSize", Const.PAGESIZE);
        httpGet(getParam(), getUrl(), httpCallback());
    }

    public void Result(List<T> data) {
        if (refreshLoad.isLoadMore()) {
            list.addAll(data);
        } else {
            list.clear();
            list.addAll(data);
        }
        if (data == null) {
            refreshLoad.complete(false,data.isEmpty());
            return;
        }
        refreshLoad.complete(data.size() >= Const.PAGESIZE_INT,data.isEmpty());
        adapter.notifyDataSetChanged();
    }
}
