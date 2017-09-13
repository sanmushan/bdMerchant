package com.gzdb.warehouse.me;

import android.view.View;
import android.widget.EditText;

import com.gzdb.response.Api;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.main.MeFragmentEvent;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.Http;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.StringUtils;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public class SetPrintActivity extends AfinalActivity implements View.OnClickListener {

    @Bind(R.id.et_value)
    EditText etValue;
    @Bind(R.id.btn_ok)
    View btnOk;

    BaseTitleBar titleBar;

    @Override
    public int getContentViewId() {
        return R.layout.activity_supplier_set_print;
    }

    @Override
    public void initView(View view) {
        setTranslucentStatus();
        titleBar = new BaseTitleBar(view);
        titleBar.setLeftBack(this);
        titleBar.setCenterTxt("设置打印设备");
        String old_value = getIntent().getStringExtra("value");
        if(old_value != null) {
            etValue.setText(old_value);
            etValue.setSelection(old_value.length());
        }
        btnOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_ok) {
            final String value = etValue.getText().toString();
            if(StringUtils.isEmpty(value)) {
                ToastUtil.showToast(SetPrintActivity.this, "请输入打印设备号码");
                return;
            }
            //保存
            Map map = new HashMap();
            map.put("passportId", Cache.passport.getPassportIdStr());
            map.put("printerNumber", value);
            Http.post(this, map, Api.SET_PRINT, new HttpCallback() {
                @Override
                public void onSuccess(Object data) {
                    EventBus.getDefault().post(new MeFragmentEvent(MeFragmentEvent.SET_PRINT, value));
                    finish();
                }
            }.setPass());
        }
    }
}
