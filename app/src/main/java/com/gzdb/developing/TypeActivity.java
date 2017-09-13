package com.gzdb.developing;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import butterknife.Bind;
import com.gzdb.developing.adapter.TypeAdapter;
import com.gzdb.developing.adapter.TypeChildAdapter;
import com.gzdb.response.Api;
import com.gzdb.response.ApiRequest;
import com.gzdb.response.ItemType;
import com.gzdb.utils.GlobalData;
import com.gzdb.warehouse.R;
import com.gzdb.widget.ScrollerListView;
import com.gzdb.widget.Tools;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.http.HttpCallback;
import com.zhumg.anlib.utils.ToastUtil;
import com.zhumg.anlib.widget.bar.BaseTitleBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 作   者：liyunbiao
 * 时   间：2017/5/24
 * 修 改 人：
 * 日   期：
 * 描   述：
 */

public class TypeActivity extends AfinalActivity implements View.OnClickListener ,ScrollerListView.IXListViewListener{
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

    @Override
    public int getContentViewId() {
        return R.layout.activity_type;
    }

    @Override
    public void initView(View view) {
        try {
            baseTitleBar = new BaseTitleBar(view);
            baseTitleBar.setLeftBack(this);
            baseTitleBar.setCenterTxt("经营类别");
            GlobalData.itemTypeId="";

            baseTitleBar.setRightTxt("完成", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        GlobalData.itemTypeId="";
                        for(int i=0;i<Alllist.size();i++){
                            if(Alllist.get(i).isSelect()) {
                                GlobalData.itemTypeId =  GlobalData.itemTypeId +Alllist.get(i).getId() + ",";
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent();
                    intent.putExtra("op","type");
                    setResult(RESULT_OK, intent);
                    TypeActivity.this.finish();
                }
            });
            list = new ArrayList<>();
            Alllist = new ArrayList<>();
            child_list=new ArrayList<>();
            adapter = new TypeAdapter(TypeActivity.this, list);
            lv_type.setAdapter(adapter);
            typeChildAdapter=new TypeChildAdapter(TypeActivity.this,child_list);
            lv_child_list.setAdapter(typeChildAdapter);
            lv_child_list.setPullLoadEnable(true);
            lv_child_list.setXListViewListener(this);
            initDataBigType();
            lv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        ItemType itemType=(ItemType)parent.getAdapter().getItem(position);
                        child_list.clear();
                        for(int i=0;i<Alllist.size();i++){
                            if(Alllist.get(i).getParentId()==null){
                                break;
                            }
                            if(Alllist.get(i).getParentId().equals(itemType.getId())){
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
                        ItemType itemType=(ItemType)parent.getAdapter().getItem(position);
                        if(itemType==null){
                            return;
                        }
                        if(itemType.getPos()>0){
                            lv_type.getChildAt(itemType.getPos()).setBackgroundColor(Color.parseColor("#f2f2f2"));
                        }
                        (typeChildAdapter.getItem(position-1)).setSelect(!child_list.get(position-1).isSelect());
                        typeChildAdapter.notifyDataSetChanged();

                        for(int i=0;i<Alllist.size();i++){
                            if(typeChildAdapter.getItem(position-1).getId()==Alllist.get(i).getId()){
                                Alllist.get(i).setSelect(typeChildAdapter.getItem(position-1).isSelect());
                                return;
                            }
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

                    for(int i=0;i<data.size();i++){
                        if(data.get(i).getParentId()==null){
                            break;
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                startActivity(new Intent(TypeActivity.this, NextDevelopingActivity.class));
                break;
        }
    }
}
