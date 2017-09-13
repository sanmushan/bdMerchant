package com.gzdb.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作   者：liyunbiao
 * 时   间：16/4/12.
 * 修 改 人：
 * 日   期：
 * 描   述：
 */
public abstract class BaseGenericAdapter<T> extends BaseAdapter {
    public final String TAG = getClass().getSimpleName();
    protected List<T> list;
    protected Context context;
    protected LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局

    public BaseGenericAdapter(Context context){
        this.context = context.getApplicationContext();
        list = new ArrayList<>();
        mInflater = LayoutInflater.from(context);
    }

    public BaseGenericAdapter(Context context, List<T> list) {
        this.context = context.getApplicationContext();
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup viewGroup);

    public void delete(int pos) {
        list.remove(pos);
    }

    public void deleteData(T itemData){
        if (list != null && list.size() > 0){
            list.remove(itemData);
            notifyDataSetChanged();
        }
    }

    public void add(T t) {
        list.add(t);
    }
    public void add(T t,int pos) {
        list.add(pos, t);
    }

    public List<T> getData() {
        return list;
    }

    public void setData(List<T> data){
        this.list = data;
        notifyDataSetChanged();
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public void addData(List<T> data){
        if (data != null && !data.isEmpty()) {
            list.addAll(data);
            notifyDataSetChanged();
        }
    }

    public void addNewData(List<T> data){
        if (data != null && !data.isEmpty()) {
            list.clear();
            list.addAll(data);
            notifyDataSetChanged();
        }
    }

    protected abstract class ViewHolder {}

}