package com.king.android.ui.my;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityAddressInfoBinding;
import com.king.android.model.Address;
import com.king.android.model.City;
import com.king.android.model.User;
import com.king.android.tools.MyTimer;
import com.king.android.ui.login.ForgetPasswordActivity;
import com.king.base.activity.BaseActivity;
import com.king.base.dialog.LoadingDialog;
import com.king.base.dialog.PopupListDialog;
import com.king.base.listener.ViewClickListener;
import com.king.base.model.IntentData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public
class AddressInfoActivity extends BaseActivity<ActivityAddressInfoBinding> {
    String district_id;
    String area_id;
    private String address_id;
    private List<City> cityList;
    LoadingDialog dialog;

    @Override
    public void init() {
        TextView titleTv = binding.getRoot().findViewById(R.id.title_tv);
        ImageView backIv = binding.getRoot().findViewById(R.id.back_iv);
        backIv.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        IntentData data = getIntentData();
        Address model = data.getModel(0, Address.class);
        if (model != null){
            address_id = model.getAddress_id();
            titleTv.setText("修改送貨地址");
            binding.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    edit();
                }
            });
            binding.editAddress.setText(model.getAddress());
            binding.editName.setText(model.getName());
            binding.editPhone.setText(model.getTel());
            binding.cityTv.setText(model.getArea_name());
            binding.quTv.setText(model.getDistrict_name());
            area_id = model.getArea_id();
            district_id = model.getDistrict_id();
        }else {
            titleTv.setText("新增送貨地址");
            binding.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    save();
                }
            });
        }

        dialog = new LoadingDialog(thisAtv);
        initListener();

        getArea();
    }

    private void initListener(){
        binding.address1Layout.setOnClickListener(new ViewClickListener() {
            @Override
            public void click(View v) {
                if (cityList == null){
                    toast("數據初始化中");
                    return;
                }
                List<String> nameList = new ArrayList<>();
                List<String> idList = new ArrayList<>();
                for (int i = 0; i < cityList.size(); i++) {
                    City city = cityList.get(i);
                    nameList.add(city.getArea_name());
                    idList.add(city.getArea_id());
                }
                PopupListDialog listDialog = new PopupListDialog();
                listDialog.setList(nameList);
                listDialog.setOnItemClickListener(new PopupListDialog.OnItemClickListener() {
                    @Override
                    public void onClick(int position, String text) {
                        area_id = idList.get(position);
                        district_id = null;
                        binding.cityTv.setText(text);
                        binding.quTv.setText("未選擇");
                    }
                });
                listDialog.show(binding.cityTv);
            }
        });
        binding.address2Layout.setOnClickListener(new ViewClickListener() {
            @Override
            public void click(View v) {
                if (cityList == null){
                    toast("數據初始化中");
                    return;
                }
                if (TextUtils.isEmpty(area_id)){
                    toast("請選擇城市");
                    return;
                }
                List<String> nameList = new ArrayList<>();
                List<String> idList = new ArrayList<>();
                for (int i = 0; i < cityList.size(); i++) {
                    City city = cityList.get(i);
                    if (TextUtils.equals(city.getArea_id(),area_id)){
                        for (int j = 0; j < city.getArea().size(); j++) {
                            City city1 = city.getArea().get(j);
                            nameList.add(city1.getArea_name());
                            idList.add(city1.getArea_id());
                        }
                        break;
                    }
                }
                PopupListDialog listDialog = new PopupListDialog();
                listDialog.setList(nameList);
                listDialog.setOnItemClickListener(new PopupListDialog.OnItemClickListener() {
                    @Override
                    public void onClick(int position, String text) {
                        district_id = idList.get(position);
                        binding.quTv.setText(text);
                    }
                });
                listDialog.show(binding.quTv);
            }
        });
    }

    private void getArea(){
        dialog.show();
        ApiHttp.getApiService().getCityAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    dialog.dismiss();
                    cityList = data.getData();
                }, t -> {
                    dialog.dismiss();
                    t.printStackTrace();
                });
    }

    private void save(){
        String phone = binding.editPhone.getText().toString();
        String name = binding.editName.getText().toString();
        String address = binding.editAddress.getText().toString();
        if (name.isEmpty()){
            toast("請輸入名字");
            return;
        }
        if (TextUtils.isEmpty(area_id)){
            toast("請選擇城市");
            return;
        }
        if (TextUtils.isEmpty(district_id)){
            toast("請選擇地區");
            return;
        }
        if (address.isEmpty()){
            toast("請輸入詳細地址");
            return;
        }
        if (phone.isEmpty()){
            toast("請輸入手機號碼");
            return;
        }
        dialog.show();
        User user = User.getInstance();
        Map<String,Object> map  =new HashMap<>();
        map.put("access_token",user.getAccess_token());
        map.put("name",name);
        map.put("tel",phone);
        map.put("address",address);
        map.put("area_id",area_id);
        map.put("district_id",district_id);
        ApiHttp.getApiService().addAddress(map)
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

    private void edit(){
        String phone = binding.editPhone.getText().toString();
        String name = binding.editName.getText().toString();
        String address = binding.editAddress.getText().toString();
        if (name.isEmpty()){
            toast("請輸入名字");
            return;
        }
        if (address.isEmpty()){
            toast("請輸入詳細地址");
            return;
        }
        if (phone.isEmpty()){
            toast("請輸入手機號碼");
            return;
        }
        dialog.show();
        User user = User.getInstance();
        Map<String,Object> map  =new HashMap<>();
        map.put("access_token",user.getAccess_token());
        map.put("name",name);
        map.put("tel",phone);
        map.put("address",address);
        map.put("area_id",area_id);
        map.put("district_id",district_id);
        map.put("address_id",address_id);
        ApiHttp.getApiService().editAddress(map)
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
