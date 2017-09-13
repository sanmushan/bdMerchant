package com.zhumg.anlib.utils;


import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkUtils {

    /**
     * 安装 apk
     */
    public static void install(Context context, File uriFile) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(uriFile), "application/vnd.android.package-archive");
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    /**
     * 卸载APK
     */
    public static void uninstall(Context context, String packageName) {
        Uri packageURI = Uri.parse("package:" + packageName);

        Intent intent = new Intent("android.intent.action.DELETE", packageURI);

        context.startActivity(intent);
    }


    public static boolean isAvilible(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();

        List packageInfos = packageManager.getInstalledPackages(0);

        List packageNames = new ArrayList();

        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = ((PackageInfo) packageInfos.get(i)).packageName;
                packageNames.add(packName);
            }
        }

        return packageNames.contains(packageName);
    }

    public static boolean isAppInstall(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        List packageInfos = packageManager.getInstalledPackages(0);
        List packageNames = new ArrayList();
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String packName = ((PackageInfo) packageInfos.get(i)).packageName;
                packageNames.add(packName);
            }
        }
        return packageNames.contains(packageName);
    }

    public static String getChannelFromApk(Context context, String channelPrefix) {
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;

        String key = "META-INF/" + channelPrefix;
        String ret = "";
        ZipFile zipfile = null;
        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration entries = zipfile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String entryName = entry.getName();
                if (entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String[] split = ret.split(channelPrefix);
        String channel = "";
        if ((split != null) && (split.length >= 2)) {
            channel = ret.substring(key.length());
        }
        return channel;
    }

    public static int getVersionCode(Context context) {
        int versionCode = 0;
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            versionCode = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}

