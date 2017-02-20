package com.fwk.school4.ui.Song;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fwk.school4.R;
import com.fwk.school4.constant.Keyword;
import com.fwk.school4.constant.SpLogin;
import com.fwk.school4.listener.NetWorkListener;
import com.fwk.school4.model.ChildBean;
import com.fwk.school4.model.FristFaChe;
import com.fwk.school4.model.StaBean;
import com.fwk.school4.model.StationBean;
import com.fwk.school4.model.StationFADAOBean;
import com.fwk.school4.model.UpDownCar;
import com.fwk.school4.network.HTTPURL;
import com.fwk.school4.network.api.CarDZNetWork;
import com.fwk.school4.network.api.DownCarNetWork;
import com.fwk.school4.network.api.EndNetWork;
import com.fwk.school4.network.api.UpCarNetWork;
import com.fwk.school4.ui.Jie.JieChildListActivity2;
import com.fwk.school4.ui.NFCBaseActivity;
import com.fwk.school4.ui.ShangcheActivity;
import com.fwk.school4.ui.XiacheActivity;
import com.fwk.school4.ui.adapter.JieChildListAdapter2;
import com.fwk.school4.utils.ChildData;
import com.fwk.school4.utils.GetDateTime;
import com.fwk.school4.utils.LogUtils;
import com.fwk.school4.utils.SharedPreferencesUtils;
import com.fwk.school4.utils.ToastUtil;
import com.fwk.school4.weight.MainDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by fanwenke on 16/12/23.
 */

public class SongChildListActivity2 extends NFCBaseActivity implements JieChildListAdapter2.OnItemAdapterListener, NetWorkListener {

    @InjectView(R.id.title_tv)
    TextView title;
    @InjectView(R.id.rv_recyle_activity)
    RecyclerView rv;
    @InjectView(R.id.btn_fache)
    Button btn;
    private String[] askForLeaveStatus = new String[]{"已上车", "病假", "事假", "家长接送"};
    private LinearLayoutManager manager;
    private JieChildListAdapter2 adapter;
    private SharedPreferencesUtils sp = new SharedPreferencesUtils();
//    private SharedPreferencesUtils2 sp = new SharedPreferencesUtils2();

    private Map<String, List<ChildBean.RerurnValueBean>> map;//幼儿map
    private StaBean staBean;//选中幼儿所在的站点
    private int mItem;//站点中幼儿的位置数
    private int position;
    private boolean jumpPosition;
    private int dingwei;//定位
    private List<StationBean.RerurnValueBean> stationlist;
    private boolean isJieShu = false;
    private int selStationID;

    private String FacheUrl;
    private String ShangcheUrl;
    private String XiacheUrl;

    public SongChildListActivity2() {

        stationlist = (List<StationBean.RerurnValueBean>) sp.queryForSharedToObject(Keyword.SP_STATION_LIST);
    }

    @Override
    public int getLayoutId() {
        return R.layout.jie_child_list2;
    }

    @Override
    public void init() {
        title.setText(R.string.song);

        Intent intent = getIntent();
        position = intent.getIntExtra(Keyword.STATIONPOSITION, 0);
        jumpPosition = intent.getBooleanExtra(Keyword.JUMPPOSITION, false);
        selStationID = intent.getIntExtra(Keyword.SELECTSTATIONID, -1);
        dingwei = intent.getIntExtra(Keyword.DINGWEI, 0);

        manager = new LinearLayoutManager(this);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(manager);
        adapter = new JieChildListAdapter2(selStationID);
        rv.setAdapter(adapter);
        adapter.setOnItemAdapterListener(this);
        rv.scrollToPosition(dingwei);
        if (position == stationlist.size() - 1) {
            btn.setText("结束");
            isJieShu = true;
        }
        if (!jumpPosition) {
            btn.setVisibility(View.GONE);
            title.setText(R.string.chakan);
        }
    }

    @Override
    public void setOnItemListener(StaBean staid, int position) {
        //手动选择幼儿状态
        staBean = staid;
        mItem = position;
        map = (Map<String, List<ChildBean.RerurnValueBean>>) sp.queryForSharedToObject(Keyword.MAPLIST);
        List<ChildBean.RerurnValueBean> list = map.get(staid.getStrid());
        ChildBean.RerurnValueBean bean = list.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        if (staid.getType() == 1) {
            Intent intent = new Intent(this, ShangcheActivity.class);
            intent.putExtras(bundle);
            intent.putExtra(Keyword.JUMPPOSITION, jumpPosition);
            startActivityForResult(intent, 3);
        }
        if (staid.getType() == 2) {
            Intent intent = new Intent(this, XiacheActivity.class);
            intent.putExtras(bundle);
            intent.putExtra(Keyword.JUMPPOSITION, jumpPosition);
            startActivityForResult(intent, 4);
        }
    }

    private int childPosition;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 3) {
                //上车重新分组
                shangchefenzu(true, data, null);
            } else if (requestCode == 4) {
                //下车重新分组
                xiachefenzu(true, data, null);
            }
        }
    }

    private void xiachefenzu(boolean b, Intent data, ChildBean.RerurnValueBean valueBean) {
        ChildBean.RerurnValueBean bean;
        if (b) {
            childPosition = data.getIntExtra(Keyword.SP_SELECT_ID, 0);
            bean = map.get(staBean.getStrid()).get(mItem);
        } else {
            bean = valueBean;
        }
        if (bean.getSelectid() == childPosition) {
            ToastUtil.show(bean.getChildName() + "已下车");
            return;
        }
        /**
         * 字段：派车单号、幼儿编号、站点、时间、状态、kgid、上下车类型（1、上车；2、下车）
         */
        XiacheUrl = String.format(
                HTTPURL.API_STUDENT_OPEN_DOWN,
                sp.getInt(Keyword.SP_PAICHEDANHAO),
                bean.getChildId(),
                staBean.getId(),
                GetDateTime.getdatetime(),
                childPosition,
                SpLogin.getKgId(),
                2);
        LogUtils.d("下车接口：" + XiacheUrl);
        UpCarNetWork upCarNetWork = UpCarNetWork.newInstance(this);
        upCarNetWork.setNetWorkListener(this);
        upCarNetWork.setUrl(Keyword.FLAGUPCAR, XiacheUrl, UpDownCar.class);
    }

    private void shangchefenzu(boolean b, Intent data, ChildBean.RerurnValueBean valueBean) {
        ChildBean.RerurnValueBean bean;
        if (b) {
            childPosition = data.getIntExtra(Keyword.SP_SELECT_ID, 0);
            bean = map.get(staBean.getStrid()).get(mItem);
        } else {
            bean = valueBean;
        }
        if (bean.getSelectid() == childPosition) {
            ToastUtil.show(bean.getChildName() + askForLeaveStatus[childPosition - 1]);
            return;
        }
        showDialog();
        /**
         * 字段：派车单号、幼儿编号、站点、时间、状态、kgid、上下车类型（1、上车；2、下车）
         */
        ShangcheUrl = String.format(
                HTTPURL.API_STUDENT_OPEN_DOWN,
                sp.getInt(Keyword.SP_PAICHEDANHAO),
                bean.getChildId(),
                staBean.getId(),
                GetDateTime.getdatetime(),
                childPosition,
                SpLogin.getKgId(),
                1);
        LogUtils.d("上车接口-----：" + ShangcheUrl);
        DownCarNetWork downCarNetWork = DownCarNetWork.newInstance(this);
        downCarNetWork.setNetWorkListener(this);
        downCarNetWork.setUrl(Keyword.FLAGDOWNCAR, ShangcheUrl, UpDownCar.class);

    }

    @OnClick(R.id.btn_fache)
    public void onClick(View view) {
        if (isJieShu) {
            /**
             * 发车字段为：班次编号、kgid、发车时间、类型(1发车、2停车)
             * 停车字段为：派车单号、kgid、发车时间、类型(1发车、2停车)
             */
            if (sp.getInt(Keyword.CARNUMBER) == 0) {
                showDialog();
                String url = String.format(HTTPURL.API_OPEN, sp.getInt(Keyword.SP_PAICHEDANHAO), SpLogin.getKgId(), GetDateTime.getdatetime(), 2, SpLogin.getWorkerExtensionId());
                LogUtils.d("结束URL：" + url);
                EndNetWork endNetWork = EndNetWork.newInstance(this);
                endNetWork.setNetWorkListener(this);
                endNetWork.setUrl(Keyword.FLAGENDDAOZHAN, url, FristFaChe.class);
            } else {
                ToastUtil.show("车上还有幼儿，请仔细检查");
            }
        } else {
            if (SurplusShangcheName() + SurplusXiacheName() != 0) {
                //有未上车或者未下车
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(false);
                builder.setTitle("警告").setMessage("还有学生没有上下车，是否发车？");
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        facheUrl();
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            } else {
                facheUrl();
            }
        }

    }

    private void facheUrl() {
        showDialog();
        FacheUrl = String.format(HTTPURL.API_PROCESS, SpLogin.getKgId(), stationlist.get(position).getStationId(),
                sp.getInt(Keyword.SP_PAICHEDANHAO), 2, GetDateTime.getdatetime());
        LogUtils.d("发车URL：" + FacheUrl);
        CarDZNetWork carDZNetWork = CarDZNetWork.newInstance(SongChildListActivity2.this);
        carDZNetWork.setNetWorkListener(SongChildListActivity2.this);
        carDZNetWork.setUrl(Keyword.FLAGDAOZHAN, FacheUrl, StationFADAOBean.class);
    }

    @Override
    public void NetWorkSuccess(int Flag) {
        switch (Flag) {
            case Keyword.FLAGDAOZHAN:
                handler.sendEmptyMessage(Keyword.FLAGDAOZHAN);
                break;
            case Keyword.FLAGENDDAOZHAN:
                handler.sendEmptyMessage(Keyword.FLAGENDDAOZHAN);
                break;
            case Keyword.FLAGDOWNCAR:
                handler.sendEmptyMessage(Keyword.FLAGDOWNCAR);
                break;
            case Keyword.FLAGUPCAR:
                handler.sendEmptyMessage(Keyword.FLAGUPCAR);
                break;
        }
    }

    @Override
    public void NetWorkError(int Flag) {
        switch (Flag) {
            case Keyword.FLAGDAOZHANERROR:
                List<String> url = (List<String>) sp.queryForSharedToObject(Keyword.LIXIANFASONGCARURL);
                if (url == null) {
                    url = new ArrayList<>();
                }
                url.add(FacheUrl);
                sp.saveToShared(Keyword.LIXIANFASONGCARURL, url);
                handler.sendEmptyMessage(Keyword.FLAGDAOZHAN);
                break;
            case Keyword.XiaURL:
                List<String> url2 = (List<String>) sp.queryForSharedToObject(Keyword.LIXIANSHANGXIAURL);
                if (url2 == null) {
                    url2 = new ArrayList<>();
                }
                url2.add(XiacheUrl);
                sp.saveToShared(Keyword.LIXIANSHANGXIAURL, url2);
                break;
            case Keyword.ShangURL:
                List<String> url3 = (List<String>) sp.queryForSharedToObject(Keyword.LIXIANSHANGXIAURL);
                if (url3 == null) {
                    url3 = new ArrayList<>();
                }
                url3.add(ShangcheUrl);
                sp.saveToShared(Keyword.LIXIANSHANGXIAURL, url3);
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            closeDialog();
            switch (msg.what) {
                case Keyword.FLAGDAOZHAN:
                    position++;
                    sp.setInt(Keyword.THISSATION, position);
                    sp.setboolean(Keyword.ISDAOZHAN, false);
                    Intent intent = new Intent(SongChildListActivity2.this, SongStationMapActivity.class);
                    intent.putExtra(Keyword.POTIONIT, -1);
                    startActivity(intent);
                    finish();
                    break;
                case Keyword.FLAGENDDAOZHAN:
                    ToastUtil.show("结束了");
                    sp.removData();
                    sp.removData();
                    finish();
                    break;
                case Keyword.FLAGDOWNCAR:
                    ChildData.setSongData(map, staBean, mItem, childPosition);
                    adapter.getData(selStationID);
                    adapter.notifyDataSetChanged();
                    break;
                case Keyword.FLAGUPCAR:
                    ChildData.setXiache(map, staBean, mItem, childPosition, 2);
                    adapter.getData(selStationID);
                    adapter.notifyDataSetChanged();
                    ToastUtil.show(map.get(staBean.getStrid()).get(mItem).getChildName() + "下车");
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (jumpPosition) {
            ToastUtil.show("正在等待上车...");
            return;
        }
        super.onBackPressed();
    }

    /**
     * 刷卡返回
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String CarId = readICCardNo(intent);
        LogUtils.d("CarId:" + CarId);
        staBean = ((List<StaBean>) sp.queryForSharedToObject(Keyword.SELECTSTA)).get(position);
        if (map == null) {
            map = (Map<String, List<ChildBean.RerurnValueBean>>) sp.queryForSharedToObject(Keyword.MAPLIST);
        }
        List<ChildBean.RerurnValueBean> shanglist = map.get(stationlist.get(position).getStationId() + "01");
        List<ChildBean.RerurnValueBean> xialist = map.get(stationlist.get(position).getStationId() + "02");
        boolean isCan = false;
        if (shanglist != null) {
            for (int i = 0; i < shanglist.size(); i++) {
                if (CarId.equals(shanglist.get(i).getSACardNo())) {
                    //请求操作接口
                    childPosition = 1;
                    shangchefenzu(false, null, shanglist.get(i));
                    isCan = true;
                    break;
                }
            }
        }
        if (xialist != null) {
            for (int i = 0; i < xialist.size(); i++) {
                if (CarId.equals(xialist.get(i).getSACardNo())) {
                    //请求操作接口
                    childPosition = 5;
                    xiachefenzu(false, null, xialist.get(i));
                    isCan = true;
                    break;
                }
            }
        }
        if (!isCan) {
            ToastUtil.show("当前站没有此学生");
        }
    }

    private int SurplusShangcheName() {
        if (map == null) {
            map = (Map<String, List<ChildBean.RerurnValueBean>>) sp.queryForSharedToObject(Keyword.MAPLIST);
        }
        List<ChildBean.RerurnValueBean> shanglist = map.get(stationlist.get(position).getStationId() + "01");
        int number = 0;
        if (shanglist != null) {
            for (ChildBean.RerurnValueBean bean : shanglist) {
                if (bean.getSelectid() == 0) {
                    number++;
                }
            }
        }
        return number;
    }

    private int SurplusXiacheName() {
        if (map == null) {
            map = (Map<String, List<ChildBean.RerurnValueBean>>) sp.queryForSharedToObject(Keyword.MAPLIST);
        }
        List<ChildBean.RerurnValueBean> xialist = map.get(stationlist.get(position).getStationId() + "02");
        int number = 0;
        if (xialist != null) {
            for (ChildBean.RerurnValueBean bean : xialist) {
                if (bean.getSelectid() != 5) {
                    number++;
                }
            }
        }
        return number;
    }
}
