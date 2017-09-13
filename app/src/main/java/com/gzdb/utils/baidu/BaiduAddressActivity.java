package com.gzdb.utils.baidu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.gzdb.warehouse.R;
import com.gzdb.response.EditAddress;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.widget.AdapterModel;
import com.zhumg.anlib.widget.bar.SearchTitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public class BaiduAddressActivity extends AfinalActivity {


    @Bind(R.id.default_listview)
    ListView default_listview;

    @Bind(R.id.search_listview)
    ListView search_listview;

    @Bind(R.id.search_layout_txt)
    TextView errorTxt;

    @Bind(R.id.map_layout)
    View map_layout;

    @Bind(R.id.layout)
    ViewGroup layout;

    @Bind(R.id.point)
    ImageView point;

    // ////////////////////////////////

    String lontitude;
    String latitude;

    // 原位置相关
    List<BaiduPoiInfo> default_list;
    BaiduAddressAdapter default_adapter;

    // 百度搜索 POI 相关
    List<BaiduPoiInfo> search_list;
    BaiduAddressAdapter search_adapter;

    MapView mMapView;
    BaiduMap mBaiduMap;

    LocationAddress now_address;
    SearchTitleBar searchTitleBar;

    @Override
    public int getContentViewId() {
        return R.layout.activity_baidu_address;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();
        searchTitleBar = new SearchTitleBar(view);
        searchTitleBar.setLeftBack(this);

        // 百度搜索POI相关
        search_list = new ArrayList<BaiduPoiInfo>();
        search_adapter = new BaiduAddressAdapter(this, search_list);
        search_adapter.setUi_type(BaiduAddressAdapter.UI_TYPE_DEFAULT);

        search_listview.setAdapter(search_adapter);
        search_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                BaiduPoiInfo info = null;
                if (isSearchPoiUi) {
                    info = search_list.get(position);
                } else {
                    info = default_list.get(position - 1);//这里-1是因为在前面添加了一列当前地址
                }
                back(info);
                //Log.e("zhumg", "info=" + info);
                //addBack(info.city, info.name, info.address, info.latitude, info.longtitude);
            }
        });

        default_list = new ArrayList<BaiduPoiInfo>();
        default_adapter = new BaiduAddressAdapter(this, this.default_list);
        default_adapter.setUi_type(BaiduAddressAdapter.UI_TYPE_ICON);

        default_listview.setAdapter(default_adapter);
        default_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                BaiduPoiInfo info = null;
                if (isSearchPoiUi) {
                    info = search_list.get(position);
                } else {
                    info = default_list.get(position);
                }
                back(info);
                //Log.e("zhumg", "info=" + info);
                //addBack(info.city, info.name, info.address, info.latitude, info.longtitude);
            }
        });

        searchTitleBar.setEditTxtListener(new SearchTitleBar.EditTextListener() {
            @Override
            public void notifyInputTxt(String str) {
                if (str != null) {
                    // 调用进行搜索
                    map_layout.setVisibility(View.GONE);
                    default_listview.setVisibility(View.GONE);

                    search_listview.setVisibility(View.VISIBLE);
                    errorTxt.setVisibility(View.GONE);
                    isSearchPoiUi = true;
                    if (now_address == null) {
                        search_listview.setVisibility(View.GONE);
                        errorTxt.setVisibility(View.VISIBLE);
                    } else {
                        baiduPOI(str);
                    }
                } else {
                    isSearchPoiUi = false;
                    map_layout.setVisibility(View.VISIBLE);
                    default_listview.setVisibility(View.VISIBLE);

                    search_listview.setVisibility(View.GONE);
                    errorTxt.setVisibility(View.GONE);
                }
            }
        });

        baiduInit();

        now_address = BaiduLocationManager.getNowAddress();
        if (now_address != null) {
            handlerBaiduLocation();
            return;
        }

        // 起动位置定位
        BaiduLocationManager.startLocation(new BaiduLocationManager.BaiduLocationNotify() {
            @Override
            public void callNotify(LocationAddress location) {
                if (location != null) {
                    //Log.e("zhumg", "找到地址：" + location.toString());
                    now_address = location;
                    // 根据区位置搜索POI
                    handlerBaiduLocation();
                } else {
                    geo_runing = true;
                    Projection projection = mBaiduMap.getProjection();
                    int x = (point.getRight() + point.getLeft()) / 2;
                    int y = (point.getBottom() + point.getTop()) / 2;
                    Point p = new Point(x, y);
                    final LatLng lng = projection.fromScreenLocation(p);
                    lontitude = lng.longitude + "";
                    latitude = lng.latitude + "";
                    //Log.e("zhumg", "新的经玮度：" + lontitude + ", " + latitude);
                    mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(lng));
                    //ToastUtil.showToast(BaiduAddressActivity.this, "无法定位你当前位置");
                }
            }
        });
    }


    void handlerBaiduLocation() {

        latitude = now_address.getLatitude();
        lontitude = now_address.getLongtitude();

        // 根据区位置搜索POI
        LatLng lng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(lontitude));

        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(lng));

        // 滚动到目标位置
        LatLng nowLatlng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(lontitude));
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
                .newLatLng(nowLatlng);
        mBaiduMap.animateMapStatus(mapStatusUpdate);
    }

    @Override
    public void finish() {

        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSearch != null)
            this.mSearch.destroy();

        if (this.suggestionSearch != null)
            this.suggestionSearch.destroy();
    }

    /**
     * 百度相关初始化
     */
    private void baiduInit() {

        // 百度地图相关
        BaiduMapOptions baiduMapOptions = new BaiduMapOptions();
        baiduMapOptions.rotateGesturesEnabled(false);
        baiduMapOptions.overlookingGesturesEnabled(false);
        MapStatus ms = new MapStatus.Builder().zoom(17).build();
        baiduMapOptions.mapStatus(ms);

        mMapView = new MapView(this, baiduMapOptions);
        layout.addView(mMapView, 0);

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);
        mMapView.setClickable(true);
        // 传入null则，恢复默认图标
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, null));
        mBaiduMap.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                //Log.e("zhumg", " mBaiduMap.setOnMapTouchListener "
                //        + motionEvent.getAction());
                int action = motionEvent.getAction();
                switch (action) {
                    case 0:// 按下
                        break;
                    case 1:// 放起
                        if (geo_runing) {
                            return;
                        }
                        geo_runing = true;
                        Projection projection = mBaiduMap.getProjection();
                        int x = (point.getRight() + point.getLeft()) / 2;
                        int y = (point.getBottom() + point.getTop()) / 2;
                        Point p = new Point(x, y);
                        final LatLng lng = projection.fromScreenLocation(p);
                        lontitude = lng.longitude + "";
                        latitude = lng.latitude + "";
                        //Log.e("zhumg", "新的经玮度：" + lontitude + ", " + latitude);
                        mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                                .location(lng));
                        break;
                    case 2:// 拖动
                        break;
                }
            }
        });

        // 百度反向地理相关
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this.geoCoderResultListener);
    }

    private boolean poi_runing = false;
    private SuggestionSearch suggestionSearch;
    private OnGetSuggestionResultListener suggestionSearchListener = new OnGetSuggestionResultListener() {
        public void onGetSuggestionResult(SuggestionResult res) {
            poi_runing = false;
            if (res == null || res.getAllSuggestions() == null) {
                //Log.e("zhumg", "结果异常：：：：：");
                search_listview.setVisibility(View.GONE);
                errorTxt.setVisibility(View.VISIBLE);
                return;
            }
            // 获取在线建议检索结果
            poi_runing = false;
            search_list.clear();
            List<SuggestionResult.SuggestionInfo> infos = res
                    .getAllSuggestions();
            for (int i = 0; i < infos.size(); i++) {
                BaiduPoiInfo info = new BaiduPoiInfo();
                if (info.inSuggestionInfo(infos.get(i))) {
                    search_list.add(info);
                }
            }
            search_adapter.notifyDataSetChanged();
            if (search_list.size() < 1) {
                search_listview.setVisibility(View.GONE);
                errorTxt.setVisibility(View.VISIBLE);
            } else {
                search_listview.setVisibility(View.VISIBLE);
                errorTxt.setVisibility(View.GONE);
            }
            //Log.e("zhumg", "百度POI结果成功，size=" + res.getAllSuggestions().size());
        }
    };

    private void baiduPOI(String key) {
        if (poi_runing) {
            Log.e("zhumg", "运行中");
            return;
        }
        poi_runing = true;
        if (this.suggestionSearch == null) {
            this.suggestionSearch = SuggestionSearch.newInstance();
            this.suggestionSearch
                    .setOnGetSuggestionResultListener(suggestionSearchListener);
        }
        // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
        suggestionSearch.requestSuggestion((new
                SuggestionSearchOption()).city(now_address.getCity())
                .keyword(key));
    }

    // 当前显示 POI 的是那个UI
    private boolean isSearchPoiUi;
    // 百度反向地理编码
    private boolean geo_runing = false;
    private GeoCoder mSearch = null;
    private OnGetGeoCoderResultListener geoCoderResultListener = new OnGetGeoCoderResultListener() {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            Log.e("zhumg", "onGetGeoCodeResult");
        }

        @Override
        public void onGetReverseGeoCodeResult(
                ReverseGeoCodeResult reverseGeoCodeResult)
        {
            if (reverseGeoCodeResult == null || reverseGeoCodeResult.getPoiList() == null || reverseGeoCodeResult.getPoiList().size() < 1) {
                return;
            }
            // 根据当前新的key，进行搜索
            String result_city = reverseGeoCodeResult.getAddressDetail().city;
            default_list.clear();
            // 添加 当前地址
            BaiduPoiInfo now = new BaiduPoiInfo();
            now.address = reverseGeoCodeResult.getAddress();
            double latitude = reverseGeoCodeResult.getLocation().latitude;
            double longtitude = reverseGeoCodeResult.getLocation().longitude;

            now.latitude = latitude + "";// 精度
            now.longtitude = longtitude + "";// 纬度

            now.now = true;
            now.inAddressComponent(reverseGeoCodeResult.getAddressDetail());

            default_list.add(now);

            List<PoiInfo> poiInfos = reverseGeoCodeResult.getPoiList();
            for (int i = 0; i < poiInfos.size(); i++) {
                PoiInfo poiInfo = poiInfos.get(i);
                BaiduPoiInfo info = new BaiduPoiInfo();
                info.inPoiInfo(poiInfo);
                info.city = result_city;
                default_list.add(info);
            }
            if(now_address == null) {
                now_address = new LocationAddress();
                now_address.copyPoi(default_list.get(0));
            }
            default_adapter.notifyDataSetChanged();
            geo_runing = false;
        }
    };


    void back(BaiduPoiInfo poi) {
        EditAddress editAddress = new EditAddress();
        editAddress.inPoi(poi);
        Intent intent = new Intent();
        intent.putExtra("address", editAddress);
        setResult(1, intent);
        finish();
    }


    public static class BaiduPoiInfo implements AdapterModel{

        public String address;// 地址
        public String name;// 名称
        public String city;
        public String district;
        public String latitude;
        public String longtitude;
        public boolean now;

        public void inAddressComponent(
                ReverseGeoCodeResult.AddressComponent addressComponent) {
            this.name = addressComponent.street;
            this.city = addressComponent.city;
            this.district = addressComponent.district;
        }

        public boolean inSuggestionInfo(SuggestionResult.SuggestionInfo info) {
            this.name = info.key;
            this.city = info.city;
            this.district = info.district;
            if (info.pt == null) {
                //Log.e("zhumg", " 搜索百度位置，SuggestionResult is null, 没法找到经玮度!");
                this.latitude = "";
                this.longtitude = "";
                return false;
            } else {
                // double[] dt = BDTransUtil.gcj2bd(info.pt.latitude,
                // info.pt.longitude);
                // this.latitude = dt[0] + "";//精度
                // this.longtitude = dt[1] + "";//纬度

                // double[] dt = BDTransUtil.gcj2bd(info.pt.latitude,
                // info.pt.longitude);
                this.latitude = info.pt.latitude + "";// 精度
                this.longtitude = info.pt.longitude + "";// 纬度
            }
            // 过滤掉KEY里面的城市，区
            String regEx = "\\[" + city + district + "\\]";
            try {
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(this.name);
                String aaa = m.replaceAll("").trim();
                if (aaa.length() > 0) {
                    this.name = aaa;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            this.address = this.city + this.district + this.name;
            return true;
        }

        public void inPoiInfo(PoiInfo poiInfo) {
            this.city = poiInfo.city;
            this.name = poiInfo.name;
            this.address = poiInfo.address;
            this.latitude = poiInfo.location.latitude + "";
            this.longtitude = poiInfo.location.longitude + "";
        }

        public void inNow(LocationAddress locationAddress) {
            this.name = locationAddress.getStreet();
            this.city = locationAddress.getCity();
            this.address = locationAddress.getAddress();
            this.district = locationAddress.getDistrict();
            this.latitude = locationAddress.getLatitude();
            this.longtitude = locationAddress.getLongtitude();
            this.now = true;
        }

        public String toString() {
            return "inPoiInfo address=" + address + ", city=" + city
                    + ", name=" + name + ", latitude=" + latitude
                    + ", longtitude=" + longtitude;
        }

        @Override
        public int getUiType() {
            return 0;
        }
    }
}
