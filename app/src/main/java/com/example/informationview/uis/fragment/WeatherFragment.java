package com.example.informationview.uis.fragment;

import static com.example.informationview.constant.APIConst.WEATHER_API;
import static com.example.informationview.constant.APIConst.WEATHER_KEY;
import static com.example.informationview.constant.APIConst.WEATHER_LIFE_API;
import static com.example.informationview.constant.WeatherType.*;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.informationview.adapter.FutureDayAdapter;
import com.example.informationview.bean.LifeResultBean;
import com.example.informationview.bean.WeatherBean;
import com.example.informationview.constant.APIConst;
import com.example.informationview.constant.WeatherType;
import com.example.informationview.databinding.FragmentWeatherBinding;
import com.example.informationview.util.common.DateUtil;
import com.example.informationview.viewmodel.ApiNetViewModel;

import java.util.HashMap;
import java.util.List;

public class WeatherFragment extends BaseFragment implements ApiNetViewModel.WeatherCallback, ApiNetViewModel.LifeCallback {

    private static final String TAG = "WeatherFragment";
    private static WeatherFragment fragment;
    private com.example.informationview.databinding.FragmentWeatherBinding mBinding;
    private String mCity;
    private HashMap<String, String> mWeatherHashMap;
    private ApiNetViewModel apiNetViewModel;
    private String mLastCity;
    private LinearLayoutManager mWeatherLLM;
    private FutureDayAdapter mFutureAdap;
    private WeatherBean.ResultBean.RealtimeBean realtime;
    private List<WeatherBean.ResultBean.FutureBean> futureBeans;
    private LifeResultBean.ResultBean.LifeBean lifeBean;
    private String error;
    private WeatherType type;

    public static WeatherFragment getInstance(){
        Log.i(TAG, "getInstance: fragment="+fragment);
        if (fragment == null) {
            synchronized (WeatherFragment.class){
                fragment = new WeatherFragment();
            }
        }
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentWeatherBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    protected void initData() {
        apiNetViewModel = ApiNetViewModel.getInstance();
    }

    @Override
    protected void initView() {
        if (TextUtils.isEmpty(mCity)){
            showView(NONE);
        }else{
            if (type == ERROR){
                updateError(error);
                showView(ERROR);
            }
            showView(WEATHER);
            updateRealTime(realtime);
            updateFutureDay(futureBeans);
            if (!TextUtils.isEmpty(mLastCity)){
                updateLife(lifeBean);
            }
        }

        mBinding.etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()>0){
                    mBinding.ivDelete.setVisibility(View.VISIBLE);
                }else{
                    mBinding.ivDelete.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mBinding.ivDelete.setOnClickListener(v->{
            mBinding.etCity.setText("");
        });
        mBinding.tvSearch.setOnClickListener(v->{
            mCity = mBinding.etCity.getText().toString();
            if (TextUtils.isEmpty(mCity)){
                showTips("请输入城市名字");
                return;
            }else{
                mWeatherHashMap = new HashMap<>();
                mWeatherHashMap.put("key", WEATHER_KEY);
                mWeatherHashMap.put("city",mCity);
                apiNetViewModel.setWeatherCallback(this);
                apiNetViewModel.weatherExecute(WEATHER_API,mWeatherHashMap);
            }
        });
        mBinding.tvLife.setOnClickListener(v->{
            Log.i(TAG, "initView: mLastCity="+mLastCity);
            if (TextUtils.isEmpty(mLastCity) || !TextUtils.equals(mLastCity,mCity)){
                apiNetViewModel.setLifeCallback(this);
                apiNetViewModel.lifeExecute(WEATHER_LIFE_API,mWeatherHashMap);
            }else{
                showView(LIFE);
            }
        });mBinding.tvBack.setOnClickListener(v->{
            showView(WEATHER);
        });
    }

    private void showView(WeatherType type) {
        this.type = type;
        switch (type){
            case NONE:
                mBinding.llInfo.setVisibility(View.GONE);
                mBinding.tvError.setVisibility(View.GONE);
                mBinding.llLife.setVisibility(View.GONE);
                break;
            case WEATHER:
                mBinding.llInfo.setVisibility(View.VISIBLE);
                mBinding.tvError.setVisibility(View.GONE);
                mBinding.llLife.setVisibility(View.GONE);
                break;
            case LIFE:
                mBinding.llInfo.setVisibility(View.GONE);
                mBinding.tvError.setVisibility(View.GONE);
                mBinding.llLife.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                mBinding.llInfo.setVisibility(View.GONE);
                mBinding.tvError.setVisibility(View.VISIBLE);
                mBinding.llLife.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onSuccess(WeatherBean.ResultBean resultBean) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showView(WEATHER);
                realtime = resultBean.getRealtime();
                updateRealTime(realtime);
                futureBeans = resultBean.getFuture();
                updateFutureDay(futureBeans);
            }
        });

    }

    @Override
    public void onSuccess(LifeResultBean.ResultBean resultBean) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showView(LIFE);
                mBinding.tvCityName.setText(mCity);
                lifeBean = resultBean.getLife();
                mLastCity = resultBean.getCity();
                updateLife(lifeBean);
            }
        });

    }

    @Override
    public void onFail(String error) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WeatherFragment.this.error = error;
                showView(ERROR);
                updateError(error);
            }
        });

    }

    private void updateError(String error) {
        mBinding.tvError.setText(error);
    }

    private void updateRealTime(WeatherBean.ResultBean.RealtimeBean realtime) {
        mBinding.tvQueryTime.setText("查询时间："+ DateUtil.getNowDateTimeFormat());
        mBinding.tvCityWeather.setText(mCity+"--"+realtime.getInfo());
        mBinding.tvTemperature.setText("温度："+realtime.getTemperature()+"℃");
        mBinding.tvHumidity.setText("湿度："+realtime.getHumidity());
        mBinding.tvDirect.setText("风向："+realtime.getDirect());
        mBinding.tvPower.setText("风力："+realtime.getPower());
        mBinding.tvAqi.setText("空气质量指数："+realtime.getAqi());
    }

    private void updateFutureDay(List<WeatherBean.ResultBean.FutureBean> futureBeans) {
        mWeatherLLM = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mBinding.rvFutureDay.setLayoutManager(mWeatherLLM);
        mFutureAdap = new FutureDayAdapter(getContext(), futureBeans);
        mBinding.rvFutureDay.setAdapter(mFutureAdap);
    }

    private void updateLife(LifeResultBean.ResultBean.LifeBean lifeBean) {
        mBinding.tvKongtiao.setText(lifeBean.getKongtiao().getV()+"\n"+lifeBean.getKongtiao().getDes());
        mBinding.tvGuomin.setText(lifeBean.getGuomin().getV()+"\n"+lifeBean.getGuomin().getDes());
        mBinding.tvShushidu.setText(lifeBean.getShushidu().getV()+"\n"+lifeBean.getShushidu().getDes());
        mBinding.tvChuanyi.setText(lifeBean.getChuanyi().getV()+"\n"+lifeBean.getChuanyi().getDes());
        mBinding.tvDiaoyu.setText(lifeBean.getDiaoyu().getV()+"\n"+lifeBean.getDiaoyu().getDes());
        mBinding.tvGanmao.setText(lifeBean.getGanmao().getV()+"\n"+lifeBean.getGanmao().getDes());
        mBinding.tvZiwaixian.setText(lifeBean.getZiwaixian().getV()+"\n"+lifeBean.getZiwaixian().getDes());
        mBinding.tvXiche.setText(lifeBean.getXiche().getV()+"\n"+lifeBean.getXiche().getDes());
        mBinding.tvYundong.setText(lifeBean.getYundong().getV()+"\n"+lifeBean.getYundong().getDes());
        mBinding.tvDaisan.setText(lifeBean.getDaisan().getV()+"\n"+lifeBean.getDaisan().getDes());
    }
}
