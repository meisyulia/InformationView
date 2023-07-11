package com.example.informationview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.informationview.bean.WeatherBean;
import com.example.informationview.databinding.ItemFutureDayBinding;

import java.util.List;

public class FutureDayAdapter extends RecyclerView.Adapter {

    private final Context context;
    private final List<WeatherBean.ResultBean.FutureBean> futureBeans;

    public FutureDayAdapter(Context context, List<WeatherBean.ResultBean.FutureBean> futureBeans){
        this.context = context;
        this.futureBeans = futureBeans;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFutureDayBinding binding = ItemFutureDayBinding.inflate(LayoutInflater.from(context));
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(5, 5, 5, 5);
        binding.getRoot().setLayoutParams(layoutParams);
        return new ItemHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder = (ItemHolder) holder;
        WeatherBean.ResultBean.FutureBean item = futureBeans.get(position);
        if (item!=null){
            itemHolder.mBinding.tvDate.setText(item.getDate());
            String temperature = item.getTemperature();
            String desc = "最低温度：";
            String[] split = temperature.split("/");
            desc += split[0]+"℃    最高温度："+split[1];
            itemHolder.mBinding.tvTemperature.setText(desc);
            itemHolder.mBinding.tvWeather.setText("天气情况："+item.getWeather());
            itemHolder.mBinding.tvDirect.setText("风向："+item.getDirect());
        }
    }

    @Override
    public int getItemCount() {
        return futureBeans!=null&&futureBeans.size()>0?futureBeans.size():0;
    }

    private class ItemHolder extends RecyclerView.ViewHolder {

        private final com.example.informationview.databinding.ItemFutureDayBinding mBinding;

        public ItemHolder(ItemFutureDayBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }
    }
}
