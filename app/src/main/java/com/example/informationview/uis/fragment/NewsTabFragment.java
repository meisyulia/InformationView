package com.example.informationview.uis.fragment;

import static com.example.informationview.constant.APIConst.NEWS_API;
import static com.example.informationview.constant.APIConst.NEWS_KEY;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.informationview.adapter.NewsAdapter;
import com.example.informationview.bean.NewsBean;
import com.example.informationview.databinding.ItemTabNewsBinding;
import com.example.informationview.listener.OnItemClickListener;
import com.example.informationview.uis.DetailActivity;
import com.example.informationview.util.common.DateUtil;
import com.example.informationview.viewmodel.ApiNetViewModel;
import com.example.informationview.widget.ReFreshParent;

import java.util.HashMap;
import java.util.List;

public class NewsTabFragment extends BaseFragment implements ApiNetViewModel.ResultCallback, ReFreshParent.RefreshCompleteListener {

    private static final String TAG = "NewsTabFragment";
    private static NewsTabFragment fragment;
    private com.example.informationview.databinding.ItemTabNewsBinding mBinding;
    private String type;
    private Context context;
    private NewsAdapter mNewsAdapt;
    private String refreshTime;
    private HashMap<String, String> mNewsParams;
    private ApiNetViewModel apiNetViewModel;
    private List<NewsBean.ResultBean.DataBean> dataBeans;

    public static NewsTabFragment getInstance(String type){
        fragment = new NewsTabFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getContext();
        if (getArguments()!=null){
            type = getArguments().getString("type");
            Log.i(TAG, "onCreateView: type="+type);
        }
        mBinding = ItemTabNewsBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    protected void initData() {
        if (!TextUtils.isEmpty(type)){
            mNewsParams = new HashMap<>();
            mNewsParams.put("key", NEWS_KEY);
            mNewsParams.put("type",type);
        }
    }

    private void updateNews() {
        if (!TextUtils.isEmpty(type)){
            ApiNetViewModel apiNetViewModel = ApiNetViewModel.getInstance();
            apiNetViewModel.setResultCallback(this);
            //Log.i(TAG, "updateNews: refreshTime="+refreshTime);
            //Log.i(TAG, "updateNews: DateUtil.getNowDateTime(\"MM-dd HH:mm\")="+DateUtil.getNowDateTime("MM-dd HH:mm"));
            //一个假的刷新，真实刷新要隔5分钟
            mBinding.rpRefresh.initRefreshTime( DateUtil.getNowDateTime("MM-dd HH:mm"));
            if (TextUtils.equals(DateUtil.getNowDateTime("MM-dd HH:mm"),refreshTime)){
                //如果时间相同，暂时不请求，（主要原因是请求的次数不多，容易很快没了）
                return;
            }
            if (!TextUtils.isEmpty(refreshTime)){
                long timeDiffer = DateUtil.calculTimeDiffer("MM-dd HH:mm", refreshTime, DateUtil.getNowDateTime("MM-dd HH:mm"));
                if (timeDiffer<60*5){ //小于5分钟的也先不请求
                    return;
                }
            }
            apiNetViewModel.newsApiExecute(NEWS_API,mNewsParams);
        }
    }

    @Override
    protected void initView() {
        //mBinding.tvContent.setText(type);
        LinearLayoutManager LLM = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        mBinding.rvNews.setLayoutManager(LLM);
        mBinding.rpRefresh.setRefreshCompleteListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Log.i(TAG, "onResume: type="+mNewsParams.get("type"));
        //发送请求
        updateNews();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onSuccess(List<NewsBean.ResultBean.DataBean> dataBeans) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NewsTabFragment.this.dataBeans = dataBeans;
                updateNews(dataBeans);
            }
        });
    }

    private void updateNews(List<NewsBean.ResultBean.DataBean> dataBeans) {
        mNewsAdapt = new NewsAdapter(context, dataBeans);
        mBinding.rvNews.setAdapter(mNewsAdapt);
        mNewsAdapt.setOnItemClickListener(position -> {
            NewsBean.ResultBean.DataBean dataBean = dataBeans.get(position);
            if (TextUtils.equals(dataBean.getIs_content(),"1")){
                //跳转到详情页
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("news_url",dataBean.getUrl());
                startActivity(intent);
            }
        });
        refreshTime = DateUtil.getNowDateTime("MM-dd HH:mm");

        //mBinding.rpRefresh.initRefreshTime(refreshTime);
    }

    @Override
    public void onFail(String error) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mBinding.rpRefresh.setVisibility(View.GONE);
                mBinding.tvError.setVisibility(View.VISIBLE);
                mBinding.tvError.setText("暂时无法获取信息，原因如下："+error);
            }
        });
    }

    @Override
    public void refreshed() {
        //已经刷新更新数据
        updateNews();
    }


}
