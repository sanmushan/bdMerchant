package com.gzdb.utils;

import java.util.ArrayList;

/**
 * Created by ramonqlee on 6/17/16.
 */
public class PushDispatcher {
    private static PushDispatcher sPushDispatcher = new PushDispatcher();
    private ArrayList<PushObserver> mPushObserverList = new ArrayList<>();

    public static PushDispatcher sharedInstance() {
        return sPushDispatcher;
    }

    public void addObserver(PushObserver observer) {
        if (null == observer || -1 != mPushObserverList.indexOf(observer)) {
            return;
        }
        mPushObserverList.add(observer);
    }

    public void removeObserver(PushObserver observer) {
        if (null == observer) {
            return;
        }
        mPushObserverList.remove(observer);
    }

    public void clearObserver() {
        mPushObserverList.clear();
    }

    public void dispatch(String message) {
        try {
            //bugfix:防止出现ConcurrentModificationException
            ArrayList<PushObserver> tempList = new ArrayList<>(mPushObserverList);
            for (PushObserver observer : tempList) {
                if (null == observer) {
                    continue;
                }
                observer.onMessage(message);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
