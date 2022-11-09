package com.king.android.ui.anim;

import java.util.List;

public
abstract
class RvAddItemRunnable<D> implements Runnable{

    private int position;
    private List<D> data;

    @Override
    public void run() {
        if (data != null && data.size() > position){
            onData(data.get(position),position);
            position ++;
        }else {
            position = 0;
        }
    }

    public void setData(List<D> data) {
        this.data = data;
        position = 0;
    }

    public abstract void onData(D data,int position);
}
