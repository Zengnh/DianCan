package com.king.android.ui.shopcar;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.FragmentShopcarBinding;
import com.king.android.databinding.ItemEmptyShopCartBinding;
import com.king.android.databinding.ItemShopcarBinding;
import com.king.android.model.Cart;
import com.king.android.model.User;
import com.king.android.ui.QRCodeActivity;
import com.king.android.ui.WebViewActivity;
import com.king.android.ui.ZhifuActivity;
import com.king.android.ui.home.ShopDetailsActivity;
import com.king.android.ui.my.AddressListActivity;
import com.king.base.adapter.BaseKAdapter;
import com.king.base.adapter.BaseKViewHolder;
import com.king.base.adapter.KViewHolder;
import com.king.base.dialog.LoadingDialog;
import com.king.base.fragment.BaseFragment;
import com.king.base.listener.ViewClickListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public
class ShopCarFragment extends BaseFragment<FragmentShopcarBinding> {

    private BaseKAdapter<Cart, ItemShopcarBinding> adapter;
    private LoadingDialog dialog;
    private ActivityResultLauncher<Intent> launcher;
    private boolean isRefresh = true;

    @Override
    public void init() {
        TextView titleTv = binding.getRoot().findViewById(R.id.title_tv);
        titleTv.setText("購物車");

        String tagName= "shop_car";
        binding.scanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(QRCodeActivity.class).add(tagName).start();
            }
        });
        broadcast = new ScanBroadcast();
        IntentFilter it = new IntentFilter();
        it.addAction(tagName);
        thisAtv.registerReceiver(broadcast,it);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                int resultCode = result.getResultCode();
                if (resultCode == RESULT_OK) {

                    String address = data.getStringExtra("address");
                    String name = data.getStringExtra("name");
                    String mobile = data.getStringExtra("mobile");
                    addCartOrder(name,address,mobile);
                }
            }
        });

        adapter = new BaseKAdapter<Cart, ItemShopcarBinding>() {
            @Override
            public void onItemBindData(BaseKViewHolder holder, ItemShopcarBinding binding, Cart data, int position) {
                data.setTotal(new BigDecimal(data.getNum()).multiply(new BigDecimal(data.getPrice())).stripTrailingZeros().toPlainString());
                binding.setData(data);
                RequestOptions options = new RequestOptions().placeholder(R.mipmap.logo_new);
                Glide.with(thisAtv).load(data.getProduct_img()).apply(options).into(binding.imgIv);
                binding.delIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remove(position);
                        delCart(data);
                        total();
                    }
                });
                binding.addTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str = binding.numberTv.getText().toString();
                        int num = Integer.parseInt(str);
                        num++;
                        binding.numberTv.setText(num + "");
                        data.setNum(num+"");
                        BigDecimal db = new BigDecimal(num).multiply(new BigDecimal(data.getPrice()));
                        data.setTotal(db.stripTrailingZeros().toPlainString());
                        binding.totalTv.setText(data.getTotal());
                        editCartNum(data);
                        total();
                    }
                });
                binding.subTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str = binding.numberTv.getText().toString();
                        int num = Integer.parseInt(str);
                        num--;
                        if (num <= 0) {
                            toast("數量不能少於1份");
                            return;
                        }
                        binding.numberTv.setText(num + "");
                        data.setNum(num+"");
                        BigDecimal db = new BigDecimal(num).multiply(new BigDecimal(data.getPrice()));
                        data.setTotal(db.stripTrailingZeros().toPlainString());
                        binding.totalTv.setText(data.getTotal());
                        editCartNum(data);
                        total();
                    }
                });
                binding.titleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (Cart datum : getData()) {
                            if (data == datum){
                                continue;
                            }
                            datum.setSelect(false);
                        }
                        data.setSelect(!data.isSelect());
                        notifyDataSetChanged();
                    }
                });
            }
        };
        adapter.setEmptyViewHolder(new KViewHolder<ItemEmptyShopCartBinding>() {
            @Override
            public void onViewBindData(ItemEmptyShopCartBinding binding) {

            }
        });
        binding.rv.setAdapter(adapter);

        //直接购买
        binding.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                List<Cart> data = adapter.getData();
                if (data.isEmpty()){
                    toast("當前購物車爲空，請添加商品后購買");
                    return;
                }

                addCartOrder("2","","");
//
//                String[] items = {"自取","外送"};
//                AlertDialog.Builder dialog = new AlertDialog.Builder(thisAtv);
//                dialog.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        isRefresh = false;
//                        dialog.dismiss();
//
//                        payment = "2";
//                        ShopCarFragment.this.which = which;
//
//                        if (which == 0){
//                            addCartOrder("","","");
//                        }else {
//
//                            Intent it = new Intent(thisAtv, AddressListActivity.class);
//                            it.putExtra("selectType", 1);
//                            launcher.launch(it);
//                        }
//                    }
//                });
//                dialog.setPositiveButton("取消",null);
//                dialog.show();


            }
        });
        //付现金
        binding.fuMoney.setOnClickListener(new ViewClickListener() {
            @Override
            public void click(View v) {
                List<Cart> data = adapter.getData();
                if (data.isEmpty()){
                    toast("當前購物車爲空，請添加商品后購買");
                    return;
                }

                addCartOrder("1","","");

//                String[] items = {"自取","外送"};
//                AlertDialog.Builder dialog = new AlertDialog.Builder(thisAtv);
//                dialog.setItems(items, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        isRefresh = false;
//                        dialog.dismiss();
//
//                        ShopCarFragment.this.which = which;
//                        payment = "1";
//
//                        if (which == 0){
//                            addCartOrder("","","");
//                        }else {
//                            Intent it = new Intent(thisAtv, AddressListActivity.class);
//                            it.putExtra("selectType", 1);
//                            launcher.launch(it);
//                        }
//
//                    }
//                });
//                dialog.setPositiveButton("取消",null);
//                dialog.show();
            }
        });

        dialog = new LoadingDialog(thisAtv);
    }

    private void total(){
        BigDecimal total = new BigDecimal("0");
        List<Cart> data = adapter.getData();
        for (Cart datum : data) {
            total = total.add(new BigDecimal(datum.getNum()).multiply(new BigDecimal(datum.getPrice())));
        }

       binding.totalTv.setText("小計:"+total.stripTrailingZeros().toPlainString()+"元");
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh){
            getData();
        }
        isRefresh = true;
    }

    //获取购物车列表
    private void getData() {
        dialog.show();
        User user = User.getInstance();
        ApiHttp.getApiService().myCart(user.getAccess_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    dialog.dismiss();

                    String body = new Gson().toJson(data.getData());

                    JSONObject json = new JSONObject(body);
                    JSONArray list = json.getJSONArray("list");
                    Gson gson = new Gson();
                    List<Cart> cartList = new ArrayList<>();
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject object = list.getJSONObject(i);
                        String shop_name = object.getString("shop_name");
                        String shop_id = object.getString("shop_id");
                        JSONArray list1 = object.getJSONArray("list");
                        for (int j = 0; j < list1.length(); j++) {
                            JSONObject obj = list1.getJSONObject(j);
                            Cart cart = gson.fromJson(obj.toString(),Cart.class);
                            cart.setShop_name(shop_name);
                            try {
                                cart.setIs_outside(obj.getJSONObject("info").getInt("is_outside")+"");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            cart.setShop_id(shop_id);
                            cart.setIndex(j);
                            cartList.add(cart);
                        }
                    }
                    adapter.setNewData(cartList);
                    total();
                }, t -> {
                    t.printStackTrace();
                    dialog.dismiss();
                });
    }

    //从服务器删除购物车
    private void delCart(Cart cart) {
        dialog.show();
        User user = User.getInstance();
        ApiHttp.getApiService().delCart(user.getAccess_token(), cart.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    dialog.dismiss();
                }, t -> {
                    dialog.dismiss();
                });
    }

    private void editCartNum(Cart cart){
        dialog.show();
        User user = User.getInstance();
        ApiHttp.getApiService().editCartNum(user.getAccess_token(), cart.getId(),cart.getNum())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    dialog.dismiss();
                }, t -> {
                    dialog.dismiss();
                });
    }


    //下单
    private void addCartOrder(String payment,String address,String mobile) {

        String remark = null;
        String is_outside = null;
        List<Cart> list = adapter.getData();
        StringBuffer sb = new StringBuffer();
        String selectShopId = null;
        for (int i = 0; i < list.size(); i++) {
            Cart cart = list.get(i);
            if (selectShopId == null){
                if (!cart.isSelect()){
                    continue;
                }
                selectShopId = cart.getShop_id();
            }
            if (TextUtils.equals(selectShopId,cart.getShop_id())){
                sb.append(cart.getId()).append(",");
                if (TextUtils.isEmpty(remark)){
                    remark = cart.getRemark();
                }
                if (TextUtils.isEmpty(is_outside)){
                    is_outside = cart.getIs_outside();
                }
            }
        }
        if (sb.length() ==0){
            Toast.makeText(thisAtv, "請選擇指定的店鋪下單", Toast.LENGTH_SHORT).show();
            return;
        }
        dialog.show();
        sb.setLength(sb.length()-1);

        User user = User.getInstance();
        Map<String,Object> map  =new HashMap<>();
        map.put("access_token",user.getAccess_token());
//        map.put("is_send",which);
        map.put("ids",sb.toString());
        map.put("address", address);
        map.put("mobile", mobile);
        map.put("remark", remark);
        if (is_outside!= null){
            map.put("is_outside", is_outside);
        }
        map.put("payment", payment);
        if (!TextUtils.isEmpty(floor) && !TextUtils.isEmpty(number)){
            map.put("seat", floor+","+number);
        }

        System.out.println("abacadf_"+map);
        ApiHttp.getApiService().addCartOrder(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( data -> {
                    toast(data.getMsg());
                    if (data.isSuccess()) {
                        if ("2".equals(payment)){
                            payOrder(data.getData().getOrder_id());
                        }
                        getData();
                    }
                }, t -> {
                    t.printStackTrace();
                    dialog.dismiss();
                });
    }

    private void payOrder(long order_id) {
        User user = User.getInstance();
        ApiHttp.getApiService().payOrder(user.getAccess_token(), order_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    dialog.dismiss();
                    if (data.isSuccess()) {
                        Intent it = new Intent(thisAtv, WebViewActivity.class);
                        it.putExtra("html", data.getData().getHtml());
                        startActivity(it);
                    } else {
                        toast(data.getMsg());
                    }
                }, t -> {
                    dialog.dismiss();
                });
    }


    String floor ;
    String number ;
    private ScanBroadcast broadcast;
    public class ScanBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            floor = intent.getStringExtra("floor");
            number = intent.getStringExtra("number");
            binding.floorTipsTv.setText(floor+"樓"+number+"號");
            binding.scanIv.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        thisAtv.unregisterReceiver(broadcast);
        super.onDestroy();
    }

}
