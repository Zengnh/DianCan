package com.king.android.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;


public
class MyListView extends SmartRefreshLayout {

    private RecyclerView rv;

    public MyListView(Context context) {
        super(context);
        init(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context){

        ClassicsHeader ch = new ClassicsHeader(context);
        LayoutParams params3 = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(ch,params3);

        rv = new RecyclerView(context);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setOverScrollMode(OVER_SCROLL_NEVER);
        LayoutParams params1 = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(rv,params1);

        ClassicsFooter cf = new ClassicsFooter(context);
        LayoutParams params2 = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(cf,params2);

    }

    public RecyclerView getRv() {
        return rv;
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        rv.setAdapter(adapter);
    }

    public void finishLoad(){
        finishRefresh();
        finishLoadMore();
    }
}
