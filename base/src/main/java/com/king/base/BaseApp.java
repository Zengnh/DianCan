package com.king.base;

import android.app.Application;

import com.tencent.mmkv.MMKV;

public
class BaseApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MMKV.initialize(this);
    }
}
