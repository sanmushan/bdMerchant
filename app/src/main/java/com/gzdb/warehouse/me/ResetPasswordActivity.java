package com.gzdb.warehouse.me;

import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.enums.SmsCodeTypeEnum;
import com.gzdb.response.enums.UpdatePasswordTypeEnum;
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
 * Created by Administrator on 2017/4/16 0016.
 */

public class ResetPasswordActivity extends AfinalActivity implements View.OnClickListener {

    @Bind(R.id.et_phone)
    TextView et_phone;

    @Bind(R.id.et_code)
    TextView et_code;

    @Bind(R.id.et_password)
    TextView et_password;

    @Bind(R.id.et_password2)
    TextView et_password2;

    @Bind(R.id.tv_password)
    TextView tv_password;

    @Bind(R.id.tv_password2)
    TextView tv_password2;

    @Bind(R.id.btn_code)
    TextView btn_code;

    @Bind(R.id.phone_ll)
    View phone_ll;
    @Bind(R.id.phone_g)
    View phone_g;

    @Bind(R.id.btn_next)
    View btn_next;

    UpdatePasswordTypeEnum updatePasswordTypeEnum = UpdatePasswordTypeEnum.UPDATE_LOGIN_PASSWORD;
    BaseTitleBar baseTitleBar;

    int mTotalTime = 60;
    Handler handler;
    Runnable runnable;
    boolean canGetCode = true;

    String codePhone;

    @Override
    public int getContentViewId() {
        return R.layout.activity_reset_password;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);

        int updateType = getIntent().getIntExtra("updateType", 0);
        updatePasswordTypeEnum = UpdatePasswordTypeEnum.getUpdatePasswordTypeEnum(updateType);

        if (updatePasswordTypeEnum == UpdatePasswordTypeEnum.UPDATE_PAY_PASSWORD) {
            baseTitleBar.setCenterTxt("重置支付密码");
            tv_password.setText("支付密码");
            tv_password2.setText("确认支付密码");
            phone_ll.setVisibility(View.GONE);
            //phone_g.setVisibility(View.GONE);
        } else if (updatePasswordTypeEnum == UpdatePasswordTypeEnum.FIND_PASSWORD) {
            baseTitleBar.setCenterTxt("重置登录密码");
            tv_password.setText("登录密码");
            tv_password2.setText("确认登录密码");
        }

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

        btn_next.setOnClickListener(this);
        btn_code.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            //确定
            updatePassword();
        } else if (v.getId() == R.id.btn_code) {
            //获取验证码
            if (!canGetCode) {
                return;
            }
            String input_phone = null;
            if (updatePasswordTypeEnum == UpdatePasswordTypeEnum.UPDATE_PAY_PASSWORD) {
                input_phone = Cache.passport.getPhoneNumber();
            } else if (updatePasswordTypeEnum == UpdatePasswordTypeEnum.FIND_PASSWORD) {
                //电话
                input_phone = et_phone.getText().toString().trim();
                if (StringUtils.isEmpty(this, input_phone, "请输入手机号码")) {
                    return;
                }
            }
            final String phone = input_phone;

            //设置获取为true
            canGetCode = false;
            mTotalTime = 60;
            btn_code.setText("重新获取(" + mTotalTime + ")");
            btn_code.setBackgroundResource(R.drawable.btn_grey);
            handler.postDelayed(runnable, 1000);
            //获取 验证码
            Http.post(this, ApiRequest.requestVerificationCode(phone, String.valueOf(SmsCodeTypeEnum.MODIFY_PASSWORD.getKey())), Api.GET_SMS, new HttpCallback() {
                @Override
                public void onSuccess(Object data) {
                    if (!StringUtils.isEmpty(msg)) {
                        ToastUtil.showToast(ResetPasswordActivity.this, msg);
                    }
                    //记录获取了验证码的手机号码
                    codePhone = phone;
                }
            }.setPass());
        }
    }

    void updatePassword() {

        String smsCode = et_code.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String password2 = et_password2.getText().toString().trim();

        String phone = "";
        if (StringUtils.isTrimEmpty(this, smsCode, "请输入验证码")) {
            return;
        }
        if (StringUtils.isTrimEmpty(this, password, "请输入密码")) {
            return;
        }
        if (StringUtils.isTrimEmpty(this, password2, "请输入确认密码")) {
            return;
        }
        if(!password.equals(password2)) {
            ToastUtil.showToast(this, "两次输入的密码不一致");
            return;
        }

        if (updatePasswordTypeEnum == UpdatePasswordTypeEnum.UPDATE_PAY_PASSWORD) {
            resetPayPassword(smsCode, password);
        } else if (updatePasswordTypeEnum == UpdatePasswordTypeEnum.FIND_PASSWORD) {
            //电话
            phone = et_phone.getText().toString().trim();
            if (StringUtils.isTrimEmpty(this, phone, "请输入手机号")) {
                return;
            }
            resetLoginPassword(smsCode, password, password2, phone);
        }

    }

    void resetLoginPassword(String phone, String smsCode, String password, String password2) {
        Map map = new HashMap();
        map.put("phoneNumber", phone);
        map.put("smsCode", smsCode);
        map.put("password", password);
        map.put("confirmPassword", password2);
        httpPost(map, Api.FORGET_PASSWORD, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                ToastUtil.showToast(ResetPasswordActivity.this, msg);
                finish();
            }
        }.setPass());
    }

    void resetPayPassword(String smsCode, String newPassword) {
        Map map = new HashMap();
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("phone", Cache.passport.getPhoneNumber());
        map.put("smsCode", smsCode);
        map.put("smsType", String.valueOf(SmsCodeTypeEnum.MODIFY_PASSWORD.getKey()));
        map.put("paymentPassword", newPassword);
        httpPost(map, Api.UPDATE_PAY_PASSWORD, new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                ToastUtil.showToast(ResetPasswordActivity.this, msg);
                finish();
            }
        }.setPass());
    }
}
