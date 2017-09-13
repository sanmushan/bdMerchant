package com.gzdb.buyer;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.gzdb.utils.UpdateManager;
import com.gzdb.warehouse.R;
import com.gzdb.warehouse.main.MeFragment;
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
 * Created by Administrator on 2017/4/20 0020.
 */

public class BuyerActivity extends AfinalActivity {

    /**
     *

     com.gzdb.warehouse
     6ac86a8f815f882b5715f1f6

     com.gzdb.merchant
     7c541e6049b67ee42223c3ba

     */


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

        EventBus.getDefault().register(this);

        List<Fragment> fragments = new ArrayList<Fragment>();
      //  fragments.add(new BuyerWarehouseFragment());
        fragments.add(new BuyerOrderFragment());
        fragments.add(new BuyerWareItemFragment());
        fragments.add(new BuyerItemFragment());
        fragments.add(new MeFragment());

        List<String> strings = new ArrayList<String>();
       // strings.add(new String("库存"));
        strings.add(new String("订单"));
        strings.add(new String("仓库"));
        strings.add(new String("采购"));
        strings.add(new String("我的"));

        List<Drawable> drawables = new ArrayList<Drawable>();

        //drawables.add(getResources().getDrawable(R.drawable.icon_oder));
      //  drawables.add(getResources().getDrawable(R.drawable.icon_oder_full));
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
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 执行在主线程。
     * 非常实用，可以在这里将子线程加载到的数据直接设置到界面中。
     * @param msg 事件1
     */
    @Subscribe
    public void onEventMainThread(BuyerActivityEvent msg){
        Log.e("db", "BuyerActivity onEventMainThread");
        tabViewPager.setPagerCurrentItem(msg.fragmentIndex);
    }

}
