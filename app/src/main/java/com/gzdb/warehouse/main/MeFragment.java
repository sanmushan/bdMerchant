package com.gzdb.warehouse.main;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.gzdb.LoginActivity;
import com.gzdb.buyer.BuyerShopCart;
import com.gzdb.developing.DevelopingListActivity;
import com.gzdb.picking.PickGroupActivity;
import com.gzdb.picking.PickPersonActivity;
import com.gzdb.picking.PickingMainActivity;
import com.gzdb.response.Api;
import com.gzdb.response.Currency;
import com.gzdb.response.SupplierProperties;
import com.gzdb.response.enums.ClientTypeEnum;
import com.gzdb.response.enums.UpdatePasswordTypeEnum;
import com.gzdb.utils.JpushAliasUtil;
import com.gzdb.utils.UpdateManager;
import com.gzdb.utils.Utils;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.me.ResetPasswordActivity;
import com.gzdb.warehouse.me.SetPrintActivity;
import com.gzdb.warehouse.me.UpdateLoginPasswordActivity;
import com.gzdb.warehouse.ware.WareShopCart;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ApkUtils;
import com.zhumg.anlib.widget.RoundedImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class MeFragment extends AfinalFragment implements View.OnClickListener {

    @Bind(R.id.iv_head)
    RoundedImageView imageView;

    @Bind(R.id.tv_name)
    TextView tv_name;

    @Bind(R.id.tv_phone)
    TextView tv_phone;

    @Bind(R.id.up_pay_pass_g)
    View up_pay_pass_g;

    @Bind(R.id.up_pay_pass)
    View up_pay_pass;

    @Bind(R.id.up_login_pass)
    View up_login_pass;

    @Bind(R.id.print_ll)
    View print_ll;

    @Bind(R.id.print_ll_g)
    View print_ll_g;

    @Bind(R.id.tv_balance)
    TextView tv_balance;

    @Bind(R.id.balance_ll)
    View balance_ll;

    @Bind(R.id.tv_print)
    TextView tv_print;

    @Bind(R.id.ver_ll)
    View ver_ll;

    @Bind(R.id.tv_ver)
    TextView tv_ver;

    @Bind(R.id.kf_ll)
    View kf_ll;
    @Bind(R.id.dve_ll_g)
    View dve_ll_g;

    @Bind(R.id.exit_btn)
    View exit_btn;

    @Bind(R.id.tv_back)
    View tv_back;

    @Bind(R.id.ll_group)
    View ll_group;
    @Bind(R.id.ll_pick)
    View ll_pick;
    @Bind(R.id.rel_dev)
    View rel_dev;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initViewData(View view) {

        EventBus.getDefault().register(this);

        up_login_pass.setOnClickListener(this);
        up_pay_pass.setOnClickListener(this);
        print_ll.setOnClickListener(this);
        ver_ll.setOnClickListener(this);
        kf_ll.setOnClickListener(this);
        kf_ll.setVisibility(View.GONE);//客服热线由于公司没有电话暂时不显示
        exit_btn.setOnClickListener(this);
        rel_dev.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        ll_group.setOnClickListener(this);
        ll_pick.setOnClickListener(this);

        tv_ver.setText("v" + String.valueOf(ApkUtils.getVersionCode(this.getActivity())));

        tv_name.setText(Cache.passport.getShowName());
        tv_phone.setText(Cache.passport.getPhoneNumber());

        if(Cache.clientTypeEnum != ClientTypeEnum.PURCHASE) {
            //仓库不需要 支付密码 设置
            up_pay_pass.setVisibility(View.GONE);
            up_pay_pass_g.setVisibility(View.GONE);
            //仓库不需要 余额显示
            tv_balance.setVisibility(View.GONE);
            balance_ll.setVisibility(View.GONE);

            //获得打印设备
            getPrintValue();
        } else {
            //采购不需要打印机设置
            print_ll.setVisibility(View.GONE);
            print_ll_g.setVisibility(View.GONE);
            tv_back.setVisibility(View.GONE);
            //获取 余额
            getCurrencys();
        }

        if(Cache.clientTypeEnum != ClientTypeEnum.WAREHOUSE) {
            rel_dev.setVisibility(View.VISIBLE);
            dve_ll_g.setVisibility(View.VISIBLE);
            tv_back.setVisibility(View.GONE);
        }else {
            tv_back.setVisibility(View.GONE);//功能开发完成后才显示
            rel_dev.setVisibility(View.GONE);
            dve_ll_g.setVisibility(View.GONE);
        }

    }

    //获取用户货币
    void getCurrencys() {
        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        Http.post(this.getActivity(), map, Api.GET_CURRENCYS, new HttpCallback<List<Currency>>("datas") {
            @Override
            public void onSuccess(List<Currency> datas) {
                for (int i = 0; i < datas.size(); i++) {
                    if (datas.get(i).getType() == 1) {
                        tv_balance.setText("¥:" + Utils.toYuanStr(datas.get(i).getCurrentAmount()));
                        break;
                    }
                }
            }
        });
    }

    //拿属性
    void getPrintValue() {
        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        Http.post(this.getActivity(), map, Api.SUPPLIER_PROPERTIES, new HttpCallback<SupplierProperties>() {
            @Override
            public void onSuccess(SupplierProperties data) {
                //properties = data;
                //tvPsjl.setText(Utils.formatDistance(properties.getDeliveryDistance()));
                //setDistanceTxt(properties.getDeliveryDistance());
                //tvPhone.setText(properties.getContactNumber());
                tv_print.setText(data.getPrinterNumber());
                //refreshState();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.up_login_pass) {
            //修改登录密码
            Intent intent = new Intent(this.getActivity(), UpdateLoginPasswordActivity.class);
            startActivity(intent);
        } else if(id == R.id.up_pay_pass) {
            //修改支付密码
            Intent intent = new Intent(this.getActivity(), ResetPasswordActivity.class);
            intent.putExtra("updateType", UpdatePasswordTypeEnum.UPDATE_PAY_PASSWORD.getKey());
            startActivity(intent);
        } else if (id == R.id.print_ll) {
            //修改打印设备
            Intent intent = new Intent(this.getActivity(), SetPrintActivity.class);
            intent.putExtra("value", tv_print.getText().toString());
            startActivity(intent);
        } else if (id == R.id.ver_ll) {
            //版本更新
            UpdateManager.httpCheckUpdate(this.getActivity());
        } else if (id == R.id.kf_ll) {

        } else if (id == R.id.exit_btn) {
            Cache.passport = null;
            WareShopCart.removeAll();
            BuyerShopCart.removeAll();
            JpushAliasUtil.setJpushAlias(this.getActivity(), "");
            ActivityManager.startActivity(this.getActivity(), LoginActivity.class);
            getActivity().finish();
        }else  if(id==R.id.rel_dev){
            startActivity(new Intent(this.getActivity(), DevelopingListActivity.class));
        }else  if(id==R.id.tv_back){
            startActivity(new Intent(this.getActivity(),BackOrderActivity.class));
        }else if(id==R.id.ll_group){
            ActivityManager.startActivity(getActivity(), PickGroupActivity.class);
        }else  if(id==R.id.ll_pick){
            ActivityManager.startActivity(getActivity(), PickPersonActivity.class);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    /**
     * 执行在主线程。
     * 非常实用，可以在这里将子线程加载到的数据直接设置到界面中。
     *
     * @param msg 事件1
     */
    @Subscribe
    public void onEventMainThread(MeFragmentEvent msg) {
        String value = msg.getValue();
        if (msg.getType() == MeFragmentEvent.SET_PRINT) {
            tv_print.setText(value);
        }
    }
}

