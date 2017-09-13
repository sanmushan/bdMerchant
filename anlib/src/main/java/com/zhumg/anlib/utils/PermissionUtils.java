package com.zhumg.anlib.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;

import com.zhumg.anlib.widget.dialog.TipClickListener;
import com.zhumg.anlib.widget.dialog.TipDialog;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by zhumg on 2017/3/24 0024.
 */

public class PermissionUtils {


    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private PermissionCallback permissionCallback = null;
    private List<String> open = new ArrayList<>();
    private List<String> close = new ArrayList<>();
    private Activity activity;
    private String[] permissions;
    private String[] infos;

    public PermissionUtils(Activity activity, String[] permissions, String[] infos, PermissionCallback callback) {
        this.activity = activity;
        this.permissions = permissions;
        this.infos = infos;
        this.permissionCallback = callback;
    }

    public interface PermissionCallback {
        /**
         * 所有权限都通过
         */
        public void onAllSuccess();

        /**
         * 指定某个权限失败
         */
        public void onFailde();
    }

    private boolean selfPermissionGranted(Context context, String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int targetSdkVersion = 0;
            try {
                final PackageInfo info = context.getPackageManager().getPackageInfo(
                        context.getPackageName(), 0);
                targetSdkVersion = info.applicationInfo.targetSdkVersion;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            if (targetSdkVersion >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = context.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(context, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }

    /**
     * 检测 权限
     */
    public void checkPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            permissionCallback.onAllSuccess();
            return;
        }

        open.clear();
        close.clear();

        for (int i = 0; i < permissions.length; i++) {
            //检测 是否开启某权限了
            if (selfPermissionGranted(activity, permissions[i])) {
                open.add(permissions[i]);
            } else {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // close.add(permissions[i]);
                    if (activity.shouldShowRequestPermissionRationale(permissions[i])) {
                        close.add(permissions[i]);
                    } else {
                        open.add(permissions[i]);
                    }
                }
            }
        }

        if (close.size() > 0) {
            //弹出系统级别的权限申请
            Log.e("db", "申请权限");
            ActivityCompat.requestPermissions(activity, close.toArray(new String[close.size()]), 1);
        } else {
            permissionCallback.onAllSuccess();
        }
    }

    private void showMessageOKCancel(final Activity context, String message) {
        final TipDialog tipDialog = new TipDialog(context);
        tipDialog.setContentMsg(message);
        tipDialog.setTipClickListener(new TipClickListener() {
            @Override
            public void onClick(boolean left) {
                permissionCallback.onFailde();
                if (!left) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Log.d(TAG, "getPackageName(): " + context.getPackageName());
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                    context.startActivity(intent);
                }
            }
        });
        tipDialog.show();
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public final boolean isOpen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }

    public void onRequestPermissionsResult(final Activity activity, int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            //如果没有
            boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i]);
            //未开通
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (isTip) {//表明用户没有彻底禁止弹出权限请求
                    //requestPermission(PermissionHelper.getInstance().filterPermissions(permissions));
                } else {//表明用户已经彻底禁止弹出权限请求
                    //   PermissionMonitorService.start(this);//这里一般会提示用户进入权限设置界面
                    showMessageOKCancel(activity, infos[i]);
                }
                return;
            }
        }
    }
}
