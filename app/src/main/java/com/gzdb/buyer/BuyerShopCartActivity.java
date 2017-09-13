package com.gzdb.buyer;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;

import com.gzdb.warehouse.R;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.utils.DialogUtils;
import com.zhumg.anlib.widget.AdapterModel;
import com.zhumg.anlib.widget.dialog.TipClickListener;
import com.zhumg.anlib.widget.dialog.TipDialog;
import com.zhumg.anlib.widget.mvc.LoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zhumg on 4/24.
 */
public class BuyerShopCartActivity extends AfinalActivity implements View.OnClickListener, Runnable {

    @Bind(R.id.title_left)
    View title_left;

    @Bind(R.id.title_right)
    View title_right;

    @Bind(R.id.supply_cartlist)
    ListView listView;

    @Bind(R.id.okbtn)
    View okbtn;

    LoadingView loadingView;

    List<AdapterModel> items = new ArrayList<>();
    BuyerItemAdapter adapter = null;

    //提示窗
    TipDialog tipDialog;
    //仓库ID
    long targetWarehouseId;

    @Override
    public int getContentViewId() {
        return R.layout.activity_warehouse_cart;
    }


    @Override
    public void initView(View view) {
        setTranslucentStatus();
        targetWarehouseId = getIntent().getLongExtra("targetWarehouseId", 0);
        loadingView = new LoadingView(view);

        title_right.setOnClickListener(this);
        title_left.setOnClickListener(this);

        tipDialog = DialogUtils.createTipDialog(this, "确定要清空购物车的商品吗?", new TipClickListener() {
            @Override
            public void onClick(boolean left) {
                if (!left) {
                    //清空
                    BuyerShopCart.removeAll();
                    clearAll();
                }
            }
        });

        loadingView.hibe();

        refreshDatas();

        adapter = new BuyerItemAdapter(this, items, null, true);
        adapter.delRunnable = this;

        listView.setAdapter(adapter);

        if (adapter.isEmpty()) {
            clearAll();
        } else {
            adapter.notifyDataSetChanged();
        }

        okbtn.setOnClickListener(this);
    }

    void clearAll() {
        title_right.setVisibility(View.INVISIBLE);
        adapter.getDatas().clear();
        listView.setVisibility(View.GONE);
        okbtn.setVisibility(View.GONE);
        loadingView.setResetListener(BuyerShopCartActivity.this);
        loadingView.showLoadingTxtBtn("购物车是空的", "去逛逛");
    }

    void refreshDatas() {
        for (int i = 0; i < BuyerShopCart.itemLists.size(); i++) {
            items.add(BuyerShopCart.itemLists.get(i));
        }
        if (items.size() < 1) {
            loadingView.showLoadingTxtBtn("购物车为空", "去逛逛");
            loadingView.setResetListener(this);
            listView.setVisibility(View.GONE);
        } else {
            loadingView.hibe();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.title_left) {
            finish();
        } else if (id == R.id.wlv_btn) {
            //去逛逛标志
            finish();
        } else if (id == R.id.title_right) {
            //清空
            if (adapter.isEmpty()) {
                return;
            }
            tipDialog.show();
        } else if(id == R.id.okbtn) {

            Intent intent = new Intent(this, BuyerOrderSubmitActivity.class);
            intent.putExtra("targetWarehouseId", targetWarehouseId);
            this.startActivity(intent);
        }
    }

    @Override
    public void run() {
        if (BuyerShopCart.getAllCount() <= 0) {
            //清空
            clearAll();
        }
    }

}
