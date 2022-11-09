package com.king.base.adapter;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

public class BaseKViewHolder<VB extends ViewDataBinding> extends RecyclerView.ViewHolder {

    public VB binding;
    private int itemPosition;

    public BaseKViewHolder(VB binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public int getItemPosition() {
        return itemPosition;
    }

    public void setItemPosition(int itemPosition) {
        this.itemPosition = itemPosition;
    }
}