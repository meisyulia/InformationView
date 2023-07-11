package com.example.informationview.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.informationview.bean.NewsBean;
import com.example.informationview.databinding.ItemNewsBinding;
import com.example.informationview.listener.OnItemClickListener;
import com.example.informationview.util.PictureUtil;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<NewsBean.ResultBean.DataBean> dataList;
    private OnItemClickListener onItemClickListener;

    public NewsAdapter(Context context, List<NewsBean.ResultBean.DataBean> dataList){
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemNewsBinding binding = ItemNewsBinding.inflate(LayoutInflater.from(context));
        return new ItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        NewsBean.ResultBean.DataBean item = dataList.get(position);
        if (item!=null){
            itemHolder.mBinding.tvTitle.setText(item.getTitle());
            itemHolder.mBinding.tvAuthor.setText(item.getAuthor_name());
            itemHolder.mBinding.tvDate.setText(item.getDate());
            if (!TextUtils.isEmpty(item.getThumbnail_pic_s())){
                itemHolder.mBinding.ivPic1.setVisibility(View.VISIBLE);
                PictureUtil.loadPicture(context,item.getThumbnail_pic_s(),itemHolder.mBinding.ivPic1);
            }else{
                itemHolder.mBinding.ivPic1.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(item.getThumbnail_pic_s02())){
                itemHolder.mBinding.ivPic2.setVisibility(View.VISIBLE);
                PictureUtil.loadPicture(context,item.getThumbnail_pic_s02(),itemHolder.mBinding.ivPic2);
            }else{
                itemHolder.mBinding.ivPic2.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(item.getThumbnail_pic_s03())){
                itemHolder.mBinding.ivPic3.setVisibility(View.VISIBLE);
                PictureUtil.loadPicture(context,item.getThumbnail_pic_s03(),itemHolder.mBinding.ivPic3);
            }else{
                itemHolder.mBinding.ivPic3.setVisibility(View.GONE);
            }
            itemHolder.mBinding.llItem.setOnClickListener(v->{
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList!=null&&dataList.size()>0? dataList.size():0;
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private final com.example.informationview.databinding.ItemNewsBinding mBinding;

        public ItemHolder(ItemNewsBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }



}
