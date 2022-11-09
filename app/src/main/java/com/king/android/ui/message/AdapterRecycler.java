package com.king.android.ui.message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.king.android.R;
import com.king.android.model.AppMessage;

import java.util.List;

public class AdapterRecycler extends RecyclerView.Adapter {
    private List<AppMessage> dataList;
    public AdapterRecycler(List<AppMessage> dataList){
        this.dataList=dataList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message,parent,false);
        return new MsgHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    class MsgHolder extends RecyclerView.ViewHolder{

        public MsgHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
