package com.fwk.school4.network.api;

import android.app.Activity;

import com.fwk.school4.constant.Keyword;
import com.fwk.school4.model.StationFADAOBean;
import com.fwk.school4.utils.ToastUtil;

import java.io.IOException;


/**
 * Created by fanwenke on 16/11/22.
 * 发车接口
 */

public class CarFCNetWork extends BaseNetWork {

    private static Activity mActivity;
    private static int mFlag;

    public static CarFCNetWork newInstance(Activity activity){
        mActivity = activity;
        return new CarFCNetWork();
    }

    private CarFCNetWork(){
        initURL();
    }
    public void getFlag(int flag){
        mFlag = flag;
    }
    @Override
    public void onSuccess(Object cla, int flag) {
        if (flag == Keyword.FLAGFACHE){
            if (cla != null){

                StationFADAOBean fadaoBean = (StationFADAOBean) cla;

                listener.NetWorkSuccess(Keyword.FLAGFACHE);
            }
        } else if (flag == Keyword.FLAGFACHE1){
            if (cla != null){

                StationFADAOBean fadaoBean = (StationFADAOBean) cla;

                listener.NetWorkSuccess(Keyword.FLAGFACHE1);
            }
        }
    }

    @Override
    public void onFailure(IOException e) {
        if (mFlag == Keyword.FLAGFACHE) {
            listener.NetWorkError(Keyword.FLAGFACHEERROR);
        } else if (mFlag == Keyword.FLAGFACHE1){
            listener.NetWorkError(Keyword.FLAGFACHEERROR1);
        }
    }
}
