package com.zhumg.anlib.widget.tabb;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;


public class IconButtonGroupView extends SelectedViewGroup {


    public IconButtonGroupView(Context context) {
        super(context);
    }

    public IconButtonGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconButtonGroupView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化 图标 按钮组
     *
     * @param drawables
     * @param names
     * @param textColor
     * @param selectTextColor
     * @param textSize
     * @param selectTextSize
     */
    public void initTopIconButtons(List<Drawable> drawables, List<String> names, int textColor, int selectTextColor, int textSize, int selectTextSize, int iconStyle) {
        tabItems.clear();
        removeAllViews();
        int index = 0;
        for (int i = 0; i < names.size(); i++) {
            Drawable defaultDrawable = drawables.get(index);
            Drawable selectDrawable = drawables.get(index + 1);

            index += 2;

            String name = names.get(i);

            IconButtonView tab = new IconButtonView(getContext(), iconStyle);

            tab.setTextColor(textColor);
            tab.setTextSize(textSize);
            tab.setDrawable(defaultDrawable);
            tab.setText(name);

            //选中状态
            tab.setSelectTextColor(selectTextColor);
            tab.setSelectTextSize(selectTextSize);
            tab.setSelectDrawable(selectDrawable);
            tab.setSelected(false);

            tabItems.add(tab);

            //添加到当前
            View view = tab.getView();
            view.setTag(i);
            view.setOnClickListener(this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            );
            lp.weight = 1;

            view.setLayoutParams(lp);
            addView(view);
        }
        requestLayout();
    }

    /**
     * 切换
     *
     * @param icoUrls
     * @param names
     */
    public void changeTopIconButtons(List<String> icoUrls, List<String> names) {
        int index = 0;
        for (int i = 0; i < tabItems.size(); i++) {
            IconButtonView tab = (IconButtonView)tabItems.get(i);
            String icoUrl = icoUrls.get(index);
            String selectIcoUrl = icoUrls.get(index + 1);
            String name = names.get(i);
            tab.setIcoUrl(icoUrl);
            tab.setSelectIcoUrl(selectIcoUrl);
            tab.setText(name);
        }
    }
}
