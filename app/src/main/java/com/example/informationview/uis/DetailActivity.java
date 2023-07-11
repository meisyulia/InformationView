package com.example.informationview.uis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.informationview.R;
import com.example.informationview.databinding.ActivityDetailBinding;
import com.example.informationview.databinding.LayoutTopTitleBinding;
import com.example.informationview.util.common.StatusBarUtil;

public class DetailActivity extends BaseActivity {

    private com.example.informationview.databinding.ActivityDetailBinding mBinding;
    private com.example.informationview.databinding.LayoutTopTitleBinding mTitleBinding;
    private String mNewsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_detail);
        mBinding = ActivityDetailBinding.inflate(getLayoutInflater());
        mTitleBinding = LayoutTopTitleBinding.bind(mBinding.getRoot());
        setContentView(mBinding.getRoot());
        StatusBarUtil.setStatusBarColor(this,getResources().getColor(R.color.white));
        initData();
        initView();
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mNewsUrl = intent.getStringExtra("news_url");
    }

    @Override
    protected void initView() {
        mTitleBinding.tvTitle.setText("新闻详情");
        mTitleBinding.ivBack.setOnClickListener(v->{
            onBackPressed();
        });
        mBinding.webView.loadUrl(mNewsUrl);
    }
}