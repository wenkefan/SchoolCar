package com.fwk.school4.lineData;

import com.fwk.school4.constant.Keyword;
import com.fwk.school4.listener.NetWorkListener;
import com.fwk.school4.network.api.BaseNetWork;

import java.io.IOException;

/**
 * Created by fanwenke on 2017/2/20.
 */

public class FaDaoUrl extends BaseNetWork {

    public FaDaoUrl(){
        initURL();
    }

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
