package com.fwk.school4.network.api;

import android.app.Activity;

import com.fwk.school4.constant.Keyword;
import com.fwk.school4.model.StationFADAOBean;
import com.fwk.school4.utils.ToastUtil;

import java.io.IOException;


/**
 * Created by fanwenke on 16/11/22.
 * 到站接口
 */

public class CarDZNetWork extends BaseNetWork {

    private static Activity mActivity;

    public static CarDZNetWork newInstance(Activity activity) {
        mActivity = activity;
        return new CarDZNetWork();
    }

    private CarDZNetWork() {
        initURL();
    }

    @Override
    public void onSuccess(Object cla, int flag) {
        if (flag == Keyword.FLAGDAOZHAN) {
            if (cla != null) {


                listener.NetWorkSuccess(Keyword.FLAGDAOZHAN);
            }
        }
    }

    @Override
    public void onFailure(IOException e) {
        listener.NetWorkError(Keyword.FLAGDAOZHANERROR);
    }
}
