package com.gzdb.picking;

import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gzdb.picking.adapter.PickPersonAddAdapter;
import com.gzdb.response.Api;
import com.gzdb.response.PickGroupBean;
import com.gzdb.utils.GlobalData;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.ActivityManager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by liyunbiao on 2017/8/24.
 */

public class PickPersonAddActivity extends AfinalActivity implements View.OnClickListener {

    BaseTitleBar baseTitleBar;
    @Bind(R.id.ll_group_name)
    LinearLayout ll_group_name;
    @Bind(R.id.tv_group_name)
    TextView tv_group_name;

    @Bind(R.id.edit_phoneNumber)
    EditText edit_phoneNumber;

    @Bind(R.id.edit_realName)
    EditText edit_realName;
    @Bind(R.id.edit_password)
    EditText edit_password;

    Dialog wheelViewDialog;
    PickPersonAddAdapter adapter;
    List<PickGroupBean> items = new ArrayList<>();

    PickGroupBean pick = new PickGroupBean();

    @Override
    public int getContentViewId() {
        return R.layout.activity_pick_person_add;
    }

    @Override
    public void initView(View view) {
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("添加拣货员");
        baseTitleBar.setLeftListener(this);
        ll_group_name.setOnClickListener(this);
        baseTitleBar.setRightTxt("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
        initData();
    }

    public Dialog createWheelListDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(PickPersonAddActivity.this, R.layout.show_dialog_type,
                null);
        ListView lv_type = (ListView) view.findViewById(R.id.lv_type);
        adapter = new PickPersonAddAdapter(PickPersonAddActivity.this, items);
        lv_type.setAdapter(adapter);
        lv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pick = (PickGroupBean) parent.getAdapter().getItem(position);
                tv_group_name.setText(pick.getGroupName());
                wheelViewDialog.dismiss();
            }
        });
        dialog.setContentView(view);
        return dialog;
    }

    public void initData() {
        try {

            Map<String, String> map = new HashMap<>();
            map.put("passportId", Cache.passport.getPassportIdStr());
            httpPostNoShow(map, Api.BasesupplychainRemoteURL() + "warehouse/selGroup", new HttpCallback<List<PickGroupBean>>("datas") {
                @Override
                public void onSuccess(List<PickGroupBean> data) {

                    try {
                        items.clear();
                        if (data == null || data.size() == 0) {
                            //refreshLoad.getLoadingView().showReset(msg);
                            return;
                        }

                        items.addAll(data);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure() {
                    super.onFailure();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submit() {
        Map<String, String> map = new HashMap<>();
        if (edit_password.getText().toString().length() == 0) {
            ToastUtil.showToast(PickPersonAddActivity.this, "请输入密码!!!");
        }
        if (edit_password.getText().toString().length() <= 0) {
            ToastUtil.showToast(PickPersonAddActivity.this, "请输入大于8位数的密码!!!");
        }
        if (edit_phoneNumber.getText().toString().length() == 0) {
            ToastUtil.showToast(PickPersonAddActivity.this, "请输入电话号码!!!");
        }
        if (edit_realName.getText().toString().length() == 0) {
            ToastUtil.showToast(PickPersonAddActivity.this, "请输入真实姓名!!!");
        }
        if (pick == null) {
            ToastUtil.showToast(PickPersonAddActivity.this, "请选择分组!!!");
            return;
        }
        map.put("passportId", Cache.passport.getPassportIdStr());
        map.put("password", edit_password.getText().toString());
        map.put("phoneNumber", edit_phoneNumber.getText().toString());
        map.put("realName", edit_realName.getText().toString());
        map.put("groupId", pick.getId());
        map.put("value", pick.getItemTypeId());

        httpPost(map, Api.BasesupplychainRemoteURL() + "warehouse/registerPassportPick", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                ToastUtil.showToast(PickPersonAddActivity.this, msg);
                ActivityManager.finishActivity(PickPersonAddActivity.class);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                ToastUtil.showToast(PickPersonAddActivity.this, msg);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                ActivityManager.finishActivity(PickPersonAddActivity.class);
                break;
            case R.id.ll_group_name:
                if (items != null) {
                    wheelViewDialog = createWheelListDialog();
                    wheelViewDialog.show();
                } else {
                    ToastUtil.showToast(PickPersonAddActivity.this, "没有数据!");
                }
                break;
        }
    }
}
