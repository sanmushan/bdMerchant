package com.zhumg.anlib.widget;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by zhumg on 2017/4/14 0014.
 */

public class RemoveListWidget {

    View delView;
    int delPosition;
    AfinalAdapter afinalAdapter;
    DelCallback delCallback;

    public RemoveListWidget(AfinalAdapter afinalAdapter, DelCallback delCallback) {
        this.afinalAdapter = afinalAdapter;
        this.delCallback = delCallback;
    }

    public void setDelDatas(View delv, int delp) {
        this.delView = delv;
        this.delPosition = delp;
    }

    public void runDelete() {
        final int originHeight = delView.getMeasuredHeight();
        Animation.AnimationListener al = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                delView.getLayoutParams().height = originHeight;
                delView.setVisibility(View.VISIBLE);
                //删除并刷新
                afinalAdapter.remove(delPosition);
                afinalAdapter.notifyDataSetChanged();
                if(delCallback!=null){
                    delCallback.onDelSuccess();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
        deletePattern(al);
    }

    void deletePattern(Animation.AnimationListener al) {
        final int originHeight = delView.getMeasuredHeight();
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1.0f) {

                } else {
                    delView.getLayoutParams().height = originHeight - (int) (originHeight * interpolatedTime);
                    delView.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        if (al != null) {
            animation.setAnimationListener(al);
        }
        animation.setDuration(500);
        delView.startAnimation(animation);
    }


    public static interface DelCallback {
        public void onDelSuccess();
    }
}
