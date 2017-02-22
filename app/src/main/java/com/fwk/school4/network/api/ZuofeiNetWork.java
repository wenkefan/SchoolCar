package com.fwk.school4.network.api;

import android.app.Activity;

import com.fwk.school4.listener.NetWorkListener;

import java.io.IOException;

/**
 * Created by fanwenke on 2017/2/21.
 */

public class ZuofeiNetWork extends BaseNetWork {

    private static Activity mActivity;

    public static ZuofeiNetWork newInstance(Activity activity) {
        mActivity = activity;
        return new ZuofeiNetWork();
    }

    private ZuofeiNetWork() {
        initURL();
    }

    @Override
    public void setNetWorkListener(NetWorkListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSuccess(Object cla, int flag) {
        listener.NetWorkSuccess(flag);
    }

    @Override
    public void onFailure(IOException e) {

    }
}
