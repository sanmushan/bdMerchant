package com.gzdb.utils;

import android.content.Context;
import android.util.Log;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by Administrator on 2017/3/23 0023.
 */

public class JpushAliasUtil {

    private static final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    logs = "设置极光别名成功 ( " + alias + " )";
                    Log.e("db", logs);
                    break;
                case 6002:
                    logs = "设置极光 tag and alias timeout. Try again after 60s.";
//                    if (N.isConnected(mContext)) {
//                        mHandler.sendMessageDelayed(
//                                mHandler.obtainMessage(MSG_SET_ALIAS, alias),
//                                1000 * 60);
//                    } else {
                    Log.e("db", "设置极光 No network");
//                    }
                    break;
                default:
                    logs = "设置极光 tag and alias Failed with errorCode = " + code;
                    Log.e("db", logs);
            }
            // ExampleUtil.showToast(logs, getApplicationContext());
        }
    };

    public static void setJpushAlias(Context context, String alias) {
        Log.e("db", "需要设置极光别名：" + alias);
        JPushInterface.setAliasAndTags(context,
                alias, null, mAliasCallback);
    }
}
