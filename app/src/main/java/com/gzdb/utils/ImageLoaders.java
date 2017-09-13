package com.gzdb.utils;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.gzdb.warehouse.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;


import java.io.InputStream;


/**
 * 作   者：liyunbiao
 * 时   间：16/4/11.
 * 修 改 人：
 * 日   期：
 * 描   述：
 */
public class ImageLoaders {
    private static DisplayImageOptions options;


    public static class MyListener implements ImageLoadingListener {
        private Context context;

        public MyListener(Context context) {
            this.context = context;
        }

        @Override
        public void onLoadingCancelled(String arg0, View arg1) {

        }

        @Override
        public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {

        }

        @Override
        public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
            System.out.println("=========");
            System.out.println(arg0);
            System.out.println(arg2);
            System.out.println("=========");
            try {
                if (arg0.contains("_index")) {
                    arg0 = arg0.replace("_index", "");
                    if (arg1 != null)
                        ImageLoader.getInstance().displayImage(arg0, (ImageView) arg1, getOptions(context), this);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void onLoadingStarted(String arg0, View arg1) {
            // TODO Auto-generated method stub

        }

    }

    public static void display(Context context, ImageView imageView, String uri) {

      //  if(NetUtil.isNetworkConnected(context)){
            if (imageView == null || uri == null || uri.equals("")) {
                ImageLoader.getInstance().displayImage(uri, imageView, getOptions(context), new MyListener(context));
                return;
            }
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
            if (!uri.equals(imageView.getTag())) {
                imageView.setTag(uri);
                ImageLoader.getInstance().displayImage(uri, imageView, getOptions(context), new MyListener(context));
            }
      //  }

    }

    public static void display(Context context, ImageView imageView, String uri,int defaultImg) {
        try{

            DisplayImageOptions options = new  DisplayImageOptions.Builder()
                    .showImageOnLoading(defaultImg) // 设置图片在下载期间显示的图片
                    .showImageForEmptyUri(defaultImg)// 设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(defaultImg) // 设置图片加载/解码过程中错误时候显示的图片
                    .cacheInMemory(false)// 设置下载的图片是否缓存在内存中
                    .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                    // .decodingOptions(android.graphics.BitmapFactory.Options
                    // decodingOptions)//设置图片的解码配置
                    // .delayBeforeLoading(int delayInMillis)//int
                    // delayInMillis为你设置的下载前的延迟时间
                    // 设置图片加入缓存前，对bitmap进行设置
                    // .preProcessor(BitmapProcessor preProcessor)

                    .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                    // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                    .displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
                    .build();
            if (imageView == null || uri == null || uri.equals("")) {
                ImageLoader.getInstance().displayImage(uri, imageView, options, new MyListener(context));
                return;
            }

            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
            if (!uri.equals(imageView.getTag())) {
                imageView.setTag(uri);
                ImageLoader.getInstance().displayImage(uri, imageView, options, new MyListener(context));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void display(Context context, ImageView imageView, String uri, ImageLoadingListener ImageLoadingListener) {

        if (imageView == null || uri == null || uri.equals("")) {
            ImageLoader.getInstance().displayImage(uri, imageView, getOptions(context), ImageLoadingListener);
            return;
        }
        if (!uri.equals(imageView.getTag())) {
            imageView.setTag(uri);
            ImageLoader.getInstance().displayImage(uri, imageView, getOptions(context), ImageLoadingListener);
        }
    }

    private static DisplayImageOptions getOptions(Context context) {
        if (options == null) {
            options = getBitmapOptions(context);
        }
        return options;
    }

    private static DisplayImageOptions getBitmapOptions(Context context) {
        return new DisplayImageOptions.Builder()//
                .showImageOnLoading(R.drawable.app_icon) // 设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.app_icon)// 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.app_icon) // 设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false)// 设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)// 设置下载的图片是否缓存在SD卡中
                .considerExifParams(true) // 是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)// 设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
                // .decodingOptions(android.graphics.BitmapFactory.Options
                // decodingOptions)//设置图片的解码配置
                // .delayBeforeLoading(int delayInMillis)//int
                // delayInMillis为你设置的下载前的延迟时间
                // 设置图片加入缓存前，对bitmap进行设置
                // .preProcessor(BitmapProcessor preProcessor)

                .resetViewBeforeLoading(true)// 设置图片在下载前是否重置，复位
                // .displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                .displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
                .build();// 构建完成
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap readBitMap(Context context, int resId) {

        BitmapFactory.Options opt = new BitmapFactory.Options();

        opt.inPreferredConfig = Bitmap.Config.RGB_565;

        opt.inPurgeable = true;

        opt.inInputShareable = true;

        // 获取资源图片

        InputStream is = context.getResources().openRawResource(resId);

        return BitmapFactory.decodeStream(is, null, opt);

    }
}
