package com.example.informationview.uis.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.cy.tablayoutniubility.FragPageAdapterVp2;
import com.cy.tablayoutniubility.TabAdapter;
import com.cy.tablayoutniubility.TabAdapterNoScroll;
import com.cy.tablayoutniubility.TabMediatorVp2;
import com.cy.tablayoutniubility.TabMediatorVp2NoScroll;
import com.cy.tablayoutniubility.TabViewHolder;
import com.example.informationview.R;
import com.example.informationview.bean.NewsBean;
import com.example.informationview.databinding.FragmentNewsBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

public class NewsFragment extends BaseFragment{

    private static final String TAG = "NewsFragment";
    private static NewsFragment fragment = null;
    private com.example.informationview.databinding.FragmentNewsBinding mBinding;
    private Context mContext;
    private LinkedHashMap<String, String> mTopic;
    private ArrayList<String> mTitle;
    private FragPageAdapterVp2<String> adapterVp2;
    private TabAdapter<String> tabAdapter;


    public static NewsFragment getInstance(){
        Log.i(TAG, "getInstance: fragment="+fragment);
        if (fragment == null) {
            synchronized (NewsFragment.class){
                fragment = new NewsFragment();
            }
        }
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mBinding = FragmentNewsBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    protected void initData() {
        initTopic();


    }

    private void initTopic() {
        mTopic = new LinkedHashMap<>();
        mTopic.put("推荐","top");
        mTopic.put("国内","guonei");
        mTopic.put("国际","guoji");
        mTopic.put("娱乐","yule");
        mTopic.put("体育","tiyu");
        mTopic.put("军事","junshi");
        mTopic.put("科技","junshi");
        mTopic.put("财经","caijing");
        mTopic.put("游戏","youxi");
        mTopic.put("汽车","qiche");
        mTopic.put("健康","jiankang");
        mTitle = new ArrayList<>(mTopic.keySet());
    }

    @Override
    protected void initView() {
        adapterVp2 = new FragPageAdapterVp2<String>(requireActivity()) {
            @Override
            public void bindDataToTab(TabViewHolder holder, int position, String bean, boolean isSelected) {
                TextView tv_item = holder.getView(R.id.tv_item);
                if (isSelected) {
                    tv_item.setTextColor(0xffe45540);
                    tv_item.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                } else {
                    tv_item.setTextColor(0xff444444);
                    tv_item.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                }
                tv_item.setText(bean);
            }

            @Override
            public int getTabLayoutID(int position, String bean) {
                return R.layout.item_tab;
            }

            @Override
            public Fragment createFragment(String bean, int position) {
                return NewsTabFragment.getInstance(mTopic.get(bean));
            }


        };
        tabAdapter = new TabMediatorVp2<String>(mBinding.tabLayout,
                mBinding.viewPager).setAdapter(adapterVp2);
        tabAdapter.add(mTitle);
        adapterVp2.add(mTitle);
    }

    @Override
    public void onDestroyView() {
        Log.i(TAG, "onDestroyView: newsFragment--destroy->");
        super.onDestroyView();
        mBinding = null;
    }
}
