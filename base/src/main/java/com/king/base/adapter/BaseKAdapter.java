package com.king.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.king.base.interface_.OnItemClickListener;
import com.king.base.utils.ClickUtils;
import com.king.base.utils.ViewDataBindingUtils;

import java.util.ArrayList;
import java.util.List;

public
abstract
class BaseKAdapter<D,VB extends ViewDataBinding> extends RecyclerView.Adapter<BaseKViewHolder> implements View.OnClickListener{

    private static final int TYPE_HEADER = -11;
    private static final int TYPE_FOOTER = -22;
    private static final int TYPE_EMPTY = -33;
    private static final int TYPE_ITEM = -55;
    private static final int NOT_ITEM = -44;

    private List<D> list;
    private Class viewHolder;
    private KViewHolder headViewHolder;
    private KViewHolder footerViewHolder;
    private KViewHolder emptyViewHolder;
    private OnItemClickListener<D,VB> onItemClickListener;
    public Context context;

    public BaseKAdapter() {
        list = new ArrayList<>();
        viewHolder = ViewDataBindingUtils.getParamClass(getClass(), 1);
    }

    public void add(D data) {
        list.add(data);
        notifyDataSetChanged();
    }

    public void addItem(D data) {
        list.add(data);
        notifyItemInserted(list.size());
    }

    public void addAll(List<D> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setNewData(List<D> list) {
        this.list.clear();
        if (list != null) {
            this.list.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void clear(){
        list.clear();
        notifyDataSetChanged();
    }

    public D getItem(int position) {
        if (position < list.size()) {
            return list.get(position);
        }
        return null;
    }

    public List<D> getData(){
        return list;
    }

    public D remove(int position) {
        D data = null;
        if (position < list.size()) {
            data = list.remove(position);
            notifyDataSetChanged();
        }
        return data;
    }

    public D removeItem(int position) {
        D data = null;
        if (position < list.size()) {
            data = list.remove(position);
            notifyItemRemoved(position);
        }
        return data;
    }

    public void remove(D data) {
        list.remove(data);
        notifyDataSetChanged();
    }

    public void remove(List<D> list) {
        if (list == null) return;
        while (list.size() > 0) {
            for (D d : list) {
                this.remove(d);
                list.remove(d);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener<D,VB> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置头部
     * @param headViewHolder
     */
    public void setHeadViewHolder(KViewHolder headViewHolder) {
        this.headViewHolder = headViewHolder;
        notifyDataSetChanged();
    }

    /**
     * 设置尾部
     * @param footerViewHolder
     */
    public void setFooterViewHolder(KViewHolder footerViewHolder) {
        this.footerViewHolder = footerViewHolder;
        notifyDataSetChanged();
    }

    /**
     * 设置空数据
     * 如果list没有数据显示的布局
     * @param emptyViewHolder
     */
    public void setEmptyViewHolder(KViewHolder emptyViewHolder) {
        this.emptyViewHolder = emptyViewHolder;
        notifyDataSetChanged();
    }

    public boolean headViewIsNull(){
        return headViewHolder == null;
    }

    public boolean footerViewIsNull(){
        return footerViewHolder == null;
    }

    public boolean emptyViewIsNull(){
        return emptyViewHolder == null;
    }

    public boolean dataIsEmpty(){
        return list.isEmpty();
    }

    public boolean positionIsLast(int position){
        return getItemCount() - 1 == position;
    }

    /**
     * 当前position转list的下标
     * 如果返回-1，证明不是list的下标，
     * 大于list.size() 也会返回-1
     * @param position 当前position
     * @return
     */
    public int positionToItemPosition(int position){
        if (dataIsEmpty()){
            return -1;
        }
        if (!headViewIsNull() && position > 0){
            int p = position - 1;
            if (p < list.size()){
                return p;
            }
        } else if (headViewIsNull()){
            if (position < list.size()){
                return position;
            }
        }
        return -1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && !headViewIsNull()) {
            return TYPE_HEADER;
        }else {
            if (positionToItemPosition(position) != -1){
                return TYPE_ITEM;
            }
            if (!footerViewIsNull() && positionIsLast(position)){
                return TYPE_FOOTER;
            }
            if (dataIsEmpty() && !emptyViewIsNull()){
                return TYPE_EMPTY;
            }
        }
        throw new RuntimeException("position = "+ position);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        context = recyclerView.getContext();
        if (recyclerView.getLayoutManager() == null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        }
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (viewType == TYPE_HEADER || viewType == TYPE_FOOTER || viewType == TYPE_EMPTY) {
                        return manager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public final BaseKViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        BaseKViewHolder holder = null;
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        if (viewType == TYPE_HEADER) {
            if (headViewHolder.holder == null) {
                headViewHolder.initHolder(inflater,parent);
            }
            holder = headViewHolder.holder;
        } else if (viewType == TYPE_FOOTER) {
            if (footerViewHolder.holder == null) {
                footerViewHolder.initHolder(inflater,parent);
            }
            holder = footerViewHolder.holder;
        } else if (viewType == TYPE_EMPTY){
            if (emptyViewHolder.holder == null) {
                emptyViewHolder.initHolder(inflater,parent);
            }
//            if (emptyViewHolder.isHeightMatchParent()){
//                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//            }
            holder = emptyViewHolder.holder;
        } else if (viewType == TYPE_ITEM) {
            Object binding = ViewDataBindingUtils.getViewBinding(viewHolder, inflater,parent,false);
            holder = new BaseKViewHolder((VB) binding);
        }
        if (holder == null)
            throw new RuntimeException("没有找到的viewType");

//        holder.itemView.setLayoutParams(layoutParams);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseKViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_HEADER) {
            headViewHolder.onViewBindData(holder.binding);
        } else if (viewType == TYPE_FOOTER) {
            footerViewHolder.onViewBindData(holder.binding);
        }else if (viewType == TYPE_EMPTY){
            emptyViewHolder.onViewBindData(holder.binding);
        } else {
            int p = positionToItemPosition(position);
            D data = list.get(p);

            holder.setItemPosition(p);
            holder.itemView.setTag(null);
            onItemBindData(holder,(VB) holder.binding, data, p);
            if (holder.itemView.getTag() != null){
                throw new RuntimeException("不能使用itemView.setTag()方法");
            }
            holder.itemView.setTag(holder);
            holder.itemView.setOnClickListener(this);
        }
    }

    public abstract void onItemBindData(BaseKViewHolder holder,VB binding, D data, int position);

    @Override
    public int getItemCount() {
        int i = 0;
        if (!headViewIsNull()) {
            i++;
        }
        if (!footerViewIsNull()) {
            i++;
        }
        if (dataIsEmpty() && !emptyViewIsNull()){
            i++;
        }
        return list.size() + i;
    }

    @Override
    public void onClick(View v) {
        if (onItemClickListener == null || !ClickUtils.click(500)){
            return;
        }
        if (v.getTag() instanceof BaseKViewHolder){
            BaseKViewHolder holder = (BaseKViewHolder) v.getTag();
            int p = holder.getItemPosition();
            D data = getItem(p);
            VB binding = (VB) holder.binding;
            onItemClickListener.onClick(v,data,binding,p);
        }
    }
}
