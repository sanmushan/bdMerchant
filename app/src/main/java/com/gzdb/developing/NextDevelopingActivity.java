package com.gzdb.developing;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.mbaas.oss.callback.SaveCallback;
import com.aliyun.mbaas.oss.model.OSSException;
import com.gzdb.utils.Bimp;
import com.gzdb.utils.Const;
import com.gzdb.utils.ImageItem;
import com.gzdb.utils.ImageUploadUtil;
import com.gzdb.warehouse.App;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.Bind;

/**
 * 作   者：liyunbiao
 * 时   间：2017/5/24
 * 修 改 人：
 * 日   期：
 * 描   述：
 */

public class NextDevelopingActivity extends AfinalActivity implements View.OnClickListener {
    BaseTitleBar baseTitleBar;
    @Bind(R.id.ll_permit)
    LinearLayout ll_permit;

    @Bind(R.id.ll_contract)
    LinearLayout ll_contract;

    @Bind(R.id.ll_id)
    LinearLayout ll_id;
    @Bind(R.id.btn_save)
    Button btn_save;
    @Bind(R.id.tv_idnum)
    TextView tv_idnum;
    @Bind(R.id.tv_contract)
    TextView tv_contract;
    @Bind(R.id.tv_permit)
    TextView tv_permit;

    private boolean isPermit = false;
    private boolean isId = false;
    private boolean isContract = false;

    @Override
    public int getContentViewId() {
        return R.layout.activity_next_developing;
    }

    @Override
    public void initView(View view) {
        baseTitleBar = new BaseTitleBar(view);
       // baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("开拓供应商");
        baseTitleBar.setRightTxt("返回", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextDevelopingActivity.this.finish();
            }
        });
        ll_contract.setOnClickListener(this);
        ll_permit.setOnClickListener(this);
        ll_id.setOnClickListener(this);
        btn_save.setOnClickListener(this);

        isPermit = false;
        isId = false;
        isContract = false;
        tv_idnum.setText("");
        tv_contract.setText("");
        tv_permit.setText("");

    }

    @Override
    public void onClick(View v) {
        Intent myintent = new Intent();
        switch (v.getId()) {
            case R.id.ll_permit:
                myintent.setClass(NextDevelopingActivity.this, PermitActivity.class);
                startActivityForResult(myintent, 1);

                break;
            case R.id.ll_contract:
                myintent.setClass(NextDevelopingActivity.this, ContractActivity.class);
                startActivityForResult(myintent, 2);
                break;
            case R.id.ll_id:
                myintent.setClass(NextDevelopingActivity.this, IDActivity.class);
                startActivityForResult(myintent, 3);
                break;
            case R.id.btn_save:
                if( isPermit &&isId&&isContract){
                    ToastUtil.showToast(NextDevelopingActivity.this,"提交成功");
                    NextDevelopingActivity.this.finish();
                }else {
                    if(!isPermit)
                    {
                        ToastUtil.showToast(NextDevelopingActivity.this,"营业证及许可证还没有填写 ");
                    }else  if(!isId){
                        ToastUtil.showToast(NextDevelopingActivity.this,"经营信息不完整");
                    }else  if(!isContract){
                        ToastUtil.showToast(NextDevelopingActivity.this,"请上传合同信息");
                    }
                }
                break;

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case 1:
                        isPermit = true;
                        tv_permit.setText("已填写");
                        break;
                    case 2:
                        isContract = true;
                        tv_contract.setText("已填写");
                        break;
                    case 3:
                        isId = true;
                        tv_idnum.setText("已填写");
                        break;

                }

            }
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }
    }
}
