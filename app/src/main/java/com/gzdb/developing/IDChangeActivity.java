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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.OSSException;
import com.gzdb.developing.adapter.BankTypeAdapter;
import com.gzdb.response.Api;
import com.gzdb.response.Bank;
import com.gzdb.utils.Bimp;
import com.gzdb.utils.Const;
import com.gzdb.utils.GlobalData;
import com.gzdb.utils.ImageItem;
import com.gzdb.utils.ImageLoaders;
import com.gzdb.utils.ImageUploadUtil;
import com.gzdb.warehouse.App;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;

/**
 * Created by liyunbiao on 2017/6/2.
 */
public class IDChangeActivity  extends AfinalActivity implements View.OnClickListener {
    BaseTitleBar baseTitleBar;
    @Bind(R.id.img_id_num)
    ImageView img_id_num;

    @Bind(R.id.edit_real_name)
    EditText edit_real_name;

    @Bind(R.id.edit_id_number)
    EditText edit_id_number;
    @Bind(R.id.edit_card_no)
    EditText edit_card_no;
    @Bind(R.id.edit_subbranch)
    EditText edit_subbranch;

    @Bind(R.id.edit_bran_name)
    EditText edit_bran_name;
    @Bind(R.id.tv_bank)
    TextView tv_bank;
    private String imagPath = "";
    private String identityImg = "";
    Dialog wheelViewDialog;
    Bitmap bm = null;
    private BankTypeAdapter adapter;
    @Bind(R.id.ll_bank)
    LinearLayout ll_bank;
    @Bind(R.id.img_identityBackImg)
    ImageView img_identityBackImg;
    List<Bank> datalist=null;
    Bank bank=null;
    private  int img_id=0;
    private String identityBackImg = "";
    @Override
    public int getContentViewId() {
        return R.layout.activity_id;
    }

    @Override
    public void initView(View view) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("修改供应商");
        baseTitleBar.setRightTxt("完成", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDeveLoping();
            }
        });
        img_id_num.setOnClickListener(this);
        ll_bank.setOnClickListener(this);
        datalist=new ArrayList<>();
        if(GlobalData.evelopingBean!=null){
         //   edit_real_name.setText(GlobalData.evelopingBean.);
        }
        initData();
    }
    public Dialog createWheelListDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(IDChangeActivity.this, R.layout.show_dialog_type,
                null);
        ListView lv_type=(ListView) view.findViewById(R.id.lv_type);
        adapter=new BankTypeAdapter(IDChangeActivity.this,datalist);
        lv_type.setAdapter(adapter);
        lv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bank=(Bank) parent.getAdapter().getItem(position);
                tv_bank.setText(bank.getName());
                wheelViewDialog.dismiss();
            }
        });
        dialog.setContentView(view);
        return dialog;
    }
    private void initData() {
        Map map = new HashMap();
        httpGet(map, Api.paymentRemoteURL + "paymentController/showBankTemplates", new HttpCallback<List<Bank>>("datas") {
            @Override
            public void onSuccess(List<Bank> data) {
                datalist.addAll(data);
            }
        });
        map=new HashMap();
        map.put("passportId", Cache.passport.getPassportId() + "");
        map.put("achieveId", GlobalData.evelopingBean.getAchieveId());
        httpGet(map, Api.BasedeveloingURL() + "json/achieve/cdn/achieveAttestation", new HttpCallback<IDInfoBean>() {
            @Override
            public void onSuccess(IDInfoBean data) {
                if(data!=null) {
                    edit_real_name.setText(data.getRealName());
                    edit_id_number.setText(data.getIdNumber());
                    edit_bran_name.setText(data.getBranName());
                    edit_subbranch.setText(data.getSubbranch());
                    edit_card_no.setText(data.getCardNo());
                    tv_bank.setText(data.getBank());
                    identityBackImg=data.getIdentityBackImg();
                    ImageLoaders.display(IDChangeActivity.this,img_identityBackImg,data.getIdentityBackImg());
                    identityImg=data.getIdentityImg();
                    ImageLoaders.display(IDChangeActivity.this,img_id_num,data.getIdentityImg());

                }
            }
        });
    }

    private void saveDeveLoping() {

        Map map = new HashMap();

        if (edit_real_name.getText().toString().length() == 0) {
            ToastUtil.showToast(IDChangeActivity.this, "请输入真实姓名!");
            return;
        }
        if (identityImg.length() == 0) {
            ToastUtil.showToast(IDChangeActivity.this, "请上传身份证正面!");
            return;
        }
        if (edit_bran_name.getText().toString().length() == 0) {
            ToastUtil.showToast(IDChangeActivity.this, "请输入银行名称!");
            return;
        }
        if (edit_id_number.getText().toString().length() == 0) {
            ToastUtil.showToast(IDChangeActivity.this, "请输入份身证号码!");
            return;
        }
        if (edit_card_no.getText().toString().length() == 0) {
            ToastUtil.showToast(IDChangeActivity.this, "请输入银行卡!");
            return;
        }
        if(bank==null){
            ToastUtil.showToast(IDChangeActivity.this, "请选择开户银行!");
            return;
        }
        if(identityBackImg==null){
            ToastUtil.showToast(IDChangeActivity.this, "请上传身份证反面!");
            return;
        }

        map.put("achieveId", GlobalData.achieveId);
        map.put("realName", edit_real_name.getText().toString());
        map.put("identityBackImg", identityBackImg);
        map.put("bank", tv_bank.getText().toString());
        map.put("branName", edit_bran_name.getText().toString());
        map.put("subbranch", edit_subbranch.getText().toString());
        map.put("idNumber", edit_id_number.getText().toString());
        map.put("identityImg", identityImg);
        map.put("cardNo", edit_card_no.getText().toString());
        map.put("cardImg", "");
        httpPost(map, Api.BasedeveloingURL() + "json/achieve/save_attestation", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                setResult(RESULT_OK);
                IDChangeActivity.this.finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                startActivity(new Intent(IDChangeActivity.this, NextDevelopingActivity.class));
                break;
            case R.id.img_id_num:
                img_id=1;
                wheelViewDialog = createWheelViewDialog();
                wheelViewDialog.show();
                break;
            case R.id.img_identityBackImg:
                img_id=2;
                wheelViewDialog = createWheelViewDialog();
                wheelViewDialog.show();
                break;
            case R.id.ll_bank:
                wheelViewDialog = createWheelListDialog();
                wheelViewDialog.show();
                break;

        }
    }

    public Dialog createWheelViewDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(IDChangeActivity.this, R.layout.showdialog,
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
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, 2);
    }

    private static final int TAKE_PICTURE = 0x000001;

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
                                            if(img_id==1) {
                                                imagPath = "http://oss.0085.com/" + arg0;
                                                identityImg = imagPath;
                                                App.getUIHandler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        img_id_num.setImageBitmap(bm);

                                                    }
                                                }, 100);
                                            }else  if(img_id==2){
                                                imagPath = "http://oss.0085.com/" + arg0;
                                                identityBackImg = imagPath;
                                                App.getUIHandler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        img_identityBackImg.setImageBitmap(bm);

                                                    }
                                                }, 100);
                                            }
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
                                            if(img_id==1) {
                                                imagPath = "http://oss.0085.com/" + arg0;
                                                identityImg = imagPath;
                                                App.getUIHandler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        img_id_num.setImageBitmap(bm);

                                                    }
                                                }, 100);
                                            }else  if(img_id==2){
                                                imagPath = "http://oss.0085.com/" + arg0;
                                                identityBackImg = imagPath;
                                                App.getUIHandler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        img_identityBackImg.setImageBitmap(bm);

                                                    }
                                                }, 100);
                                            }
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
