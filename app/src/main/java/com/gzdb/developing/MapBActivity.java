package com.gzdb.developing;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.gzdb.overlayutil.PoiOverlay;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.AfinalActivity;

import java.util.ArrayList;

import butterknife.Bind;

public class MapBActivity extends AfinalActivity implements OnGetPoiSearchResultListener, View.OnClickListener {
//    SearchTitleBar searchTitleBar;
    @Bind(R.id.title_left)
    RelativeLayout title_left;
    @Bind(R.id.title_center_txt)
    EditText keyWork;
    @Bind(R.id.title_right)
    TextView title_right;


    @Bind(R.id.bmapView)
    MapView mapView = null;
    BaiduMap mBaiduMap;
    @Bind(R.id.lv_location_nearby)
    ListView mListView;
    @Bind(R.id.request)
    Button mRequestLocation;
    boolean isFirstLoc = true; // 是否首次定位

    PoiCitySearchOption poiCitySearchOption;
    String poiName ;
    String poiAddress ;
    PoiInfo info;

    //搜索周边
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    /***定位SDK的核心类*/
    public LocationClient mLocationClient = null;
    /**
     * 当前标记
     **/
    private Marker mCurrentMarker;
    //定位图标
    private BitmapDescriptor currentMarker;
    public BDLocationListener myListener = new MyLocationListener();
    private ArrayList<PoiInfo> dataList;
    private ListAdapter adapter;
    private int locType;
    private double longitude;// 精度
    private double latitude;// 维度
    private float radius;// 定位精度半径，单位是米
    private String addrStr;// 反地理编码
    private String province;// 省份信息
    private String city="广州";// 城市信息
    private String district;// 区县信息
    private float direction;// 手机方向信息
    private int checkPosition;

    // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
    // 注意该方法要再setContentView方法之前实现


    @Override
    public int getContentViewId() {
        return R.layout.activity_map_b;
    }

    @Override
    public void initView(View view) {
        SDKInitializer.initialize(getApplicationContext());
//        searchTitleBar = new SearchTitleBar(view);
//        searchTitleBar.setTextHint("请输入地址关键字");
//        searchTitleBar.setRightTxt("搜索");
//        searchTitleBar.setLeftBack(this);
        title_left.setOnClickListener(this);
        title_right.setOnClickListener(this);

        dataList = new ArrayList<PoiInfo>();
        checkPosition = 0;
        adapter = new ListAdapter(0);
        mListView.setAdapter(adapter);


        initEvent();
        initLocation();
//        search();




    }

    /**
     * 搜索，未做
     */
//    private void search() {
////        创建在线建议查询实例
//        mSuggestionSearch = SuggestionSearch.newInstance();
////        创建在线建议查询监听者
//        OnGetSuggestionResultListener listener = new OnGetSuggestionResultListener() {
//            public void onGetSuggestionResult(SuggestionResult res) {
//                if (res == null || res.getAllSuggestions() == null) {
//                    Toast.makeText(MapBActivity.this, "未找到相关结果", Toast.LENGTH_SHORT).show();
//                    return;
//                    //未找到相关结果
//                }
//                //获取在线建议检索结果
//                Toast.makeText(MapBActivity.this, res.toString(), Toast.LENGTH_SHORT).show();
//            }
//        };
////        发起在线建议查询
//        // 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
//        mSuggestionSearch.requestSuggestion((new SuggestionSearchOption())
//                .keyword(keyWork.getText().toString())
//                .city(city));
////        释放在线建议查询实例
//        mSuggestionSearch.destroy();
//    }


    //定位
    private void initLocation() {
        //重新设置
        checkPosition = 0;
        adapter.setCheckposition(0);
        mBaiduMap = mapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);//普通地图
        mBaiduMap.setTrafficEnabled(true);//交通图
        mBaiduMap.clear();
//        dataList.clear();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(17).build()));   // 设置级别

        // 定位初始化
        mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
        mLocationClient.registerLocationListener(myListener);// 注册定位监听接口

        /**
         * 设置定位参数
         */
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
//		option.setScanSpan(5000);// 设置发起定位请求的间隔时间,ms
        option.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向
        option.setOpenGps(true);
        option.setAddrType("all");// 返回的定位结果包含地址信息
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        mLocationClient.setLocOption(option);
        mLocationClient.start(); // 调用此方法开始定位


    }

    /**
     * 事件初始化
     */
    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                checkPosition = position;
                adapter.setCheckposition(position);
                adapter.notifyDataSetChanged();
                PoiInfo ad = (PoiInfo) adapter.getItem(position);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ad.location);
                mBaiduMap.animateMapStatus(u);
                mCurrentMarker.setPosition(ad.location);

                /**
                 * 选择地址跳转回上一级界面
                 */
                Intent intent = new Intent();
                intent.putExtra("name", dataList.get(checkPosition).name);
                intent.putExtra("latitude", dataList.get(checkPosition).location.latitude);
                intent.putExtra("longitude", dataList.get(checkPosition).location.longitude);
                intent.putExtra("address", dataList.get(checkPosition).address);
                intent.putExtra("op","address");
                setResult(RESULT_OK, intent);
                MapBActivity.this.finish();


            }
        });
        //地图中得搜索image
        mRequestLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//              ToastUtil.showToast(getApplicationContext(), "名称是: " + dataList.get(checkPosition).name + " 地址是：" + dataList.get(checkPosition).address);
                dataList.clear();
                adapter.notifyDataSetChanged();
                initLocation();


            }
        });


    }
// 当点击覆盖物的时候,查询详细的数据信息,作为回调返回数据信息
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_right){
            //开始搜索定位
            try {
                if(city==null){
                    city="广州市";
                }
                poiCitySearchOption = new PoiCitySearchOption().city(city).keyword(keyWork.getText().toString());
                mPoiSearch.searchInCity(poiCitySearchOption);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            search();

        }else if (id == R.id.title_left){
            //返回上一级
            finish();
        }

    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation == null || mapView == null) {
                return;
            }
            if ("4.9E-324".equals(bdLocation.getLongitude()) || "4.9E-324".equals(bdLocation.getLatitude()))
                return;//百度地图定位失败的默认值

            locType = bdLocation.getLocType();
            Log.i("百度定位", "当前定位的返回值是：" + locType);
            longitude = bdLocation.getLongitude();
            latitude = bdLocation.getLatitude();
            if (bdLocation.hasRadius()) {
                //判断是否有定位精度半径
                radius = bdLocation.getRadius();
            }
            if (locType == BDLocation.TypeNetWorkLocation) {
                addrStr = bdLocation.getAddrStr();// 获取反地理编码(文字描述的地址)
                Log.i("mybaidumap", "当前定位的地址是：" + addrStr);
            }
            direction = bdLocation.getDirection();// 获取手机方向，【0~360°】,手机上面正面朝北为0°
            province = bdLocation.getProvince();// 省份
            city = bdLocation.getCity();// 城市
            district = bdLocation.getDistrict();// 区县

            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            //将当前位置加入List里面
            info = new PoiInfo();
            info.address = bdLocation.getAddrStr();
            info.city = bdLocation.getCity();
            info.location = ll;
            info.name = bdLocation.getAddrStr();
//            dataList.clear();
//            dataList.add(info);
//            adapter.notifyDataSetChanged();
            Log.i("mybaidumap", "province是：" + province + " city是" + city + " 区县是: " + district);


            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
            //画标志
            CoordinateConverter converter = new CoordinateConverter();
            converter.coord(ll);
            converter.from(CoordinateConverter.CoordType.COMMON);
            LatLng convertLatLng = converter.convert();

            currentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
            OverlayOptions overlayOptions = new MarkerOptions().position(ll).icon(currentMarker);
            mBaiduMap.addOverlay(overlayOptions);
            mCurrentMarker = (Marker) mBaiduMap.addOverlay(overlayOptions);




            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(convertLatLng, 17.0f);
            mBaiduMap.animateMapStatus(u);

            //画当前定位标志
            MapStatusUpdate uc = MapStatusUpdateFactory.newLatLng(ll);
            mBaiduMap.animateMapStatus(uc);

            mapView.showZoomControls(false);
            //poi 搜索周边
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Looper.prepare();
                    searchNeayBy();
                    Looper.loop();
                }
            }).start();

        }

        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null) {
                return;
            }
        }
    }

    /**
     * 搜索周边
     */
    private void searchNeayBy() {
        // POI初始化搜索模块，注册搜索事件监听

        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        PoiNearbySearchOption poiNearbySearchOption = new PoiNearbySearchOption();

        poiNearbySearchOption.keyword("公司");
        poiNearbySearchOption.location(new LatLng(latitude, longitude));
        poiNearbySearchOption.radius(500);  // 检索半径，单位是米
        poiNearbySearchOption.pageCapacity(25);  // 默认每页20条
        mPoiSearch.searchNearby(poiNearbySearchOption);  // 发起附近检索请求
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.i("----------------", "---------------------");
                    adapter.notifyDataSetChanged();
                    break;

                default:
                    break;
            }
        }
    };

    /*
     *  * 接受周边地理位置结果
    */
    @Override
    public void onGetPoiResult(PoiResult result) {
        // 获取POI检索结果
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
            Toast.makeText(this, "未找到结果", Toast.LENGTH_LONG).show();
            return;
        }

        if (result.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
            mBaiduMap.clear();
            dataList.clear();
            dataList.add(info);
            if (result != null) {

                if (keyWork.getText().toString() != null){

                     poiName = result.getAllPoi().get(0).name;
                     poiAddress = result.getAllPoi().get(0).address;
                    dataList.addAll(result.getAllPoi());

                    // 将poi结果显示到地图上  
                    PoiOverlay poiOverlay = new PoiOverlay(mBaiduMap);
                    mBaiduMap.setOnMarkerClickListener(poiOverlay);
                    poiOverlay.setData(result);
                    poiOverlay.addToMap();
                    poiOverlay.zoomToSpan();
                    return;

                }else if(result.getAllPoi() != null && result.getAllPoi().size() > 0) {
                    dataList.addAll(result.getAllPoi());
                }else{
                    return;
                }
                Message msg = new Message();
                msg.what = 0;
                handler.sendMessage(msg);
            }
        }
    }

    /**
     * 用于显示poi的overly
     *
     * @author Administrator
     */
//    private class MyPoiOverlay extends PoiOverlay {
//
//
//        public MyPoiOverlay(BaiduMap baiduMap) {
//            super(baiduMap);
//        }
//
//        @Override
//        public boolean onPoiClick(int index) {
//            super.onPoiClick(index);
//            PoiInfo poiInfo = getPoiResult().getAllPoi().get(index);
//            mPoiSearch.searchPoiDetail(new PoiDetailSearchOption().poiUid(poiInfo.uid));
//            return true;
//        }
//    }


    class ListAdapter extends BaseAdapter {

        private int checkPosition;

        public ListAdapter(int checkPosition) {
            this.checkPosition = checkPosition;
        }

        public void setCheckposition(int checkPosition) {
            this.checkPosition = checkPosition;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(MapBActivity.this).inflate(R.layout.item_map, null);

                holder.textView = (TextView) convertView.findViewById(R.id.text_name);
                holder.textAddress = (TextView) convertView.findViewById(R.id.text_address);
                holder.imageLl = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
//            显示公司名和地址
            Log.i("mybaidumap", "name地址是：" + dataList.get(position).name);
            Log.i("mybaidumap", "address地址是：" + dataList.get(position).address);

            if (dataList.get(position).name.equals(null)||dataList.get(position).address.equals(null)){
                Log.e("","网络不稳定");
            }else {
                holder.textView.setText(dataList.get(position).name);
                holder.textAddress.setText(dataList.get(position).address);
            }



            if (checkPosition == position) {
                holder.imageLl.setVisibility(View.VISIBLE);
            } else {
                holder.imageLl.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }

    }

    class ViewHolder {
        TextView textView;
        TextView textAddress;
        ImageView imageLl;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mPoiSearch!=null){
            mPoiSearch.destroy();
        }
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        if (mLocationClient != null) {
            mLocationClient.stop();
        }

        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);

        if(mPoiSearch!=null){
            mPoiSearch.destroy();
        }

        // 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }


}

