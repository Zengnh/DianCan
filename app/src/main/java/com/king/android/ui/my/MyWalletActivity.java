package com.king.android.ui.my;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.king.android.Constants;
import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityMyPointsBinding;
import com.king.android.databinding.ActivityMyWalletBinding;
import com.king.android.databinding.ItemMyPointsBinding;
import com.king.android.databinding.ItemMyWalletBinding;
import com.king.android.model.MoneyLog;
import com.king.android.model.User;
import com.king.base.activity.BaseActivity;
import com.king.base.adapter.BaseKAdapter;
import com.king.base.adapter.BaseKViewHolder;
import com.king.base.dialog.LoadingDialog;
import com.king.base.utils.DpUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 我的钱包页面
 */
public
class MyWalletActivity extends BaseActivity<ActivityMyWalletBinding> {
    private BaseKAdapter<MoneyLog.Data, ItemMyWalletBinding> adapter;
    private LoadingDialog dialog;
    private int page = 0;

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
        ImageView rightIv = binding.getRoot().findViewById(R.id.right_iv);
        rightIv.setImageResource(R.mipmap.share_ic);
        int dp10 = (int) DpUtils.dp2px(15);
        rightIv.setPadding(dp10,dp10,dp10,dp10);
        rightIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(RechargeActivity.class).start();
            }
        });

        adapter = new BaseKAdapter<MoneyLog.Data, ItemMyWalletBinding>() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            @Override
            public void onItemBindData(BaseKViewHolder holder, ItemMyWalletBinding binding, MoneyLog.Data data, int position) {
                binding.moneyTv.setText(data.getMoney());
                binding.moneyTv.setTextColor(Color.parseColor("#F44336"));
                binding.timeTv.setText(format.format(new Date(data.getTime())));
                if ("0".equals(data.getType())){
                    if ("1".equals(data.getStatus())){
                        binding.tipsTv.setText("充值成功");
                        binding.moneyTv.setText("+"+data.getMoney());
                        binding.moneyTv.setTextColor(Color.parseColor("#FCC328"));
                    }else {
                        binding.tipsTv.setText("充值失敗");
                    }
                }else if ("1".equals(data.getType())){
                    if ("1".equals(data.getStatus())){
                        binding.tipsTv.setText("消費成功");
                        binding.moneyTv.setText("-"+data.getMoney());
                        binding.moneyTv.setTextColor(Color.parseColor("#FCC328"));
                    }else {
                        binding.tipsTv.setText("消費失敗");
                    }
                }else {
                    binding.tipsTv.setText("");
                }
            }
        };
        binding.list.setAdapter(adapter);
        binding.list.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 0;
                getData();
            }
        });
        binding.list.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                getData();
            }
        });

        dialog = new LoadingDialog(thisAtv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 0;
        getData();
    }

    private void getData(){
        dialog.show();
        page ++;
        User user = User.getInstance();
        ApiHttp.getApiService().moneyLog(user.getAccess_token(),page, Constants.pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    dialog.dismiss();

                    if (TextUtils.isEmpty(data.getData().getMoney())){
                        data.getData().setMoney("0");
                    }
                    user.setMoney(data.getData().getMoney());
                    user.save();
                    binding.moneyTv.setText(data.getData().getMoney());

                    if (page == 1){
                        adapter.setNewData(data.getData().getList());
                    }else {
                        adapter.addAll(data.getData().getList());
                    }

                    binding.list.setEnableLoadMore(data.getData().getList().size() == Constants.pageSize);
                    binding.list.finishLoad();
                }, t -> {
                    t.printStackTrace();
                    dialog.dismiss();
                    binding.list.finishLoad();
                });
    }
}
