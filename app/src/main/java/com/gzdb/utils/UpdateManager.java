package com.gzdb.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.gzdb.response.Api;
import com.gzdb.response.VersionInfo;
import com.gzdb.response.enums.ClientTypeEnum;
import com.gzdb.response.enums.DeviceTypeEnum;
import com.gzdb.response.enums.VersionTypeEnum;
import com.gzdb.warehouse.Cache;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ApkUtils;
import com.zhumg.anlib.utils.DialogUtils;
import com.zhumg.anlib.widget.dialog.TipClickListener;
import com.zhumg.anlib.widget.dialog.TipDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class UpdateManager {

    public static boolean checkUpdate(final Context context, boolean showTi, final VersionInfo versionInfo, final View.OnClickListener listener) {
        if(versionInfo == null) return false;
        if(versionInfo.getUpgradeType() == VersionTypeEnum.FORCE_UPGRADE.getKey()) {
            //请更新到最新版本，强制更新
            final TipDialog tipDialog = new TipDialog(context);
            tipDialog.setTitle(versionInfo.getTitle());
            tipDialog.setContentMsg(versionInfo.getIntroduce());
            tipDialog.setClick_close(false);
            tipDialog.setLeftBtn("退出应用");
            tipDialog.setRightBtn("马上更新");
            tipDialog.setTipClickListener(new TipClickListener() {
                @Override
                public void onClick(boolean left) {
                    if(left) {
                        ActivityManager.AppExit(context);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(versionInfo.getVersionUrl()));
                        context.startActivity(intent);
                        ActivityManager.AppExit(context);
                    }
                }
            });
            tipDialog.show();
            return false;
        } else if(versionInfo.getUpgradeType() == VersionTypeEnum.UN_FORCE_UPGRADE.getKey()) {
            //有一个版本可更新，不强制
            final TipDialog tipDialog = new TipDialog(context);
            tipDialog.setTitle(versionInfo.getTitle());
            tipDialog.setContentMsg(versionInfo.getIntroduce());
            tipDialog.setClick_close(false);
            tipDialog.setLeftBtn("取消");
            tipDialog.setRightBtn("马上更新");
            tipDialog.setTipClickListener(new TipClickListener() {
                @Override
                public void onClick(boolean left) {
                    if(left) {
                        tipDialog.dismiss();
                        if(listener != null) {
                            listener.onClick(null);
                        }
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(versionInfo.getVersionUrl()));
                        context.startActivity(intent);
                        ActivityManager.AppExit(context);
                    }
                }
            });
            tipDialog.show();
            return false;
        } else if(showTi) {
            DialogUtils.createTipDialog(context, "你的版本已是最新版", "确定").show();
            return true;
        } else {
            return true;
        }
    }

    public static void httpCheckUpdate(final Context c) {
        Map map = new HashMap();
        map.put("deviceType", String.valueOf(DeviceTypeEnum.DEVICE_TYPE_ANDROID.getType()));
        map.put("clientType", String.valueOf(ClientTypeEnum.WAREHOUSE.getKey()));
        map.put("versionIndex", String.valueOf(ApkUtils.getVersionCode(c)));
        Http.post(c, map, Api.VERSION_UPGRADE, new HttpCallback<VersionInfo>() {
            @Override
            public void onSuccess(VersionInfo data) {
                if(Cache.passport != null) {
                    Cache.passport.setVersionMessage(data);
                }
                checkUpdate(c, true, data, null);
            }
        });
    }

    public static void httpCheckUpdate(final Context c, final boolean showTi) {
        Map map = new HashMap();
        map.put("deviceType", String.valueOf(DeviceTypeEnum.DEVICE_TYPE_ANDROID.getType()));
        map.put("clientType", String.valueOf(ClientTypeEnum.WAREHOUSE.getKey()));
        map.put("versionIndex", String.valueOf(ApkUtils.getVersionCode(c)));
        Http.post(c, map, Api.VERSION_UPGRADE, new HttpCallback<VersionInfo>() {
            @Override
            public void onSuccess(VersionInfo data) {
                if(Cache.passport != null) {
                    Cache.passport.setVersionMessage(data);
                }
                checkUpdate(c, showTi, data, null);
            }
        });
    }
}
