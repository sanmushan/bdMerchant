package com.zhumg.anlib.widget.tabb;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.zhumg.anlib.R;

import java.util.ArrayList;
import java.util.List;

public class SelectedViewGroup extends LinearLayout implements View.OnClickListener {

    protected List<SelectedView> tabItems = new ArrayList<>();
    protected List<OnSelectedListener> listener = new ArrayList<>();
    protected int selectIndex = -1;
    protected boolean clickAniOpen;
    protected Animation ani;

    public SelectedViewGroup(Context context) {
        super(context);
        initView();
    }

    public SelectedViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SelectedViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER);
    }

    public void setClickAniOpen(boolean open) {
        clickAniOpen = open;
    }

    /**
     * 设置某个按钮点击事件
     */
    public void addSelectedListener(OnSelectedListener listener) {
        this.listener.add(listener);
    }

    public void setSelected(int index) {
        if (selectIndex == index) return;
        if (selectIndex != -1) {
            //先设置为默认状态
            tabItems.get(selectIndex).setSelected(false);
        }
        selectIndex = index;
        tabItems.get(selectIndex).setSelected(true);
    }

    @Override
    public void onClick(View v) {
        if(clickAniOpen) {
            if(ani == null) {
                ani = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_action);
            }
            v.startAnimation(ani);
        }
        int tag = (Integer) v.getTag();
        if (tag == selectIndex) {
            return;
        }
        int old_index = selectIndex;
        if (selectIndex != -1) {
            //先设置为默认状态
            tabItems.get(selectIndex).setSelected(false);
        }
        selectIndex = tag;
        tabItems.get(selectIndex).setSelected(true);

        if (this.listener != null) {
            for (int i = 0; i < this.listener.size(); i++) {
                this.listener.get(i).onSelected(old_index, selectIndex);
            }
        }
    }

    public SelectedView getSelectedView(int index) {
        return tabItems.get(index);
    }

    public int getSelectedViewCount() {
        return this.tabItems.size();
    }

    public interface OnSelectedListener {
        public void onSelected(int old_index, int new_index);
    }
}
