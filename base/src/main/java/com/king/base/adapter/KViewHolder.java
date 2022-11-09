package com.king.base.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.ViewDataBinding;

import com.king.base.utils.ViewDataBindingUtils;

public
abstract
class KViewHolder<VB extends ViewDataBinding> {

    BaseKViewHolder<VB> holder;
    private Class viewHolder;
    private boolean isHeightMatchParent = true;

    public KViewHolder() {
        viewHolder = ViewDataBindingUtils.getParamClass(getClass(), 0);
    }

    public KViewHolder(boolean isHeightMatchParent) {
        this();
        this.isHeightMatchParent = isHeightMatchParent;
    }

    public void initHolder(LayoutInflater inflater, ViewGroup viewGroup){
        Object binding = ViewDataBindingUtils.getViewBinding(viewHolder,inflater,viewGroup,false);
        holder = new BaseKViewHolder<VB>((VB) binding);
    }

    public abstract void onViewBindData(VB binding);

    public boolean isHeightMatchParent() {
        return isHeightMatchParent;
    }
}
