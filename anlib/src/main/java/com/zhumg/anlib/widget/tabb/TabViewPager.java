package com.zhumg.anlib.widget.tabb;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhumg.anlib.widget.FragmentAdapter;

import java.util.List;

public class TabViewPager {

    protected List<Fragment> fragments;
    protected int nowIndex = -1;
    protected ViewPager pager;
    protected SelectedViewGroup buttonGroupView;
    protected View.OnClickListener[] listeners;
    protected int[] indexs;
    protected TabViewPagerSelectListener tabSelectListener;

    public TabViewPager(FragmentActivity activity, final ViewPager pager, final SelectedViewGroup buttonGroupView, List<Fragment> fragments) {
        this.pager = pager;
        this.buttonGroupView = buttonGroupView;
        this.fragments = fragments;

        this.pager.setAdapter(new FragmentAdapter<>(activity.getSupportFragmentManager(), fragments));

        this.pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < indexs.length; i++) {
                    if (indexs[i] != position) {
                        continue;
                    }
                    nowIndex = position;
                    pager.setCurrentItem(position);
                    buttonGroupView.setSelected(i);
                    return;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        this.buttonGroupView.addSelectedListener(new SelectedViewGroup.OnSelectedListener() {
            @Override
            public void onSelected(int old_index, int new_index) {
                //如果有事件
                if (listeners != null) {
                    View.OnClickListener listener = listeners[new_index];
                    if (listener != null) {
                        //回滚选中
                        buttonGroupView.setSelected(old_index);
                        //事件点击
                        listener.onClick(buttonGroupView.getSelectedView(old_index).getView());
                        return;
                    }
                }
                int position = indexs[new_index];
                pager.setCurrentItem(position);
                if(tabSelectListener != null) {
                	tabSelectListener.onSelected(new_index);
                }
                nowIndex = position;
            }
        });

        int buttonCount = buttonGroupView.getSelectedViewCount();
        if (buttonCount == 0) {
            throw new IllegalAccessError("IconButtonGroupView not init, button count is 0");
        }
        this.indexs = new int[buttonCount];
        for (int i = 0; i < indexs.length; i++) {
            indexs[i] = i;
        }
    }
    
    public void setTabViewPageSelectListener(TabViewPagerSelectListener listener) {
    	this.tabSelectListener = listener;
    }

    public void setPagerCurrentItem(int index) {
        if(nowIndex == index) {
            return;
        }
        this.pager.setCurrentItem(index);
        this.buttonGroupView.setSelected(index);
    }

    public Fragment getFragment(int index) {
        try {
            return fragments.get(index);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 设置按钮可以被单独点击处理
     *
     * @param index    按钮索引位置
     * @param listener 点击处理器
     */
    public void setSelfClickListener(int index, View.OnClickListener listener) {
        int buttonCount = buttonGroupView.getSelectedViewCount();
        if (buttonCount == 0) {
            throw new IllegalAccessError("IconButtonGroupView not init, button count is 0");
        }
        if (this.listeners == null) {
            this.listeners = new View.OnClickListener[buttonCount];
        }
        if (indexs == null) {
            this.indexs = new int[buttonCount];
        }
        int sIndex = 0;
        for (int i = 0; i < indexs.length; i++) {

            if (i == index) {
                indexs[i] = -1;
            } else if (indexs[i] == -1) {
                continue;
            } else {
                indexs[i] = sIndex;
                sIndex++;
            }
        }
        this.listeners[index] = listener;
    }
}