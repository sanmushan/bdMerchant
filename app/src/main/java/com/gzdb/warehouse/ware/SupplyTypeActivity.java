package com.gzdb.warehouse.ware;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gzdb.response.HomePageRecommendItemTypes;
import com.gzdb.utils.baidu.BaiduLocationManager;
import com.gzdb.utils.baidu.LocationAddress;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.widget.AfinalAdapter;
import com.zhumg.anlib.widget.bar.BaseTitleBar;
import com.zhumg.anlib.widget.mvc.LoadingView;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by zhumg on 4/19.
 */
public class SupplyTypeActivity extends AfinalActivity {

    public static long click_select_type_id = 0;

    @Bind(R.id.type_ll)
    View type_ll;

    @Bind(R.id.supply_list)
    ListView listview;

    @Bind(R.id.supply_gridview)
    GridView gridview;

    @Bind(R.id.ptr)
    PtrClassicFrameLayout ptr;

    LeftAdapter leftAdapter;
    List<HomePageRecommendItemTypes> leftTypes;

    RightAdapter rightAdapter;
    List<HomePageRecommendItemTypes> rightTypes;

    // 上一次选中的左边项
    int selectLeftIndex = -1;

    int selectItemTypeColor;
    int defaultItemTypeColor;
    int defaultBgColor;
    int selectBgColor;

    LoadingView loadingView;
    BaseTitleBar titleBar;
    RefreshLoad refreshLoad;

    //定位地址
    String location = null;
    //商品分类 类型
    boolean buyer_bool = false;//true采购员商品分类，false仓管商品分类

    Cache.CacheCallback leftHttpCallback = new Cache.CacheCallback<List<HomePageRecommendItemTypes>>() {
        @Override
        public void onSuccess(List<HomePageRecommendItemTypes> types) {
            if (types == null || types.size() < 1) {
                // 显示为空
                loadingView.showLoadingTxt("没有商品分类");
                return;
            }
            //隐藏
            refreshLoad.complete(false, false);
            // 是否有设置过
            boolean setselect_bool = false;
            // 设置左边
            leftTypes.clear();

            for (int i = 0; i < types.size(); i++) {
                HomePageRecommendItemTypes type = types.get(i);
                if (type.getSubItemTypes() == null || type.getSubItemTypes().size() < 1) {
                    continue;
                }
                type.setSelect(false);
                leftTypes.add(type);

                if (selectLeftIndex == i) {
                    // 存在内容
                    type.setSelect(true);
                    setselect_bool = true;
                }
            }
            // 设置左边
            if (!setselect_bool) {
                // 默认选中0项
                leftTypes.get(0).setSelect(true);
                selectLeftIndex = 0;
            }
            leftAdapter.notifyDataSetChanged();
            setRight();
        }

        @Override
        public void onFailure(int code, String msg) {
            refreshLoad.showError(msg);
        }
    };

    @Override
    public int getContentViewId() {
        return R.layout.activity_item_type;
    }

    @Override
    public void initView(View view) {

        setTranslucentStatus();
        buyer_bool = getIntent().getBooleanExtra("buyer", false);

        //默认清除
        click_select_type_id = 0;

        loadingView = new LoadingView(view);

        titleBar = new BaseTitleBar(view);
        titleBar.setCenterTxt("商品分类");
        titleBar.setLeftBack(this);
        titleBar.setRightTxt("全部", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_select_type_id = 0;
                finish();
            }
        });

        leftTypes = new ArrayList<>();
        rightTypes = new ArrayList<>();

        leftAdapter = new LeftAdapter(this, leftTypes);
        rightAdapter = new RightAdapter(this, rightTypes);

        listview.setAdapter(leftAdapter);
        gridview.setAdapter(rightAdapter);

        selectItemTypeColor = this.getResources().getColor(R.color.blue);
        defaultItemTypeColor = this.getResources().getColor(R.color.font_6);

        defaultBgColor = this.getResources().getColor(R.color.transparent);
        selectBgColor = this.getResources().getColor(R.color.white);

        refreshLoad = new RefreshLoad(SupplyTypeActivity.this, ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (over) {
                    ptr.setVisibility(View.VISIBLE);
                } else {
                    ptr.setVisibility(View.GONE);
                    if (buyer_bool) {
                        //直接加载数据
                        startGetItemTypes();
                    } else {
                        if (location == null) {
                            //定位
                            startLocation();
                        } else {
                            //直接加载数据
                            startGetItemTypes();
                        }
                    }
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    startGetItemTypes();
                }
            }
        });

        if (buyer_bool) {
            refreshLoad.showLoading();
        } else {
            LocationAddress locationAddress = BaiduLocationManager.getNowAddress();
            //如果需要定位
            if (locationAddress == null) {
                //启动定位
                refreshLoad.showLoading("正在定位...");
            } else {
                location = locationAddress.getLocation();
                //直接加载数据
                refreshLoad.showLoading();
            }
        }
    }

    boolean location_bool = false;

    void startLocation() {
        BaiduLocationManager.startLocation(new BaiduLocationManager.BaiduLocationNotify() {
            @Override
            public void callNotify(LocationAddress address) {
                if (address != null) {
                    location = address.getLocation();
                } else {
                    //没有找到定位，则显示
                    //refreshLoad.showError("当前无法定位");
                    //return;
                    location = "";
                }
                //访问服务器
                if (!location_bool) {
                    location_bool = true;
                    refreshLoad.getLoadingView().showLoading();
                    startGetItemTypes();
                }
            }
        });
    }

    void startGetItemTypes() {
        if (buyer_bool) {
            Cache.getBuyerTypes(SupplyTypeActivity.this,
                    Cache.passport.getPassportId(), SupplyTypeActivity.this.leftHttpCallback);
        } else {
            Cache.getTypes(SupplyTypeActivity.this,
                    Cache.passport.getPassportId(), location, SupplyTypeActivity.this.leftHttpCallback);
        }
    }

    class LeftAdapter extends AfinalAdapter<HomePageRecommendItemTypes> implements View.OnClickListener {

        public LeftAdapter(Context context, List<HomePageRecommendItemTypes> list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = View.inflate(SupplyTypeActivity.this, R.layout.widget_left_itemtype, null);
            }

            HomePageRecommendItemTypes type = getItem(position);

            TextView name_txt = (TextView) convertView.findViewById(R.id.it_name);
            name_txt.setText(type.getItemTypeName());
            convertView.setOnClickListener(this);
            convertView.setTag(position);

            View v = convertView.findViewById(R.id.it_select);

            if (type.isSelect()) {
                name_txt.setTextColor(selectItemTypeColor);
                v.setVisibility(View.VISIBLE);
                convertView.setBackgroundColor(selectBgColor);
            } else {
                name_txt.setTextColor(defaultItemTypeColor);
                v.setVisibility(View.GONE);
                convertView.setBackgroundColor(defaultBgColor);
            }

            return convertView;
        }

        @Override
        public void onClick(View arg0) {

            int oldIndex = selectLeftIndex;
            if (oldIndex != -1) {
                getItem(oldIndex).setSelect(false);
            }
            selectLeftIndex = (int) arg0.getTag();
            getItem(selectLeftIndex).setSelect(true);

            leftAdapter.notifyDataSetChanged();

            setRight();
        }
    }


    class RightAdapter extends AfinalAdapter<HomePageRecommendItemTypes> implements View.OnClickListener {

        public RightAdapter(Context context, List<HomePageRecommendItemTypes> list) {
            super(context, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                convertView = View.inflate(SupplyTypeActivity.this, R.layout.adapter_supply_headerbutton, null);
            }

            convertView.setTag(position);

            HomePageRecommendItemTypes type = getItem(position);

            TextView name_txt = (TextView) convertView.findViewById(R.id.tv_title);
            name_txt.setText(type.getItemTypeName());

            ImageView v = (ImageView) convertView.findViewById(R.id.iv_image);

            ImageLoader.getInstance().displayImage(type.getItemTypeImage(), v);

            convertView.setOnClickListener(this);

            return convertView;
        }

        @Override
        public void onClick(View arg0) {

            int position = (int) arg0.getTag();
            click_select_type_id = getItem(position).getItemTypeId();

            finish();
        }

    }

    void setRight() {
        if (selectLeftIndex < 0 || selectLeftIndex >= leftTypes.size())
            return;
        HomePageRecommendItemTypes type = leftTypes.get(selectLeftIndex);
        List<HomePageRecommendItemTypes> types = type.getSubItemTypes();
        rightTypes.clear();
        if (types == null || types.size() < 1)
            return;
        for (int i = 0; i < types.size(); i++) {
            rightTypes.add(types.get(i));
        }
        rightAdapter.notifyDataSetChanged();
    }
}
