package com.gzdb.warehouse;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gzdb.response.Api;
import com.gzdb.warehouse.main.MeFragment;
import com.gzdb.warehouse.main.OrderFragment;
import com.gzdb.warehouse.main.WareItemFragment;
import com.gzdb.warehouse.main.WareOrderFragment;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.widget.tabb.IconButtonGroupView;
import com.zhumg.anlib.widget.tabb.IconButtonView;
import com.zhumg.anlib.widget.tabb.TabViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class MainActivity extends AfinalActivity {

    @Bind(R.id.wta_viewpager)
    ViewPager wtaViewpager;
    @Bind(R.id.wta_h)
    View wtaH;
    @Bind(R.id.wta_tab)
    IconButtonGroupView wtaTab;
    TabViewPager tabViewPager;

    @Bind(R.id.txtTest)
    TextView txtTest;

    @Override
    public int getContentViewId() {
        return R.layout.widget_activity_tab;
    }

    @Override
    public void initView(View view) {

        setTranslucentStatus();

        EventBus.getDefault().register(this);

        if (Api.isULR == true){
            txtTest.setVisibility(View.GONE);
        }


        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(new OrderFragment());

        fragments.add(new WareItemFragment());
        WareOrderFragment wf = new WareOrderFragment();
        fragments.add(wf);
        fragments.add(new MeFragment());

        List<String> strings = new ArrayList<String>();
        strings.add(new String("订单"));
        strings.add(new String("调拨"));
        strings.add(new String("仓管"));
        strings.add(new String("我的"));

        List<Drawable> drawables = new ArrayList<Drawable>();

        drawables.add(getResources().getDrawable(R.drawable.icon_oder));
        drawables.add(getResources().getDrawable(R.drawable.icon_oder_full));

        drawables.add(getResources().getDrawable(R.drawable.icon_warehouse));
        drawables.add(getResources().getDrawable(R.drawable.icon_warehouse_full));

        drawables.add(getResources().getDrawable(R.drawable.icon_procurement));
        drawables.add(getResources().getDrawable(R.drawable.icon_procurement_full));



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
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    /**
     * 执行在主线程。
     * 非常实用，可以在这里将子线程加载到的数据直接设置到界面中。
     *
     * @param msg 事件1
     */
    @Subscribe
    public void onEventMainThread(MainActivityEvent msg) {
        Log.e("db", "MainActivity onEventMainThread");
        tabViewPager.setPagerCurrentItem(msg.fragmentIndex);
    }
}
