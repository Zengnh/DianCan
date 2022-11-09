package com.king.base.interface_;

import android.view.View;

import androidx.databinding.ViewDataBinding;

public
interface OnItemClickListener<D,VB extends ViewDataBinding> {

    void onClick(View v,D data,VB binding,int position);
}
