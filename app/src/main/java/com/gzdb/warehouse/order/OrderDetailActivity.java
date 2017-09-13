package com.gzdb.warehouse.order;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gzdb.response.Api;
import com.gzdb.response.ConsumerOrderItem;
import com.gzdb.response.OrderDetail;
import com.gzdb.response.enums.OrderStatusEnum;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.utils.CreateQRImage;
import com.gzdb.utils.Utils;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.zoom.SpaceImageDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.DialogUtils;
import com.zhumg.anlib.utils.StringUtils;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;
import com.zhumg.anlib.widget.dialog.TipClickListener;
import com.zhumg.anlib.widget.mvc.LoadingView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class OrderDetailActivity extends AfinalActivity implements View.OnClickListener {

    public static final byte Btn_HandlerType_Pay = 1;//采购员，去付款
    public static final byte Btn_HandlerType_Confirm = 2;//采购员，确认订单
    public static final byte Btn_HandlerType_ConfirmShouhuo = 3;//采购员，确认收货
    public static final byte Btn_HandlerType_ConfirmFlag = 4;//采购员，标记完成

    public static final byte Btn_HandlerType_SendItem = 10;//仓管，销售订单，去发货

    public static final byte Btn_HandlerType_Print = 20;//打印小票
    public static final byte Btn_HandlerType_Code = 21;//扫码收货

    int btnHandlerType;
    int btnHandlerType1;

    OrderDetail detail;
    long orderId;

    @Bind(R.id.p_layout)
    View p_layout;

    @Bind(R.id.tv_order_pnum)
    TextView tv_order_pnum;

    @Bind(R.id.tv_txtname)
    TextView tv_txtname;

    @Bind(R.id.tv_name)
    TextView tv_name;

    @Bind(R.id.tv_order_id)
    TextView tv_order_id;

    @Bind(R.id.tv_time)
    TextView tv_time;

    @Bind(R.id.tv_state)
    TextView tv_state;

    @Bind(R.id.items_view)
    LinearLayout items_view;

    @Bind(R.id.tv_allcount)
    TextView tv_allcount;

    @Bind(R.id.tv_shou_user)
    TextView tv_shou_user;

    @Bind(R.id.tv_shou_phone)
    TextView tv_shou_phone;

    @Bind(R.id.tv_shou_address)
    TextView tv_shou_address;

    @Bind(R.id.tv_order_code)
    TextView tv_order_code;

    @Bind(R.id.tv_order_code_g)
    View tv_order_code_g;

    @Bind(R.id.btn_ll)
    View btn_ll;

    @Bind(R.id.btn1)
    TextView btn1;

    @Bind(R.id.btn2)
    TextView btn2;

    @Bind(R.id.img_qr_code)
    ImageView img_qr_code;
    @Bind(R.id.ll00)
    LinearLayout ll00;
    @Bind(R.id.ll01)
    LinearLayout ll01;
    @Bind(R.id.ll02)
    LinearLayout ll02;
    @Bind(R.id.ll05)
    LinearLayout ll05;

    //拍照收货单签名照
    @Bind(R.id.img01)
    ImageView img01;
    @Bind(R.id.img02)
    ImageView img02;
    @Bind(R.id.img03)
    ImageView img03;
    @Bind(R.id.img04)
    ImageView img04;
    @Bind(R.id.img05)
    ImageView img05;

    BaseTitleBar baseTitleBar;
    LoadingView loadingView;

    int orderRoleType;
    int orderType;

    //是否有货需要发
    boolean send_bool = false;

    boolean shou_bool = false;


    boolean is_handler_send = false;//是否是发货详情

    List<String> datas = new ArrayList<String>();

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView(View view) {

        try {
            setTranslucentStatus();

            baseTitleBar = new BaseTitleBar(view);
            baseTitleBar.setCenterTxt("订单详情");
            baseTitleBar.setLeftBack(this);

            Object obj = getIntent().getSerializableExtra("detail");
            if (obj != null) {
                detail = (OrderDetail) obj;
            }
            orderRoleType = getIntent().getIntExtra("orderRoleType", 0);
            orderType = getIntent().getIntExtra("orderType", 0);
            orderId = getIntent().getLongExtra("orderId", 0);

            btn1.setOnClickListener(this);
            btn2.setOnClickListener(this);
            img01.setOnClickListener(this);
            img02.setOnClickListener(this);
            img03.setOnClickListener(this);
            img04.setOnClickListener(this);
            img05.setOnClickListener(this);

            loadingView = new LoadingView(view);
            loadingView.setResetListener(this);

            btn_ll.setVisibility(View.GONE);

            //获取 订单详情
            if (detail != null) {
                // 没有该订单信息
                loadingView.hibe();
                p_layout.setVisibility(View.VISIBLE);
                refreshOrder(detail);
            } else {
                // 没有该订单信息
                p_layout.setVisibility(View.GONE);
                loadingView.showLoading();
                startGetOrderDetail();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn1) {
            //针对按钮类型处理
            if (btnHandlerType == 0) return;

            switch (btnHandlerType) {
                case OrderDetailActivity.Btn_HandlerType_Confirm://确认订单
                {
                    Map map = new HashMap();
                    map.put("orderId", String.valueOf(detail.getOrderId()));
                    map.put("passportId", Cache.passport.getPassportIdStr());
                    Http.post(OrderDetailActivity.this, map, Api.CONFIRM_ORDER, new HttpCallback() {
                        @Override
                        public void onSuccess(Object data) {
                            btn_ll.setVisibility(View.GONE);
                            ToastUtil.showToast(OrderDetailActivity.this, msg);
                        }
                    }.setPass());
                }
                return;
                case OrderDetailActivity.Btn_HandlerType_SendItem: {
                    Intent intent = new Intent(OrderDetailActivity.this, SendItemActivity.class);
                    intent.putExtra("detail", detail);
                    startActivity(intent);
                    return;
                }
                case OrderDetailActivity.Btn_HandlerType_ConfirmShouhuo: {
                    Intent intent = new Intent(this, ShouItemActivity.class);
                    intent.putExtra("detail", detail);
                    startActivityForResult(intent, 123);
                    return;
                }
            }

        } else if (id == com.zhumg.anlib.R.id.wlv_btn) {
            loadingView.showLoading();
            startGetOrderDetail();
        } else if (id == R.id.btn2) {//标记完成状态
            //针对按钮类型处理
            if (btnHandlerType1 == 0) return;
            switch (btnHandlerType1) {
                case OrderDetailActivity.Btn_HandlerType_ConfirmFlag: {

                    if (detail.getStatusValue().equals("分批配送")) {
                        Dialog dialog = DialogUtils.createTipDialog(OrderDetailActivity.this, "部分未收货，是否确认标记完成？", new TipClickListener() {
                            @Override
                            public void onClick(boolean left) {

                                if (!left) {

                                    Map map = new HashMap();
                                    map.put("orderId", String.valueOf(detail.getOrderId()));
                                    map.put("passportId", String.valueOf(Cache.passport.getPassportIdStr()));
                                    Http.post(OrderDetailActivity.this, map, Api.FLAG_ORDER_STATE, new HttpCallback() {
                                        @Override
                                        public void onSuccess(Object data) {
                                            ToastUtil.showToast(OrderDetailActivity.this, msg);
                                            finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            super.onFailure();
                                        }
                                    }.setPass());
                                }
                            }
                        });
                        dialog.show();
                    } else {
                        ToastUtil.showToast(OrderDetailActivity.this, "未收货不可标记完成");
                    }

                    return;
                }
            }

        } else if (id == R.id.img01) {

            zoom(img01, 0);
        } else if (id == R.id.img02) {
            zoom(img02, 1);
        } else if (id == R.id.img03) {
            zoom(img03, 2);
        } else if (id == R.id.img04) {
            zoom(img04, 3);
        } else if (id == R.id.img05) {
            zoom(img05, 4);
        }
    }

    //点击订单照片图片放大
    private void zoom(final ImageView img, final int position) {
        datas.addAll(detail.getOrderReceiptImgs());
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderDetailActivity.this, SpaceImageDetailActivity.class);
                intent.putExtra("images", (ArrayList<String>) datas);
                int[] location = new int[2];
                img.getLocationOnScreen(location);
                intent.putExtra("position", position);
                intent.putExtra("locationX", location[0]);
                intent.putExtra("locationY", location[1]);
                intent.putExtra("width", img.getWidth());
                intent.putExtra("height", img.getHeight());
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

    }

    void startGetOrderDetail() {
        Map map = new HashMap();
        map.put("orderId", String.valueOf(orderId));
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("roleType", String.valueOf(orderRoleType));
        Http.post(this, map, Api.ORDER_DETAIL, new HttpCallback<OrderDetail>() {
            @Override
            public void onSuccess(OrderDetail data) {
                loadingView.hibe();
                //设置内容
                refreshOrder(data);

            }

            @Override
            public void onFailure() {
                loadingView.showLoadingTxtBtn(msg, "重试");
                loadingView.setResetListener(OrderDetailActivity.this);
            }
        });
    }

    void refreshOrder(OrderDetail detail) {

        try {
            this.detail = detail;
            String sort_id = detail.getDailySort();
            if (!StringUtils.isEmpty(sort_id)) {
                tv_order_pnum.setText("排号：" + sort_id);
            }
            CreateQRImage img = new CreateQRImage();
            Bitmap bitmap = img.createQRImage(detail.getQrCode(), 1000, 1000);
            img_qr_code.setImageBitmap(bitmap);
            //整个订单的购买问题
            int count = 0;
            List<ConsumerOrderItem> items = detail.getItemSnapshotArray();

            if (orderType == OrderTypeEnum.ALLOCATION_ORDER_TYPE.getType()) {//调拨单

                if (Cache.passport.isMainWarehouse()) {
                    tv_txtname.setText("调货仓库：");
                    tv_name.setText(detail.getReceiptNickName());
                    is_handler_send = true;
                    for (int i = 0; i < items.size(); i++) {
                        ConsumerOrderItem c = items.get(i);
                        count += c.getQuantity();
                        if (c.getUnShippingQuantity() > 0) {
                            send_bool = true;//允许发货
                        }
                    }
                } else {
                    tv_txtname.setText("发货仓库：");
                    tv_name.setText(detail.getShippingNickName());
                    //如果是分批送，或者已送达
                    if (detail.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_BATCH.getType() ||
                            detail.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_ARRIVE.getType()) {
                        shou_bool = true;//允许收货
                    }
                    for (int i = 0; i < items.size(); i++) {
                        ConsumerOrderItem c = items.get(i);
                        count += c.getQuantity();
                        //                    if(c.getUnReceiptQuantity() > 0){
                        //                    }
                    }
                }

            } else if (orderType == OrderTypeEnum.PURCHASE_ORDER_TYPE.getType()) {//采购单

                is_handler_send = false;
                tv_txtname.setText("采购门店：");
                tv_name.setText(detail.getReceiptNickName());

                boolean shou = false;

                //如果是不是未支付，则默认是确认收货
                if (detail.getOrderStatus() != OrderStatusEnum.ORDER_STATUS_DEFAULT.getType()) {
                    shou = true;
                }

                for (int i = 0; i < items.size(); i++) {
                    ConsumerOrderItem c = items.get(i);
                    count += c.getQuantity();


                    if (shou && c.getUnReceiptQuantity() > 0 && detail.getOrderStatus() != OrderStatusEnum.ORDER_STATUS_CONFIRM.getType()) {
                        shou_bool = true;//允许收货
                    }
                }

            } else { //销售订单
                is_handler_send = true;
                tv_txtname.setText("商家名称：");
                tv_name.setText(detail.getReceiptNickName());
                for (int i = 0; i < items.size(); i++) {
                    ConsumerOrderItem c = items.get(i);
                    count += c.getQuantity();
                    if (c.getUnShippingQuantity() > 0) {
                        send_bool = true;//允许发货
                    }
                }
            }

            refreshItemViews(false);

            tv_order_id.setText(String.valueOf(detail.getOrderSequenceNumber()));
            tv_time.setText(detail.getCreateTime());
            tv_state.setText(detail.getStatusValue());

            if (orderType == OrderTypeEnum.ALLOCATION_ORDER_TYPE.getType()) {
                tv_allcount.setText("x " + count);
            } else {
                tv_allcount.setText("¥ " + Utils.toYuanStr(detail.getTotalPrice()));
            }

            tv_shou_user.setText(detail.getReceiptNickName());
            tv_shou_phone.setText(detail.getReceiptPhone());
            tv_shou_address.setText(detail.getReceiptAddress());


            p_layout.setVisibility(View.VISIBLE);

            btn_ll.setVisibility(View.VISIBLE);

            handlerBtn();

            //显示采购端进去的订单详情界面的收货订单照片
            if (detail.getOrderReceiptImgs().size() > 0) {

                ll00.setVisibility(View.VISIBLE);
                ll05.setVisibility(View.VISIBLE);

                if (detail.getOrderReceiptImgs().get(0) != null) {
                    ImageLoader.getInstance().displayImage(detail.getOrderReceiptImgs().get(0), img01);
                }
                if (detail.getOrderReceiptImgs().get(1) != null) {
                    img02.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(detail.getOrderReceiptImgs().get(1), img02);
                }
                if (detail.getOrderReceiptImgs().get(2) != null) {
                    ll01.setVisibility(View.VISIBLE);
                    img03.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(detail.getOrderReceiptImgs().get(2), img03);
                }
                if (detail.getOrderReceiptImgs().get(3) != null) {
                    img04.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(detail.getOrderReceiptImgs().get(3), img04);
                }
                if (detail.getOrderReceiptImgs().get(4) != null) {
                    ll02.setVisibility(View.VISIBLE);
                    img05.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().displayImage(detail.getOrderReceiptImgs().get(4), img05);
                }

            } else {
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void handlerBtn() {

        if (orderType == OrderTypeEnum.ALLOCATION_ORDER_TYPE.getType()) {//调拨订单
            tv_order_code.setVisibility(View.GONE);
            tv_order_code_g.setVisibility(View.GONE);
            //如果是主仓
            if (Cache.passport.isMainWarehouse()) {
                //可以出货，则出货
                if (send_bool) {
                    btn_ll.setVisibility(View.VISIBLE);
                    btn1.setText("立即出货");
                    btn2.setVisibility(View.GONE);
                    btnHandlerType = Btn_HandlerType_SendItem;
                    return;
                }
                btn_ll.setVisibility(View.GONE);
            } else {
                //子仓调拨单
                if (shou_bool) {
                    btn_ll.setVisibility(View.VISIBLE);
                    btn1.setVisibility(View.VISIBLE);
                    btn1.setText("确认收货");
                    btnHandlerType = OrderDetailActivity.Btn_HandlerType_Confirm;
                    btn2.setVisibility(View.GONE);
                    return;
                }
                btn_ll.setVisibility(View.GONE);
            }

        } else if (orderType == OrderTypeEnum.PURCHASE_ORDER_TYPE.getType()) {//采购单
            tv_order_code.setVisibility(View.GONE);
            tv_order_code_g.setVisibility(View.GONE);

            btn2.setText("标记完成");
            btnHandlerType1 = OrderDetailActivity.Btn_HandlerType_ConfirmFlag;

            //可以收货
            if (shou_bool) {
                btn_ll.setVisibility(View.VISIBLE);
                btn1.setVisibility(View.VISIBLE);
                btn1.setText("确认收货");
                btnHandlerType = OrderDetailActivity.Btn_HandlerType_ConfirmShouhuo;
                return;
            }

            btn_ll.setVisibility(View.GONE);
        } else {
            //仓库的销售订单，即商家下单给仓库
            tv_order_code.setVisibility(View.VISIBLE);
            tv_order_code_g.setVisibility(View.VISIBLE);

            if (detail.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_ACCEPT.getType()) {//接单状态了
                //接单了，立即出货
                if (send_bool) {
                    btn_ll.setVisibility(View.VISIBLE);
                    btn1.setText("立即出货");
                    btn2.setVisibility(View.GONE);
                    btnHandlerType = Btn_HandlerType_SendItem;
                    return;
                }
            } else if (detail.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_BATCH.getType()
                    || detail.getOrderStatus() == OrderStatusEnum.ORDER_STATUS_ARRIVE.getType()) {//分批送
                //分批送中，再次出货
                if (send_bool) {
                    btn_ll.setVisibility(View.VISIBLE);
                    btn1.setText("再次出货");
                    btn2.setVisibility(View.GONE);
                    btnHandlerType = Btn_HandlerType_SendItem;
                    return;
                }
            }
            btn_ll.setVisibility(View.GONE);
        }
    }


    private void refreshItemViews(boolean all) {
        items_view.removeAllViews();

        int count = 0;

        List<ConsumerOrderItem> items = detail.getItemSnapshotArray();

        if (all) {
            count = items.size();
        } else {
            count = items.size() > Api.updown_count ? Api.updown_count
                    : items.size();
        }
        for (int i = 0; i < count; i++) {
            ConsumerOrderItem item = items.get(i);
            //如果是调拨单，则不显示价格
            if (orderType == OrderTypeEnum.ALLOCATION_ORDER_TYPE.getType()) {
                refreshItemViewTxt(item.getItemName(), "", item.getQuantity() + item.getUnitName(), 2);
            } else {
                refreshItemViewTxt(item.getItemName(), item.getQuantity()
                        + item.getUnitName(), "¥" + Utils.toYuanStr(item.getNormalPrice()), 2);
            }
            //如果是处理发货的
            if (is_handler_send) {
                if (item.getUnShippingQuantity() > 0) {
                    View vvvvv = refreshItemViewTxt("", "已发货：" + (item.getQuantity() - item.getUnShippingQuantity()), "未完成发货", 2);
                    ((TextView) vvvvv.findViewById(R.id.item_t3)).setTextColor(getResources().getColor(R.color.red));
                } else {
                    View vvvvv = refreshItemViewTxt("", "已发货：" + item.getQuantity(), "完成全部发货", 2);
                    ((TextView) vvvvv.findViewById(R.id.item_t3)).setTextColor(getResources().getColor(R.color.orange));
                }
            } else {
                if (item.getReceiptQuantity() != item.getQuantity()) {
                    View vvvvv = refreshItemViewTxt("", "已收货：" + item.getReceiptQuantity(), "未完成收货", 2);
                    ((TextView) vvvvv.findViewById(R.id.item_t3)).setTextColor(getResources().getColor(R.color.red));
                } else {
                    View vvvvv = refreshItemViewTxt("", "已收货：" + item.getReceiptQuantity(), "完成全部收货", 2);
                    ((TextView) vvvvv.findViewById(R.id.item_t3)).setTextColor(getResources().getColor(R.color.orange));
                }
            }
        }
        if (items.size() > count) {
            // 添加一个下拉条
            refreshG_down();
        } else if (count > Api.updown_count) {
            // 添加上拉条
            refreshG_up();
        } else {

        }

        // 配送费
        //refreshItemViewTxt("配送费", null, wCartItem.splitBean.getWarehouseDeliveryFee(), 10);
        //refreshItemViewTxt("付款金额", null, "¥:" + wCartItem.getAllPrice(), 5);

        items_view.requestLayout();
    }

    private View refreshItemViewTxt(String t1, String t2, String t3, int top) {

        View p_view = View.inflate(this, R.layout.widget_supply_buy_item,
                null);
        if (top != 0) {
            p_view.setPadding(0, top, 0, 0);
        }

        TextView textview1 = (TextView) p_view.findViewById(R.id.item_t1);
        if (t1 != null) {
            textview1.setText(t1);
        } else {
            textview1.setVisibility(View.INVISIBLE);
        }

        TextView textview2 = (TextView) p_view.findViewById(R.id.item_t2);
        if (t2 != null) {
            textview2.setText(t2);
        } else {
            textview2.setVisibility(View.INVISIBLE);
        }

        TextView textview3 = (TextView) p_view.findViewById(R.id.item_t3);
        if (t3 != null) {
            textview3.setText(t3);
        } else {
            textview3.setVisibility(View.INVISIBLE);
        }
        items_view.addView(p_view);
        return p_view;
    }

    private void refreshItemViewTxtCu(String t1, int top) {

        View p_view = View.inflate(this, R.layout.widget_supply_buy_item,
                null);
        if (top != 0) {
            p_view.setPadding(0, top, 0, 0);
        }

        TextView textview1 = (TextView) p_view.findViewById(R.id.item_t1);
        if (t1 != null) {
            textview1.setText(t1);
            textview1.setTextColor(0xffF04848);
        } else {
            textview1.setVisibility(View.INVISIBLE);
        }

        TextView textview2 = (TextView) p_view.findViewById(R.id.item_t2);
        textview2.setVisibility(View.INVISIBLE);

        TextView textview3 = (TextView) p_view.findViewById(R.id.item_t3);
        textview3.setVisibility(View.INVISIBLE);

        items_view.addView(p_view);
    }

    private void refreshG_down() {
        View p_view = View.inflate(this, R.layout.widget_g, null);
        p_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                refreshItemViews(true);
            }
        });
        ViewGroup vg = (ViewGroup) p_view;
        ((ImageView) (vg.findViewById(R.id.widget_g_img)))
                .setImageResource(R.drawable.down_icon);
        items_view.addView(p_view);
    }

    private void refreshG_up() {
        View p_view = View.inflate(this, R.layout.widget_g, null);
        p_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                refreshItemViews(false);
            }
        });
        ViewGroup vg = (ViewGroup) p_view;
        ((ImageView) (vg.findViewById(R.id.widget_g_img)))
                .setImageResource(R.drawable.up_icon);
        items_view.addView(p_view);
    }
}
