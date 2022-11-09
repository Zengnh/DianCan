package com.king.android.tools;

import android.os.CountDownTimer;

public class MyTimer extends CountDownTimer {

    public static final int second = 1000;
    public static final int minute = 1000 * 60;
    public static final int hour = 1000 * 60 * 60;

    private OnListener onListener;

    public MyTimer(long subtle) {
        super(subtle, 1000);
    }

    public MyTimer(int second) {
        super(second * MyTimer.second, 1000);
    }

    public MyTimer(int minute,int second){
        this( Long.parseLong(String.valueOf((minute * MyTimer.minute) + (second * MyTimer.second))));
    }

    public MyTimer(int hour,int minute,int second){
        this(Long.parseLong(String.valueOf((hour * MyTimer.hour) + (minute * MyTimer.minute) + (second * MyTimer.second))));
    }

    public MyTimer setOnListener(OnListener onListener) {
        this.onListener = onListener;
        return this;
    }

    @Override
    public void onTick(long mi) {
        if (onListener != null){
            int h = (int) (mi / hour);
            int m = (int) (mi / minute);
            int s = (int) ((mi % minute / second));
            onListener.onTime(h,m,s);
        }
    }

    @Override
    public void onFinish() {
        if (onListener != null){
            onListener.onFinish();
        }
    }

    public interface OnListener{
        void onTime(int hour,int minute,int second);

        void onFinish();
    }
}
