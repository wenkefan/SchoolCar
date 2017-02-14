package com.fwk.school4.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fwk.school4.R;
import com.fwk.school4.constant.Keyword;
import com.fwk.school4.constant.SpLogin;
import com.fwk.school4.listener.NetWorkListener;
import com.fwk.school4.model.ChildBean;
import com.fwk.school4.model.FristFaChe;
import com.fwk.school4.model.StaBean;
import com.fwk.school4.model.UpDownCar;
import com.fwk.school4.network.HTTPURL;
import com.fwk.school4.network.api.EndNetWork;
import com.fwk.school4.network.api.UpCarNetWork;
import com.fwk.school4.ui.adapter.BaseRecyclerAdapter;
import com.fwk.school4.ui.adapter.ResidueAdapter;
import com.fwk.school4.utils.GetDateTime;
import com.fwk.school4.utils.LogUtils;
import com.fwk.school4.utils.SharedPreferencesUtils;
import com.fwk.school4.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by fanwenke on 2017/2/10.
 * 查看车上剩余的人数
 */

public class ResidueActivity extends NFCBaseActivity implements BaseRecyclerAdapter.OnItemListener, NetWorkListener {

    @InjectView(R.id.title_tv)
    TextView title;
    @InjectView(R.id.residue_view)
    RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;

    private ResidueAdapter adapter;

    private List<StaBean> staBeanList;

    private Map<String, List<ChildBean.RerurnValueBean>> map;

    private SharedPreferencesUtils sp = new SharedPreferencesUtils();

//    private SharedPreferencesUtils2 spData = new SharedPreferencesUtils2();

    private List<ChildBean.RerurnValueBean> residueList;

    private int selectItme;

    private int stationId;

    @Override
    public int getLayoutId() {
        return R.layout.residue_activity;
    }

    @Override
    public void init() {

        title.setText("车上剩余幼儿");
        residueList();
        Intent intent = getIntent();
        stationId = intent.getIntExtra(Keyword.SELECTITME, 0);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ResidueAdapter(residueList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemListener(this);
    }

    private void residueList() {
        staBeanList = (List<StaBean>) sp.queryForSharedToObject(Keyword.SELECTSTA);
        map = (Map<String, List<ChildBean.RerurnValueBean>>) sp.queryForSharedToObject(Keyword.MAPLIST);
        residueList = new ArrayList<>();
        for (StaBean bean : staBeanList) {
            if (bean.getOrder() % 2 == 0) {
                for (ChildBean.RerurnValueBean rerurnValueBean : map.get(bean.getStrid())) {
                    if (rerurnValueBean.getSelectid() == 0) {
                        residueList.add(rerurnValueBean);
                    }
                }
            }
        }
    }

    @Override
    public void setOnItemListener(int position, BaseRecyclerAdapter.ClickableViewHolder holder) {
        this.selectItme = position;
        Intent intent = new Intent(this, XiacheActivity.class);
        ChildBean.RerurnValueBean bean = residueList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        intent.putExtras(bundle);
        intent.putExtra(Keyword.JUMPPOSITION, true);
        startActivityForResult(intent, 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 4) {
                //下车
                xiachefenzu(data);
            }
        }
    }

    private void xiachefenzu(Intent data) {
        ChildBean.RerurnValueBean bean;
        int childPosition = 0;
        childPosition = data.getIntExtra(Keyword.SP_SELECT_ID, 0);
        bean = residueList.get(selectItme);
        if (bean.getSelectid() == 5) {
            ToastUtil.show(bean.getChildName() + "已下车");
            return;
        }
        /**
         * 字段：派车单号、幼儿编号、站点、时间、状态、kgid、上下车类型（1、上车；2、下车）
         */
        String url = String.format(
                HTTPURL.API_STUDENT_OPEN_DOWN,
                sp.getInt(Keyword.SP_PAICHEDANHAO),
                bean.getChildId(),
                selectItme,
                GetDateTime.getdatetime(),
                childPosition,
                SpLogin.getKgId(),
                2);
        LogUtils.d("下车接口：" + url);
        UpCarNetWork upCarNetWork = UpCarNetWork.newInstance(this);
        upCarNetWork.setNetWorkListener(this);
        upCarNetWork.setUrl(Keyword.FLAGUPCAR, url, UpDownCar.class);
    }

    @Override
    public void NetWorkSuccess(int Flag) {
        switch (Flag) {
            case Keyword.FLAGUPCAR:
                handler.sendEmptyMessage(Keyword.FLAGUPCAR);
                break;
            case Keyword.FLAGENDDAOZHAN:
                handler.sendEmptyMessage(Keyword.FLAGENDDAOZHAN);
                break;
        }
    }

    @Override
    public void NetWorkError(int Flag) {

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Keyword.FLAGUPCAR:
                    residueList.get(selectItme).setSelectid(5);
                    adapter.getData(residueList);
                    adapter.notifyDataSetChanged();
                    ToastUtil.show(residueList.get(selectItme).getChildName() + "下车");
                    break;
                case Keyword.FLAGENDDAOZHAN:
                    ToastUtil.show("结束了");
                    sp.removData();
                    sp.removData();
                    finish();
                    break;
            }
        }
    };

    @OnClick(R.id.btn_jieshu)
    public void onClick(View view){
        if (residueNumber()){
            ToastUtil.show("还有学生未下车");
        } else {
            String url1 = String.format(HTTPURL.API_OPEN, sp.getInt(Keyword.SP_PAICHEDANHAO), SpLogin.getKgId(), GetDateTime.getdatetime(), 2, SpLogin.getWorkerExtensionId());
            LogUtils.d("结束URL：" + url1);
            EndNetWork endNetWork = EndNetWork.newInstance(this);
            endNetWork.setNetWorkListener(this);
            endNetWork.setUrl(Keyword.FLAGENDDAOZHAN, url1, FristFaChe.class);
        }
    }



    private boolean residueNumber(){
        for (ChildBean.RerurnValueBean bean : residueList){
            if (bean.getSelectid() == 0){
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String CarId = readICCardNo(intent);
        LogUtils.d("CarId:" + CarId);
        for (int i = 0; i < residueList.size(); i++){
            if (residueList.get(i).getSACardNo().equals(CarId)){
                if (residueList.get(i).getSelectid() == 5){
                    ToastUtil.show(residueList.get(i).getChildName() + "已下车");
                } else {
                    String url = String.format(
                            HTTPURL.API_STUDENT_OPEN_DOWN,
                            sp.getInt(Keyword.SP_PAICHEDANHAO),
                            residueList.get(i).getChildId(),
                            selectItme,
                            GetDateTime.getdatetime(),
                            5,
                            SpLogin.getKgId(),
                            2);
                    LogUtils.d("下车接口：" + url);
                    UpCarNetWork upCarNetWork = UpCarNetWork.newInstance(this);
                    upCarNetWork.setNetWorkListener(this);
                    upCarNetWork.setUrl(Keyword.FLAGUPCAR, url, UpDownCar.class);
                }
            }
        }
    }
}
