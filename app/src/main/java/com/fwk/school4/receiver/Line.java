package com.fwk.school4.receiver;

import android.os.Handler;
import android.os.Message;

import com.fwk.school4.constant.Keyword;
import com.fwk.school4.lineData.FaDaoUrl;
import com.fwk.school4.lineData.UpDownUrl;
import com.fwk.school4.listener.NetWorkListener;
import com.fwk.school4.model.StationFADAOBean;
import com.fwk.school4.model.UpDownCar;
import com.fwk.school4.utils.LogUtils;
import com.fwk.school4.utils.SharedPreferencesUtils;

import java.util.List;

/**
 * Created by fanwenke on 2017/2/20.
 */

public class Line implements NetWorkListener {

    private SharedPreferencesUtils sp;

    private List<String> list;
    private List<String> list2;

    public Line() {
        sp = new SharedPreferencesUtils();
    }

    public void initData() {
        fadaoData();
        shangxiacheData();
    }

    @Override
    public void NetWorkSuccess(int Flag) {
        switch (Flag) {
            case Keyword.FLAGFIRSTFACHEError:
                list.remove(0);
                sp.saveToShared(Keyword.LIXIANFASONGCARURL,list);
                handler.sendEmptyMessage(Keyword.FLAGFIRSTFACHEError);
                break;
            case Keyword.FLAGDOWNCAR:
                list2.remove(0);
                sp.saveToShared(Keyword.LIXIANSHANGXIAURL,list2);
                handler.sendEmptyMessage(Keyword.FLAGDOWNCAR);
                break;
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Keyword.FLAGFIRSTFACHEError:
                    fadaoData();
                    break;
                case Keyword.FLAGDOWNCAR:
                    shangxiacheData();
                    break;
            }
        }
    };


    private void fadaoData(){
        list = (List<String>) sp.queryForSharedToObject(Keyword.LIXIANFASONGCARURL);
        if (list != null && list.size() > 0){
            fadaoURL(list.get(0));
        }
    }

    private void fadaoURL(String url){
        FaDaoUrl daoUrl = new FaDaoUrl();
        daoUrl.setNetWorkListener(this);
        daoUrl.setUrl(Keyword.ShangURL,url,StationFADAOBean.class);
    }

    private void shangxiacheData(){
        list2 = (List<String>) sp.queryForSharedToObject(Keyword.LIXIANSHANGXIAURL);
        LogUtils.d(list2.size() + "------size");
        if (list2 != null && list2.size() > 0){
            shangxiaURL(list2.get(0));
        }
    }
    private void shangxiaURL(String url){
        UpDownUrl upDownUrl = new UpDownUrl();
        upDownUrl.setNetWorkListener(this);
        upDownUrl.setUrl(Keyword.ShangURL,url,UpDownCar.class);
    }

    @Override
    public void NetWorkError(int Flag) {

    }
}
