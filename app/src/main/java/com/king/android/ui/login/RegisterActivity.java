package com.king.android.ui.login;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityLoginBinding;
import com.king.android.databinding.ActivityRegisterBinding;
import com.king.android.tools.MyTimer;
import com.king.base.activity.BaseActivity;
import com.king.base.dialog.LoadingDialog;
import com.king.base.listener.ViewClickListener;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 注册
 */
public
class RegisterActivity extends BaseActivity<ActivityRegisterBinding> implements MyTimer.OnListener{

    private MyTimer timer;
    private boolean guizeSelect = true;
    private  LoadingDialog dialog;

    @Override
    public void init() {
        TextView titleTv = binding.getRoot().findViewById(R.id.title_tv);
        titleTv.setText("注冊");

        ImageView backIv = binding.getRoot().findViewById(R.id.back_iv);
        backIv.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guizeSelect == false){
                    toast("請閲讀並勾選同意會員規則");
                    return;
                }
                register();
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
        binding.guizeLayout.setOnClickListener(new ViewClickListener() {
            @Override
            public void click(View v) {
                guizeSelect = !guizeSelect;
                if (!guizeSelect){
                    binding.guizeV.setBackgroundResource(R.drawable.shape_line_c6c6c6_radlis_100);
                }else {
                    binding.guizeV.setBackgroundResource(R.drawable.shape_select_check);
                }
            }
        });

        dialog = new LoadingDialog(thisAtv);
    }

    private void register(){
        String phone = binding.editPhone.getText().toString();
        String password1 = binding.editPsw.getText().toString();
        String password2 = binding.editPsw2.getText().toString();
        String name = binding.editName.getText().toString();
        String code = binding.editCode.getText().toString();
        String card_no = binding.editId.getText().toString();
        String iv_code = binding.editIvcode.getText().toString();

        if (phone.isEmpty() || password1.isEmpty()
        || password2.isEmpty() || name.isEmpty()
        || code.isEmpty() || card_no.isEmpty()
//                || iv_code.isEmpty()
        ){
            toast("請完成其他信息");
            return;
        }

        dialog.show();
        Map<String,Object> map  =new HashMap<>();
        map.put("mobile",phone);
        map.put("password1",password1);
        map.put("password2",password2);
        map.put("name",name);
        map.put("code",code);
        map.put("card_no",card_no);
        map.put("recommend_code",iv_code);
        ApiHttp.getApiService().register(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    dialog.dismiss();
                    toast(data.getMsg());
                    if (data.isSuccess()){
                        finish();
                    }
                }, t -> {
                    toast("網絡異常");
                    dialog.dismiss();
                });
    }

    private void sendCode(){
        String code = binding.editPhone.getText().toString();
        if (code.isEmpty()){
            toast("請輸入手機號碼");
            return;
        }
        dialog.show();
        ApiHttp.getApiService().sendCode(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    dialog.dismiss();
                    toast(data.getMsg());
                    if (data.isSuccess()){
                        timer = new MyTimer(60);
                        timer.setOnListener(RegisterActivity.this);
                        timer.start();
                    }
                }, t -> {
                    dialog.dismiss();
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
