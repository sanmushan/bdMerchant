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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.OSSException;
import com.gzdb.response.Api;
import com.gzdb.response.DevelopingType;
import com.gzdb.utils.*;
import com.gzdb.warehouse.App;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liyunbiao on 2017/6/4.
 */
public class ContractChangeActivity extends AfinalActivity implements View.OnClickListener {
    BaseTitleBar baseTitleBar;
    @Bind(R.id.img_contract01)
    ImageView img_contract01;

    @Bind(R.id.img_contract02)
    ImageView img_contract02;

    @Bind(R.id.img_contract03)
    ImageView img_contract03;

    @Bind(R.id.img_contract04)
    ImageView img_contract04;
    @Bind(R.id.edit_contract_num)
    EditText edit_contract_num;
    private String imagPath = "";
    private List<ContractBean> imagPathAll;

    private String char7id01;
    private String char7id02;
    private String char7id03;
    private String char7id04;

    private int id = 0;
    Dialog wheelViewDialog;
    Bitmap bm = null;
    @Override
    public int getContentViewId() {
        return R.layout.activity_contract;
    }

    @Override
    public void initView(View view) {
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("修改供应商合同");
        imagPathAll=new ArrayList<>();
        baseTitleBar.setRightTxt("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDeveLoping();
            }
        });
        img_contract01.setOnClickListener(this);
        img_contract02.setOnClickListener(this);
        img_contract03.setOnClickListener(this);
        img_contract04.setOnClickListener(this);
        img_contract04.setTag(null);
        img_contract03.setTag(null);
        img_contract02.setTag(null);
        img_contract01.setTag(null);
        if(GlobalData.evelopingBean!=null){
            if(GlobalData.evelopingBean.getContractNum()!=null){
                edit_contract_num.setText(GlobalData.evelopingBean.getContractNum());//合同编号
            }
        }
        initData();

    }
    private  void  initData(){
        Map map = new HashMap();
        map.put("achieveId", GlobalData.achieveId+"");
        map.put("achieveImgType", 1 + "");
        httpGet(map, Api.BasedeveloingURL() + "json/achieve/cdn/achieveImgs", new HttpCallback<List<ContractBean>>() {
            @Override
            public void onSuccess(List<ContractBean> data) {
                try {

                    if (data.size() > 0) {
                        ImageLoaders.display(ContractChangeActivity.this,img_contract01,data.get(0).getContractImg());
                        img_contract01.setTag(data.get(0).getContractImg());
                         char7id01=data.get(0).getId();
                    }
                    if (data.size() > 1) {
                        ImageLoaders.display(ContractChangeActivity.this,img_contract02,data.get(1).getContractImg());
                        img_contract02.setTag(data.get(1).getContractImg());
                        char7id02=data.get(1).getId();

                    }
                    if (data.size() > 2) {
                        ImageLoaders.display(ContractChangeActivity.this,img_contract03,data.get(2).getContractImg());
                        img_contract03.setTag(data.get(2).getContractImg());
                        char7id03=data.get(2).getId();
                    }
                    if (data.size() > 3) {
                        ImageLoaders.display(ContractChangeActivity.this,img_contract04,data.get(3).getContractImg());
                        img_contract04.setTag(data.get(3).getContractImg());
                        char7id04=data.get(3).getId();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }

    private void saveDeveLoping() {

        Map map = new HashMap();
        if (edit_contract_num.getText().toString().length() == 0) {
            ToastUtil.showToast(ContractChangeActivity.this, "请输入正确的合同编号");
            return;
        }

        if(img_contract04.getTag()==null&&img_contract03.getTag()==null&&img_contract02.getTag()==null&&img_contract01.getTag()==null){
            ToastUtil.showToast(ContractChangeActivity.this, "请上传合同照片");
            return;
        }

        if (img_contract01.getTag() != null) {
            ContractBean bean=new ContractBean();
            bean.setId(char7id01);
            bean.setContractImg(img_contract01.getTag().toString());
            imagPathAll.add(bean);
        } else if (img_contract02.getTag() != null) {
            ContractBean bean=new ContractBean();
            bean.setId(char7id02);
            bean.setContractImg(img_contract02.getTag().toString());
            imagPathAll.add(bean);
        } else if (img_contract03.getTag() != null) {
            ContractBean bean=new ContractBean();
            bean.setId(char7id03);
            bean.setContractImg(img_contract03.getTag().toString());
            imagPathAll.add(bean);
        } else if (img_contract04.getTag() != null) {
            ContractBean bean=new ContractBean();
            bean.setId(char7id04);
            bean.setContractImg(img_contract04.getTag().toString());
            imagPathAll.add(bean);
        }
        String char7="";
        for(int i=0;i<imagPathAll.size();i++){
            if(imagPathAll.get(i)!=null) {
                if (i == imagPathAll.size() - 1)
                {
                    char7=char7+"0"+Const.CHAR7+imagPathAll.get(i);
                }else {
                    char7=char7+"0"+Const.CHAR7+imagPathAll.get(i)+Const.CHAR7;
                }
            }
        }
        map.put("achieveId", GlobalData.achieveId);
        map.put("contractNum", edit_contract_num.getText().toString());
        map.put("contractImg", imagPathAll);
        httpPost(map, Api.BasedeveloingURL() + "json/achieve/save_contract", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                setResult(RESULT_OK);
                ContractChangeActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_contract01:
                id = v.getId();
                wheelViewDialog = createWheelViewDialog();
                wheelViewDialog.show();
                break;
            case R.id.img_contract02:
                id = v.getId();
                wheelViewDialog = createWheelViewDialog();
                wheelViewDialog.show();
                break;
            case R.id.img_contract03:
                id = v.getId();
                wheelViewDialog = createWheelViewDialog();
                wheelViewDialog.show();
                break;
            case R.id.img_contract04:
                id = v.getId();
                wheelViewDialog = createWheelViewDialog();
                wheelViewDialog.show();
                break;
        }
    }
    public Dialog createWheelViewDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(ContractChangeActivity.this, R.layout.showdialog,
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
    /**
     * 拍照
     */
    public void photo() {
        try {
            Intent openCameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
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

    private static final int TAKE_PICTURE = 0x000001;


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            switch (requestCode) {
                case TAKE_PICTURE:
                    if (resultCode == RESULT_OK) {

                        if (data.getExtras() == null) {
                            return;
                        }
                        String fileName = String.valueOf(System.currentTimeMillis());
                        final Bitmap bm = (Bitmap) data.getExtras().get("data");
                        if (bm == null) {
                            return;
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
                                                        if (id == R.id.img_contract01) {
                                                            img_contract01.setImageBitmap(bm);
                                                            img_contract01.setTag(imagPath);
                                                        } else if (id == R.id.img_contract02) {
                                                            img_contract02.setImageBitmap(bm);
                                                            img_contract02.setTag(imagPath);
                                                        } else if (id == R.id.img_contract03) {
                                                            img_contract03.setImageBitmap(bm);
                                                            img_contract03.setTag(imagPath);
                                                        } else if (id == R.id.img_contract04) {
                                                            img_contract04.setImageBitmap(bm);
                                                            img_contract04.setTag(imagPath);
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
                                                        if (id == R.id.img_contract01) {
                                                            img_contract01.setImageBitmap(bm);
                                                            img_contract01.setTag(imagPath);
                                                        } else if (id == R.id.img_contract02) {
                                                            img_contract02.setImageBitmap(bm);
                                                            img_contract02.setTag(imagPath);
                                                        } else if (id == R.id.img_contract03) {
                                                            img_contract03.setImageBitmap(bm);
                                                            img_contract03.setTag(imagPath);
                                                        } else if (id == R.id.img_contract04) {
                                                            img_contract04.setImageBitmap(bm);
                                                            img_contract04.setTag(imagPath);
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
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }
    }
}
