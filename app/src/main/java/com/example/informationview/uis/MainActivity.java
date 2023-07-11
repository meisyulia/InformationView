package com.example.informationview.uis;

import static com.example.informationview.constant.APIConst.NEWS_API;
import static com.example.informationview.constant.APIConst.NEWS_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.example.informationview.R;
import com.example.informationview.bean.NewsBean;
import com.example.informationview.constant.APIConst;
import com.example.informationview.databinding.ActivityMainBinding;
import com.example.informationview.uis.BaseActivity;
import com.example.informationview.uis.fragment.NewsFragment;
import com.example.informationview.uis.fragment.WeatherFragment;
import com.example.informationview.util.common.GsonUtil;
import com.example.informationview.util.common.StatusBarUtil;
import com.example.informationview.viewmodel.ApiNetViewModel;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private com.example.informationview.databinding.ActivityMainBinding mBinding;
    private Bundle instanceState;
    //当savedInstanceState为null时，表示是首次创建Activity，而不是重新进入应用。

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        StatusBarUtil.setStatusBarColor(this,getResources().getColor(R.color.white));
        //instanceState = savedInstanceState;
        initData();
        initView();
    }

    @Override
    protected void initData() {
        /*ApiNetViewModel apiNetViewModel = new ApiNetViewModel();
        apiNetViewModel.setResultCallback(this);
        HashMap<String, String> newsParams = new HashMap<>();
        newsParams.put("key", NEWS_KEY);
        newsParams.put("type","top");
        apiNetViewModel.newsApiExecute(NEWS_API,newsParams);*/
    }

    @Override
    protected void initView() {
        changePage(0);
        mBinding.tvNews.setOnClickListener(v->changePage(0));
        mBinding.tvWeather.setOnClickListener(v->changePage(1));
    }

    private void changePage(int i) {
        //boolean isFirst = instanceState == null? true:false;
        //Log.i(TAG, "changePage: isFirst="+isFirst);
        switch (i){
            case 0:
                mBinding.tvNews.setSelected(true);
                mBinding.tvWeather.setSelected(false);
                getSupportFragmentManager().beginTransaction().replace(R.id.fcv_main,NewsFragment.getInstance(),NewsFragment.class.getSimpleName())
                        .commitAllowingStateLoss();
                break;
            case 1:
                mBinding.tvNews.setSelected(false);
                mBinding.tvWeather.setSelected(true);
                getSupportFragmentManager().beginTransaction().replace(R.id.fcv_main,WeatherFragment.getInstance(),WeatherFragment.class.getSimpleName())
                        .commitAllowingStateLoss();
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //MainActivity将要销毁时将fragment销毁，不然重新进入应用时会保存值的
        /*Fragment newsFragment = getSupportFragmentManager().findFragmentByTag(NewsFragment.class.getSimpleName());
        //Log.i(TAG, "onDestroy: newsFragment="+newsFragment);
        if (newsFragment!=null) {
            getSupportFragmentManager().beginTransaction().remove(newsFragment).commit();
            newsFragment = null;
        }
        Fragment weatherFragment = getSupportFragmentManager().findFragmentByTag(WeatherFragment.class.getSimpleName());
        if (weatherFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(weatherFragment).commit();
            weatherFragment = null;
        }*/

         try {
            getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy: mainActivity--destroy");

        super.onDestroy();

    }
}