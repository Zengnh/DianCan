package com.king.android.ui.my;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityAddressListBinding;
import com.king.android.databinding.ItemAddressListBinding;
import com.king.android.model.Address;
import com.king.android.model.User;
import com.king.base.activity.BaseActivity;
import com.king.base.adapter.BaseKAdapter;
import com.king.base.adapter.BaseKViewHolder;
import com.king.base.dialog.LoadingDialog;
import com.king.base.interface_.OnItemClickListener;
import com.king.base.utils.DpUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public
class AddressListActivity extends BaseActivity<ActivityAddressListBinding> {

    private BaseKAdapter<Address, ItemAddressListBinding> adapter;
    private LoadingDialog dialog;

    @Override
    public void init() {
        int select = getIntent().getIntExtra("selectType",0);
        TextView titleTv = binding.getRoot().findViewById(R.id.title_tv);
        titleTv.setText("用戶送貨地址");
        ImageView backIv = binding.getRoot().findViewById(R.id.back_iv);
        backIv.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView rightTv = binding.getRoot().findViewById(R.id.right_tv);
        rightTv.setTextSize(28);
        rightTv.setText("+");
        rightTv.setTextColor(Color.BLACK);
        rightTv.setPadding((int)DpUtils.dp2px(10),0,(int)DpUtils.dp2px(28),0);
        rightTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(AddressInfoActivity.class).start();
            }
        });

        adapter = new BaseKAdapter<Address, ItemAddressListBinding>() {
            @Override
            public void onItemBindData(BaseKViewHolder holder, ItemAddressListBinding binding, Address data, int position) {
                binding.setData(data);
                binding.delIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(position);
                        delAddress(data.getAddress_id());
                    }
                });
                binding.editIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        launch(AddressInfoActivity.class).add(data).start();
                    }
                });
            }
        };
        if (select == 1){
            adapter.setOnItemClickListener(new OnItemClickListener<Address, ItemAddressListBinding>() {
                @Override
                public void onClick(View v, Address data, ItemAddressListBinding binding, int position) {
                    Intent it =new Intent();
                    it.putExtra("address",data.getArea_name()+data.getDistrict_name()+data.getAddress());
                    it.putExtra("name",data.getName());
                    it.putExtra("mobile",data.getTel());
                    setResult(RESULT_OK,it);
                    finish();
                }
            });
        }

        binding.rv.setAdapter(adapter);

        dialog = new LoadingDialog(thisAtv);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData(){
        dialog.show();
        User user = User.getInstance();
        ApiHttp.getApiService().getAddressList(user.getAccess_token())
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

    private void delAddress(String id) {
        User user = User.getInstance();
        ApiHttp.getApiService().delAddress(user.getAccess_token(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {

                }, t -> {
                });
    }
}
