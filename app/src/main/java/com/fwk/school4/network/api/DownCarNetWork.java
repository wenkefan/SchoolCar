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

public class DownCarNetWork extends BaseNetWork {

    private static Activity mActivity;

    public static DownCarNetWork newInstance(Activity activity){
        mActivity = activity;
        return new DownCarNetWork();
    }

    private DownCarNetWork(){
        initURL();
    }

    @Override
    public void onSuccess(Object cla, int flag) {
        if (flag == Keyword.FLAGDOWNCAR){
            if (cla != null){

                UpDownCar upDownCar = (UpDownCar) cla;

                listener.NetWorkSuccess(Keyword.FLAGDOWNCAR);
            }
        }
    }

    @Override
    public void onFailure(IOException e) {
        listener.NetWorkError(Keyword.ShangURL);
    }
}
