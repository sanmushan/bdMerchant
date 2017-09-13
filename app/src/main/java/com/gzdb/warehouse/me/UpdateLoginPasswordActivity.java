package com.gzdb.warehouse.me;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.enums.SmsCodeTypeEnum;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.StringUtils;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by zhumg on 5/5.
 */
public class UpdateLoginPasswordActivity extends AfinalActivity implements View.OnClickListener {

    @Bind(R.id.et_code)
    EditText et_code;
    @Bind(R.id.et_src_password)
    EditText et_src_password;
    @Bind(R.id.et_password)
    EditText et_password;
    @Bind(R.id.et_password2)
    EditText et_password2;

    @Bind(R.id.btn_code)
    TextView btn_code;

    @Bind(R.id.btn_ok)
    View btn_ok;

    Handler handler;
    Runnable runnable;
    int mTotalTime = 60;
    boolean canGetCode = true;

    BaseTitleBar baseTitleBar;

    @Override
    public int getContentViewId() {
        return R.layout.activity_update_login_password;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("修改登录密码");

        btn_code.setOnClickListener(this);
        btn_ok.setOnClickListener(this);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                mTotalTime--;
                if (mTotalTime == 0) {
                    //停止
                    canGetCode = true;
                    handler.removeCallbacks(runnable);
                    btn_code.setText("获取验证码");
                    btn_code.setBackgroundResource(R.drawable.btn_blue);
                } else {
                    btn_code.setText("重新获取(" + mTotalTime + ")");
                    handler.postDelayed(runnable, 1000);
                }
            }
        };
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_ok) {
            //确定
            updatePassword();
        } else if (v.getId() == R.id.btn_code) {
            //获取验证码
            if (!canGetCode) {
                return;
            }
            //设置获取为true
            canGetCode = false;
            mTotalTime = 60;
            btn_code.setText("重新获取(" + mTotalTime + ")");
            btn_code.setBackgroundResource(R.drawable.btn_grey);
            handler.postDelayed(runnable, 1000);
            //获取 验证码
            Http.post(this, ApiRequest.requestVerificationCode(Cache.passport.getPhoneNumber(),
                    String.valueOf(SmsCodeTypeEnum.MODIFY_PASSWORD.getKey())),
                    Api.GET_SMS, new HttpCallback() {
                        @Override
                        public void onSuccess(Object data) {
                            if (!StringUtils.isEmpty(msg)) {
                                ToastUtil.showToast(UpdateLoginPasswordActivity.this, msg);
                            }
                        }
                    }.setPass());
        }
    }


    void updatePassword() {

        String smsCode = et_code.getText().toString().trim();
        String src_password = et_src_password.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String password2 = et_password2.getText().toString().trim();

        if (StringUtils.isTrimEmpty(this, smsCode, "请输入验证码")) {
            return;
        }
        if (StringUtils.isTrimEmpty(this, src_password, "请输入原密码")) {
            return;
        }
        if (StringUtils.isTrimEmpty(this, password, "请输入密码")) {
            return;
        }
        if (StringUtils.isTrimEmpty(this, password2, "请输入确认密码")) {
            return;
        }
        if (!password.equals(password2)) {
            ToastUtil.showToast(this, "两次输入的密码不一致");
        }

        Map map = new HashMap();
        map.put("username", Cache.passport.getLoginName());
        map.put("oldPassword", src_password);
        map.put("newPassword", password);
        map.put("confirmPassword", password2);
        httpPost(map, Api.MODIFY_PASSWORD, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                ToastUtil.showToast(UpdateLoginPasswordActivity.this, msg);
                finish();
            }
        }.setPass());
    }
}
