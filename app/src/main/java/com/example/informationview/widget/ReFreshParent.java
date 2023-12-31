package com.example.informationview.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Scroller;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.view.NestedScrollingParent;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.informationview.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReFreshParent extends LinearLayout implements NestedScrollingParent {
    private boolean isMeasured;
    //刷新的头部局
    private View mHeader;
    //RecycleView列表页
    private RecyclerView mContent;
    //头部局的高度
    private int mHeaderHeight;
    private Scroller mScroller;
    //进度条
    private ImageView mIvRefresh;
    //刷新提示
    private TextView mTvTip;
    //指示箭头
    private ImageView mArrow;
    //上次刷新的时间
    private long mLastTime;
    private TextView mTvTime;
    private ValueAnimator mUpAnim;
    private ValueAnimator mDownAnim;
    private ObjectAnimator mRefreshAnim;

    private enum STATUS {
        //刷新中,下拉刷新，释放刷新
        REFRESHING, PULLREFRESH, RELEASEREFRESH
    }

    //当前刷新状态
    private STATUS mStatus = STATUS.PULLREFRESH;
    private STATUS mLastStatus;


    public ReFreshParent(Context context) {
        this(context, null);
    }

    public ReFreshParent(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        View view = LayoutInflater.from(context).inflate(R.layout.rv_header, this, false);
        mIvRefresh = view.findViewById(R.id.iv_refresh);
        mTvTip = view.findViewById(R.id.tv_tip);
        mArrow = view.findViewById(R.id.iv_arrow);
        mTvTime = view.findViewById(R.id.tv_time);

        addView(view);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mHeader.layout(0, -mHeaderHeight, getWidth(), 0);
        mContent.layout(0, 0, getWidth(), getBottom());
       // LogUtils.LogE("getBottom =" + getBottom());
    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes) {
        //当前状态不是正在刷新中当前布局就可以滑动
        return mStatus != STATUS.REFRESHING;

    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed) {
        //上滑头部局逐渐隐藏
        boolean hiddenRefresh = dy > 0 && getScrollY() <= 0;
        //下滑头部局逐渐出现
        boolean showRefresh = dy < 0 && getScrollY() >= -mHeaderHeight && !ViewCompat.canScrollVertically(target, -1);

        if (getScrollY() >= -mHeaderHeight / 2) {
            mStatus = STATUS.PULLREFRESH;
        } else {
            mStatus = STATUS.RELEASEREFRESH;
        }
        refreshByStatus(mStatus);

        if (hiddenRefresh || showRefresh) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!isMeasured) {
            mHeader = getChildAt(0);
            mContent = (RecyclerView) getChildAt(1);
            measureChildren(widthMeasureSpec, heightMeasureSpec);
            //获取头部局的高度
            mHeaderHeight = mHeader.getMeasuredHeight();
            isMeasured = true;
            //recycleView手抬起时的监听
            mContent.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {

                        if (getScrollY() <= -mHeaderHeight / 2) {
                            mStatus = STATUS.REFRESHING;
                            refreshByStatus(mStatus);
                        } else {
                            scrollTo(0, 0);
                        }
                    }
                    return false;
                }
            });

            mDownAnim = ValueAnimator.ofFloat(180, 360);
            mDownAnim.setDuration(500);
            mDownAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {

                    mArrow.setRotation((Float) animation.getAnimatedValue());
                }
            });

            mUpAnim = ValueAnimator.ofFloat(0, 180);
            mUpAnim.setDuration(500);
            mUpAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mArrow.setRotation((Float) animation.getAnimatedValue());
                }
            });
        }
    }

    /**
     * 根据状态去改变
     */
    private void refreshByStatus(STATUS status) {
        if (mLastStatus != status) {
            switch (status) {
                case PULLREFRESH:
                    if (mLastStatus != null) {
                        mTvTip.setText("下拉刷新");
                        mDownAnim.start();
                    }

                    break;
                case REFRESHING:
                    mTvTip.setText("正在刷新");
                    mIvRefresh.setVisibility(VISIBLE);
                    startRefresh();
                    mArrow.setVisibility(GONE);
                    //mLastTime = System.currentTimeMillis();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mIvRefresh.setVisibility(GONE);
                            stopRefresh();
                            mArrow.setVisibility(VISIBLE);
                            scrollTo(0, 0);
                            mStatus = STATUS.PULLREFRESH;
                            mLastStatus = null;
                            //mTvTime.setText("上次更新:" + formartTime(mLastTime));
                            //刷新完毕
                            if (mRefreshCompleteListener != null)
                                mRefreshCompleteListener.refreshed();
                        }
                    }, 1500);
                    break;

                case RELEASEREFRESH:
                    mTvTip.setText("释放刷新");
                    mUpAnim.start();
                    break;
            }
            mLastStatus = status;
        }

    }

    private void startRefresh(){
        mRefreshAnim = ObjectAnimator.ofFloat(mIvRefresh, "rotation", 0f, 360f);
        mRefreshAnim.setDuration(800);
        mRefreshAnim.setRepeatCount(ValueAnimator.INFINITE);
        mRefreshAnim.setInterpolator(new LinearInterpolator());
        mRefreshAnim.start();
    }

    private void stopRefresh(){
        mRefreshAnim.cancel();    }

    private String formartTime(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y <= -mHeaderHeight) {
            y = -mHeaderHeight;
        } else if (y >= 0) {
            y = 0;
        }
        super.scrollTo(x, y);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

    public void initRefreshTime(String time){
        mTvTime.setText("上次更新："+time);
    }

    public interface RefreshCompleteListener {
        void refreshed();
    }

    private RefreshCompleteListener mRefreshCompleteListener;

    public void setRefreshCompleteListener(RefreshCompleteListener refreshCompleteListener) {
        mRefreshCompleteListener = refreshCompleteListener;
    }
}
