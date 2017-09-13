package com.gzdb.picking;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gzdb.picking.adapter.StockManagerAdapter;
import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.ItemStock;
import com.gzdb.response.OrderDetail;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.main.WareOrderFragment;
import com.gzdb.warehouse.order.OrderDetailActivity;
import com.gzdb.widget.ScanActivity;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
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
 * Created by liyunbiao on 2017/9/2.
 */

public class StockMobileActivity extends AfinalActivity implements View.OnClickListener {
    //选中的itemTypeId
    BaseTitleBar baseTitleBar;
    long selectTypeId;
    @Bind(R.id.tv_location)
    TextView tv_location;
    RefreshLoad refreshLoad;
    List<ItemStock> items = new ArrayList<>();
    StockManagerAdapter adapter;
    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;
    @Bind(R.id.fr_listview)
    ListView listView;
    @Bind(R.id.edit_input)
    EditText edit_input;
    int pageIndex = 1;

    @Bind(R.id.base_encode)
    LinearLayout base_encode;
    @Bind(R.id.base_body)
    LinearLayout base_body;
    private String locationNum = "";
    private String encode = "";
    Boolean isnew = true;
    @Bind(R.id.tv_finish)
    TextView tv_finish;

    @Bind(R.id.tv_sel)
    Button tv_sel;
    ScanActivity.ScanCallback scanCallback;

    @Override
    public int getContentViewId() {
        return R.layout.activity_stock_mobile;
    }

    @Override
    public void initView(View view) {

        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("盘点");
        baseTitleBar.setLeftListener(this);
        base_encode.setVisibility(View.VISIBLE);
        base_body.setVisibility(View.GONE);

        adapter = new StockManagerAdapter(StockMobileActivity.this, items);
        listView.setAdapter(adapter);
        adapter.setOnClickListenerDel(new StockManagerAdapter.OnClickListenerDel() {
            @Override
            public void setOnClickListener(ItemStock item, String nStock) {
//                if (item.getStock().equals(nStock)) {
//                    ToastUtil.showToast(StockMobileActivity.this, "盘点库存和系统库存一致？");
//                    return;
//                }
                changeStock(item.getItemId() + "", item.getItemTemplateId(), item.getStock(), nStock, nStock);
            }
        });
        refreshLoad = new RefreshLoad(StockMobileActivity.this, ptr, view, new RefreshLoadListener() {
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
        tv_finish.setOnClickListener(this);
        tv_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationNum = edit_input.getText().toString().trim();
                refreshLoad.showLoading();
                base_encode.setVisibility(View.GONE);
                base_body.setVisibility(View.VISIBLE);
                items.clear();
                adapter.notifyDataSetChanged();
            }
        });
        //扫码
        scanCallback = new ScanActivity.ScanCallback() {
            @Override
            public void onScanSuccess(String code) {
                String str = code.toString().trim();
                edit_input.setText(str);
                if (str.length() == 0) {
                    return;
                }
                locationNum = str;
                refreshLoad.showLoading();
                base_encode.setVisibility(View.GONE);
                base_body.setVisibility(View.VISIBLE);
                items.clear();
                adapter.notifyDataSetChanged();
            }
        };
    }


    public void changeStock(String itemId, String itemTemplateId, String beforeStock, String afterStock, String itemQuantity) {
        httpPost(ApiRequest.changeStock(itemId, itemTemplateId, beforeStock, afterStock, itemQuantity), Api.BasesupplychainRemoteURL() + "item/changeStock", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                encode = "";
                refreshLoad.showLoading();
                ToastUtil.showToast(StockMobileActivity.this, msg);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                refreshLoad.showLoading();
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
                ActivityManager.finishActivity(StockMobileActivity.class);
                break;
            case R.id.tv_finish:
                ScanActivity.setScanCallback(scanCallback);
                Intent intent = new Intent(StockMobileActivity.this, ScanActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_save:


                break;
        }
    }
}
