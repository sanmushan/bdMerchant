package com.gzdb.developing;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;

import com.gzdb.developing.adapter.DevelopingTypeAdapter;
import com.gzdb.response.Api;
import com.gzdb.response.DevelopingType;
import com.gzdb.utils.GlobalData;
import com.gzdb.utils.TelNumMatch;
import com.gzdb.warehouse.R;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * 作   者：liyunbiao
 * 时   间：2017/5/24
 * 修 改 人：
 * 日   期：
 * 描   述：
 */

public class DevelopingAddActivity extends AfinalActivity implements View.OnClickListener {
    BaseTitleBar baseTitleBar;
    @Bind(R.id.btn_next)
    Button btn_next;
    @Bind(R.id.ll_type)
    LinearLayout ll_type;
    @Bind(R.id.ll_product_type)
    LinearLayout ll_product_type;
    @Bind(R.id.edit_shop_mobile)
    EditText edit_shop_mobile;
    @Bind(R.id.edit_shop_name)
    EditText edit_shop_name;

    @Bind(R.id.ll_address)
    LinearLayout ll_address;
    @Bind(R.id.tv_addressdetail)
    TextView tv_addressdetail;
    @Bind(R.id.tv_mark)
    TextView tv_mark;
    @Bind(R.id.tv_doing)
    TextView tv_doing;
    @Bind(R.id.tv_type_name)
    TextView tv_type_name;

    private String latitude;
    private String longitude;

    Dialog wheelViewDialog;
    private DevelopingTypeAdapter adapter;
    List<DevelopingType> dataType;

    DevelopingType developingType;

    @Override
    public int getContentViewId() {
        return R.layout.activity_developing_add;
    }

    @Override
    public void initView(View view) {
        GlobalData.itemTypeId = "";
        baseTitleBar = new BaseTitleBar(view);
        baseTitleBar.setLeftBack(this);
        baseTitleBar.setCenterTxt("添加供应商");
        btn_next.setOnClickListener(this);
        ll_type.setOnClickListener(this);
        ll_product_type.setOnClickListener(this);
        ll_address.setOnClickListener(this);
        tv_mark.setText("未标志");
        dataType = new ArrayList<>();
        iniData();
    }

    private void iniData() {
        Map map = new HashMap();
        map.put("achieveTypeId", "1");
        httpGet(map, Api.BasedeveloingURL() + "json/achieve/cdn_typename_list", new HttpCallback<List<DevelopingType>>() {
            @Override
            public void onSuccess(List<DevelopingType> data) {
                dataType.addAll(data);
            }
        });
    }
    private  void checkUser(String phoneNumber){
        Map map=new HashMap();
        map.put("phoneNumber",phoneNumber);
        httpGet(map, Api.basePassportRemoteURL()+"openApi/getVerification", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {
                try {
                    if(data==null){
                        return;
                    }
                    JSONObject json =new JSONObject(data.toString());
                    if (json==null){
                        return;
                    }
                    String passportId= json.optString("passportId");
                    GlobalData.passportId=passportId;
                    saveDeveLoping();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure() {
                super.onFailure();
                if(code==1){
                    wheelViewDialog =RegisterWheelListDialog();
                    wheelViewDialog.show();
                }else  if(code==2){

                }else  if(code==3){

                }else {
                    saveDeveLoping();
                }
            }
        });
    }


    public Dialog createWheelListDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(DevelopingAddActivity.this, R.layout.show_dialog_type,
                null);
        ListView lv_type = (ListView) view.findViewById(R.id.lv_type);
        adapter = new DevelopingTypeAdapter(DevelopingAddActivity.this, dataType);
        lv_type.setAdapter(adapter);
        lv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                developingType = (DevelopingType) parent.getAdapter().getItem(position);
                tv_type_name.setText(developingType.getAchieveTypeName());
                wheelViewDialog.dismiss();
            }
        });
        dialog.setContentView(view);
        return dialog;
    }

    private void saveDeveLoping() {
        if (edit_shop_name.getText().toString().length() == 0) {
            ToastUtil.showToast(DevelopingAddActivity.this, "请输入店铺名称");
            return;
        }
//        if (edit_shop_mobile.getText().toString().length() == 0) {
//            ToastUtil.showToast(DevelopingAddActivity.this, "请输入联系电话");
//            return;
//        }
//        if (!TelNumMatch.isValidPhoneNumber(edit_shop_mobile.getText().toString())) {
//            ToastUtil.showToast(DevelopingAddActivity.this, "请输入正确的联系电话");
//            return;
//        }
        if (tv_mark.getText().toString().equals("未标志")) {
            ToastUtil.showToast(DevelopingAddActivity.this, "请选择地址");
            return;
        }
        if (developingType == null) {
            ToastUtil.showToast(DevelopingAddActivity.this, "请选择供应商类型");
            return;
        }
        if (GlobalData.itemTypeId.length() == 0) {
            ToastUtil.showToast(DevelopingAddActivity.this, "请选择经营类目");
            return;
        }
        Map map = new HashMap();
        map.put("showNameTwo", edit_shop_name.getText().toString());
        map.put("phoneNumber", edit_shop_mobile.getText().toString());
        map.put("address", tv_addressdetail.getText().toString());
        map.put("achieveTypeId", developingType.getId());
        map.put("achieveTypeName", developingType.getAchieveTypeName());
        map.put("operation_id", GlobalData.passportId);
        map.put("operationId", GlobalData.passportId);
        map.put("achievePlatFormId", developingType.getId());
        map.put("itemTypeParentId", "");
        map.put("itemTypeId", GlobalData.itemTypeId);
        map.put("latitude", latitude + "");
        map.put("longitude", longitude + "");
        map.put("achieveType", "1");
        httpPost(map, Api.BasedeveloingURL() + "json/achieve/save_achieve", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {

                try {
                    DevelopingAddActivity.this.finish();
                    JSONObject json = new JSONObject(data.toString());
                    GlobalData.achieveId = json.optString("achieveId");
                    startActivity(new Intent(DevelopingAddActivity.this, NextDevelopingActivity.class));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }
        });
    }

    public Dialog RegisterWheelListDialog() {
        final Dialog dialog = new Dialog(this, R.style.Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = View.inflate(DevelopingAddActivity.this, R.layout.show_dialog_register,
                null);
        TextView   tv_go_register= (TextView) view.findViewById(R.id.tv_go_register);
        TextView   tv_back= (TextView) view.findViewById(R.id.tv_back);
        tv_go_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent   myintent = new Intent();
                myintent.setClass(DevelopingAddActivity.this, RegisterPassportActivity.class);
                myintent.putExtra("mobile",edit_shop_mobile.getText().toString());
                startActivity(myintent);
                wheelViewDialog.dismiss();
            }
        });
        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wheelViewDialog.dismiss();
            }
        });

        dialog.setContentView(view);
        return dialog;
    }

    @Override
    public void onClick(View v) {
        Intent myintent = null;
        switch (v.getId()) {
            case R.id.btn_next:
                if (edit_shop_mobile.getText().toString().length() == 0) {
                    ToastUtil.showToast(DevelopingAddActivity.this, "请输入联系电话");
                    return;
                }

                checkUser(edit_shop_mobile.getText().toString());

                break;
            case R.id.ll_type:
                if (dataType != null) {
                    wheelViewDialog = createWheelListDialog();
                    wheelViewDialog.show();
                } else {
                    ToastUtil.showToast(DevelopingAddActivity.this, "没有数据!");
                }
                break;
            case R.id.ll_product_type:
                myintent = new Intent();
                myintent.setClass(DevelopingAddActivity.this, TypeActivity.class);
                startActivityForResult(myintent, 1);
                break;
            case R.id.ll_address:
                myintent = new Intent();
                myintent.setClass(DevelopingAddActivity.this, MapBActivity.class);
                startActivityForResult(myintent, 1);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle == null) {
                return;
            }
            if (bundle.getString("op") != null && bundle.getString("op").equals("address")) {
                String name = bundle.getString("name");
                latitude = bundle.getDouble("latitude") + "";
                longitude = bundle.getDouble("longitude") + "";
                String address = bundle.getString("address");
                tv_addressdetail.setText(address);
                if (address != null) {
                    tv_mark.setText("已标志");
                } else {
                    tv_mark.setText("未标志");
                }
            } else if (bundle.getString("op") != null && bundle.getString("op").equals("type")) {

                if (GlobalData.itemTypeId.length() > 0) {
                    tv_doing.setText("已标志");
                } else {
                    tv_doing.setText("未标志");
                }
            }
        }
    }
}
