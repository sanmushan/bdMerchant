package com.gzdb.developing;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gzdb.response.Api;
import com.gzdb.utils.GlobalData;
import com.gzdb.warehouse.App;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;

/**
 * Created by PVer on 2017/6/5.
 */

public class RegisterPassportActivity extends AfinalActivity implements View.OnClickListener {
    BaseTitleBar baseTitleBar;
    @Bind(R.id.btn_register)
    Button btn_register;
    @Bind(R.id.btn_code)
    Button btn_code;

    @Bind(R.id.edit_username)
    EditText edit_username;
    @Bind(R.id.edit_pwd)
    EditText edit_pwd;
    @Bind(R.id.edit_mobile)
    EditText edit_mobile;
    @Bind(R.id.edit_code)
    EditText edit_code;
    private  int count=60;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_code:
                btn_code.setEnabled(false);
                btn_code.setText("发送中..");
                btn_code.setBackgroundResource(R.drawable.btn_gray);
                sendCode();
                break;
            case R.id.btn_register:
                subimt();
                break;
        }
    }
    private void subimt() {

        Map map = new HashMap();
        if (edit_username.getText().toString().length() == 0) {
            ToastUtil.showToast(RegisterPassportActivity.this, "请输入帐号");
            return;
        }
        if (edit_pwd.getText().toString().length() == 0) {
            ToastUtil.showToast(RegisterPassportActivity.this, "请输入密码");
            return;
        }
        if (edit_mobile.getText().toString().length() == 0) {
            ToastUtil.showToast(RegisterPassportActivity.this, "请输入手机号码");
            return;
        }
        if (edit_code.getText().toString().length() == 0) {
            ToastUtil.showToast(RegisterPassportActivity.this, "请输入验证码");
            return;
        }
        map.put("name", edit_username.getText().toString());// - String 登录用户名，必填参数。
        map.put("password", edit_pwd.getText().toString());// - String 登录密码，必填参数。
        map.put("smsCode", edit_code.getText().toString());// - String 登录密码，必填参数。
        map.put("fromChannel", "100000");// - String 登录密码，必填参数。
        map.put("versionIndex", "2");// - String 登录密码，必填参数。
        map.put("phoneNumber", edit_mobile.getText().toString());// - String 联系号码，必填参数；可作为另一个登录帐号。
        map.put("phoneNumber", edit_mobile.getText().toString());// - String 联系号码，必填参数；可作为另一个登录帐号。
        httpPost(map, Api.basePassportRemoteURL() + "passport/registerPassport", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                try {
                    count=-1;
                    JSONObject json=new JSONObject(data.toString());
                    GlobalData.passportId=json.optString("passportId");
                    RegisterPassportActivity.this.finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void sendCode() {
        Map map = new HashMap();
        if (edit_mobile.getText().toString().length() == 0) {
            ToastUtil.showToast(RegisterPassportActivity.this, "请输入手机号码");
            return;
        }
        map.put("phoneNumber", edit_mobile.getText().toString());// - String 联系号码，必填参数；可作为另一个登录帐号。
        map.put("type","2");// - String 联系号码，必填参数；可作为另一个登录帐号。
        httpPost(map, Api.basePassportRemoteURL() + "sms/requestVerificationCode", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                count=60;
                initDate();

            }

            @Override
            public void onFailure() {
                btn_code.setText("获取验证码");
                btn_code.setEnabled(true);
                btn_code.setBackgroundResource(R.drawable.next_btn_border);
                super.onFailure();
            }
        });
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_register_assport;
    }

    @Override
    public void initView(View view) {
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("注册通行证");
        btn_register.setOnClickListener(this);
        btn_code.setOnClickListener(this);
        Bundle bundle=this.getIntent().getExtras();
        if(bundle!=null){
            if(bundle.getString("mobile")!=null){
                edit_mobile.setText(bundle.getString("mobile"));
            }
        }
    }
    private  void  initDate(){
        App.getUIHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if(count>0) {
                        initDate();
                        btn_code.setText(count+"秒");
                        btn_code.setEnabled(true);
                    }else {
                        btn_code.setText("获取验证码");
                        btn_code.setEnabled(true);
                        btn_code.setBackgroundResource(R.drawable.next_btn_border);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                count--;
            }
        },1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
