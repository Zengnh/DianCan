package com.king.android.ui.login;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityForgetPassowrdBinding;
import com.king.android.tools.MyTimer;
import com.king.base.activity.BaseActivity;
import com.king.base.listener.ViewClickListener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 忘记密码
 */
public
class ForgetPasswordActivity extends BaseActivity<ActivityForgetPassowrdBinding> implements MyTimer.OnListener{

    private MyTimer timer;

    @Override
    public void init() {
        TextView titleTv = binding.getRoot().findViewById(R.id.title_tv);
        titleTv.setText("忘記密碼");

        ImageView backIv = binding.getRoot().findViewById(R.id.back_iv);
        backIv.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.updatePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveMember();
            }
        });


        binding.sendCodeBtn.setOnClickListener(new ViewClickListener() {
            @Override
            public void click(View v) {
                if (timer == null){
                    sendCode();
                }
            }
        });
    }

    private void retrieveMember(){
        String phone = binding.editPhone.getText().toString();
        String password1 = binding.editPsw.getText().toString();
        String password2 = binding.editPsw2.getText().toString();
        String code = binding.editCode.getText().toString();
        if (phone.isEmpty() || password1.isEmpty()
                || password2.isEmpty() || code.isEmpty()){
            toast("請完成全部信息");
            return;
        }

        Map<String,Object> map  =new HashMap<>();
        map.put("mobile",phone);
        map.put("password1",password1);
        map.put("password2",password2);
        map.put("code",code);
        ApiHttp.getApiService().retrieveMember(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    toast(data.getMsg());
                    if (data.isSuccess()){
                        finish();
                    }
                }, t -> {});
    }

    private void sendCode(){
        String code = binding.editPhone.getText().toString();
        if (code.isEmpty()){
            toast("請輸入手機號碼");
            return;
        }
        ApiHttp.getApiService().sendCode(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    toast(data.getMsg());
                    if (data.isSuccess()){
                        timer = new MyTimer(60);
                        timer.setOnListener(ForgetPasswordActivity.this);
                        timer.start();
                    }
                }, t -> {
                    toast("網絡異常");
                });
    }

    @Override
    public void onTime(int hour, int minute, int second) {
        binding.sendCodeBtn.setText(second+"s");
    }

    @Override
    public void onFinish() {
        timer = null;
        binding.sendCodeBtn.setText("發送驗證碼");
    }
}
