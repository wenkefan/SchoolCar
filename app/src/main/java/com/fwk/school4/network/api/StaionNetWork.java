package com.fwk.school4.network.api;

import android.app.Activity;


import com.fwk.school4.constant.Keyword;
import com.fwk.school4.model.StaBean;
import com.fwk.school4.model.StationBean;
import com.fwk.school4.model.StationModeBean;
import com.fwk.school4.listener.NetWorkListener;
import com.fwk.school4.utils.StationMode;
import com.fwk.school4.utils.ToastUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanwenke on 16/11/21.
 * 站点接口
 */

public class StaionNetWork extends BaseNetWork {


    private static Activity mActivity;
    private List<StationBean.RerurnValueBean> list = new ArrayList<>();


    public static StaionNetWork newInstance(Activity activity) {
        mActivity = activity;
        return new StaionNetWork();
    }

    private StaionNetWork() {
        initURL();
    }

    @Override
    public void setNetWorkListener(NetWorkListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSuccess(Object cla, int flag) {
        if (flag == Keyword.FLAGSTATION) {

            if (cla != null) {

                StationBean bean = (StationBean) cla;
                try {

                    for (StationBean.RerurnValueBean bean1 : bean.getRerurnValue()) {
                        StationBean.RerurnValueBean valueBean = bean1;
                        String modeDown = StationMode.setDown(bean1.getStationName());
                        String modeUp = StationMode.setUp(bean1.getStationName());
                        valueBean.setStationiddown(modeDown);
                        valueBean.setStationidup(modeUp);
                        list.add(valueBean);
                    }

                } catch (Exception o) {

                    final StationBean finalBean = bean;
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.show(finalBean.getMessage());
                        }
                    });

                }
                List<StaBean> staBeen = new ArrayList<>();
                for (int i = 0; i < bean.getRerurnValue().size();i++) {
                    StationBean.RerurnValueBean bean1 = bean.getRerurnValue().get(i);
                    StaBean staBean1 = new StaBean();
                    staBean1.setId(bean1.getStationId());
                    staBean1.setName(StationMode.setDown(bean1.getStationName()));
                    staBean1.setStrid(bean1.getStationId() + "01");
                    staBean1.setType(1);
                    staBean1.setOrder(i * 2 + 1);
                    staBeen.add(staBean1);
                    StaBean staBean2 = new StaBean();
                    staBean2.setId(bean1.getStationId());
                    staBean2.setName(StationMode.setUp(bean1.getStationName()));
                    staBean2.setStrid(bean1.getStationId() + "02");
                    staBean2.setType(2);
                    staBean2.setOrder(i * 2);
                    staBeen.add(staBean2);
                }

                sp.saveToShared(Keyword.SP_STATION_LIST, list);
                sp.saveToShared(Keyword.STAIDLIST, staBeen);
                listener.NetWorkSuccess(Keyword.FLAGSTATION);
                bean = null;

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
