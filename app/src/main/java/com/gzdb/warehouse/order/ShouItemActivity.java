package com.gzdb.warehouse.order;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.OSSException;
import com.gzdb.response.Api;
import com.gzdb.response.ConsumerOrderItem;
import com.gzdb.response.OrderDetail;
import com.gzdb.utils.ImageUploadUtil;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.StringUtils;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;

/**
 * Created by zhumg on 4/21.
 */
public class ShouItemActivity extends AfinalActivity {

    BaseTitleBar baseTitleBar;
    OrderDetail detail;

    @Bind(R.id.fr_listview)
    ListView listView;

    @Bind(R.id.btn1)
    View btn1;

    @Bind(R.id.img_signature)
    ImageView img_signature;

    List<ConsumerOrderItem> items = new ArrayList<>();
    ShouItemAdapter adapter;

    private static final int TAKE_PHOTO = 0;
    private static final int PICK_PHOTO = 1;
    private static final int MY_PERMISSIONS_REQUEST_CALL_CAMERA = 1010;//请求码
    private static final int _IMG_SIGNATURE = 1001;
    private Map<Integer,String> mImagePaths;
    String imgSignature = null;

    @Override
    public int getContentViewId() {
        return R.layout.activity_shou_item;
    }

    @Override
    public void initView(View view) {

        setTranslucentStatus();

        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setCenterTxt("收货信息");
        baseTitleBar.setLeftBack(this);
        mImagePaths = new HashMap<>();
        detail = (OrderDetail) getIntent().getSerializableExtra("detail");

        List<ConsumerOrderItem> os = detail.getItemSnapshotArray();
        for (int i = 0; i < os.size(); i++) {
            ConsumerOrderItem item = os.get(i);
            if (item.getUnReceiptQuantity() <= 0) {
                continue;
            }
            item.setShouCount(item.getUnReceiptQuantity());
            items.add(item);
        }

        adapter = new ShouItemAdapter(this, items);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        img_signature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照上传
                camera(_IMG_SIGNATURE);
            }
        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //收货
                if (null != mImagePaths.get(_IMG_SIGNATURE)) {
                    imgSignature = mImagePaths.get(_IMG_SIGNATURE);
                    shou();
                }else{
                    ToastUtil.showToast(ShouItemActivity.this, "请拍照收货单签名照上传");
                    return;
                }
            }
        });
    }

    private void img() {
        Map map = new HashMap();
        map.put("orderId", String.valueOf(detail.getOrderId()));
        map.put("passportId", String.valueOf(Cache.passport.getPassportIdStr()));
        map.put("url", imgSignature);//签名图url
        Log.e("linbin","--imgSignature--"+imgSignature);
        Http.post(ShouItemActivity.this, map, Api.ORDER_RECEIPTIMG, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                ToastUtil.showToast(ShouItemActivity.this, msg);
                //todo 关闭掉 详情----------------------------------------------------------
                Log.e("linbin","----onSuccess---签名图url");
                ActivityManager.finishActivity(OrderDetailActivity.class);
                finish();
            }

            @Override
            public void onFailure() {
                super.onFailure();
                ToastUtil.showToast(ShouItemActivity.this, msg);
                Log.e("linbin","---onFailure----签名图url");
            }
        }.setPass());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case _IMG_SIGNATURE:

                    smallBitmap(_IMG_SIGNATURE, data, img_signature);

                    break;
            }
        }
    }

    protected void smallBitmap(int pickCode, Intent data, ImageView image) {
        ContentResolver resolver = getContentResolver();
        //照片的原始资源地址
        Uri originalUri = data.getData();
        try {
            Bitmap bitmap = null;

            if (null == originalUri) {
                Bundle bundle = data.getExtras();
                bitmap = (Bitmap) bundle.get("data");
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
            }

            //使用ContentProvider通过URI获取原始图片
            image.setImageBitmap(bitmap);
            oSS(pickCode, bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void camera(final int pickCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("图片来源");
        builder.setNegativeButton("取消", null);
        builder.setItems(new String[]{"拍照", "打开相册"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case TAKE_PHOTO:

                        if (ContextCompat.checkSelfPermission(ShouItemActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            //如果没有授权，则请求授权
                            ActivityCompat.requestPermissions(ShouItemActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CALL_CAMERA);
                        } else {
                            //有授权，直接开启摄像头
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, pickCode);
                        }
                        break;
                    case PICK_PHOTO:
                        //从相册获取照片
                        Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                        intent2.setType("image/*");
                        startActivityForResult(intent2, pickCode);
                        break;
                }

            }

        });
        builder.create().show();

    }

    private void oSS(final int pickCode, final Bitmap bm) {
        showLoadingDialog();
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
                singleThreadExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ImageUploadUtil.doUploadRegisters(bm, new SaveCallback() {
                                @Override
                                public void onSuccess(String arg0) {
                                    try {
                                        String oosPath = "http://oss.0085.com/" + arg0;
                                        Log.e("上传成功", "========================================= 图片地址:" + oosPath);
                                        mImagePaths.put(pickCode, oosPath);
                                    } catch (Exception e) {
                                        Log.e("Exception", " : " + e);
                                    }
                                    closeLoadingDialog();
                                }

                                @Override
                                public void onFailure(String arg0, OSSException arg1) {
                                    Log.e("uploadInBackground", "上传失败" + arg0 + "Exception:" + arg1);

                                }

                                @Override
                                public void onProgress(String arg0, int arg1, int arg2) {

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
            }
        });
    }


    void shou() {
        int show_count = 0;
        //弹界面出来
        JSONObject jsonObject = new JSONObject();
        for (int i = 0; i < items.size(); i++) {
            ConsumerOrderItem item = items.get(i);
            int sc = item.getShouCount();
            if (sc > 0) {
                try {
                    show_count += sc;
                    jsonObject.put(String.valueOf(item.getItemSnapshotId()), String.valueOf(sc));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (show_count <= 0) {
            ToastUtil.showToast(this, "请输入收货数量");
            return;
        }
        Map map = new HashMap();
        map.put("orderId", String.valueOf(detail.getOrderId()));
        map.put("passportId", String.valueOf(Cache.passport.getPassportIdStr()));
        map.put("itemSnapshotSet", jsonObject.toString());
        Http.post(this, map, Api.BATCH_RECEIPT, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                ToastUtil.showToast(ShouItemActivity.this, msg);
                img();
            }

            @Override
            public void onFailure() {
                super.onFailure();

            }
        }.setPass());
    }

}
