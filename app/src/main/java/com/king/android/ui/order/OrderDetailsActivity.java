package com.king.android.ui.order;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityOrderDetailsBinding;
import com.king.android.model.Order;
import com.king.android.model.User;
import com.king.base.activity.BaseActivity;
import com.king.base.dialog.LoadingDialog;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 订单详情
 */
public
class OrderDetailsActivity extends BaseActivity<ActivityOrderDetailsBinding> {

    private int selectXingXing = 0;
    private LoadingDialog dialog;
    private Order order;

    @Override
    public void init() {
        String json = getIntentData().getString(0);
        order = new Gson().fromJson(json,Order.class);

        ImageView backIv = binding.getRoot().findViewById(R.id.back_iv);
        backIv.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView titleTv = binding.getRoot().findViewById(R.id.title_tv);
        titleTv.setText("評論");

        initXingXing();
        selectXingXing();

        RequestOptions options = new RequestOptions().placeholder(R.mipmap.wu_img);
        Glide.with(thisAtv).load(order.getProductImage()).apply(options).into(binding.imgIv);
        binding.setData(order);
        binding.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = binding.editComment.getText().toString();
                if (comment.isEmpty()){
                    toast("請輸入評論");
                    return;
                }
                String num = String.valueOf(selectXingXing);
                commentOrder(comment,num);
            }
        });
        dialog = new LoadingDialog(thisAtv);
    }

    private void initXingXing(){
        int count = binding.xingxingLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = binding.xingxingLayout.getChildAt(i);
            int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectXingXing = finalI;
                    selectXingXing();
                }
            });
        }
    }

    private void selectXingXing(){
        int count = binding.xingxingLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView view = (ImageView) binding.xingxingLayout.getChildAt(i);
          if (i <= selectXingXing){
              view.setImageResource(R.mipmap.xingxing_select);
          }else {
              view.setImageResource(R.mipmap.xingxing_unselect);
          }
        }
    }

    private void commentOrder(String comment,String star_num){
        dialog.show();
        User user = User.getInstance();
        ApiHttp.getApiService().commentOrder(user.getAccess_token(),order.getOrder_id(),comment,star_num)
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
