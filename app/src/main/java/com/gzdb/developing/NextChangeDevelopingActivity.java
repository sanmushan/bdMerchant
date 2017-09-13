package com.gzdb.developing;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import com.gzdb.utils.GlobalData;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

/**
 * Created by liyunbiao on 2017/6/4.
 */
public class NextChangeDevelopingActivity  extends AfinalActivity implements View.OnClickListener {
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
                NextChangeDevelopingActivity.this.finish();
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
        if(GlobalData.evelopingBean==null){
            return;
        }
        if(GlobalData.evelopingBean.getPermitNo()!=null){
            isPermit=true;
        }


    }

    @Override
    public void onClick(View v) {
        Intent myintent = new Intent();
        switch (v.getId()) {
            case R.id.ll_permit:
                myintent.setClass(NextChangeDevelopingActivity.this, PermitChangeActivity.class);
                startActivityForResult(myintent, 1);

                break;
            case R.id.ll_contract:
                myintent.setClass(NextChangeDevelopingActivity.this, ContractChangeActivity.class);
                startActivityForResult(myintent, 2);
                break;
            case R.id.ll_id:
                myintent.setClass(NextChangeDevelopingActivity.this, IDChangeActivity.class);
                startActivityForResult(myintent, 3);
                break;
            case R.id.btn_save:
                if( isPermit &&isId&&isContract){
                    ToastUtil.showToast(NextChangeDevelopingActivity.this,"提交成功");
                    NextChangeDevelopingActivity.this.finish();
                }else {
                    if(!isPermit)
                    {
                        ToastUtil.showToast(NextChangeDevelopingActivity.this,"营业证及许可证还没有填写 ");
                    }else  if(!isId){
                        ToastUtil.showToast(NextChangeDevelopingActivity.this,"经营信息不完整");
                    }else  if(!isContract){
                        ToastUtil.showToast(NextChangeDevelopingActivity.this,"请上传合同信息");
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
