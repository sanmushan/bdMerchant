package com.zhumg.anlib;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lzy.okgo.OkGo;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ApkUtils;
import com.zhumg.anlib.utils.DialogUtils;
import com.zhumg.anlib.utils.GlobalDataParam;

import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by zhumg on 3/15.
 */
public abstract class AfinalActivity extends FragmentActivity implements AfinalActivityHttpLife {

    public static int top_bg_color_resid = 0;

    public abstract int getContentViewId();

    public abstract void initView(View view);

    protected Dialog loadingDialog;

    protected boolean httpLife = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.addActivity(this);
        int viewid = getContentViewId();
        View rootView = null;
        if (viewid != 0) {
            rootView = View.inflate(this, viewid, null);
            setContentView(rootView);
            ButterKnife.bind(this);
            initView(rootView);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void finish() {
        super.finish();
        httpLife = true;
        OkGo.getInstance().cancelTag(this);
        ActivityManager.removeActivity(this);
    }

    public boolean isHttpFinish() {
        return httpLife;
    }

    /**
     * 设置状态栏背景状态
     */
    public void setTranslucentStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        int resid = top_bg_color_resid;
        if (resid != 0) {
            tintManager.setStatusBarTintResource(resid);// 状态栏无背景
        }
    }

    //网络请求
    public void httpGet(Map map, String url, HttpCallback callback) {
        showLoadingDialog();
        Http.get(this, map, url, callback, loadingDialog);
    }

    //POS
    public void httpPost(Map map, String url, HttpCallback callback) {
        showLoadingDialog();
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID="无法获取设备号";
        if(tm!=null) {
            try {
                DEVICE_ID = tm.getDeviceId();
            } catch (Exception e) {
                DEVICE_ID="无法获取设备号";
                e.printStackTrace();
            }
        }
        map.put("accessToken", GlobalDataParam.accessToken);
        map.put("passportId", GlobalDataParam.passportId);
        map.put("roleValue", GlobalDataParam.roleValue);
        map.put("showName", GlobalDataParam.showName);
        map.put("version",  String.valueOf(ApkUtils.getVersionCode(AfinalActivity.this)));
        map.put("platform","2");
        map.put("device",DEVICE_ID);
        map.put("channel","0");
        Http.post(this, map, url, callback, loadingDialog);
    }

    //POS
    public void httpPostNoShow(Map map, String url, HttpCallback callback) {

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID="无法获取设备号";
        if(tm!=null) {
            try {
                DEVICE_ID = tm.getDeviceId();
            } catch (Exception e) {
                DEVICE_ID="无法获取设备号";
                e.printStackTrace();
            }
        }
        map.put("accessToken", GlobalDataParam.accessToken);
        map.put("passportId", GlobalDataParam.passportId);
        map.put("roleValue", GlobalDataParam.roleValue);
        map.put("showName", GlobalDataParam.showName);
        map.put("version",  String.valueOf(ApkUtils.getVersionCode(AfinalActivity.this)));
        map.put("platform","2");
        map.put("device",DEVICE_ID);
        map.put("channel","0");
        Http.post(this, map, url, callback, loadingDialog);
    }

    protected void showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog = DialogUtils.createLoadingDialog(this, true, true);
        }
        loadingDialog.show();
    }

    protected void closeLoadingDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }
//
//    //TODO 解决 activity 多层 fragment 嵌套时，fragment 的 onActivityResult 无法调用问题
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        FragmentManager fm = getSupportFragmentManager();
//        int index = requestCode >> 16;
//        if (index != 0) {
//            index--;
//            if (fm.getFragments() == null || index < 0
//                    || index >= fm.getFragments().size()) {
//                Log.w("AfinalActivity", "Activity result fragment index out of range: 0x"
//                        + Integer.toHexString(requestCode));
//                onActivityResultImpl(requestCode, resultCode, data);
//                return;
//            }
//            Fragment frag = fm.getFragments().get(index);
//            if (frag == null) {
//                Log.w("AfinalActivity", "Activity result no fragment exists for index: 0x"
//                        + Integer.toHexString(requestCode));
//            } else {
//                handleResult(frag, requestCode, resultCode, data);
//            }
//            return;
//        }
//        onActivityResultImpl(requestCode, resultCode, data);
//    }
//
//    protected void onActivityResultImpl(int requestCode, int resultCode, Intent data) {
//    }
//
//    /**
//     * 递归调用，对所有子Fragement生效
//     *
//     * @param frag
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     */
//    private void handleResult(Fragment frag, int requestCode, int resultCode,
//                              Intent data) {
//        frag.onActivityResult(requestCode & 0xffff, resultCode, data);
//        List<Fragment> frags = frag.getChildFragmentManager().getFragments();
//        if (frags != null) {
//            for (Fragment f : frags) {
//                if (f != null)
//                    handleResult(f, requestCode, resultCode, data);
//            }
//        }
//    }
}
