package com.gzdb.picking;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gzdb.picking.adapter.PickingAdapter;
import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.BackOrder;
import com.gzdb.response.DetailsBean;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.response.enums.StatusPickingEnum;
import com.gzdb.response.showPick;
import com.gzdb.utils.GlobalData;
import com.gzdb.utils.PushDispatcher;
import com.gzdb.utils.PushObserver;
import com.gzdb.warehouse.App;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.SpUtils;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.mvc.RefreshLoad;
import com.zhumg.anlib.widget.mvc.RefreshLoadListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by liyunbiao on 2017/8/8.
 */

public class PickingListFragment extends AfinalFragment {

    StatusPickingEnum statusPickingEnum;
    OrderRoleTypeEnum orderRoleTypeEnum;
    OrderTypeEnum orderTypeEnum;

    StatusPickingEnum new_statusEnterEnum;
    OrderRoleTypeEnum new_orderRoleTypeEnum;
    OrderTypeEnum new_orderTypeEnum;

    @Bind(R.id.fr_ptr)
    PtrClassicFrameLayout ptr;

    @Bind(R.id.fr_listview)
    ListView listView;

    PickingAdapter orderAdapter;
    List<showPick> orders = new ArrayList<>();
    List<showPick> ordersAll = new ArrayList<>();

    RefreshLoad refreshLoad;

    TextView tv_finish;
    Dialog wheelViewDialog;
    int pageIndex = 1;
    int pageSize = 10;
    long oldTime = 0;
    private String msgAll = "";
    private EditText edit_encode;
    private boolean isInput;
    Boolean bf = true;
    PushObserver mDanmuReceiver = new PushObserver() {

        @Override
        public boolean onMessage(String message) {

            if (message.equals("&&&&&submit****")) {
                try {
                    refreshLoad.showLoading();
                    //   pickListDone(orderAdapter.getItem(0).getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (message.indexOf("_") != -1) {
                msgAll = message;
                startGetItems(message);
            }
            return true;
        }
    };

    private void registerDanmuPushHandler() {
        PushDispatcher.sharedInstance().addObserver(mDanmuReceiver);
    }

    private void unregisterDanmuPushHandler() {
        PushDispatcher.sharedInstance().removeObserver(mDanmuReceiver);
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_ptr;
    }

    @Override
    protected void initViewData(View view) {
        // 已进入就可以收到弹幕
        registerDanmuPushHandler();
        GlobalData.notPickData = new ArrayList<>();
        orderAdapter = new PickingAdapter(getActivity(), orders);
        orderAdapter.setOrderRoleTypeEnum(this.orderRoleTypeEnum);
        orderAdapter.setOrderTypeEnum(statusPickingEnum);

        listView.setAdapter(orderAdapter);
        listView.setBackgroundResource(R.color.bg_e8);

        orderAdapter.setOnClickListenerDel(new PickingAdapter.OnClickListenerDel() {
            @Override
            public void setOnClickListener(showPick item, int i) {

                pickListDone(item.getId());
            }
        });
        orderAdapter.setOnClickListenerDelItem(new PickingAdapter.OnClickListenerDelItem() {

            @Override
            public void setOnClickListenerItem(DetailsBean item, showPick order, int i) {
                try {
                    PickingMainActivity.printLabel(order.getDaySortNumber(), "订单号:" + order.getOrderSequenceNumber(),
                            "条码：" + item.getBarcode(), item.getItemName(), order.getSrcWarehouseName(), order.getSrcReceiptNickName(), order.getSrcReceiptAddress()
                            , order.getQrCode());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        refreshLoad = new RefreshLoad(this.getActivity(), ptr, view, new RefreshLoadListener() {
            @Override
            public void onLoading(boolean over) {
                if (over) {
                    ptr.setVisibility(View.VISIBLE);
                } else {
                    pageIndex = 1;
                    ptr.setVisibility(View.GONE);
                    if (statusPickingEnum.getType() == StatusPickingEnum.NOPICKING.getType()) {
                        getPickGrab();
                    } else {

                        if (msgAll.indexOf("_") != -1) {
                            startGetItems(msgAll);
                        } else {
                            if (statusPickingEnum.getType() != StatusPickingEnum.NOPICKING.getType()) {
                                refreshLoad.complete(false, orderAdapter.isEmpty());
                                //加载订单数据
                                startGetOrders();
                            }
                        }
                    }
                }
            }

            @Override
            public void onRefresh(boolean over) {
                if (!over) {
                    if (statusPickingEnum.getType() == StatusPickingEnum.NOPICKING.getType()) {
                        getPickGrab();
                    } else {
                        if (msgAll.indexOf("_") != -1) {
                            startGetItems(msgAll);
                        } else {
                            if (statusPickingEnum.getType() != StatusPickingEnum.NOPICKING.getType()) {
                                pageIndex = 1;
                                startGetOrders();
                            }
                        }
                    }
                }
            }

            @Override
            public void onLoadmore(boolean over) {
                if (!over) {
                    if (statusPickingEnum.getType() == StatusPickingEnum.NOPICKING.getType()) {
                        getPickGrab();
                    } else {
                        if (msgAll.indexOf("_") != -1) {
                            startGetItems(msgAll);
                        } else {
                            if (statusPickingEnum.getType() != StatusPickingEnum.NOPICKING.getType()) {
                                pageIndex++;
                                startGetOrders();
                            }
                        }
                    }
                }
            }

        }, listView);
        tv_finish.setVisibility(View.GONE);
        refreshLoad.complete(false, orderAdapter.isEmpty());
        tv_finish.setEnabled(true);
        tv_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_finish.setEnabled(false);
                addPickGrab();
                App.getUIHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tv_finish.setEnabled(true);
                    }
                }, 15000);
            }
        });
    }

    /**
     * 拣货
     */
    public void addPickGrab() {

        Map<String, String> map = new HashMap<>();
        map.put("passportId", Cache.passport.getPassportIdStr());

        Http.post(getActivity(), map, Api.BasesupplychainRemoteURL() + "warehouse/addPickGrab", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                tv_finish.setEnabled(true);
                try {

                    refreshLoad.showRefresh();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                tv_finish.setEnabled(true);
                refreshLoad.showLoading();
                //   ToastUtil.showToast(getActivity(), msg);
            }
        });
    }

    public void submitPicking(String encode) {
        try {
            if (ordersAll != null) {

                bf = true;
                for (int i = 0; i < ordersAll.size(); i++) {
                    for (int k = 0; k < ordersAll.get(i).getDetails().size(); k++) {
                        if (encode.equals(ordersAll.get(i).getDetails().get(k).getBarcode())) {

                            if (!ordersAll.get(i).getDetails().get(k).getStatus().equals("1")) {
                                if (bf) {
                                    pickItem(encode, ordersAll.get(i).getDetails().get(k).getPickListId(), 1, ordersAll.get(i).getDetails().get(k), ordersAll.get(i));
                                    bf = false;

                                }
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVisible() {

        //如果类型不一致，则重置
        if (orderTypeEnum != new_orderTypeEnum || statusPickingEnum != new_statusEnterEnum || orderRoleTypeEnum != new_orderRoleTypeEnum) {
            orderTypeEnum = new_orderTypeEnum;
            statusPickingEnum = new_statusEnterEnum;
            orderRoleTypeEnum = new_orderRoleTypeEnum;
            //清除数据
            if (orderAdapter != null) {
                orderAdapter.setOrderTypeEnum(statusPickingEnum);
                orderAdapter.setOrderRoleTypeEnum(orderRoleTypeEnum);
                orderAdapter.removeAll();
            }
        }
        if (statusPickingEnum.getType() == StatusPickingEnum.NOPICKING.getType()) {
            if (ordersAll == null) {
                tv_finish.setVisibility(View.VISIBLE);
            } else {
                tv_finish.setVisibility(View.GONE);
            }
        } else {
            tv_finish.setVisibility(View.GONE);
        }

        edit_encode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (isInput) {
                        String str = s.toString().trim();
                        if (str.length() == 0) {
                            return;
                        }
                        if (str.indexOf("-") != -1) {
                            ActivityManager.startActivity(getActivity(), AddWarehouseLocationActivity.class);
                        } else if (str.indexOf("_") != -1) {
                            PushDispatcher.sharedInstance().dispatch(str);
                        } else if (str.indexOf(":") != -1) {
                            String[] strd = str.split(":");
                            if (strd.length == 6) {
                                SpUtils.saveValue("bluetooth", str);
                            }
                        } else {
                            // ToastUtil.showToast(getActivity(),str);
                            submitPicking(str);
                        }


                    }
                    isInput = false;
                    edit_encode.setEnabled(false);
                    App.getUIHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isInput = true;
                            edit_encode.setEnabled(true);
                            edit_encode.setText("");
                        }
                    }, 1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //如果为空，或者 刷新时间够1分钟，则刷新
        long time = System.currentTimeMillis();
        if (orderAdapter != null && !orderAdapter.isEmpty()) {
            if (time - oldTime > 1000 * Api.LIST_REFRESH_TIME) {
                oldTime = time;
                refreshLoad.showRefresh();
            }
            return;
        }
        oldTime = time;
        refreshLoad.showLoading();
        refreshLoad.complete(false, orderAdapter.isEmpty());
    }

    public Dialog createWheelViewDialog(final DetailsBean item, final showPick order) {
        final Dialog dialog = new Dialog(getActivity(), R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(getActivity(), R.layout.picking_num,
                null);

        TextView tv_dialog_close = (TextView) view.findViewById(R.id.tv_dialog_close);
        TextView tv_dialog_ok = (TextView) view.findViewById(R.id.tv_dialog_ok);
        final EditText edit_num = (EditText) view.findViewById(R.id.edit_num);
        edit_num.setText((Integer.parseInt(item.getQuantity()) - Integer.parseInt(item.getPickedQuantity())) + "");
        tv_dialog_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation ani = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_action);
                v.startAnimation(ani);
                wheelViewDialog.dismiss();
            }
        });
        tv_dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation ani = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_action);
                v.startAnimation(ani);
                int num = Integer.parseInt(edit_num.getText().toString());
                int qnum = Integer.parseInt(item.getQuantity());
                if (num > qnum) {
                    ToastUtil.showToast(getActivity(), "拣货数量不能大于销售数量");
                }
                wheelViewDialog.dismiss();
                pickItem(item.getBarcode(), item.getPickListId(), num, item, order);

            }
        });
        dialog.setContentView(view);
        return dialog;
    }

    void startGetOrders() {
        try {

            if (statusPickingEnum.getType() == StatusPickingEnum.NOPICKING.getType()) {
                pageSize = 1;
            } else {
                pageSize = 10;
            }
            PushDispatcher.sharedInstance().dispatch("&&&&&referen****");
            Map map = ApiRequest.showPickList(Cache.passport.getPassportId(), "", statusPickingEnum.getType() + "", pageIndex, pageSize);
            Http.post(getActivity(), map, Api.showPickList, new HttpCallback<List<showPick>>("datas") {
                @Override
                public void onSuccess(List<showPick> data) {
                    GlobalData.notPickData.clear();
                    ordersAll.clear();
                    ordersAll.addAll(data);
                    if (refreshLoad.isLoadMore()) {
                        orderAdapter.addMore(data);
                    } else {
                        orderAdapter.refresh(data);
                    }
                    orderAdapter.notifyDataSetChanged();
                    refreshLoad.complete(data.size() >= Api.PAGE_SIZE, orderAdapter.isEmpty());
                }

                @Override
                public void onFailure() {
                    if (refreshLoad.isLoadMore()) {
                        refreshLoad.showError(msg);
                    } else {
                        orderAdapter.removeAll();
                        orderAdapter.notifyDataSetChanged();
                        ptr.setVisibility(View.GONE);
                        refreshLoad.showReset(msg);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void startGetItems(String strAll) {
        Map map = new HashMap();
        map.put("qrCode", strAll);
        Http.post(getActivity(), map, Api.eshowPickListByScanQRcode, new HttpCallback<List<showPick>>("datas") {
            @Override
            public void onSuccess(List<showPick> data) {
                GlobalData.notPickData.clear();
                ordersAll.clear();
                ordersAll.addAll(data);
                orderAdapter.refresh(data);
                orderAdapter.notifyDataSetChanged();
                refreshLoad.complete(data.size() >= Api.PAGE_SIZE, orderAdapter.isEmpty());
            }

            @Override
            public void onFailure() {
                refreshLoad.complete(false, orderAdapter.isEmpty());
                if (refreshLoad.isLoadMore()) {
                    //   refreshLoad.showError(msg);
                } else {
                    orderAdapter.removeAll();
                    orderAdapter.notifyDataSetChanged();
//                    ptr.setVisibility(View.GONE);
//                    refreshLoad.showReset(msg);
                }
            }
        });
    }

    void getPickGrab() {
        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        Http.post(getActivity(), map, Api.BasesupplychainRemoteURL() + "warehouse/getPickGrab", new HttpCallback<List<showPick>>("datas") {
            @Override
            public void onSuccess(List<showPick> data) {

                if (data == null) {
                    if (statusPickingEnum.getType() == StatusPickingEnum.NOPICKING.getType()) {
                        tv_finish.setVisibility(View.VISIBLE);
                    }
                } else {
                    tv_finish.setVisibility(View.GONE);
                }
                ordersAll.clear();
                ordersAll.addAll(data);
                orderAdapter.refresh(data);
                orderAdapter.notifyDataSetChanged();
                refreshLoad.complete(data.size() >= Api.PAGE_SIZE, orderAdapter.isEmpty());
            }

            @Override
            public void onFailure() {
                if (statusPickingEnum.getType() == StatusPickingEnum.NOPICKING.getType()) {
                    tv_finish.setVisibility(View.VISIBLE);
                }
                refreshLoad.complete(false, orderAdapter.isEmpty());
                if (refreshLoad.isLoadMore()) {
                    //   refreshLoad.showError(msg);
                } else {
                    orderAdapter.removeAll();
                    orderAdapter.notifyDataSetChanged();
//                    ptr.setVisibility(View.GONE);
//                    refreshLoad.showReset(msg);
                }
            }
        });
    }

    /**
     * 拣货
     */
    public void pickItem(String barcode, final String pickListId, int num, final DetailsBean detailsBean, final showPick pick) {
        if (!bf) {
            return;
        }
        Http.post(getActivity(), ApiRequest.pickItem(barcode, pickListId, num), Api.BasesupplychainRemoteURL() + "warehouse/pickItem", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {

                refreshLoad.showLoading();
                try {
                    JSONObject json = new JSONObject(data.toString());
                    String pickNumber = "";
                    if (json != null && json.optString("pickNumber") != null) {
                        pickNumber = json.optString("pickNumber");
                    }
                    PickingMainActivity.printLabel(pick.getDaySortNumber() + "-" + pickNumber, "订单号:" + pick.getOrderSequenceNumber(),
                            "条码：" + detailsBean.getBarcode(), detailsBean.getItemName(), pick.getSrcWarehouseName(), pick.getSrcReceiptNickName(), pick.getSrcReceiptAddress()
                            , pick.getQrCode());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure() {
                super.onFailure();
                if (msg.equals("此商品已经拣完")) {
                    // pickListDone(pickListId);
                }

            }
        });
    }

    /**
     * 拣货
     */
    public void pickListDone(String pickListId) {

        Http.post(getActivity(), ApiRequest.pickListDone(pickListId), Api.BasesupplychainRemoteURL() + "warehouse/pickListDone", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                try {
                    //       tv_finish.setVisibility(View.VISIBLE);
                    if (statusPickingEnum.getType() == StatusPickingEnum.NOPICKING.getType()) {
                        ordersAll.clear();
                        orderAdapter.removeAll();
                        orderAdapter.notifyDataSetChanged();
                        tv_finish.setVisibility(View.VISIBLE);
                    } else {
                        startGetOrders();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                //   ToastUtil.showToast(getActivity(), msg);
            }
        });
    }


    public static PickingListFragment create(OrderRoleTypeEnum orderRoleTypeEnum, OrderTypeEnum orderTypeEnum, StatusPickingEnum statusPickingEnum, TextView tv_finish, EditText editTextInput) {
        PickingListFragment orderFragment = new PickingListFragment();

        //默认2个一致
        orderFragment.statusPickingEnum = statusPickingEnum;
        orderFragment.orderRoleTypeEnum = orderRoleTypeEnum;
        orderFragment.orderTypeEnum = orderTypeEnum;
        orderFragment.new_statusEnterEnum = statusPickingEnum;
        orderFragment.new_orderRoleTypeEnum = orderRoleTypeEnum;
        orderFragment.new_orderTypeEnum = orderTypeEnum;
        orderFragment.tv_finish = tv_finish;
        orderFragment.edit_encode = editTextInput;

        return orderFragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterDanmuPushHandler();
    }
}
