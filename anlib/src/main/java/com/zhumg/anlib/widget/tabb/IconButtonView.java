package com.zhumg.anlib.widget.tabb;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhumg.anlib.R;


public class IconButtonView implements SelectedView {

    public static int ICON_LEFT = 0;
    public static int ICON_TOP = 1;

    private ImageView imgview;
    private TextView txtview;
    private LinearLayout layout;
    private int textColor;
    private int textSize;
    private String icoUrl;
    private Drawable drawable;
    private int selectTextColor;
    private int selectTextSize;
    private String selectIcoUrl;
    private Drawable selectDrawable;

    public IconButtonView(Context context, int iconStyle) {
        if(iconStyle == ICON_LEFT) {
            layout = (LinearLayout) View.inflate(context, R.layout.widget_btn_left_icon, null);
        }else {
            layout = (LinearLayout) View.inflate(context, R.layout.widget_btn_top_icon, null);
        }
        imgview = (ImageView)layout.findViewById(R.id.btn_icon_img);
        txtview = (TextView)layout.findViewById(R.id.btn_icon_txt);
    }

    public String getSelectIcoUrl() {
        return selectIcoUrl;
    }

    public void setSelectIcoUrl(String selectIcoUrl) {
        this.selectIcoUrl = selectIcoUrl;
    }

    public String getIcoUrl() {
        return icoUrl;
    }

    public void setIcoUrl(String icoUrl) {
        this.icoUrl = icoUrl;
    }

    public void setText(String text) {
        this.txtview.setText(text);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    public int getSelectTextColor() {
        return selectTextColor;
    }

    public void setSelectTextColor(int selectTextColor) {
        this.selectTextColor = selectTextColor;
    }

    public int getSelectTextSize() {
        return selectTextSize;
    }

    public void setSelectTextSize(int selectTextSize) {
        this.selectTextSize = selectTextSize;
    }

    public Drawable getSelectDrawable() {
        return selectDrawable;
    }

    public void setSelectDrawable(Drawable selectDrawable) {
        this.selectDrawable = selectDrawable;
    }

    public void setSelected(boolean select) {
        if(!select) {
            this.txtview.setTextSize(this.textSize);
            this.txtview.setTextColor(this.textColor);
            if(this.icoUrl != null) {
                icoUrlChangeSelect(false);
            }else {
                icoChangeSelect(false);
            }
            return;
        }
        this.txtview.setTextSize(this.selectTextSize);
        this.txtview.setTextColor(this.selectTextColor);
        if(this.icoUrl != null) {
            icoUrlChangeSelect(true);
        }else {
            icoChangeSelect(true);
        }
    }

    private void icoUrlChangeSelect(boolean select) {
        if(!select) {
            //切换回默认

        }else {
            //切换去选中

        }
    }

    private void icoChangeSelect(boolean select) {
        if(!select) {
            //切换回默认
            this.imgview.setImageDrawable(this.drawable);
        }else {
            //切换去选中
            this.imgview.setImageDrawable(this.selectDrawable);
        }
    }

    public View getView() {
        return this.layout;
    }
}

