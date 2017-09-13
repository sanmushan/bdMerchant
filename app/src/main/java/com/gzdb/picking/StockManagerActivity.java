package com.gzdb.picking;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gzdb.picking.adapter.ProductAdpater;
import com.gzdb.picking.adapter.StockManagerAdapter;
import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.Item;
import com.gzdb.response.ItemStock;
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
 * Created by liyunbiao on 2017/8/23.
 */

public class StockManagerActivity extends AfinalActivity implements View.OnClickListener {
    //选中的itemTypeId
    BaseTitleBar baseTitleBar;
    long selectTypeId;
    @Bind(R.id.edit_input)
    EditText edit_input;
    @Bind(R.id.tv_location)
    TextView tv_location;
    RefreshLoad refreshLoad;
    List<ItemStock> items = new ArrayList<>();
    StockManagerAdapter adapter;
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
        return R.layout.activity_stock_manager;
    }

    @Override
    public void initView(View view) {

        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("盘点");
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
                    locationNum = str;
                    refreshLoad.showLoading();
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
        adapter = new StockManagerAdapter(StockManagerActivity.this, items);
        listView.setAdapter(adapter);
        adapter.setOnClickListenerDel(new StockManagerAdapter.OnClickListenerDel() {
            @Override
            public void setOnClickListener(ItemStock item, String nStock) {
//                if (item.getStock().equals(nStock)) {
//                    ToastUtil.showToast(StockManagerActivity.this, "盘点库存和系统库存一致？");
//                    return;
//                }
                changeStock(item.getItemId()+"", item.getItemTemplateId(), item.getStock(), nStock, nStock);
            }
        });
        refreshLoad = new RefreshLoad(StockManagerActivity.this, ptr, view, new RefreshLoadListener() {
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


    public void changeStock(String itemId, String itemTemplateId, String beforeStock, String afterStock, String itemQuantity) {
        httpPost(ApiRequest.changeStock(itemId, itemTemplateId, beforeStock, afterStock, itemQuantity), Api.BasesupplychainRemoteURL() + "item/changeStock", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                encode = "";
                edit_input.setText("");
                edit_input.requestFocus();
                edit_input.setFocusableInTouchMode(true);
                refreshLoad.showLoading();
                ToastUtil.showToast(StockManagerActivity.this, msg);
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
            Map<String, String> map = new HashMap<>();
            map.put("passportId", Cache.passport.getPassportIdStr());
            map.put("barcode", locationNum);
            httpPostNoShow(map, Api.BasesupplychainRemoteURL() + "item/getItemForBarcodeForWarehoue", new HttpCallback<ItemStock>() {
                @Override
                public void onSuccess(ItemStock data) {

                    try {
                        items.clear();

                        items.add(data);
                        refreshLoad.complete(false, items.isEmpty());
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
                ActivityManager.finishActivity(StockManagerActivity.class);
                break;
        }
    }
}
