package com.gzdb;

import android.content.Intent;
import android.os.Handler;
import android.view.View;

import com.gzdb.buyer.BuyerActivity;
import com.gzdb.picking.PickingMainActivity;
import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.Passport;
import com.gzdb.response.enums.ClientTypeEnum;
import com.gzdb.utils.JpushAliasUtil;
import com.gzdb.utils.UpdateManager;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.MainActivity;
import com.gzdb.warehouse.R;
import com.gzdb.zwarehouse.ZWarehouseActivity;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.GlobalDataParam;
import com.zhumg.anlib.utils.SpUtils;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public class StartActivity extends AfinalActivity implements Runnable {

    private Handler handler = new Handler();
    //默认进入登陆界面
    int gotoType = 0;
    int time = 0;

    @Override
    public int getContentViewId() {
        return R.layout.activity_start;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();
        handler.postDelayed(this, 1);
        final String uname = SpUtils.loadValue("name");
        if (uname.length() <= 1) {
            gotoType = 1;
            return;
        }

        String password = SpUtils.loadValue("pass");
        String warehouse = SpUtils.loadValue("warehouse");
        ClientTypeEnum clientTypeEnum ;
        if (warehouse.equals(ClientTypeEnum.PURCHASE.toString())) {
            clientTypeEnum = ClientTypeEnum.PURCHASE;
        } else if (warehouse.equals(ClientTypeEnum.PICKING.toString())) {
            clientTypeEnum = ClientTypeEnum.PICKING;
        } else {
            clientTypeEnum = ClientTypeEnum.WAREHOUSE;
        }


        final ClientTypeEnum f = clientTypeEnum;
        //重新登陆
        Http.post(this, ApiRequest.login(this, uname, password, clientTypeEnum), Api.LOGIN, new HttpCallback<Passport>() {
            @Override
            public void onSuccess(Passport data) {
                Cache.passport = data;
                Cache.passport.setLoginName(uname);
                GlobalDataParam.versionstr = "2.0";
                GlobalDataParam.passportId = data.getPassportId() + "";
                GlobalDataParam.showName = data.getShowName();
                GlobalDataParam.roleValue = data.getRoleValue() + "";
                GlobalDataParam.accessToken = data.getAccessToken();
                Cache.clientTypeEnum = f;
                //判断版本，如果版本合法，跳过去。
                if (UpdateManager.checkUpdate(StartActivity.this, true, Cache.passport.getVersionMessage(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JpushAliasUtil.setJpushAlias(StartActivity.this, Api.getJpushAlias() + Cache.passport.getPassportIdStr());
                        gotoType = 2;
                    }
                })) {
                    JpushAliasUtil.setJpushAlias(StartActivity.this, Api.getJpushAlias() + data.getPassportIdStr());
                    gotoType = 2;
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                gotoType = 1;
            }
        });
    }

    @Override
    public void run() {
        time++;
        if (time > 2) {
            if (gotoType > 0) {
                handler.removeCallbacks(this);
            }
            if (gotoType == 1) {
                Intent intent = new Intent(this, LoginActivity.class);
                intent.putExtra("ver_check", Cache.passport == null ? true : false);
                startActivity(intent);
                return;
            } else if (gotoType == 2) {
                //直接进入
                if (Cache.clientTypeEnum == ClientTypeEnum.WAREHOUSE) {
                    //判断主仓还是子仓
                    if (Cache.passport.isMainWarehouse()) {
                        ActivityManager.startActivityAndFinish(StartActivity.this, ZWarehouseActivity.class);
                    } else {
                        //子仓
                        ActivityManager.startActivityAndFinish(StartActivity.this, MainActivity.class);
                    }
                } else if (Cache.clientTypeEnum == ClientTypeEnum.PICKING) {
                    ActivityManager.startActivityAndFinish(StartActivity.this, PickingMainActivity.class);
                } else {
                    //采购员
                    ActivityManager.startActivityAndFinish(StartActivity.this, BuyerActivity.class);
                }

                return;
            }
        }
        handler.postDelayed(this, 1000);
    }
}
