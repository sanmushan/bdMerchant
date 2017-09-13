package com.gzdb.warehouse.main;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.gzdb.response.enums.OrderRoleTypeEnum;
import com.gzdb.response.enums.OrderTypeEnum;
import com.gzdb.response.enums.StatusEnterEnum;
import com.gzdb.warehouse.R;
import com.gzdb.widget.TitleViewPager;
import com.zhumg.anlib.AfinalActivity;
import com.zhumg.anlib.AfinalFragment;
import com.zhumg.anlib.utils.DeviceUtils;
import com.zhumg.anlib.widget.bar.SearchTitleBar;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyunbiao on 2017/7/27.
 */

public class BackOrderActivity extends AfinalActivity implements View.OnClickListener{
    SearchTitleBar searchTitleBar;
    TitleViewPager titleViewPager;
    List<Fragment> fragments = new ArrayList<>();
    @Override
    public int getContentViewId() {
        return R.layout.fragment_back_order;
    }

    @Override
    public void initView(View view) {
        searchTitleBar = new SearchTitleBar(view);
        searchTitleBar.setLeftBack(BackOrderActivity.this);
        searchTitleBar.setRightTxt("搜索");
        searchTitleBar.setRightListener(this);
        searchTitleBar.getCenter_search_edit().setHint("请输入订单号进行搜索");

        searchTitleBar.setEditTxtListener(new SearchTitleBar.EditTextListener() {
            @Override
            public void notifyInputTxt(String str) {
                //如果有内容，则搜索
                if (str != null && str.trim().length() > 0) {
                    //搜索
                    //search(str);
                } else {
                    //清除

                }
            }
        });
        fragments.add(BackListFragment.create(OrderRoleTypeEnum.MERCHANT, OrderTypeEnum.SALE_ORDER_TYPE, StatusEnterEnum.HANDLER));
        fragments.add(BackListFragment.create(OrderRoleTypeEnum.MERCHANT, OrderTypeEnum.SALE_ORDER_TYPE, StatusEnterEnum.BATCH));
        fragments.add(BackListFragment.create(OrderRoleTypeEnum.MERCHANT, OrderTypeEnum.SALE_ORDER_TYPE, StatusEnterEnum.DELIVERY));
        titleViewPager = new TitleViewPager(view,getSupportFragmentManager(), fragments, DeviceUtils.screenWidth(BackOrderActivity.this));
    }

    @Override
    public void onClick(View v) {
        Animation ani= AnimationUtils.loadAnimation(BackOrderActivity.this,R.anim.alpha_action);
        v.startAnimation(ani);


    }


}
