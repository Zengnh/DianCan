package com.king.android.ui.my;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityOutPointBinding;
import com.king.android.databinding.ActivityRechargeBinding;
import com.king.android.model.User;
import com.king.base.activity.BaseActivity;
import com.king.base.dialog.LoadingDialog;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 转让点数
 */
public
class OutPointActivity extends BaseActivity<ActivityOutPointBinding> {

    private LoadingDialog dialog;

    @Override
    public void init() {
        ImageView backIv = binding.getRoot().findViewById(R.id.back_iv);
        backIv.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView titleTv = binding.getRoot().findViewById(R.id.title_tv);
        titleTv.setText("轉讓點數");

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = binding.editPhone.getText().toString();
                String number = binding.editNumber.getText().toString();
                if (phone.isEmpty()){
                    toast("請輸入手機號碼");
                    return;
                }
                if (number.isEmpty()){
                    toast("請輸入點數");
                    return;
                }
                transfer(phone,number);
            }
        });
        dialog = new LoadingDialog(thisAtv);
    }

    private void transfer(String phone,String number){
        dialog.show();
        User user = User.getInstance();
        ApiHttp.getApiService().transfer(user.getAccess_token(),phone,number)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    dialog.dismiss();
                    toast(data.getMsg());
                    if (data.isSuccess()){
                        finish();
                    }
                }, t -> {
                    dialog.dismiss();
                });
    }
}
