package com.king.android.ui.message;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.king.android.R;
import com.king.android.api.ApiHttp;
import com.king.android.databinding.FragmentMessageBinding;
import com.king.android.databinding.ItemMessageBinding;
import com.king.android.model.AppMessage;
import com.king.android.model.User;
import com.king.base.adapter.BaseKAdapter;
import com.king.base.adapter.BaseKViewHolder;
import com.king.base.dialog.LoadingDialog;
import com.king.base.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MessageFragment extends BaseFragment<FragmentMessageBinding> {
    private static final String TAG = "MessageFragment";
    private BaseKAdapter<AppMessage, ItemMessageBinding> adapter;

    @Override
    public void init() {
        TextView titleTv = binding.getRoot().findViewById(R.id.title_tv);
        titleTv.setText("最新 優惠消息");
        adapter = new BaseKAdapter<AppMessage, ItemMessageBinding>() {
            @Override
            public void onItemBindData(BaseKViewHolder holder, ItemMessageBinding binding, AppMessage data, int position) {
                binding.setData(data);
                if (data.getType() == 1) {
                    Glide.with(thisAtv).load(R.mipmap.icon_msge).into(binding.msgIcon);
                } else if (data.getType() == 2) {
                    Glide.with(thisAtv).load(R.mipmap.icon_note).into(binding.msgIcon);
                } else if (data.getType() == 3) {
                    Glide.with(thisAtv).load(R.mipmap.icon_remove).into(binding.msgIcon);
                } else {
                    Glide.with(thisAtv).load(R.mipmap.icon_msge).into(binding.msgIcon);
                }
            }
        };

        binding.msgRv.setAdapter(adapter);
        dialog = new LoadingDialog(thisAtv);
    }

    @Override
    public void onResume() {
        super.onResume();
        reqData();
    }

    private LoadingDialog dialog;

    private void reqData() {
        dialog.show();
        User user = User.getInstance();
        ApiHttp.getApiService().queryMessage(user.getAccess_token())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    List<AppMessage> msgData = (List<AppMessage>) data.getData();
//                    List<AppMessage> msgData = new ArrayList<>();
//                    msgData.add(new AppMessage(1, "系统信息", "08:35", "訂單已發貨，請留意的您的手機，保持暢通單已發貨 請留意的您的手單已發貨，請留意的您的手單已發貨 單已發貨，請留意的您的手單已發貨，請留意的您的 手，請留意的您的手"));
//                    msgData.add(new AppMessage(2, "活动消息", "昨天 12:00", "訂單已發貨，請留意的您的手機，保持暢通單已發貨 請留意的您的手單已發貨，請留意的您的手單已發貨 單已發貨，請留意的您的手單已發貨，請留意的您的 手，請留意的您的手"));
//                    msgData.add(new AppMessage(3, "物流信息", "2022-10-12 12:00", "訂單已發貨，請留意的您的手機，保持暢通單已發貨 請留意的您的手單已發貨，請留意的您的手單已發貨 單已發貨，請留意的您的手單已發貨，請留意的您的 手，請留意的您的手"));
//                    msgData.add(new AppMessage(2, "活动消息", "2022-09-12 12:00", "訂單已發貨，請留意的您的手機，保持暢通單已發貨 請留意的您的手單已發貨，請留意的您的手單已發貨 單已發貨，請留意的您的手單已發貨，請留意的您的 手，請留意的您的手"));
                    adapter.setNewData(msgData);
                    dialog.dismiss();
//                    binding.moneyTv.setText(new BigDecimal(data.getData().getMoney()).stripTrailingZeros().toPlainString());
                }, t -> {
                    t.printStackTrace();
                });
    }
}