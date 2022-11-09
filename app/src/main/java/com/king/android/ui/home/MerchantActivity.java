package com.king.android.ui.home;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityMerchantBinding;
import com.king.android.databinding.ItemMerchantBinding;
import com.king.android.model.ShopInfo;
import com.king.android.service.Gps;
import com.king.android.ui.anim.ListRunnable;
import com.king.android.ui.anim.RvItemAnimator;
import com.king.base.activity.BaseActivity;
import com.king.base.adapter.BaseKAdapter;
import com.king.base.adapter.BaseKViewHolder;
import com.king.base.dialog.LoadingDialog;
import com.king.base.interface_.OnItemClickListener;
import com.king.base.utils.DpUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 店铺
 */
public
class MerchantActivity extends BaseActivity<ActivityMerchantBinding> {

    List<TextView> tabs;
    private BaseKAdapter<ShopInfo.Product, ItemMerchantBinding> adapter;
    private Handler handler;
    private LoadingDialog dialog;
    private String shopId;
    private List<ShopInfo.Cat> catList;
    private List<ShopInfo.Product> productList;
    private TextView titleTv;

    @Override
    public void init() {

        titleTv = binding.getRoot().findViewById(R.id.title_tv);
        titleTv.setText("");

        handler = new Handler(Looper.getMainLooper());
        ImageView backIv = binding.getRoot().findViewById(R.id.back_iv);
        backIv.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        adapter = new BaseKAdapter<ShopInfo.Product, ItemMerchantBinding>() {
            @Override
            public void onItemBindData(BaseKViewHolder holder, ItemMerchantBinding binding, ShopInfo.Product data, int position) {
                RequestOptions options = new RequestOptions().placeholder(R.mipmap.wu_img);
                Glide.with(thisAtv).load(data.getImg()).apply(options).into(binding.img);

                binding.setData(data);
            }
        };
        adapter.setOnItemClickListener(new OnItemClickListener<ShopInfo.Product, ItemMerchantBinding>() {
            @Override
            public void onClick(View v, ShopInfo.Product data, ItemMerchantBinding binding, int position) {
                launch(ShopDetailsActivity.class)
                        .add("data",new Gson().toJson(data))
                        .add("product_id",data.getProduct_id())
                        .add("shop_id",shopId)
                        .add("name",shopName)
                        .add("img",data.getImg())
                        .start();
            }
        });
        binding.rv.setAdapter(adapter);
        binding.rv.setItemAnimator(new RvItemAnimator());

        shopId = getIntentData().getString(0);
        dialog = new LoadingDialog(thisAtv);
        binding.tabLayout.removeAllViews();

        getData();

    }

    private void addTabs(){
        tabs = new ArrayList<>();
        binding.tabLayout.removeAllViews();

        if (catList == null) return;

        int dp18 = (int) DpUtils.dp2px(18);
        int dp8 = (int) DpUtils.dp2px(8);
        int dp16 = (int) DpUtils.dp2px(16);
        int dp30 = (int) DpUtils.dp2px(30);
        for (int i = 0; i < catList.size(); i++) {
            ShopInfo.Cat cat = catList.get(i);
            TextView tab = new TextView(thisAtv);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,dp30);
            params.leftMargin = i == 0? dp18 : dp8;
            tab.setTextColor(i == 0? Color.WHITE:Color.parseColor("#151414"));
            tab.setBackgroundResource(i == 0? R.drawable.shape_f95f70_radius_100:R.drawable.shape_d1d1d1_radius_100);
            tab.setPadding(dp16,0,dp16,0);
            tab.setGravity(Gravity.CENTER);
            tab.setText(cat.getType_name());
            tab.setTextSize(14);
            TextPaint tp = tab .getPaint();
            tp.setFakeBoldText(true);
            tab.setLayoutParams(params);
            binding.tabLayout.addView(tab);
            tabs.add(tab);
            if (i == 0){
                showData(cat);
            }
        }

        for (int i = 0; i < tabs.size(); i++) {
            int finalI = i;
            tabs.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select(finalI);
                    showData(catList.get(finalI));
                }
            });
        }
    }

    private void select(int position){
        for (int i = 0; i < tabs.size(); i++) {
            TextView tv = tabs.get(i);
            if (i == position){
                tv.setBackgroundResource(R.drawable.shape_f95f70_radius_100);
                tv.setTextColor(Color.WHITE);
            }else{
                tv.setBackgroundResource(R.drawable.shape_d1d1d1_radius_100);
                tv.setTextColor(Color.parseColor("#151414"));
            }
        }
    }

    String shopName;
    private void getData(){
        dialog.show();
        ApiHttp.getApiService().getShopInfo(shopId, Gps.lat+"", Gps.lon+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    dialog.dismiss();
                    catList = data.getData().getCat_lists();
                    productList = data.getData().getProducts();

                    titleTv.setText(data.getData().getInfo().getShop_name());
                    RequestOptions options = new RequestOptions().placeholder(R.mipmap.home_img1);
                    Glide.with(thisAtv).load(data.getData().getInfo().getLogo()).apply(options).into(binding.img);
                    binding.setData(data.getData().getInfo());
                    shopName = data.getData().getInfo().getShop_name();
                    addTabs();
                }, t -> {
                    t.printStackTrace();
                    dialog.dismiss();
                });
    }

    private void showData(ShopInfo.Cat cat){
        adapter.clear();
        List<ShopInfo.Product> list = new ArrayList<>();
        if (productList != null){
            for (int i = 0; i < productList.size(); i++) {
                ShopInfo.Product product = productList.get(i);
                if (TextUtils.equals(cat.getType_id(),product.getType_id())){
                    list.add(product);
                }
            }
        }
        r.setData(list);
        for (int j = 0; j < list.size(); j++) {
            handler.postDelayed(r,(j+1)*50);
        }
    }

    private ListRunnable r = new ListRunnable<ShopInfo.Product>() {
        @Override
        public void onData(ShopInfo.Product data) {
            adapter.addItem(data);
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(r);
        super.onDestroy();
    }
}
