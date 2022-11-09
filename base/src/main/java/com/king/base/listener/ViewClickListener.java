package com.king.base.listener;

import android.view.View;

import com.king.base.utils.ClickUtils;

public
abstract
class ViewClickListener implements View.OnClickListener{

    private int delay;

    public ViewClickListener(int delay) {
        this.delay = delay;
    }

    public ViewClickListener() {
        this(500);
    }

    @Override
    public void onClick(View v) {
        if (ClickUtils.click(delay)){
            click(v);
        }
    }

    public abstract void click(View v);
}
