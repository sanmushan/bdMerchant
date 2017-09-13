package com.gzdb.utils.baidu;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gzdb.warehouse.R;
import com.zhumg.anlib.widget.AfinalAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/3/30 0030.
 */

public class BaiduAddressAdapter extends AfinalAdapter<BaiduAddressActivity.BaiduPoiInfo> {

    private int ui_type;
    public static final int UI_TYPE_DEFAULT = 0;
    public static final int UI_TYPE_ICON = 1;

    public BaiduAddressAdapter(Context context, List<BaiduAddressActivity.BaiduPoiInfo> baiduPoiInfos) {
        super(context, baiduPoiInfos);
    }

    public void setUi_type(int ui_type) {
        this.ui_type = ui_type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.widget_baidu_address, null);
            holder.tv_now_address = (TextView) convertView.findViewById(R.id.tv_now_address);
            holder.tv_addressdetail = (TextView) convertView.findViewById(R.id.tv_addressdetail);
            holder.tv_ico = (ImageView) convertView.findViewById(R.id.tv_ico);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BaiduAddressActivity.BaiduPoiInfo info = getItem(position);
        if (info.now) {
            holder.tv_now_address.setText("[当前]"+info.name);
            holder.tv_ico.setImageResource(R.drawable.position_map_icon_blue);
            holder.tv_addressdetail.setText(info.address);
            holder.tv_now_address.setTextColor(0xFF00a2ff);
            holder.tv_addressdetail.setTextColor(0xFF00a2ff);
        } else {
            holder.tv_now_address.setText(info.name);
            holder.tv_ico.setImageResource(R.drawable.position_map_icon_gray);
            holder.tv_addressdetail.setText(info.address);
            holder.tv_now_address.setTextColor(0xFF666666);
            holder.tv_addressdetail.setTextColor(0xFF999999);
        }
        if (this.ui_type != UI_TYPE_ICON) {
            convertView.findViewById(R.id.tv_ico).setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder {
        TextView tv_now_address;
        TextView tv_addressdetail;
        ImageView tv_ico;
    }
}
