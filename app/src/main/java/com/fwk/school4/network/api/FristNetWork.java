package com.fwk.school4.network.api;

import android.app.Activity;

import com.fwk.school4.constant.Keyword;
import com.fwk.school4.model.FristFaChe;
import com.fwk.school4.listener.NetWorkListener;
import com.fwk.school4.utils.ToastUtil;

import java.io.IOException;


/**
 * Created by fanwenke on 16/11/22.
 * 发送班次
 */

public class FristNetWork extends BaseNetWork {

    private static Activity mActivity;

    public static FristNetWork newInstance(Activity activity) {
        mActivity = activity;
        return new FristNetWork();
    }

    private FristNetWork() {
        initURL();
    }

    @Override
    public void setNetWorkListener(NetWorkListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSuccess(Object cla, int flag) {
        if (flag == Keyword.FLAGFIRSTFACHE) {

            if (cla != null) {

                final FristFaChe faChe = (FristFaChe) cla;

                try {
                    int paichedanhao = faChe.getRerurnValue();
                    sp.setInt(Keyword.SP_PAICHEDANHAO, paichedanhao);
                } catch (Exception o) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(faChe.getMessage());
                        }
                    });
                }

                listener.NetWorkSuccess(Keyword.FLAGFIRSTFACHE);

            }
        }
    }

    @Override
    public void onFailure(IOException e) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.show("网络错误");
            }
        });
    }

}
