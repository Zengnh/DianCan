package com.king.base.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.gyf.barlibrary.ImmersionBar;
import com.king.base.R;
import com.king.base.utils.StatusUtils;
import com.king.base.utils.ViewDataBindingUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public
abstract
class BaseActivity<VB extends ViewDataBinding> extends IntentActivity{

    public VB binding;
    public Activity thisAtv;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        StatusUtils.setStatusTextToBlack(this);
        thisAtv = this;
        binding = (VB) ViewDataBindingUtils.getViewBinding(getClass(),getLayoutInflater(),0);
        View view = binding.getRoot();
        setContentView(view);
        init();
        initEventBus();
    }

    public abstract void init();

    private void initEventBus(){
        try {
            if (!EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().register(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void onEventBus(String action){
        if ("finis_all".equals(action)){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    public void toast(String text){
        Toast.makeText(thisAtv, text, Toast.LENGTH_SHORT).show();
    }
}
