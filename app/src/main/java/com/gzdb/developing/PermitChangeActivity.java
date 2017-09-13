package com.gzdb.developing;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.OSSException;
import com.gzdb.response.Api;
import com.gzdb.utils.*;
import com.gzdb.warehouse.App;
import com.gzdb.warehouse.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;

/**
 * Created by liyunbiao on 2017/6/2.
 */
public class PermitChangeActivity  extends AfinalActivity implements View.OnClickListener {
    BaseTitleBar baseTitleBar;
    @Bind(R.id.img_permit01)
    ImageView img_permit01;
    @Bind(R.id.img_permit02)
    ImageView img_permit02;

    @Bind(R.id.edit_businesslicense_no)
    EditText edit_businesslicense_no;

    @Bind(R.id.edit_permitno)
    EditText edit_permitno;

    public String imagPath = "";
    public String permitImg = "";
    public String businesslicenseImg = "";
    private int id = 0;
    Bitmap bm = null;
    Dialog wheelViewDialog;
    @Override
    public int getContentViewId() {
        return R.layout.activty_permit;
    }

    @Override
    public void initView(View view) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("修改供应商");
        img_permit01.setOnClickListener(this);
        img_permit02.setOnClickListener(this);
        baseTitleBar.setRightTxt("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDeveLoping();
            }
        });
        if(GlobalData.evelopingBean==null)
        {
            return;
        }
        if(GlobalData.evelopingBean.getBusinesslicenseNo()!=null) {
            edit_businesslicense_no.setText(GlobalData.evelopingBean.getBusinesslicenseNo());
        }
        if(GlobalData.evelopingBean.getPermitNo()!=null) {
            edit_permitno.setText(GlobalData.evelopingBean.getPermitNo());
        }
        ImageLoaders.display(PermitChangeActivity.this,img_permit01,GlobalData.evelopingBean.getBusinesslicenseImg());
        ImageLoaders.display(PermitChangeActivity.this,img_permit02,GlobalData.evelopingBean.getPermitImg());

    }

    private void saveDeveLoping() {


        if (edit_permitno.getText().toString().length() == 0) {
            ToastUtil.showToast(PermitChangeActivity.this, "请输入正确的许可证编号");
            return;
        }
        if (edit_businesslicense_no.getText().toString().length() == 0) {
            ToastUtil.showToast(PermitChangeActivity.this, "请输入正确的营业执照号");
            return;
        }
        if (permitImg.length() == 0) {
            ToastUtil.showToast(PermitChangeActivity.this, "请输上传许可证");
            return;
        }
        if (businesslicenseImg.length() == 0) {
            ToastUtil.showToast(PermitChangeActivity.this, "请输上传营业执照");
            return;
        }
        Map map = new HashMap();

        map.put("achieveId", GlobalData.achieveId);
        map.put("permitImg", permitImg);
        map.put("businesslicenseNo", edit_businesslicense_no.getText().toString());
        map.put("businesslicenseImg",businesslicenseImg);
        map.put("permitNo", edit_permitno.getText().toString());
        httpPost(map, Api.BasedeveloingURL() + "json/achieve/save_permission", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                setResult(RESULT_OK);
                PermitChangeActivity.this.finish();
            }
        });
    }

    private static final int TAKE_PICTURE = 0x000001;

    /**
     * 拍照
     */
    public void photo() {
        try {
            Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.EXTRA_SHOW_ACTION_ICONS);
            startActivityForResult(openCameraIntent, TAKE_PICTURE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void selPic() {
        Intent intent = new Intent();
                /* 开启Pictures画面Type设定为image */
        intent.setType("image/*");
                /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
                /* 取得相片后返回本画面 */
        startActivityForResult(intent, 2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                startActivity(new Intent(PermitChangeActivity.this, NextDevelopingActivity.class));
                break;
            case R.id.img_permit01:
                id = R.id.img_permit01;
                wheelViewDialog = createWheelViewDialog();
                wheelViewDialog.show();
                break;
            case R.id.img_permit02:
                id = R.id.img_permit02;
                wheelViewDialog = createWheelViewDialog();
                wheelViewDialog.show();
                break;
        }
    }
    public Dialog createWheelViewDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(PermitChangeActivity.this, R.layout.showdialog,
                null);

        TextView tv_photo = (TextView) view.findViewById(R.id.tv_photo);
        tv_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                selPic();
            }
        });
        TextView tv_photos = (TextView) view.findViewById(R.id.tv_photos);
        tv_photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                photo();
            }
        });
        dialog.setContentView(view);
        return dialog;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());
                    final Bitmap bm = (Bitmap) data.getExtras().get("data");

                    // FileUtils.saveBitmap(bm, fileName);
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setImagePath(Const.getUrl() + fileName + ".png");
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                    showLoadingDialog();
                    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
                    singleThreadExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {

                                ImageUploadUtil.doUploadRegisters(bm, new SaveCallback() {
                                    @Override
                                    public void onSuccess(String arg0) {
                                        try {
                                            imagPath = "http://oss.0085.com/" + arg0;
                                            App.getUIHandler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (id == R.id.img_permit01) {
                                                        img_permit01.setImageBitmap(bm);
                                                        businesslicenseImg=imagPath;
                                                    } else if (id == R.id.img_permit02) {
                                                        img_permit02.setImageBitmap(bm);
                                                        permitImg=imagPath;
                                                    }

                                                }
                                            }, 100);
                                        } catch (Resources.NotFoundException e) {
                                            e.printStackTrace();
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


                    //adapter.update();
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {

                    String fileName = String.valueOf(System.currentTimeMillis());
                    Uri originalUri = data.getData();

                    try {

                        ContentResolver resolver = getContentResolver();
                        if (null == originalUri) {
                            Bundle bundle = data.getExtras();
                            bm = (Bitmap) bundle.get("data");
                        } else {
                            bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // FileUtils.saveBitmap(bm, fileName);
                    ImageItem takePhoto = new ImageItem();
                    takePhoto.setImagePath(Const.getUrl() + fileName + ".png");
                    takePhoto.setBitmap(bm);
                    Bimp.tempSelectBitmap.add(takePhoto);
                    showLoadingDialog();
                    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
                    singleThreadExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                ImageUploadUtil.doUploadRegisters(bm, new SaveCallback() {
                                    @Override
                                    public void onSuccess(String arg0) {
                                        try {
                                            imagPath = "http://oss.0085.com/" + arg0;
                                            App.getUIHandler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (id == R.id.img_permit01) {
                                                        img_permit01.setImageBitmap(bm);
                                                        businesslicenseImg=imagPath;
                                                    } else if (id == R.id.img_permit02) {
                                                        img_permit02.setImageBitmap(bm);
                                                        permitImg=imagPath;
                                                    }


                                                }
                                            }, 100);
                                        } catch (Resources.NotFoundException e) {
                                            e.printStackTrace();
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


                    //adapter.update();
                }
                break;
        }
    }
}
