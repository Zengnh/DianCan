package com.king.android.ui.order;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.FragmentOrderBinding;
import com.king.android.databinding.ItemOrderBinding;
import com.king.android.model.Order;
import com.king.android.model.User;
import com.king.base.adapter.BaseKAdapter;
import com.king.base.adapter.BaseKViewHolder;
import com.king.base.dialog.LoadingDialog;
import com.king.base.fragment.BaseFragment;
import com.king.base.interface_.OnItemClickListener;
import com.king.base.utils.DpUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public
class OrderFragment extends BaseFragment<FragmentOrderBinding> {

    private BaseKAdapter<Order, ItemOrderBinding> adapter;
    private LoadingDialog dialog;

    @Override
    public void init() {
        TextView titleTv = binding.getRoot().findViewById(R.id.title_tv);
        titleTv.setText("訂單記錄");

        if (TextUtils.equals("1",getIntentData().getString("isActivity"))){
            ImageView backIv = binding.getRoot().findViewById(R.id.back_iv);
            backIv.setVisibility(View.VISIBLE);
            backIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        int dp16 = (int) DpUtils.dp2px(16);
        int dp8 = (int) DpUtils.dp2px(16);
        adapter = new BaseKAdapter<Order, ItemOrderBinding>() {
            @Override
            public void onItemBindData(BaseKViewHolder holder, ItemOrderBinding binding, Order data, int position) {
                binding.setData(data);
                ViewGroup.MarginLayoutParams params = (FrameLayout.LayoutParams) binding.layout.getLayoutParams();
                if (position==0){
                    params.topMargin = dp16;
                    params.bottomMargin = dp8;
                }else if (positionIsLast(position)){
                    params.topMargin = dp8;
                    params.bottomMargin = dp16;
                }else {
                    params.topMargin = dp8;
                    params.bottomMargin = dp8;
                }
                binding.layout.setLayoutParams(params);
                RequestOptions options = new RequestOptions().placeholder(R.mipmap.logo_new);
                Glide.with(thisAtv).load(data.getProductImage()).apply(options).into(binding.imgIv);
            }
        };
        adapter.setOnItemClickListener(new OnItemClickListener<Order, ItemOrderBinding>() {
            @Override
            public void onClick(View v, Order data, ItemOrderBinding binding, int position) {
                if (!"3".equals(data.getSendStatus())){
                    toast("訂單未完成不可評論，請完成訂單后評論");
                    return;
                }

                launch(OrderDetailsActivity.class).add(new Gson().toJson(data)).start();
            }
        });
        binding.rv.setAdapter(adapter);

        dialog = new LoadingDialog(thisAtv);

    }

    private void getData(){
        dialog.show();
        User user = User.getInstance();
        ApiHttp.getApiService().myOrderList(user.getAccess_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    dialog.dismiss();
                    adapter.setNewData(data.getData());
                }, t -> {
                    t.printStackTrace();
                    dialog.dismiss();
                });
    }


    @Override
    public void onResume() {
        super.onResume();

        getData();
    }

    private void delOrder(String order_id){
        dialog.show();
        User user = User.getInstance();
        ApiHttp.getApiService().cancelOrder(user.getAccess_token(),order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    dialog.dismiss();
                }, t -> {
                    dialog.dismiss();
                });
    }
}
