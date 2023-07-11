package com.example.informationview.viewmodel;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.informationview.bean.LifeResultBean;
import com.example.informationview.bean.NewsBean;
import com.example.informationview.bean.WeatherBean;
import com.example.informationview.util.common.GsonUtil;
import com.example.informationview.util.common.HttpUtil;
import com.example.informationview.util.common.ThreadPool;

import java.util.List;
import java.util.Map;

public class ApiNetViewModel extends ViewModel {
    private static final String TAG = "ApiNetViewModel";
    private static ApiNetViewModel apiNetViewModel;
    private ResultCallback resultCallback;
    private WeatherCallback weatherCallback;
    private LifeCallback lifeCallback;

    public static ApiNetViewModel getInstance(){
        if (apiNetViewModel == null) {
            synchronized (ApiNetViewModel.class){
                apiNetViewModel = new ApiNetViewModel();
            }
        }
        return apiNetViewModel;
    }

    public void setResultCallback(ResultCallback resultCallback){
        this.resultCallback = resultCallback;
    }

    public void setWeatherCallback(WeatherCallback weatherCallback){
        this.weatherCallback = weatherCallback;
    }

    public void setLifeCallback(LifeCallback lifeCallback){
        this.lifeCallback = lifeCallback;
    }

    /**
     * 请求新闻接口
     * @param apiUrl
     * @param params
     */
    public void newsApiExecute(String apiUrl, Map<String,String> params){
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                String paramsStr = HttpUtil.urlencode(params);
                String response = HttpUtil.doGet(apiUrl, paramsStr);
                Log.i(TAG, "newsApiExecute: response="+response);
                if (!TextUtils.isEmpty(response)){
                    NewsBean newsBean = GsonUtil.parserJsonToArrayBean(response, NewsBean.class);
                    NewsBean.ResultBean result = newsBean.getResult();
                    if (newsBean.getError_code()==0){
                        List<NewsBean.ResultBean.DataBean> data = result.getData();
                        if (resultCallback != null) {
                            resultCallback.onSuccess(data);
                        }
                    }else{
                        if (resultCallback != null) {
                            resultCallback.onFail("error_code="+newsBean.getError_code()+",reason="+newsBean.getReason());
                        }
                    }
                }else{
                    if (resultCallback != null) {
                        resultCallback.onFail("请求数据失败！");
                    }
                }

            }
        }).start();*/
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String paramsStr = HttpUtil.urlencode(params);
                String response = HttpUtil.doGet(apiUrl, paramsStr);
                Log.i(TAG, "newsApiExecute: response="+response);
                if (!TextUtils.isEmpty(response)){
                    NewsBean newsBean = GsonUtil.parserJsonToArrayBean(response, NewsBean.class);
                    NewsBean.ResultBean result = newsBean.getResult();
                    if (newsBean.getError_code()==0){
                        List<NewsBean.ResultBean.DataBean> data = result.getData();
                        if (resultCallback != null) {
                            resultCallback.onSuccess(data);
                        }
                    }else{
                        if (resultCallback != null) {
                            resultCallback.onFail("error_code="+newsBean.getError_code()+",reason="+newsBean.getReason());
                        }
                    }
                }else{
                    if (resultCallback != null) {
                        resultCallback.onFail("请求数据失败！");
                    }
                }
            }
        };
        ThreadPool.getInstance().getThreadPoolExecutor().execute(runnable);
    }

    public void weatherExecute(String apiUrl,Map<String,String> params){
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                String paramsStr = HttpUtil.urlencode(params);
                String response = HttpUtil.doGet(apiUrl, paramsStr);
                Log.i(TAG, "weatherExecute: response="+response);
                if (!TextUtils.isEmpty(response)){
                    WeatherBean weatherBean = GsonUtil.parserJsonToArrayBean(response, WeatherBean.class);
                    if (weatherBean.getError_code()==0){
                        WeatherBean.ResultBean resultBean = weatherBean.getResult();
                        if (weatherCallback != null) {
                            weatherCallback.onSuccess(resultBean);
                        }
                    }else{
                        if (weatherCallback != null) {
                            weatherCallback.onFail("查询失败！原因是："+weatherBean.getReason());
                        }
                    }

                }else{
                    if (weatherCallback != null) {
                        weatherCallback.onFail("查询失败！");
                    }
                }
            }
        }).start();*/
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String paramsStr = HttpUtil.urlencode(params);
                String response = HttpUtil.doGet(apiUrl, paramsStr);
                Log.i(TAG, "weatherExecute: response="+response);
                if (!TextUtils.isEmpty(response)){
                    WeatherBean weatherBean = GsonUtil.parserJsonToArrayBean(response, WeatherBean.class);
                    if (weatherBean.getError_code()==0){
                        WeatherBean.ResultBean resultBean = weatherBean.getResult();
                        if (weatherCallback != null) {
                            weatherCallback.onSuccess(resultBean);
                        }
                    }else{
                        if (weatherCallback != null) {
                            weatherCallback.onFail("查询失败！原因是："+weatherBean.getReason());
                        }
                    }

                }else{
                    if (weatherCallback != null) {
                        weatherCallback.onFail("查询失败！");
                    }
                }
            }
        };
        ThreadPool.getInstance().getThreadPoolExecutor().execute(runnable);
    }

    public void lifeExecute(String apiUrl,Map<String,String> params){
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                String paramsStr = HttpUtil.urlencode(params);
                String response = HttpUtil.doGet(apiUrl, paramsStr);
                Log.i(TAG, "lifeExecute: response="+response);
                if (!TextUtils.isEmpty(response)){
                    LifeResultBean lifeResultBean = GsonUtil.parserJsonToArrayBean(response, LifeResultBean.class);
                    if (lifeResultBean.getError_code()==0){
                        LifeResultBean.ResultBean resultBean = lifeResultBean.getResult();
                        if (lifeCallback != null) {
                            lifeCallback.onSuccess(resultBean);
                        }
                    }else{
                        if (lifeCallback != null) {
                            lifeCallback.onFail("查询失败！原因是："+lifeResultBean.getReason());
                        }
                    }

                }else{
                    if (lifeCallback != null) {
                        lifeCallback.onFail("查询失败！");
                    }
                }
            }
        }).start();*/
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String paramsStr = HttpUtil.urlencode(params);
                String response = HttpUtil.doGet(apiUrl, paramsStr);
                Log.i(TAG, "lifeExecute: response="+response);
                if (!TextUtils.isEmpty(response)){
                    LifeResultBean lifeResultBean = GsonUtil.parserJsonToArrayBean(response, LifeResultBean.class);
                    if (lifeResultBean.getError_code()==0){
                        LifeResultBean.ResultBean resultBean = lifeResultBean.getResult();
                        if (lifeCallback != null) {
                            lifeCallback.onSuccess(resultBean);
                        }
                    }else{
                        if (lifeCallback != null) {
                            lifeCallback.onFail("查询失败！原因是："+lifeResultBean.getReason());
                        }
                    }

                }else{
                    if (lifeCallback != null) {
                        lifeCallback.onFail("查询失败！");
                    }
                }
            }
        };
        ThreadPool.getInstance().getThreadPoolExecutor().execute(runnable);
    }


    public interface ResultCallback{
        void onSuccess(List<NewsBean.ResultBean.DataBean> dataBeans);
        void onFail(String error);
    }

    public interface WeatherCallback{
        void onSuccess(WeatherBean.ResultBean resultBean);
        void onFail(String error);
    }

    public interface LifeCallback{
        void onSuccess(LifeResultBean.ResultBean resultBean);
        void onFail(String error);
    }
}
