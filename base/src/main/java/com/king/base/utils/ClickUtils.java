package com.king.base.utils;

public
class ClickUtils {
    private static long last_click_time;

    public static boolean click(int clickDelayTime){
        long nowTime = System.currentTimeMillis();
        if (nowTime - last_click_time > clickDelayTime){
            last_click_time = nowTime;
            return true;
        }
        return false;
    }
}
