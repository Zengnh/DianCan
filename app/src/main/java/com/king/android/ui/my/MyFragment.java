package com.king.android.ui.my;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.FragmentMyBinding;
import com.king.android.model.Cart;
import com.king.android.model.User;
import com.king.android.ui.MainActivity;
import com.king.android.ui.login.LoginActivity;
import com.king.base.fragment.BaseFragment;
import com.king.base.utils.DpUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public
class MyFragment extends BaseFragment<FragmentMyBinding> {
    private static final String TAG = "MyFragment";
    private ViewGroup[] views;
    private boolean viewIsShow;

    @Override
    public void init() {
        views = new ViewGroup[]{binding.personInfoLayout,binding.addressListLayout,binding.orderListLayout,binding.myPointsLayout};

        binding.personInfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(PersonInfoActivity.class).start();
            }
        });
        binding.addressListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(AddressListActivity.class).start();
            }
        });
        binding.orderListLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(MyOrderListActivity.class).start();
            }
        });
        binding.myPointsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(MyPointsActivity.class).start();
            }
        });
        binding.walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(MyWalletActivity.class).start();
            }
        });
        binding.outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                User user = new User();
                user.save();
                launch(LoginActivity.class).start();
            }
        });

    }

    private void hide(){
        for (ViewGroup view : views) {
            view.getChildAt(0).setVisibility(View.INVISIBLE);
            view.getChildAt(1).setVisibility(View.INVISIBLE);
        }
    }

    private void show(){
        Log.d(TAG, "show: ");
        for (int i = 0; i < views.length; i++) {
            ViewGroup view = views[i];
            int finalI = i;
            view.post(new Runnable() {
                @Override
                public void run() {
                    View v =  view.getChildAt(0);
                    float trX = -v.getWidth();
                    v.setTranslationX(trX);
                    v.setVisibility(View.VISIBLE);

                    ObjectAnimator animator = ObjectAnimator.ofFloat(v,"TranslationX",trX,0);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.getChildAt(1).setVisibility(View.VISIBLE);
                        }
                    });
                    animator.setDuration((finalI * 100) + 500);
                    animator.start();
                }
            });
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewIsShow == false){
            viewIsShow = true;
            hide();
            show();
        }

        initUserInfo();
        getMoney();
    }

    private void initUserInfo(){
        User user = User.getInstance();
        binding.setData(user);
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.logo);
        Glide.with(thisAtv).load(user.getCover())
                .apply(options).into(binding.avatarIv);

    }

    private void getMoney(){
        User user = User.getInstance();
        ApiHttp.getApiService().moneyTota(user.getAccess_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    binding.moneyTv.setText(new BigDecimal(data.getData().getMoney()).stripTrailingZeros().toPlainString());
                }, t -> {
                    t.printStackTrace();
                });
    }
}
