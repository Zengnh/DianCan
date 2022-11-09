package com.king.android.adapter;

import android.graphics.Color;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.king.android.R;
import com.king.android.databinding.ItemCategoryBinding;
import com.king.android.model.Category;
import com.king.base.adapter.BaseKAdapter;
import com.king.base.adapter.BaseKViewHolder;

public
class CategoryAdapter extends BaseKAdapter<Category, ItemCategoryBinding> {

    private int selectPosition;

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        notifyDataSetChanged();
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    @Override
    public void onItemBindData(BaseKViewHolder holder, ItemCategoryBinding binding, Category data, int position) {
        Glide.with(context).load(data.getCategory_icon()).into(binding.imgIv);
        binding.textTv.setText(data.getCategory_name());
        TextView tv = binding.textTv;
        if (selectPosition == position){
            binding.layoutItem.setBackgroundResource(R.drawable.bg_shape_main_sel);
//            tv.setTextColor(Color.parseColor("#94620A"));
        }else {
            binding.layoutItem.setBackgroundResource(R.drawable.bg_shape_main_nor);
//            tv.setTextColor(Color.parseColor("#7D7D7D"));
        }
    }
}
