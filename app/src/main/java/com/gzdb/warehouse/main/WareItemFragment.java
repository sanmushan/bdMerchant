package com.gzdb.warehouse.main;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.response.Api;
import com.gzdb.response.SupplyItem;
import com.gzdb.warehouse.adapter.ItemAdapter;
import com.gzdb.warehouse.back.BackProductActivity;
import com.gzdb.warehouse.ware.WareShopCartActivity;
import com.gzdb.warehouse.ware.WareSearchItemActivity;
import com.gzdb.warehouse.ware.SupplyTypeActivity;
import com.gzdb.warehouse.ware.WareShopCart;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.widget.AdapterModel;
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
 * Created by Administrator on 2017/4/16 0016.
 */

public class WareItemFragment extends AfinalFragment implements View.OnClickListener {


    RefreshLoad refreshLoad;

    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;

    @Bind(R.id.fr_listview)
    ListView listView;

    @Bind(R.id.cart)
    View cart;

    @Bind(R.id.title_left)
    RelativeLayout title_left;

    @Bind(R.id.title_right)
    RelativeLayout title_right;

    @Bind(R.id.title_back)
    RelativeLayout title_back;

    @Bind(R.id.cart_num)
    TextView cart_num;
    @Bind(R.id.title_center_txt)
    TextView title_center_txt;

    @Bind(R.id.title_left_img)
    ImageView title_left_img;

    @Bind(R.id.title_right_img)
    ImageView title_right_img;

    ItemAdapter adapter;
    List<AdapterModel> items = new ArrayList<>();

    int pageIndex = 1;
    //选中的itemTypeId
    long selectTypeId;

    //点击显示了分类activity
    boolean show_type_activity = false;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_warehouse;
    }

    @Override
    protected void initViewData(View view) {

//        baseTitleBar = new BaseTitleBar(view);
//        baseTitleBar.setLeftImg(R.drawable.icon_classification);
//        baseTitleBar.setLeftListener(this);
//        baseTitleBar.setCenterTxt("我的仓库");
//        baseTitleBar.setRightImg(R.drawable.icon_search, this);

        title_center_txt.setText("我的仓库");
        title_left_img.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_classification));
        title_right_img.setImageDrawable(this.getResources().getDrawable(R.drawable.icon_search));

        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);
        title_back.setOnClickListener(this);

        adapter = new ItemAdapter(this.getActivity(), items, cart_num, false);
        listView.setAdapter(adapter);

        cart.setOnClickListener(this);

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

        cart_num.setText(String.valueOf(WareShopCart.getAllCount()));
        adapter.notifyDataSetChanged();

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
        int id = v.getId();
        if (id == R.id.title_left) {
            //标记打开了 分类 列表
            show_type_activity = true;
            Intent intent = new Intent(this.getActivity(), SupplyTypeActivity.class);
            intent.putExtra("buyer", false);
            getActivity().startActivityForResult(intent, 1);
        } else if (id == R.id.title_right) {
            ActivityManager.startActivity(this.getActivity(), WareSearchItemActivity.class);
        } else if (id == R.id.cart) {
            ActivityManager.startActivity(this.getActivity(), WareShopCartActivity.class);
        }else  if(id==title_back.getId()){
            ActivityManager.startActivity(this.getActivity(), BackProductActivity.class);
        }
    }
}
