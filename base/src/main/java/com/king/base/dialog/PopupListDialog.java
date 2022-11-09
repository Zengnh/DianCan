package com.king.base.dialog;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;

import com.king.base.listener.ViewClickListener;
import com.king.base.utils.DpUtils;

import java.util.List;

public
class PopupListDialog extends PopupWindow {

    private List<String> list;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public void show(View view){
        if (list == null || list.isEmpty()) return;

        LinearLayout contentView = new LinearLayout(view.getContext());
        contentView.setOrientation(LinearLayout.VERTICAL);

        int dp12 = (int) DpUtils.dp2px(12);
        int dp6 = (int) DpUtils.dp2px(6);
        LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < list.size(); i++) {
            String text = list.get(i);
            TextView tv = new TextView(view.getContext());
            tv.setText(text);
            tv.setTextSize(14);
            tv.setTag(i);
            tv.setLayoutParams(itemParams);
            tv.setPadding(dp12,dp6,dp12,dp6);
            tv.setOnClickListener(clickListener);
            contentView.addView(tv);
        }

        int dp3 = (int) DpUtils.dp2px(3);
        //对话框白色背景+圆角
        CardView cardView = new CardView(view.getContext());
        cardView.setCardBackgroundColor(Color.WHITE);
        cardView.setRadius(dp6);
        cardView.setCardElevation(dp3);
        FrameLayout.LayoutParams cardParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardParams.leftMargin = dp3;
        cardParams.rightMargin = dp3;
        cardParams.topMargin = dp3;
        cardParams.bottomMargin = dp12;
        cardView.setLayoutParams(cardParams);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //对话框可以滑动
        NestedScrollView scrollView = new NestedScrollView(view.getContext());
        scrollView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        cardView.addView(scrollView,layoutParams);
        scrollView.addView(contentView,layoutParams);

        FrameLayout layout = new FrameLayout(view.getContext());
        layout.addView(cardView);
        setContentView(layout);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //点击其他地方取消对话框
        setOutsideTouchable(true);
        showAsDropDown(view);
    }


    private ViewClickListener  clickListener = new ViewClickListener() {
        @Override
        public void click(View v) {
            int position = (int) v.getTag();
            String text = list.get(position);
            if (onItemClickListener != null){
                onItemClickListener.onClick(position,text);
            }
            dismiss();
        }
    };

    public interface OnItemClickListener{
        void onClick(int position,String text);
    }
}
