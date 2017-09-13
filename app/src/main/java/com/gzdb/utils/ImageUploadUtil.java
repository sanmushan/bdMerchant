package com.gzdb.utils;

import android.graphics.Bitmap;
import android.util.Log;

import com.aliyun.mbaas.oss.OSSClient;
import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.AccessControlList;
import com.aliyun.mbaas.oss.model.OSSException;
import com.aliyun.mbaas.oss.model.TokenGenerator;
import com.aliyun.mbaas.oss.storage.OSSBucket;
import com.aliyun.mbaas.oss.storage.OSSData;
import com.aliyun.mbaas.oss.storage.OSSFile;
import com.aliyun.mbaas.oss.util.OSSToolKit;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

/**
 * Created by chenqi on 2015-09-24.
 */
public class ImageUploadUtil {

    public static OSSBucket sampleBucket;

    static {
        OSSClient.setGlobalDefaultTokenGenerator(new TokenGenerator() { // 设置全局默认加签器
            @Override
            public String generateToken(String httpMethod, String md5, String type, String date,
                                        String ossHeaders, String resource) {

                String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date + "\n" + ossHeaders
                        + resource;

                return OSSToolKit.generateToken(Const.ACCESSKEY, Const.SCRECTKEY, content);
            }
        });
        OSSClient.setGlobalDefaultACL(AccessControlList.PUBLIC_READ); // 设置全局默认bucket访问权限

        /*
         * 如果需要采用服务端加签的方式，可以直接在tokenGenerator中向你的业务服务器发起
         * 同步http post请求，把相关字段拼接之后发过去，然后获得加签结果。
         *
         *
        OSSClient.setGlobalDefaultTokenGenerator(new TokenGenerator() {
            @Override
            public String generateToken(String httpMethod, String md5, String type, String date,
                    String ossHeaders, String resource) {

                String content = httpMethod + "\n" + md5 + "\n" + type + "\n" + date + "\n" + ossHeaders
                        + resource;

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("content", content));
                HttpPost post = new HttpPost("http://localhost/oss");
                String sign = null;

                try {
                    post.setEntity(new UrlEncodedFormEntity(params));
                    HttpResponse response = new DefaultHttpClient().execute(post);
                    sign = EntityUtils.toString(response.getEntity()).trim();
                } catch (Exception ignore) {
                }
                Log.d("OSS_Test", "[genToken] - remote: " + sign);
                return sign;
            }
        });

         ********************************************************
         * 以下是加签服务器的代码示例： (nginx + lua脚本)
         *
         * local access_key = "ak";
         * local screct_key = "sk";
         * local sign_str;
         *
         * ngx.req.read_body();
         * --local body = ngx.req.get_body_data();
         *
         * local args, err = ngx.req.get_post_args();
         * for key, val in pairs(args) do
         *         if key == "content" then
         *                 sign_str = val;
         *         end
         * end
         *
         * local sign_result = ngx.encode_base64(ngx.hmac_sha1(screct_key, sign_str));
         *
         * ngx.say("OSS "..access_key..":"..sign_result);
         ********************************************************/
    }

    public static void initSampleBucket() {
        // 开始单个Bucket的设置
        sampleBucket = new OSSBucket("qidianimg");
        sampleBucket.setBucketHostId("oss-cn-shenzhen.aliyuncs.com"); // 可以在这里设置数据中心域名或者cname域名
        //sampleBucket.setBucketACL(AccessControlList.PUBLIC_READ_WRITE); // 如果这个Bucket跟全局默认的权限设置不一致
        sampleBucket.setBucketACL(AccessControlList.PRIVATE);
        // sampleBucket.setBucketHostId("oss-cn-hangzhou.aliyuncs.com"); // 如果这个Bucket跟全局默认的数据中心不一致，就需要单独设置
        // sampleBucket.setBucketTokenGen(new TokenGenerator() {...}); // 如果这个Bucket跟全局默认的加签方法不一致，就需要单独设置
    }


    /*
    * 下载
     */
    public static void doDownload() throws Exception {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                OSSData ossData = new OSSData(sampleBucket, "hello.txt");
                try {
                    ossData.get();
                } catch (OSSException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        new Thread(r).start();
    }

    /*
     *  上传图片data
    */
    public static void doUpload(Bitmap b) throws Exception {
        initSampleBucket();
        try {
            byte[] by = Bitmap2Bytes(b);
            Calendar c = Calendar.getInstance();
            int month = c.get(Calendar.MONTH) + 1;
            int date = c.get(Calendar.DATE);
            String m = month < 10 ? "0" + month : month + "";
            String d = date < 10 ? "0" + date : date + "";
            Log.e("aaaa", c.get(Calendar.YEAR) + "/" + m + d + "/" + c.getTimeInMillis() + ".jpg");

            OSSData ossData2 = new OSSData(sampleBucket, "courier/" + c.get(Calendar.YEAR) + "/" + m + d + "/" + c.getTimeInMillis() + ".jpg");
            ossData2.setData(by, "jpg");
            ossData2.uploadInBackground(new SaveCallback() {
                @Override
                public void onProgress(String arg0, int arg1, int arg2) {
                    Log.e("uploadInBackground", arg2 + "--->" + arg0 + "---->" + arg1);
                }

                @Override
                public void onFailure(String arg0, OSSException arg1) {
                    Log.e("uploadInBackground", "上传失败");
                }

                @Override
                public void onSuccess(String arg0) {
                    Log.e("上传成功", arg0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void doUploadRegisters(Bitmap b, SaveCallback newSaveCallBack) throws Exception {
        initSampleBucket();
        try {
            byte[] by = Bitmap2Bytes(b);
            final Calendar c = Calendar.getInstance();
            int month = c.get(Calendar.MONTH) + 1;
            int date = c.get(Calendar.DATE);
            String m = month < 10 ? "0" + month : month + "";
            String d = date < 10 ? "0" + date : date + "";
            Log.e("aaaa", c.get(Calendar.YEAR) + "/" + m + d + "/" + c.getTimeInMillis() + ".jpg");

            OSSData ossData2 = new OSSData(sampleBucket, "courier/" + c.get(Calendar.YEAR) + "/" + m + d + "/" + c.getTimeInMillis() + ".jpg");
            ossData2.setData(by, "jpg");
            ossData2.uploadInBackground(newSaveCallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 上传文件
     *
     * @throws Exception
     */
    public void doUploadFile() throws Exception {
        OSSFile ossFile = new OSSFile(sampleBucket, "test.jpg");
        ossFile.setUploadFilePath("/storage/sdcard0/src_file/test.jpg", "image/jpg");
        ossFile.uploadInBackground(new SaveCallback() {

            @Override
            public void onProgress(String arg0, int arg1, int arg2) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFailure(String arg0, OSSException arg1) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onSuccess(String arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
