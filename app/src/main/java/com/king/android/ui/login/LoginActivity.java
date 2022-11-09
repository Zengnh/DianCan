package com.king.android.ui.login;

import android.view.View;
import android.widget.TextView;

import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityLoginBinding;
import com.king.android.ui.MainActivity;
import com.king.base.activity.BaseActivity;
import com.king.base.dialog.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public
class LoginActivity extends BaseActivity<ActivityLoginBinding> {

    private LoadingDialog dialog;
    @Override
    public void init() {
        TextView titleTv = binding.getRoot().findViewById(R.id.title_tv);
        titleTv.setText("登  入");

        binding.registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(RegisterActivity.class).start();
            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        binding.forgetPas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(ForgetPasswordActivity.class).start();
            }
        });

        dialog = new LoadingDialog(thisAtv);
    }

    private void login(){
        String phone = binding.editPhone.getText().toString();
        String password1 = binding.editPsw.getText().toString();
        if (phone.isEmpty() || password1.isEmpty()){
            toast("請输入手机号码和密码");
            return;
        }
        dialog.show();
        Map<String,Object> map  =new HashMap<>();
        map.put("mobile",phone);
        map.put("password",password1);
        map.put("type","1");
        ApiHttp.getApiService().login(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    dialog.dismiss();
                    if (data.isSuccess()){
                        data.getData().save();
                        launch(MainActivity.class).start();
                        finish();
                    }
                }, t -> {
                    dialog.dismiss();
                });
    }
}
