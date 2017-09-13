package com.gzdb.picking;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.gzdb.developing.adapter.TypeAdapter;
import com.gzdb.developing.adapter.TypeChildAdapter;
import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.ItemType;
import com.gzdb.utils.GlobalData;
import com.gzdb.warehouse.Cache;
import com.gzdb.warehouse.R;
import com.gzdb.widget.ScrollerListView;
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

public class PickGroupEditActivity   extends AfinalActivity implements View.OnClickListener ,ScrollerListView.IXListViewListener{

    BaseTitleBar baseTitleBar;
    @Bind(R.id.lv_type)
    ListView lv_type;
    List<ItemType> list;
    List<ItemType> child_list;
    List<ItemType> Alllist;
    TypeAdapter adapter;
    TypeChildAdapter typeChildAdapter;
    @Bind(R.id.lv_child_list)
    ScrollerListView lv_child_list;
    int typePosition=0;

    @Bind(R.id.edit_group_input)
    EditText edit_group_input;

    private String id="0";
    private String groupName="";
    @Override
    public int getContentViewId() {
        return R.layout.activity_pick_group_edit;
    }

    @Override
    public void initView(View view) {
        try {
            baseTitleBar = new BaseTitleBar(view);
            baseTitleBar.setLeftBack(this);
            baseTitleBar.setCenterTxt("添加拣货组");

            Bundle bundle=this.getIntent().getExtras();
            if(bundle!=null) {
                GlobalData.itemTypeId = bundle.getString("itemTypeId");
                id = bundle.getString("id");
                groupName = bundle.getString("groupName");
            }

            baseTitleBar.setRightTxt("保存", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        GlobalData.itemTypeId = "";
                        for (int i = 0; i < Alllist.size(); i++) {
                            if (Alllist.get(i).isSelect()) {
                                GlobalData.itemTypeId = GlobalData.itemTypeId + Alllist.get(i).getId() + ",";
                            }
                        }
                        submit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            edit_group_input.setText(groupName);
            list = new ArrayList<>();
            Alllist = new ArrayList<>();
            child_list = new ArrayList<>();
            adapter = new TypeAdapter(PickGroupEditActivity.this, list);
            lv_type.setAdapter(adapter);
            typeChildAdapter = new TypeChildAdapter(PickGroupEditActivity.this, child_list);
            lv_child_list.setAdapter(typeChildAdapter);
            lv_child_list.setPullLoadEnable(true);
            lv_child_list.setXListViewListener(this);
            initDataBigType();
            lv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        typePosition=position;
                        ItemType itemType = (ItemType) parent.getAdapter().getItem(position);
                        child_list.clear();
                        for (int i = 0; i < Alllist.size(); i++) {
                            if (Alllist.get(i).getParentId() == null) {
                                break;
                            }
                            if (Alllist.get(i).getParentId().equals(itemType.getId())) {
                                Alllist.get(i).setPos(position);
                                child_list.add(Alllist.get(i));
                            }
                        }
                        typeChildAdapter.notifyDataSetChanged();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            });
            lv_child_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        ItemType itemType = (ItemType) parent.getAdapter().getItem(position);
                        if (itemType == null) {
                            return;
                        }

                        (typeChildAdapter.getItem(position - 1)).setSelect(!child_list.get(position - 1).isSelect());
                        typeChildAdapter.notifyDataSetChanged();
                        boolean isSelect=false;
                        for (int i = 0; i < Alllist.size(); i++) {
                            if(Alllist.get(i).isSelect()&&Alllist.get(i).getPos()==typePosition){
                                isSelect=true;
                            }
                            if (typeChildAdapter.getItem(position - 1).getId() == Alllist.get(i).getId()) {
                                Alllist.get(i).setSelect(typeChildAdapter.getItem(position - 1).isSelect());

                            }

                        }

                        if(isSelect) {
                            lv_type.getChildAt(typePosition).setBackgroundColor(Color.parseColor("#f2f2f2"));
                        }else {
                            lv_type.getChildAt(typePosition).setBackgroundColor(Color.parseColor("#ffffff"));
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRefresh() {
        lv_child_list.stopRefresh();
    }

    @Override
    public void onLoadMore() {
        lv_child_list.stopLoadMore();
    }

    private void initDataBigType() {
        httpGet(ApiRequest.supplierItemTypes(0), Api.BaseitemRemoteURL()+"item/loaderItemTypes", new HttpCallback<List<ItemType>>("itemTypeArray") {
            @Override
            public void onSuccess(List<ItemType> data) {
                try {
                    if(data==null){
                        return;
                    }
                    Alllist.addAll(data);

                    String []str=GlobalData.itemTypeId.split(",");
                    for(int i=0;i<data.size();i++){

                        if(data.get(i).getParentId()==null){
                            break;
                        }
                        if(str!=null&&str.length>0){
                            for(int k=0;k<str.length;k++){
                                if(str[k].equals(data.get(i).getId())){
                                    data.get(i).setSelect(true);
                                }
                            }
                        }
                        if(data.get(i).getParentId().equals("0")){
                            if(getIsSelect(data.get(i).getId())){
                                data.get(i).setSelect(true);
                            }
                            list.add(data.get(i));
                        }else {

                            if(getIsSelect(data.get(i).getId())){
                                data.get(i).setSelect(true);
                            }
                            child_list.add(data.get(i));
                        }
                    }
                    typeChildAdapter.notifyDataSetChanged();
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private  Boolean getIsSelect(String id){
        if(GlobalData.evelopingBean!=null){

            if(GlobalData.evelopingBean.getItemTypeId()!=null) {
                String[] str = GlobalData.evelopingBean.getItemTypeId().split(",");
                for(int i=0;i<str.length;i++){
                    if(str[i].equals(id)){
                        return true;
                    }
                }
            }
        }
        return  false;
    }

    public void  submit(){
        Map<String,String> map=new HashMap<>();
        if(edit_group_input.getText().toString().length()==0){
            ToastUtil.showToast(PickGroupEditActivity.this,"组名不能为空!");
            return;
        }
        map.put("groupName",edit_group_input.getText().toString());
        if( GlobalData.itemTypeId.length()>0){
            GlobalData.itemTypeId= GlobalData.itemTypeId.substring(0, GlobalData.itemTypeId.length()-1);
        }
        map.put("itemTypeId", GlobalData.itemTypeId);
        map.put("id",id);
        httpPost(map, Api.BasesupplychainRemoteURL() +"warehouse/changeGroup", new HttpCallback() {
            @Override
            public void onSuccess(Object data) {

                ActivityManager.finishActivity(PickGroupEditActivity.class);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                ToastUtil.showToast(PickGroupEditActivity.this,msg);
            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_left:
                ActivityManager.finishActivity(PickGroupAddActivity.class);
                break;
        }
    }
}
