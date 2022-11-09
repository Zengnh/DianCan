package com.king.android.ui.my;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityRechargeBinding;
import com.king.android.model.User;
import com.king.android.ui.WebViewActivity;
import com.king.base.activity.BaseActivity;
import com.king.base.dialog.LoadingDialog;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 充值
 */
public
class RechargeActivity extends BaseActivity<ActivityRechargeBinding> {

    private LoadingDialog dialog;

    private void dismiss(){
        if (dialog !=null){
            dialog.dismiss();
        }
    }
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
        titleTv.setText("充值金額");

        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String money = binding.editMoney.getText().toString();
                if (money.isEmpty()){
                    toast("請輸入金額");
                    return;
                }
                charge(money);

            }
        });
        User user =User.getInstance();
        binding.phoneTv.setText(user.getMobile());
        dialog = new LoadingDialog(thisAtv);
    }

    private void charge(String money){
        dialog.show();
        User user = User.getInstance();
        ApiHttp.getApiService().charge(user.getAccess_token(),money)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    toast(data.getMsg());
                    if (data.isSuccess()){
                        payCharge(data.getData().getOrder_no());
                    }
                }, t -> {
                    dismiss();
                });
    }

    private void payCharge(String order_no){
        User user = User.getInstance();
        ApiHttp.getApiService().payCharge(user.getAccess_token(),order_no)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    dismiss();
                    if (data.isSuccess()){
                        finish();
                        Intent it = new Intent(thisAtv, WebViewActivity.class);
                        it.putExtra("html",data.getData().getHtml());
                        startActivity(it);
                    }else {
                        toast(data.getMsg());
                    }
                }, t -> {
                    dismiss();
                });
    }
}
