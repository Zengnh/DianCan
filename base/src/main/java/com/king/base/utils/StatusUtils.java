package com.king.base.utils;

import android.app.Activity;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.gyf.barlibrary.ImmersionBar;

import crossoverone.statuslib.StatusUtil;

public
class StatusUtils {

    public static void initStatusBar(Activity context, View statusBarView,int navigationColor){
        ImmersionBar.with(context)
                .statusBarDarkFont(true)
                .navigationBarDarkIcon(true)
                .statusBarView(statusBarView) //状态控件
                .navigationBarColor(navigationColor) //导航栏颜色
                .init();
    }

    public static void initStatusBar(Fragment context, View statusBarView, int navigationColor){
        ImmersionBar.with(context)
                .statusBarDarkFont(true)
                .navigationBarDarkIcon(true)
                .statusBarView(statusBarView) //状态控件
                .navigationBarColor(navigationColor) //导航栏颜色
                .init();
    }

    public static void setStatusTextToBlack(Activity activity){
        StatusUtil.setSystemStatus(activity, false, true);
    }
}
