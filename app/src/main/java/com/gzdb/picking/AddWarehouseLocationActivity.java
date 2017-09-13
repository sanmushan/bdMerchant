package com.gzdb.picking;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gzdb.picking.adapter.ProductAdpater;
import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.BackOrder;
import com.gzdb.response.Item;
import com.gzdb.response.SupplyItem;
import com.gzdb.warehouse.App;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.adapter.BackProductAdapter;
import com.gzdb.warehouse.back.BackProductActivity;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
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
 * Created by liyunbiao on 2017/8/9.
 */

public class AddWarehouseLocationActivity extends AfinalActivity implements View.OnClickListener {
    //选中的itemTypeId
    BaseTitleBar baseTitleBar;
    long selectTypeId;
    @Bind(R.id.edit_input)
    EditText edit_input;
    @Bind(R.id.tv_location)
    TextView tv_location;
    RefreshLoad refreshLoad;
    List<Item> items = new ArrayList<>();
    ProductAdpater adapter;
    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;
    @Bind(R.id.fr_listview)
    ListView listView;
    int pageIndex = 1;

    @Bind(R.id.base_encode)
    LinearLayout base_encode;
    @Bind(R.id.base_body)
    LinearLayout base_body;
    private String locationNum = "";
    private String encode = "";
    Boolean isnew = true;

    @Override
    public int getContentViewId() {
        return R.layout.activity_add_warehouse_location;
    }

    @Override
    public void initView(View view) {

        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("商品库位绑定");
        baseTitleBar.setLeftListener(this);
        base_encode.setVisibility(View.VISIBLE);
        base_body.setVisibility(View.GONE);

        edit_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try {

                    String str = s.toString().trim();
                    if (str.length() == 0) {
                        return;
                    }
                    if (str.indexOf("-") != -1) {
                        if (locationNum.equals(str)) {
                            isnew = true;
                        } else {
                            if (locationNum != null && locationNum.length() > 0) {
                                isnew = false;
                            }
                            locationNum = str;
                            tv_location.setText(str);
                        }
                        refreshLoad.showLoading();
                    }
                    if (base_encode.getVisibility() == View.VISIBLE) {
                        // tv_location.setText(str);
                        locationNum = str;

                    } else {
                        if (isnew) {
                            encode = str;
                            if (str.indexOf("-") == -1) {
                                addProduct();
                            }
                        }
                        isnew = true;

                    }
                    edit_input.setText("");
                    base_encode.setVisibility(View.GONE);
                    base_body.setVisibility(View.VISIBLE);
                    items.clear();
                    adapter.notifyDataSetChanged();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        edit_input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    edit_input.requestFocus();
                    edit_input.setFocusableInTouchMode(true);
                }
            }
        });
        adapter = new ProductAdpater(AddWarehouseLocationActivity.this, items);
        listView.setAdapter(adapter);
        adapter.setOnClickListenerDel(new ProductAdpater.OnClickListenerDel() {
            @Override
            public void setOnClickListener(Item item, int i) {
                delProduct(item.getBarcode(), i);
            }
        });
        refreshLoad = new RefreshLoad(AddWarehouseLocationActivity.this, ptr, view, new RefreshLoadListener() {
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


    }

    private void delProduct(String encode, final int pos) {
        httpPost(ApiRequest.removeBinding(encode, locationNum), Api.BasesupplychainRemoteURL() + "warehouse/removeBinding", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                try {
                    items.remove(pos);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //  ToastUtil.showToast(AddWarehouseLocationActivity.this,msg);
            }

            @Override
            public void onFailure() {
                super.onFailure();

                // ToastUtil.showToast(AddWarehouseLocationActivity.this,msg);
            }
        });
    }


    public void addProduct() {
        if (encode == null || encode.length() == 0) {
            return;
        }
        httpPost(ApiRequest.itemAndWarehouseStor(encode, locationNum), Api.BasesupplychainRemoteURL() + "warehouse/itemAndWarehouseStorageLocationBound", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                encode = "";
                edit_input.setText("");
                edit_input.requestFocus();
                edit_input.setFocusableInTouchMode(true);
                refreshLoad.showLoading();
            }

            @Override
            public void onFailure() {
                super.onFailure();
                refreshLoad.showLoading();
                edit_input.setText("");
                edit_input.requestFocus();
                edit_input.setFocusableInTouchMode(true);
            }
        });
    }

    public void initData() {
        try {

            if (locationNum == null || locationNum.length() == 0) {
                items.clear();

                refreshLoad.complete(false, items.isEmpty());
                adapter.notifyDataSetChanged();
                return;
            }
            refreshLoad.complete(false, items.isEmpty());
            httpPostNoShow(ApiRequest.queryAllItemByStorageLocationNum(locationNum), Api.BasesupplychainRemoteURL() + "warehouse/queryAllItemByStorageLocationNum", new HttpCallback<List<Item>>("datas") {
                @Override
                public void onSuccess(List<Item> data) {

                    try {
                        items.clear();
                        if(data==null||data.size()==0){
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
                    //ToastUtil.showToast(AddWarehouseLocationActivity.this,msg);
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
                ActivityManager.finishActivity(AddWarehouseLocationActivity.class);
                break;
        }
    }
}
