package com.gzdb.warehouse.order;

import android.view.View;
import android.widget.ListView;

import com.gzdb.response.Api;
import com.gzdb.response.ConsumerOrderItem;
import com.gzdb.response.OrderDetail;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.adapter.SendItemAdapter;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by zhumg on 4/21.
 */
public class SendItemActivity extends AfinalActivity {

    BaseTitleBar baseTitleBar;
    OrderDetail detail;

    @Bind(R.id.fr_listview)
    ListView listView;

    @Bind(R.id.btn1)
    View btn1;

    List<ConsumerOrderItem> items = new ArrayList<>();
    SendItemAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_send_item;
    }

    @Override
    public void initView(View view) {

        setTranslucentStatus();

        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setCenterTxt("发货信息");
        baseTitleBar.setLeftBack(this);

        detail = (OrderDetail) getIntent().getSerializableExtra("detail");

        List<ConsumerOrderItem> os = detail.getItemSnapshotArray();
        for (int i = 0; i < os.size(); i++) {
            ConsumerOrderItem item = os.get(i);
            if (item.getUnShippingQuantity() == 0) {
                continue;
            }
            item.setSendCount(item.getUnShippingQuantity());
            items.add(item);
        }

        adapter = new SendItemAdapter(this, items);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发货
                send();
            }
        });
    }

    void send() {
        int send_count = 0;
        //弹界面出来
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < items.size(); i++) {
            ConsumerOrderItem item = items.get(i);
            int sc = item.getSendCount();
            if (sc > 0) {
                try {
                    send_count += sc;
                    jsonObject.put(String.valueOf(item.getItemSnapshotId()), String.valueOf(sc));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        if(send_count <= 0) {
            ToastUtil.showToast(this, "请输入出货数量");
            return;
        }
        Map map = new HashMap();
        map.put("orderId", String.valueOf(detail.getOrderId()));
        map.put("shippingPassportId", String.valueOf(Cache.passport.getPassportIdStr()));
        map.put("itemSet", jsonObject.toString());
        Http.post(this, map, Api.DELIVER_ORDER, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                ToastUtil.showToast(SendItemActivity.this, msg);
                //关闭掉 详情
                ActivityManager.finishActivity(OrderDetailActivity.class);
                finish();
            }
        }.setPass());
    }
}
