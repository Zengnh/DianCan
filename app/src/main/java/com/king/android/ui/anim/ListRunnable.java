package com.king.android.ui.anim;

import java.util.List;

public
abstract
class ListRunnable<D> implements Runnable{

    private int position;
    private List<D> data;

    @Deprecated
    @Override
    public final void run() {
        if (data != null && data.size() > 0){
            if (position >= data.size()){
                position = 0;
            }
            onData(data.get(position));
            position ++;
        }
    }

    public final void setData(List<D> data) {
        this.data = data;
        position = 0;
    }

    public abstract void onData(D data);

    public int getPosition() {
        return position;
    }
}
