package com.king.android.ui;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityMainBinding;
import com.king.android.model.User;
import com.king.android.tools.ToolFinish;
import com.king.android.ui.home.HomeFragment;
import com.king.android.ui.login.LoginActivity;
import com.king.android.ui.message.MessageFragment;
import com.king.android.ui.my.MyFragment;
import com.king.android.ui.order.OrderFragment;
import com.king.android.ui.shopcar.ShopCarFragment;
import com.king.base.activity.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity<ActivityMainBinding> implements View.OnClickListener{

    @Override
    public void init() {

        ToolFinish toolFinish=new ToolFinish();
//
//        if(toolFinish.isCanFinish()){
////            Log.i("znh","###################");
//            finish();
//        }else{
////            Log.i("znh","@@@@@");
//        }

        binding.tab1Layout.setOnClickListener(this);
        binding.tab2Layout.setOnClickListener(this);
        binding.tab3Layout.setOnClickListener(this);
        binding.tab4Layout.setOnClickListener(this);
        binding.tabMsgLayout.setOnClickListener(this);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new OrderFragment());
        fragments.add(new ShopCarFragment());
        fragments.add(new MyFragment());
        fragments.add(new MessageFragment());
        binding.vp.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                return fragments.get(position);
            }

            @Override
            public int getItemCount() {
                return fragments.size();
            }
        });
        binding.vp.setUserInputEnabled(false);
        binding.vp.setOffscreenPageLimit(fragments.size());
//        binding.vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                select(position);
//            }
//        });

        User u = User.getInstance();
        if(TextUtils.isEmpty(u.getAccess_token())){
            finish();
            launch(LoginActivity.class).start();
        }else {
           loginFormLocal(u);
        }

    }

    private void select(int position){
        TextView tabsTv[] = {binding.tab1Tv,binding.tab2Tv,binding.tab3Tv,binding.tab4Tv,binding.tabMsgTv};
        ImageView tabsIv[] = {binding.tab1Iv,binding.tab2Iv,binding.tab3Iv,binding.tab4Iv,binding.tabMsgIv};
        int[] selectIvIds = {R.mipmap.home_select,R.mipmap.order_select,R.mipmap.cat_select,R.mipmap.my_select,R.mipmap.msg_sel};
        int[] unselectIvIds = {R.mipmap.home_unselect,R.mipmap.order_unselect,R.mipmap.cat_unselect,R.mipmap.my_unselect,R.mipmap.msg_nor};
        for (int i = 0; i < tabsTv.length; i++) {
            TextView tv = tabsTv[i];
            ImageView iv = tabsIv[i];
            if (position == i){
                tv.setTextColor(Color.parseColor("#FCC328"));
                iv.setImageResource(selectIvIds[i]);
            }else {
                tv.setTextColor(Color.parseColor("#7D7D7D"));
                iv.setImageResource(unselectIvIds[i]);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == binding.tab1Layout){
            binding.vp.setCurrentItem(0,false);
            select(0);
        }else if (v == binding.tab2Layout){
            binding.vp.setCurrentItem(1,false);
            select(1);
        }else if (v == binding.tab3Layout){
            binding.vp.setCurrentItem(2,false);
            select(2);
        }else if (v == binding.tab4Layout){
            binding.vp.setCurrentItem(3,false);
            select(3);
        }else if (v == binding.tabMsgLayout){
            binding.vp.setCurrentItem(4,false);
            select(4);
        }
    }

    @Override
    public void onEventBus(String action) {
        if ("select_car".equals(action)){
            onClick(binding.tab3Layout);
        }else if ("select_home".equals(action)){
            onClick(binding.tab1Layout);
        }
    }

    private void loginFormLocal(User user){
        Map<String,Object> map  =new HashMap<>();
        map.put("mobile",user.getMobile());
        map.put("password",user.getPassword());
        map.put("type","1");
        ApiHttp.getApiService().login(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    if (data.isSuccess()){
                        data.getData().save();
                    }
                }, t -> {});
    }
}