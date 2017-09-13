package com.gzdb.zwarehouse;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gzdb.utils.UpdateManager;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.main.MeFragment;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.widget.tabb.IconButtonGroupView;
import com.zhumg.anlib.widget.tabb.IconButtonView;
import com.zhumg.anlib.widget.tabb.TabViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zhumg on 4/24.
 * 主仓 主界面
 */
public class ZWarehouseActivity extends AfinalActivity {

    @Bind(R.id.wta_viewpager)
    ViewPager wtaViewpager;
    @Bind(R.id.wta_h)
    View wtaH;
    @Bind(R.id.wta_tab)
    IconButtonGroupView wtaTab;
    TabViewPager tabViewPager;

    @Override
    public int getContentViewId() {
        return R.layout.widget_activity_tab;
    }

    @Override
    public void initView(View view) {

        setTranslucentStatus();

        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new ZWarehouseOrderFragment());
        fragments.add(new ZWarehouseItemFragment());
        fragments.add(new MeFragment());

        List<String> strings = new ArrayList<String>();
        strings.add(new String("订单"));
        strings.add(new String("仓库"));
        strings.add(new String("我的"));

        List<Drawable> drawables = new ArrayList<Drawable>();

        drawables.add(getResources().getDrawable(R.drawable.icon_oder));
        drawables.add(getResources().getDrawable(R.drawable.icon_oder_full));

        drawables.add(getResources().getDrawable(R.drawable.icon_warehouse));
        drawables.add(getResources().getDrawable(R.drawable.icon_warehouse_full));

        drawables.add(getResources().getDrawable(R.drawable.icon_my));
        drawables.add(getResources().getDrawable(R.drawable.icon_my_full));

        wtaTab.initTopIconButtons(drawables, strings,
                getResources().getColor(R.color.font_6), getResources().getColor(R.color.blue), 12, 12, IconButtonView.ICON_TOP);
        wtaTab.setPadding(0, 10, 0, 5);

        tabViewPager = new TabViewPager(this, wtaViewpager, wtaTab, fragments);
//        tabViewPager.setTabViewPageSelectListener(new TabViewPagerSelectListener() {
//
//            @Override
//            public void onSelected(int tab) {
//                Log.e("jzht", "click " + tab);
//                String eventName = null;
//            }
//        });
        //设置默认选中第一个
        tabViewPager.setPagerCurrentItem(0);
        //点击动画效果
        wtaTab.setClickAniOpen(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //如果需要跳转到指定页面
        if(jumpTabIndex != -1) {
            tabViewPager.setPagerCurrentItem(jumpTabIndex);
            jumpTabIndex = -1;
        }
    }

    public static int jumpTabIndex = -1;
}
