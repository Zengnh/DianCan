package com.king.base.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.king.base.activity.BaseActivity;
import com.king.base.utils.ViewDataBindingUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public
abstract
class BaseFragment<VB extends ViewDataBinding> extends BundleFragment{

    public VB binding;
    public Activity thisAtv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = (VB) ViewDataBindingUtils.getViewBinding(getClass(),inflater,0);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        thisAtv = getActivity();
        init();
        initEventBus();
    }

    public void finish(){
        thisAtv.finish();
    }

    public abstract void init();

    private void initEventBus(){
        try {
            if (!EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().register(this);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    public void toast(String text){
        if (thisAtv instanceof BaseActivity){
            ((BaseActivity<?>) thisAtv).toast(text);
        }
    }
}
