package com.king.android.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.king.android.Constants;
import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.ActivityShopDetailsBinding;
import com.king.android.databinding.ItemSizeBinding;
import com.king.android.model.Floor;
import com.king.android.model.ProductInfo;
import com.king.android.model.ShopInfo;
import com.king.android.model.SizeInfo;
import com.king.android.model.User;
import com.king.android.ui.QRCodeActivity;
import com.king.android.ui.WebViewActivity;
import com.king.android.ui.ZhifuActivity;
import com.king.android.ui.my.AddressListActivity;
import com.king.base.activity.BaseActivity;
import com.king.base.adapter.BaseKAdapter;
import com.king.base.adapter.BaseKViewHolder;
import com.king.base.dialog.LoadingDialog;
import com.king.base.dialog.PopupListDialog;
import com.king.base.listener.ViewClickListener;
import com.king.base.utils.DpUtils;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public
class ShopDetailsActivity extends BaseActivity<ActivityShopDetailsBinding> {

    private String product_id;
    private String shopId;
    private String tang = "";
    private String bing = "";
    private String la = "";
    private String hot = "";
    private boolean isSend = true;
    private PopupListDialog listDialog;
    private LoadingDialog dialog;
    private ActivityResultLauncher<Intent> launcher;
    private ShopInfo.Product data;

    @Override
    public void init() {
        product_id = getIntentData().getString("product_id");
        shopId = getIntentData().getString("shop_id");
        String name = getIntentData().getString("name");
        String img = getIntentData().getString("img");

        String json = getIntentData().getString("data");
        data = new Gson().fromJson(json, ShopInfo.Product.class);

        binding.setData(data);
        ImageView backIv = binding.getRoot().findViewById(R.id.back_iv);
        backIv.setVisibility(View.VISIBLE);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView titleTv = binding.getRoot().findViewById(R.id.title_tv);
        titleTv.setText(name);
        RequestOptions options = new RequestOptions().placeholder(R.mipmap.wu_img);
        Glide.with(thisAtv).load(img).apply(options).into(binding.avatarIv);

        listDialog = new PopupListDialog();
        dialog = new LoadingDialog(thisAtv);

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                int resultCode = result.getResultCode();
                if (resultCode == RESULT_OK) {
                    String address = data.getStringExtra("address");
                    String name = data.getStringExtra("name");
                    String mobile = data.getStringExtra("mobile");
                    addOrderBtn(address, name, mobile);
                }
            }
        });
        String tagName= "shop_details";
        binding.scanIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launch(QRCodeActivity.class).add(tagName).start();
            }
        });
        broadcast = new ScanBroadcast();
        IntentFilter it = new IntentFilter();
        it.addAction(tagName);
        registerReceiver(broadcast,it);

        initSend();
        initBtn();
        initSendTime();

        binding.tangLayout.setVisibility(View.GONE);
        binding.bingLayout.setVisibility(View.GONE);
        getData();
        getFloor();
    }

    private int productNum;
    private void getData() {
        ApiHttp.getApiService()
                .getProductInfo(product_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    if (data.isSuccess()){
                        ProductInfo data1 = data.getData();
                        try {
                            productNum = Integer.parseInt(data1.getProduct_num());
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        List<SizeInfo> list = new ArrayList<>();
                        SizeInfo info = new SizeInfo();
                        info.setName(data1.getSpec1());
                        info.setMoney(data1.getPrice1());
                        list.add(info);
                        SizeInfo info2 = new SizeInfo();
                        info2.setName(data1.getSpec2());
                        info2.setMoney(data1.getPrice2());
                        list.add(info2);
                        SizeInfo info3 = new SizeInfo();
                        info3.setName(data1.getSpec3());
                        info3.setMoney(data1.getPrice3());
                        list.add(info3);
                        addSizePungency(list);

                        ProductInfo.Param param = data1.getParam();
                        if (param.getTaste() != null && param.getTaste().size() > 0 && "1".equals(param.getTaste_open())){
                            binding.tangLayout.setVisibility(View.VISIBLE);
                            initTang(param.getTaste());
                        }
                        if (param.getIce() != null && param.getIce().size() > 0 && "1".equals(param.getIce_open())){
                            binding.bingLayout.setVisibility(View.VISIBLE);
                            initBing(param.getIce());
                        }
                        if (param.getPungency() != null && param.getPungency().size() > 0 && "1".equals(param.getPungency_open())){
                            binding.pungencyLayout.setVisibility(View.VISIBLE);
                            initLa(param.getPungency());
                        }
                        if ("1".equals(param.getCold_open())){
                            List<String> hotList = new ArrayList<>();
                            if ("1".equals(param.getCold_hot())){
                                hotList.add("冷");
                            }
                            if ("1".equals(param.getCold_cooler())){
                                hotList.add("熱");
                            }
                            if (hotList.size() > 0){
                                binding.hotLayout.setVisibility(View.VISIBLE);
                                initHot(hotList);
                            }
                        }

                    }
                }, t -> {
                    t.printStackTrace();
                });
    }

    BaseKAdapter<SizeInfo, ItemSizeBinding> sizeAdapter;
    private void addSizePungency(List<SizeInfo> list){

        sizeAdapter = new BaseKAdapter<SizeInfo, ItemSizeBinding>() {
            @Override
            public void onItemBindData(BaseKViewHolder holder, ItemSizeBinding binding, SizeInfo data, int position) {
                    binding.setData(data);

                    binding.addTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int sumNum = 1;
                            for (SizeInfo datum : getData()) {
                                sumNum+=datum.getNumber();
                            }
                            if (sumNum > productNum){
                                Toast.makeText(thisAtv, "庫存數量不足", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            data.setNumber(data.getNumber()+1);
                            binding.setData(data);
                            initTotalPrice();
                        }
                    });
                    binding.subTv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int n = data.getNumber()-1;
                            if (n < 0){
                                return;
                            }
                            data.setNumber(n);
                            binding.setData(data);
                            initTotalPrice();
                        }
                    });
            }
        };
        sizeAdapter.setNewData(list);
        binding.sizeRv.setAdapter(sizeAdapter);
    }

    /**
     * 初始化加入购物车或直接购买按钮
     */
    private void initBtn() {
        //直接购买
        binding.buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(thisAtv, AddressListActivity.class);
                it.putExtra("selectType", 1);
                launcher.launch(it);
            }
        });
        //加入购物车
        binding.addShopCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCartBtn();
            }
        });
    }

    private void initTotalPrice() {
        List<SizeInfo> data = sizeAdapter.getData();

        BigDecimal total = new BigDecimal("0");
        for (SizeInfo datum : data) {
            total = total.add(new BigDecimal(datum.getNumber()).multiply(new BigDecimal(datum.getMoney())));
        }

        binding.shopTv.setText(total.stripTrailingZeros().toPlainString() + "元");
        binding.totalTv.setText("計算後金額:" + total.stripTrailingZeros().toPlainString() + "元");

//        if (isSend) {
//            binding.shopTv.setText(total.stripTrailingZeros().toPlainString() + "元+2元包裝費");
//            binding.totalTv.setText("計算後金額:" + total.stripTrailingZeros().toPlainString() + "元");
//        } else {
//            binding.shopTv.setText(total.stripTrailingZeros().toPlainString() + "元");
//            binding.totalTv.setText("計算後金額:" + total.stripTrailingZeros().toPlainString() + "元");
//        }
    }

    /**
     * 初始化糖分按钮
     */
    private void initTang(List<String> tangs) {
        TextView[] tabs = new TextView[tangs.size()];
        int count = binding.tangLayout.getChildCount();
        for (int i = 1; i < count; i++) {
            binding.tangLayout.removeViewAt(1);
        }

        int dp12 = (int) DpUtils.dp2px(12);
        int dp6 = (int) DpUtils.dp2px(6);
        int dp16 = (int) DpUtils.dp2px(16);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = dp12;
        for (int i = 0; i < tangs.size(); i++) {
            TextView tv = new TextView(thisAtv);
            tv.setText(tangs.get(i));
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setBackgroundResource(R.drawable.shape_d1d1d1_radius_100);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(dp16,dp6,dp16,dp6);
            binding.tangLayout.addView(tv,params);
            tabs[i] = tv;
        }
        tang = tangs.get(0);
        tabAddListener(tabs, tangs,null,1);
    }

    /**
     * 初始化冰按钮
     */
    private void initBing(List<String> bings) {
        TextView[] tabs = new TextView[bings.size()];
        int count = binding.bingLayout.getChildCount();
        for (int i = 1; i < count; i++) {
            binding.bingLayout.removeViewAt(1);
        }

        int dp12 = (int) DpUtils.dp2px(12);
        int dp6 = (int) DpUtils.dp2px(6);
        int dp16 = (int) DpUtils.dp2px(16);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = dp12;
        for (int i = 0; i < bings.size(); i++) {
            TextView tv = new TextView(thisAtv);
            tv.setText(bings.get(i));
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setBackgroundResource(R.drawable.shape_d1d1d1_radius_100);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(dp16,dp6,dp16,dp6);
            binding.bingLayout.addView(tv,params);
            tabs[i] = tv;
        }
        bing = bings.get(0);
        tabAddListener(tabs, null,bings,2);
    }

    /**
     * 初始化啦按钮
     */
    private void initLa(List<String> list) {
        TextView[] tabs = new TextView[list.size()];
        int count = binding.pungencyLayout.getChildCount();
        for (int i = 1; i < count; i++) {
            binding.pungencyLayout.removeViewAt(1);
        }

        int dp12 = (int) DpUtils.dp2px(12);
        int dp6 = (int) DpUtils.dp2px(6);
        int dp16 = (int) DpUtils.dp2px(16);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = dp12;
        for (int i = 0; i < list.size(); i++) {
            TextView tv = new TextView(thisAtv);
            tv.setText(list.get(i));
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setBackgroundResource(R.drawable.shape_d1d1d1_radius_100);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(dp16,dp6,dp16,dp6);
            binding.pungencyLayout.addView(tv,params);
            tabs[i] = tv;
        }
        la = list.get(0);
        tabAddListener(tabs, null,list,3);
    }

    /**
     * 初始化啦按钮
     */
    private void initHot(List<String> list) {
        TextView[] tabs = new TextView[list.size()];
        int count = binding.hotLayout.getChildCount();
        for (int i = 1; i < count; i++) {
            binding.hotLayout.removeViewAt(1);
        }

        int dp12 = (int) DpUtils.dp2px(12);
        int dp6 = (int) DpUtils.dp2px(6);
        int dp16 = (int) DpUtils.dp2px(16);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = dp12;
        for (int i = 0; i < list.size(); i++) {
            TextView tv = new TextView(thisAtv);
            tv.setText(list.get(i));
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setBackgroundResource(R.drawable.shape_d1d1d1_radius_100);
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(dp16,dp6,dp16,dp6);
            binding.hotLayout.addView(tv,params);
            tabs[i] = tv;
        }
        hot = list.get(0);
        tabAddListener(tabs, null,list,4);
    }

    private void select(TextView[] tabs, int position) {
        for (int i = 0; i < tabs.length; i++) {
            TextView tv = tabs[i];
            if (i == position) {
                tv.setBackgroundResource(R.drawable.shape_975d19_radius_100);
                tv.setTextColor(Color.WHITE);
            } else {
                tv.setBackgroundResource(R.drawable.shape_d1d1d1_radius_100);
                tv.setTextColor(Color.BLACK);
            }
        }
    }

    private void tabAddListener(TextView[] tabs,List<String> tangs,List<String> bings, int type) {
        select(tabs,0);
        for (int i = 0; i < tabs.length; i++) {
            TextView tv = tabs[i];
            int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select(tabs, finalI);
                    if (type == 1) {
                        tang = tangs.get(finalI);
                    } else if (type == 2) {
                        bing = bings.get(finalI);
                    }else if (type == 3){
                        la = bings.get(finalI);
                    }else if (type == 4){
                        hot = bings.get(finalI);
                    }
                }
            });
        }
    }

    /**
     * 初始化自取还是外卖按钮
     */
    private void initSend() {
        binding.send1Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundResource(R.drawable.shape_975d19_radius_100);
                binding.send1Tv.setTextColor(Color.WHITE);
                binding.send2Tv.setBackgroundResource(R.drawable.shape_d1d1d1_radius_100);
                binding.send2Tv.setTextColor(Color.BLACK);
                isSend = false;

                initTotalPrice();
            }
        });
        binding.send2Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackgroundResource(R.drawable.shape_975d19_radius_100);
                binding.send2Tv.setTextColor(Color.WHITE);
                binding.send1Tv.setBackgroundResource(R.drawable.shape_d1d1d1_radius_100);
                binding.send1Tv.setTextColor(Color.BLACK);
                isSend = true;

                initTotalPrice();
            }
        });
    }

    private int type;
    private void initSendTime() {
        binding.select1Layout.setOnClickListener(new ViewClickListener() {
            @Override
            public void click(View v) {
                if (list == null){
                    return;
                }
                type = 1;
                List<String> list = new ArrayList<>();
                for (Floor floor : ShopDetailsActivity.this.list) {
                    list.add(floor.getFloor());
                }
                listDialog.setList(list);
                listDialog.show(v);
            }
        });
        binding.select2Layout.setOnClickListener(new ViewClickListener() {
            @Override
            public void click(View v) {
                if (floorInfo == null){
                    Toast.makeText(thisAtv, "請選擇樓層", Toast.LENGTH_SHORT).show();
                    return;
                }
                List<String> list = new ArrayList<>();
                for (Floor.FloorNumber number : floorInfo.getList()) {
                    if (TextUtils.equals("0",number.getStatus())){
                        list.add(number.getTable_number());
                    }
                }
                if (list.isEmpty()){
                    Toast.makeText(thisAtv, "此樓層已滿座", Toast.LENGTH_SHORT).show();
                    return;
                }

                type = 2;
                listDialog.setList(list);
                listDialog.show(v);
            }
        });
        listDialog.setOnItemClickListener(new PopupListDialog.OnItemClickListener() {
            @Override
            public void onClick(int position, String text) {
                if(type == 1){
                    floor = text;
                    binding.select1Tv.setText(text);
                    initFloor(position);
                }else if(type == 2){
                    number = text;
                    binding.select2Tv.setText(text);
                }
            }
        });
    }

    private void addOrderBtn(String address, String accept_name, String mobile) {
        StringBuffer spec = new StringBuffer();
        StringBuffer num = new StringBuffer();
        List<SizeInfo> data = sizeAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            if (spec.length() > 0){
                spec.append(",");
            }
            if (num.length() > 0){
                num.append(",");
            }
            num.append(data.get(i).getNumber());
            if (i == 0){
                spec.append("1");
            }else if (i == 1){
                spec.append("2");
            }else if (i == 2){
                spec.append("3");
            }
        }

        if (num.length() == 0) {
            toast("請選擇大小規格");
            return;
        }
        if (TextUtils.isEmpty(address)) {
            toast("選擇地址");
            return;
        }
        if (TextUtils.isEmpty(mobile)) {
            toast("請填寫手機號碼");
            return;
        }
        if (TextUtils.isEmpty(accept_name)) {
            toast("請填寫收貨人名字");
            return;
        }

        addOrder(spec.toString(), num.toString(), accept_name, address, mobile);
    }

    private void addCartBtn() {
        if (sizeAdapter == null){
            Toast.makeText(thisAtv, "未選擇產品數量", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuffer spec = new StringBuffer();
        StringBuffer num = new StringBuffer();
        List<SizeInfo> data = sizeAdapter.getData();
        for (int i = 0; i < data.size(); i++) {
            if (spec.length() > 0){
                spec.append(",");
            }
            if (num.length() > 0){
                num.append(",");
            }
            num.append(data.get(i).getNumber());
            if (i == 0){
                spec.append("1");
            }else if (i == 1){
                spec.append("2");
            }else if (i == 2){
                spec.append("3");
            }
        }

        if (num.length() == 0) {
            toast("請選擇大小規格");
            return;
        }
        addCart(spec.toString(), num.toString());
    }

    private void addCart(String spec, String num) {
        dialog.show();
        User user = User.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", user.getAccess_token());
        map.put("product_id", product_id);
        map.put("spec", spec);
        map.put("num", num);
        map.put("taste", tang);
        map.put("ice", bing);
        map.put("pungency", la);
        map.put("cold", hot);
        map.put("is_outside", isSend ? 1 : 0);
//        map.put("send_time", sendTime);
        map.put("remark", binding.editRemark.getText().toString());
        if (!TextUtils.isEmpty(floor) && !TextUtils.isEmpty(number)){
            map.put("seat", floor+","+number);
        }
        ApiHttp.getApiService().addCart(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    dialog.dismiss();
                    if (data.isSuccess()) {
                        toast("新增成功");
                        EventBus.getDefault().post("finis_all");
                        EventBus.getDefault().post("select_car");
                    } else {
                        toast(data.getMsg());
                    }
                }, t -> {
                    t.printStackTrace();
                    dialog.dismiss();
                });
    }

    private void addOrder(String spec, String num, String accept_name, String address, String mobile) {
        dialog.show();
        User user = User.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("access_token", user.getAccess_token());
        map.put("product_id", product_id);
        map.put("spec", spec);
        map.put("num", num);
        map.put("accept_name", accept_name);
        map.put("address", address);
        map.put("mobile", mobile);
        map.put("taste", tang);
        map.put("ice", bing);
        map.put("pungency", la);
        map.put("cold", hot);
        map.put("is_outside", isSend ? 1 : 0);
//        map.put("send_time", sendTime);
        map.put("remark", binding.editRemark.getText().toString());
        map.put("payment", "2");
        ApiHttp.getApiService().addOrder(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    toast(data.getMsg());
                    if (data.isSuccess()) {
                        payOrder(data.getData().getOrder_id());
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
                        EventBus.getDefault().post("finis_all");
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

    public void getFloor(){
        User user = User.getInstance();
        ApiHttp.getApiService().getFloor(user.getAccess_token(),shopId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {

                    list = data.getData();
                    initFloor(0);
                }, t -> {
                    t.printStackTrace();
                });
    }

    private List<Floor> list;
    private Floor floorInfo;

    private void initFloor(int selectIndex){
        if (list!= null && list.size() > selectIndex){
            floorInfo = list.get(selectIndex);
            binding.select1Tv.setText(floorInfo.getFloor());
            List<Floor.FloorNumber> list = floorInfo.getList();
            for (Floor.FloorNumber num : list) {
                if (TextUtils.equals("0",num.getStatus())){
                    binding.select2Tv.setText(num.getTable_number());
                    break;
                }
            }
        }
    }

    String floor ;
    String number ;
    private ScanBroadcast broadcast;
    public class ScanBroadcast extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            floor = intent.getStringExtra("floor");
            number = intent.getStringExtra("number");
            binding.select1Tv.setText(floor);
            binding.select2Tv.setText(number);
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcast);
        super.onDestroy();
    }
}
