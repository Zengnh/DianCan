package com.king.android.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.king.android.R;
import com.king.android.adapter.CategoryAdapter;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.FragmentHomeBinding;
import com.king.android.databinding.ItemCategoryBinding;
import com.king.android.databinding.ItemHomeBinding;
import com.king.android.model.Category;
import com.king.android.model.Shops;
import com.king.android.service.Gps;
import com.king.android.ui.anim.ListRunnable;
import com.king.android.ui.anim.RvItemAnimator;
import com.king.base.adapter.BaseKAdapter;
import com.king.base.adapter.BaseKViewHolder;
import com.king.base.dialog.LoadingDialog;
import com.king.base.fragment.BaseFragment;
import com.king.base.interface_.OnItemClickListener;
import com.tencent.mmkv.MMKV;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.RectangleIndicator;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {
    private BaseKAdapter<Shops, ItemHomeBinding> adapter;
    private CategoryAdapter categoryAdapter;
    private Handler handler;
    private LoadingDialog dialog;
    private List<Category> list;

    @Override
    public void init() {
        handler = new Handler(Looper.getMainLooper());
        binding.rv.setLayoutManager(new LinearLayoutManager(thisAtv));
        initCategory();
        initList();
//        initClass();
        dialog = new LoadingDialog(thisAtv);

        if (true) {
            MMKV.defaultMMKV().encode("lat", lat);
            MMKV.defaultMMKV().encode("lon", lon);
            if (loadData == false) {
                loadData = true;
                getCategory();
            }
        }
        List<String> imageList = new ArrayList<>();
        imageList.add("https://img2.baidu.com/it/u=3834190161,2920449096&fm=253&fmt=auto&app=138&f=JPEG?w=900&h=375");
        imageList.add("https://img2.baidu.com/it/u=1440508587,223818294&fm=253&fmt=auto&app=138&f=JPEG?w=500&h=188");


        binding.mainBanner.setLoopTime(3000);
        binding.mainBanner.setAdapter(new BannerImageAdapter<String>(imageList) {

            @Override
            public void onBindView(BannerImageHolder holder, String data, int position, int size) {
                Glide.with(holder.itemView).load(data).into(holder.imageView);
            }
        }).setIndicator(new RectangleIndicator(getActivity())).addBannerLifecycleObserver(getActivity());
//        binding.mainBanner.setDatas(imageList);
        binding.mainBanner.start();

    }

    private void getCategory() {
        dialog.show();
        ApiHttp.getApiService()
                .category()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    list = data.getData();
                    categoryAdapter.setNewData(list);
                    getData(list.get(0).getCategory_id());
                    loadData = false;
                }, t -> {
                    t.printStackTrace();
                    dialog.dismiss();
                });
    }

    private void getCacheData() {
        if (list == null) return;
        try {
            categoryAdapter.setNewData(list);
            int index = categoryAdapter.getSelectPosition();
            String id = list.get(index).getCategory_id();
            String data = MMKV.defaultMMKV().decodeString("home_category_listid_" + id);
            List<Shops> listData = new Gson().fromJson(data, new TypeToken<List<Shops>>() {
            }.getType());
            adapter.setNewData(listData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCategory() {
        categoryAdapter = new CategoryAdapter();
        categoryAdapter.setOnItemClickListener(new OnItemClickListener<Category, ItemCategoryBinding>() {
            @Override
            public void onClick(View v, Category data, ItemCategoryBinding binding, int position) {
                categoryAdapter.setSelectPosition(position);

                getData(data.getCategory_id());
            }
        });
        binding.categoryRv.setLayoutManager(new LinearLayoutManager(thisAtv, LinearLayoutManager.HORIZONTAL, false));
        binding.categoryRv.setAdapter(categoryAdapter);
    }

    private void initList() {
        adapter = new BaseKAdapter<Shops, ItemHomeBinding>() {
            @Override
            public void onItemBindData(BaseKViewHolder holder, ItemHomeBinding binding, Shops data, int position) {
                RequestOptions options = new RequestOptions().placeholder(R.mipmap.logo_new);
                Glide.with(thisAtv).load(data.getLogo()).apply(options).into(binding.image);
                binding.setData(data);
            }
        };
        adapter.setOnItemClickListener(new OnItemClickListener<Shops, ItemHomeBinding>() {
            @Override
            public void onClick(View v, Shops data, ItemHomeBinding binding, int position) {
                launch(MerchantActivity.class).add(data.getShop_id()).start();
            }
        });
        binding.rv.setAdapter(adapter);
        binding.rv.setItemAnimator(new RvItemAnimator());
    }

    private void getData(String id) {
        adapter.clear();
        dialog.show();
        ApiHttp.getApiService().getShops(lat + "", lon + "", id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(data -> {
            dialog.dismiss();
            if (data.getData() != null) {
                List<Shops> list = data.getData();
                MMKV.defaultMMKV().encode("home_category_listid_" + id, new Gson().toJson(list));
                r.setData(list);
                for (int j = 0; j < list.size(); j++) {
                    handler.postDelayed(r, (j + 1) * 50);
                }
            }
        }, t -> {
            t.printStackTrace();
            dialog.dismiss();
        });
    }

    private ListRunnable r = new ListRunnable<Shops>() {
        @Override
        public void onData(Shops data) {
            adapter.addItem(data);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        getCacheData();
    }

    boolean loadData = false;
    double lat = 118.11022, lon = 24.490474;
}
