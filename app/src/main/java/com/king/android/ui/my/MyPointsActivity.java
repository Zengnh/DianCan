package com.king.android.ui.my;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.king.android.Constants;
import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityMyPointsBinding;
import com.king.android.databinding.ItemMyPointsBinding;
import com.king.android.model.Score;
import com.king.android.model.User;
import com.king.base.activity.BaseActivity;
import com.king.base.adapter.BaseKAdapter;
import com.king.base.adapter.BaseKViewHolder;
import com.king.base.dialog.LoadingDialog;
import com.king.base.utils.DpUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.math.BigDecimal;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 我的点数
 */
public
class MyPointsActivity extends BaseActivity<ActivityMyPointsBinding> {

    private BaseKAdapter<Score.Data, ItemMyPointsBinding> adapter;
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
                launch(OutPointActivity.class).start();
            }
        });

        int dp7 = (int) DpUtils.dp2px(7);
        int dp34 = (int) DpUtils.dp2px(34);
        int dp12 = (int) DpUtils.dp2px(12);
        adapter = new BaseKAdapter<Score.Data, ItemMyPointsBinding>() {
            @Override
            public void onItemBindData(BaseKViewHolder holder, ItemMyPointsBinding binding, Score.Data data, int position) {

                binding.setData(data);

                binding.goodsLayout.removeAllViews();

                if ("0".equals(data.getModel())){
                    List<Score.OrderGoods> order_goods = data.getOrder_info().getOrder_goods();
                    for (int i = 0; i < order_goods.size(); i++) {
                        Score.OrderGoods orderGoods = order_goods.get(i);
                        LinearLayout layout = new LinearLayout(thisAtv);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.topMargin = dp7;
                        params.rightMargin = dp12;
                        params.leftMargin = dp34;
                        layout.setLayoutParams(params);
                        layout.setGravity(Gravity.CENTER_VERTICAL);
                        TextView nameTv = new TextView(thisAtv);
                        nameTv.setTextSize(14);
                        nameTv.setText(orderGoods.getProduct_name());
                        nameTv.setTextColor(Color.BLACK);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params1.weight = 1;
                        nameTv.setLayoutParams(params1);
                        layout.addView(nameTv);

                        TextView priceTv = new TextView(thisAtv);
                        priceTv.setTextSize(14);
                        priceTv.setText(new BigDecimal(orderGoods.getPrice()).multiply(new BigDecimal(orderGoods.getProduct_num())).stripTrailingZeros().toPlainString()+"元");
                        priceTv.setTextColor(Color.BLACK);
                        priceTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        layout.addView(priceTv);
                        binding.goodsLayout.addView(layout);
                    }
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
        getData();
    }


    private void getData(){
        dialog.show();
        page ++;

        User user = User.getInstance();
        binding.numberTv.setText(user.getScore());
        ApiHttp.getApiService().scoreLog(user.getAccess_token(),page, Constants.pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    dialog.dismiss();
                    if (page ==1){
                        adapter.setNewData(data.getData().getList());
                    }else {
                        adapter.addAll(data.getData().getList());
                    }
                    binding.list.setEnableLoadMore(data.getData().getList().size() == Constants.pageSize);
                    binding.list.finishLoad();

                    user.setScore(data.getData().getScore());
                    user.save();
                    binding.numberTv.setText(user.getScore());

                }, t -> {
                    t.printStackTrace();
                    dialog.dismiss();
                    binding.list.finishLoad();
                });
    }
}
