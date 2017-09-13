package com.gzdb.warehouse;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.gprinter.command.GpCom;
import com.gprinter.io.GpDevice;
import com.gprinter.io.PortParameters;
import com.gprinter.save.PortParamDataBase;
import com.gprinter.service.GpPrintService;
import com.gzdb.gprinter.service.*;
import com.gzdb.utils.baidu.BaiduLocationManager;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.utils.SpUtils;
import com.zhumg.anlib.widget.bar.BaseTitleBar;
import com.zhumg.anlib.widget.bar.SearchTitleBar;

import java.io.File;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017/4/15 0015.
 */

public class App extends Application {
    private static Handler sUIHandler = new Handler(Looper.getMainLooper());
    public static Handler getUIHandler() {
        return sUIHandler;
    }
    private PortParameters mPortParam[]         = new PortParameters[GpPrintService.MAX_PRINTER_CNT];
    private  int mPrinterId=0;
    @Override
    public void onCreate() {
        super.onCreate();

        //DEBUG模式
        Http.Debug = true;

        SpUtils.init(this, "dbmsp");

        //设置客户端皮肤
        skin();

        //图片相关
        initImageLoader(this);

        //初始化http
        Http.init(this);

        //百度
        BaiduLocationManager.init(this);

        //极光初始化
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        initPortParam();

        mPortParam[mPrinterId].setPortType(4);
        mPortParam[mPrinterId].setPortNumber(0);

        mPortParam[mPrinterId].setBluetoothAddr(SpUtils.loadValue("bluetooth"));
        if (CheckPortParamters(mPortParam[mPrinterId])) {
            PortParamDataBase database = new PortParamDataBase(this);
            database.deleteDataBase("" + mPrinterId);
            database.insertPortParam(mPrinterId, mPortParam[mPrinterId]);
        }



    }
    private void initPortParam() {

        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            PortParamDataBase database = new PortParamDataBase(this);
            mPortParam[i] = new PortParameters();
            mPortParam[i] = database.queryPortParamDataBase("" + i);
            mPortParam[i].setPortOpenState(false);
        }
    }
    Boolean CheckPortParamters(PortParameters param) {
        boolean rel = false;
        int type = param.getPortType();
        if (type == PortParameters.BLUETOOTH) {
            if (!param.getBluetoothAddr().equals("")) {
                rel = true;
            }
        }
        return rel;
    }


    // 皮肤相关
    private void skin() {

        AfinalActivity.top_bg_color_resid = R.color.blue;

        BaseTitleBar.bar_color_resid = R.color.blue;
        BaseTitleBar.back_btn_img_resid = R.drawable.ico_back;

        SearchTitleBar.search_left_img_resid = R.drawable.ico_seek;
        SearchTitleBar.search_btn_x_resid = R.drawable.ico_search_close;
        SearchTitleBar.search_edittext_bg_resid = R.drawable.bg_et_search;
    }

    /**
     * 【】(初始化图片加载器)
     *
     * @param context
     */
    // *************************************************************************
    private void initImageLoader(Context context) {
        File cacheDir = new File(Cache.IMAGE_FOLDER);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        //设置默认的配置，设置缓存，这里不设置可以到别的地方设置
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.drawable.d_img) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.d_img)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.d_img) // 设置图片加载/解码过程中错误时候显示的图片
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                .displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                //.memoryCacheExtraOptions(480, 800) //即保存的每个缓存文件的最大长宽
                .threadPoolSize(3) //线程池内加载的数量
                .threadPriority(Thread.NORM_PRIORITY - 2)
                //解释：当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
                .denyCacheImageMultipleSizesInMemory()  //拒绝缓存多个图片。
                .memoryCache(new WeakMemoryCache()) //缓存策略你可以通过自己的内存缓存实现 ，这里用弱引用，缺点是太容易被回收了，不是很好！
                .memoryCacheSize(2 * 1024 * 1024) //设置内存缓存的大小
                .diskCacheSize(50 * 1024 * 1024) //设置磁盘缓存大小 50M
                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO) //设置图片下载和显示的工作队列排序
                .diskCacheFileCount(100) //缓存的文件数量
                .diskCache(new UnlimitedDiskCache(cacheDir)) //自定义缓存路径
                .defaultDisplayImageOptions(defaultOptions) //显示图片的参数，默认：DisplayImageOptions.createSimple()
                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .writeDebugLogs() //打开调试日志
                .build();//开始构建

        ImageLoader.getInstance().init(config);
    }

}
