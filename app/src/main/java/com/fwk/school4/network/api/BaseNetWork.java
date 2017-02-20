package com.fwk.school4.network.api;


import com.fwk.school4.network.OKHttp;
import com.fwk.school4.listener.NetWorkListener;
import com.fwk.school4.listener.OnSucceedListener;
import com.fwk.school4.utils.SharedPreferencesUtils;
import com.fwk.school4.utils.SharedPreferencesUtils2;

import java.io.IOException;

import testlibrary.hylk.com.loginlibrary.okhttp.LK_OkHttpUtil;
import testlibrary.hylk.com.loginlibrary.okhttp.OkHttpUtil;

/**
 * Created by fanwenke on 16/11/21.
 */

public abstract class BaseNetWork implements LK_OkHttpUtil.OnRequestListener {
    public NetWorkListener listener;


    public void setNetWorkListener(NetWorkListener listener){
        this.listener = listener;
    };
    public LK_OkHttpUtil okHttpUtil;
    public SharedPreferencesUtils sp;
    public SharedPreferencesUtils2 spData;

    public void initURL(){
        sp = new SharedPreferencesUtils();
        spData = new SharedPreferencesUtils2();
        okHttpUtil = LK_OkHttpUtil.getOkHttpUtil();

    }

    public void setUrl(int Flag, String url, Class cla){

        okHttpUtil.setOnRequestListener(this);
        okHttpUtil.get(url,cla,Flag);

    }

    @Override
    public abstract void onSuccess(Object cla, int flag);

    @Override
    public void onError(int i, Exception e) {

    }

    @Override
    public abstract void onFailure(IOException e);
}
