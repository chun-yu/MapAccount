package com.chun.mapaccount;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //表示事件是否攔截，返回false表示不攔截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    //重寫onTouchEvent事件，什麼都不用做
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }
}