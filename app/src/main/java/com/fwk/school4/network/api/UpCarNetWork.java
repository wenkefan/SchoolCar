package com.fwk.school4.network.api;

import android.app.Activity;

import com.fwk.school4.constant.Keyword;
import com.fwk.school4.model.UpDownCar;
import com.fwk.school4.utils.ToastUtil;

import java.io.IOException;


/**
 * Created by fanwenke on 16/11/22.
 * 上车接口
 */

public class UpCarNetWork extends BaseNetWork {

    private static Activity mActivity;

    public static UpCarNetWork newInstance(Activity activity){
        mActivity = activity;
        return new UpCarNetWork();
    }

    private UpCarNetWork(){
        initURL();
    }

    @Override
    public void onSuccess(Object cla, int flag) {
        if (flag == Keyword.FLAGUPCAR){
            if (cla != null){

                UpDownCar upDownCar = (UpDownCar) cla;

                listener.NetWorkSuccess(Keyword.FLAGUPCAR);
            }
        }
    }

    @Override
    public void onFailure(IOException e) {
        listener.NetWorkError(Keyword.XiaURL);
    }
}
