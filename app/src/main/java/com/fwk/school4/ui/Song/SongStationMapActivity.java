package com.fwk.school4.ui.Song;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fwk.school4.R;
import com.fwk.school4.constant.Keyword;
import com.fwk.school4.constant.SpLogin;
import com.fwk.school4.listener.DaoZhanListener;
import com.fwk.school4.listener.NetWorkListener;
import com.fwk.school4.listener.RecyclerViewListener;
import com.fwk.school4.model.BanciBean;
import com.fwk.school4.model.ChildBean;
import com.fwk.school4.model.FristFaChe;
import com.fwk.school4.model.StaBean;
import com.fwk.school4.model.StateStationBean;
import com.fwk.school4.model.StationBean;
import com.fwk.school4.model.StationFADAOBean;
import com.fwk.school4.network.HTTPURL;
import com.fwk.school4.network.api.CarDZNetWork;
import com.fwk.school4.network.api.CarFCNetWork;
import com.fwk.school4.network.api.ChildNetWork;
import com.fwk.school4.network.api.EndNetWork;
import com.fwk.school4.network.api.StaionNetWork;
import com.fwk.school4.network.api.ZuofeiNetWork;
import com.fwk.school4.ui.BaseActivity;
import com.fwk.school4.ui.MainActivity;
import com.fwk.school4.ui.ResidueActivity;
import com.fwk.school4.ui.adapter.BaseRecyclerAdapter;
import com.fwk.school4.ui.adapter.MapRecyclerViewAdapter;
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
 * Created by fanwenke on 16/12/7.
 */

public class SongStationMapActivity extends BaseActivity implements NetWorkListener, BaseRecyclerAdapter.OnItemListener, DaoZhanListener {

    @InjectView(R.id.station_map_recycler)
    RecyclerView mRecyclerView;
    @InjectView(R.id.title_tv)
    TextView title;
    @InjectView(R.id.tv_main_station)
    TextView mStationNaem;
    @InjectView(R.id.tv_main_station_time)
    TextView mStationTime;

    @InjectView(R.id.tv_next_name)
    TextView nextName;
    @InjectView(R.id.tv_yjtiem)
    TextView yjTime;
    @InjectView(R.id.tv_count)
    TextView mCount;
    @InjectView(R.id.title_right_iv)
    ImageView zf;

    private MapRecyclerViewAdapter adapter;

    private SharedPreferencesUtils sp;

    private List<BanciBean.RerurnValueBean> list;

    private DisplayMetrics display;

    private BanciBean.RerurnValueBean bean;
    private int stationPosition = 0;
    private List<String> times;
    private LinearLayoutManager layoutManager;

    private String DaozhanUrl;
    private String FacheUrl;

    @Override
    public int getLayoutId() {
        return R.layout.station_map_activity2;
    }

    @Override
    public void init() {
        zf.setVisibility(View.VISIBLE);
        sp = new SharedPreferencesUtils();
//        sp = new SharedPreferencesUtils2();
        Intent intent = getIntent();
        if (bean == null) {
            bean = (BanciBean.RerurnValueBean) sp.queryForSharedToObject(Keyword.SELECTBANCI);
        }
        if (intent.getIntExtra(Keyword.POTIONIT, 0) == -1) {
            setData();
        } else {
            initData();
        }

        title.setText(bean.getBusScheduleName());

        if (intent.getBooleanExtra(Keyword.SELECTZUOFEI, false)) {
            ZuofeiNetWork work = ZuofeiNetWork.newInstance(this);
            work.setNetWorkListener(this);
            MainDialog.ZF(this, work);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean is = sp.getBoolean(Keyword.ISDAOZHAN);
        try {
            StateStationBean stateStationBean = (StateStationBean) sp.queryForSharedToObject(Keyword.STATESTATIONBEAN);
            if (is) {
                Intent intent = new Intent(SongStationMapActivity.this, SongChildListActivity2.class);
                intent.putExtra(Keyword.JUMPPOSITION, stateStationBean.isJUMPPOSITION());
                intent.putExtra(Keyword.STATIONPOSITION, stateStationBean.getPosition());
                intent.putExtra(Keyword.SELECTSTATIONID, stateStationBean.getStationSelId());
                intent.putExtra(Keyword.DINGWEI, stateStationBean.getDingwei());
                startActivity(intent);
                finish();
            } else {
                mRecyclerView.scrollToPosition(stateStationBean.getPosition() + 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {//初始化数据
        //站点url
        String url = String.format(HTTPURL.API_ZHANDIAN, SpLogin.getKgId(),
                bean.getAttendanceDirections(), bean.getLineId());
        LogUtils.d("站点接口:" + url);
        StaionNetWork staionNetWork = StaionNetWork.newInstance(this);
        staionNetWork.setNetWorkListener(this);
        staionNetWork.setUrl(Keyword.FLAGSTATION, url, StationBean.class);
    }

    private void setData() {//继续运行加载数据
        stationPosition = sp.getInt(Keyword.THISSATION);
        nextName.setText(sp.getString(Keyword.NEXTSTANAME));
        yjTime.setText(sp.getString(Keyword.NEXTTIME));
        mCount.setText(sp.getInt(Keyword.CARNUMBER) + "");
        recyclerInit();
    }

    private void recyclerInit() {
        times = (List<String>) sp.queryForSharedToObject(Keyword.GETSJTIME);
        if (times == null) {
            times = new ArrayList<>();
        }
        display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new MapRecyclerViewAdapter(display, stationPosition, times);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemListener(this);
        adapter.setOnClickListener(this);
    }
    @OnClick(R.id.title_right_iv)
    public void onClick(View view){
        ZuofeiNetWork work = ZuofeiNetWork.newInstance(this);
        work.setNetWorkListener(this);
        MainDialog.ZF(this, work);
    }
    @Override
    public void NetWorkSuccess(int Flag) {
        switch (Flag) {

            case Keyword.FLAGSTATION:
                handler.sendEmptyMessage(Keyword.FLAGSTATION);
                break;

            case Keyword.FLAGCHILD:
                handler.sendEmptyMessage(Keyword.FLAGCHILD);
                break;
            case Keyword.FLAGFACHE:
                handler.sendEmptyMessage(Keyword.FLAGFACHE);
                break;
            case Keyword.FLAGFACHE1:
                handler.sendEmptyMessage(Keyword.FLAGFACHE1);
                break;
            case Keyword.FLAGDAOZHAN:
                handler.sendEmptyMessage(Keyword.FLAGDAOZHAN);
                break;
            case Keyword.FLAGENDDAOZHAN:
                handler.sendEmptyMessage(Keyword.FLAGENDDAOZHAN);
                break;
            case Keyword.ZUOFEI:
                sp.removData();
                startActivity(new Intent(SongStationMapActivity.this, MainActivity.class));
                SongStationMapActivity.this.finish();
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
            case Keyword.FLAGFACHEERROR:
                List<String> url1 = (List<String>) sp.queryForSharedToObject(Keyword.LIXIANFASONGCARURL);
                if (url1 == null) {
                    url1 = new ArrayList<>();
                }
                url1.add(DaozhanUrl);
                sp.saveToShared(Keyword.LIXIANFASONGCARURL, url1);
                handler.sendEmptyMessage(Keyword.FLAGFACHE);
            case Keyword.FLAGFACHEERROR1:
                List<String> url2 = (List<String>) sp.queryForSharedToObject(Keyword.LIXIANFASONGCARURL);
                if (url2 == null) {
                    url2 = new ArrayList<>();
                }
                url2.add(DaozhanUrl);
                sp.saveToShared(Keyword.LIXIANFASONGCARURL, url2);
                handler.sendEmptyMessage(Keyword.FLAGFACHE1);
                break;

        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case Keyword.FLAGSTATION:
                    String url = String.format(HTTPURL.API_CHILD, SpLogin.getKgId(),
                            bean.getBusScheduleId(), bean.getAttendanceDirections());
                    LogUtils.d("幼儿接口:" + url);
                    ChildNetWork childNetWork = ChildNetWork.newInstance(SongStationMapActivity.this);
                    childNetWork.setNetWorkListener(SongStationMapActivity.this);
                    childNetWork.setUrl(Keyword.FLAGCHILD, url, ChildBean.class);
                    setTitleNemaTime();
                    break;

                case Keyword.FLAGCHILD:

                    recyclerInit();

                    break;
                case Keyword.FLAGFACHE:
                    StateStationBean stateStationBean = new StateStationBean();
                    stateStationBean.setJUMPPOSITION(true);
                    stateStationBean.setStationSelId(stationSelId);
                    stateStationBean.setPosition(Position);
                    stateStationBean.setDingwei(getPoistion());
                    sp.saveToShared(Keyword.STATESTATIONBEAN, stateStationBean);
                    sp.setboolean(Keyword.ISDAOZHAN, true);
                    setSJTime();
                    setTitleNemaTime();
                    Intent intent = new Intent(SongStationMapActivity.this, SongChildListActivity2.class);
                    intent.putExtra(Keyword.JUMPPOSITION, true);
                    intent.putExtra(Keyword.STATIONPOSITION, Position);
                    intent.putExtra(Keyword.SELECTSTATIONID, stationSelId);
                    intent.putExtra(Keyword.DINGWEI, getPoistion());
                    startActivity(intent);
                    finish();
                    break;
                case Keyword.FLAGFACHE1:
                    setSJTime();
                    List<StationBean.RerurnValueBean> stationList = (List<StationBean.RerurnValueBean>) sp.queryForSharedToObject(Keyword.SP_STATION_LIST);
                    FacheUrl = String.format(HTTPURL.API_PROCESS, SpLogin.getKgId(), stationList.get(stationPosition).getStationId(), sp.getInt(Keyword.SP_PAICHEDANHAO), 2, GetDateTime.getdatetime());
                    LogUtils.d("--发车URL：" + FacheUrl);
                    CarDZNetWork carDZNetWork = CarDZNetWork.newInstance(SongStationMapActivity.this);
                    carDZNetWork.setNetWorkListener(SongStationMapActivity.this);
                    carDZNetWork.setUrl(Keyword.FLAGDAOZHAN, FacheUrl, StationFADAOBean.class);
                    break;
                case Keyword.FLAGDAOZHAN:
                    closeDialog();
                    stationPosition++;
                    sp.setInt(Keyword.THISSATION, stationPosition);
                    setTitleNemaTime();
                    adapter.setPostion(stationPosition);
                    adapter.setNumberSX();
                    adapter.notifyDataSetChanged();
                    ToastUtil.show("本站无上下车学生，已直接过站...");
                    break;
                case Keyword.FLAGENDDAOZHAN:
//                    startActivity(new Intent(SongStationMapActivity.this, FinishActivity.class));
//                    finish();
                    MainDialog.DaoZhan(SongStationMapActivity.this,bean.getBusScheduleName(),sp);
                    break;
            }
        }
    };

    @Override
    public void setOnItemListener(int position, BaseRecyclerAdapter.ClickableViewHolder holder) {
        Intent intent = new Intent(this, SongChildListActivity2.class);
        intent.putExtra(Keyword.JUMPPOSITION, false);
        intent.putExtra(Keyword.STATIONPOSITION, position);
        startActivity(intent);
    }

    private void setTitleNemaTime() {
        List<StationBean.RerurnValueBean> stationList =
                (List<StationBean.RerurnValueBean>) sp.queryForSharedToObject(Keyword.SP_STATION_LIST);
        nextName.setText(stationList.get(stationPosition).getStationName());
        yjTime.setText(GetDateTime.getYJTime(stationList.get(stationPosition).getDuration()));
        mCount.setText(sp.getInt(Keyword.CARNUMBER) + "");
        sp.setString(Keyword.NEXTSTANAME, stationList.get(stationPosition).getStationName());
        sp.setString(Keyword.NEXTTIME, GetDateTime.getYJTime(stationList.get(stationPosition).getDuration()));
    }

    private int Position;
    private int stationSelId;

    @Override
    public void OnClickListener(int position) {
        List<StationBean.RerurnValueBean> stationList = (List<StationBean.RerurnValueBean>) sp.queryForSharedToObject(Keyword.SP_STATION_LIST);
        this.Position = position;
        this.stationSelId = stationList.get(position).getStationId();
        DaozhanUrl = String.format(HTTPURL.API_PROCESS, SpLogin.getKgId(), stationList.get(position).getStationId(), sp.getInt(Keyword.SP_PAICHEDANHAO), 1, GetDateTime.getdatetime());
        LogUtils.d("到站URL：" + DaozhanUrl);
        CarFCNetWork carFCNetWork = CarFCNetWork.newInstance(this);
        carFCNetWork.setNetWorkListener(this);
        if (getShangChenumber(stationSelId) + getXiaCheNumber(stationSelId) > 0) {
            carFCNetWork.getFlag(Keyword.FLAGFACHE);
            carFCNetWork.setUrl(Keyword.FLAGFACHE, DaozhanUrl, StationFADAOBean.class);
        } else {
            showDialog();
            if (position != stationList.size() - 1) {
                carFCNetWork.getFlag(Keyword.FLAGFACHE1);
                carFCNetWork.setUrl(Keyword.FLAGFACHE1, DaozhanUrl, StationFADAOBean.class);
            } else {

                int child = sp.getInt(Keyword.CARNUMBER);
                if (child == 0) {
                    String url1 = String.format(HTTPURL.API_OPEN, sp.getInt(Keyword.SP_PAICHEDANHAO), SpLogin.getKgId(), GetDateTime.getdatetime(), 2, SpLogin.getWorkerExtensionId());
                    LogUtils.d("结束URL：" + url1);
                    EndNetWork endNetWork = EndNetWork.newInstance(this);
                    endNetWork.setNetWorkListener(this);
                    endNetWork.setUrl(Keyword.FLAGENDDAOZHAN, url1, FristFaChe.class);
                } else {
                    Intent intent = new Intent(SongStationMapActivity.this, ResidueActivity.class);
                    intent.putExtra(Keyword.SELECTITME, stationList.get(stationList.size() - 1).getStationId());
                    startActivity(intent);
                    SongStationMapActivity.this.finish();
                }
            }
        }
    }

    /**
     * 记录实际到站时间
     */
    private void setSJTime() {
        int H = GetDateTime.getH();
        int M = GetDateTime.getM();
        String Hh;
        String Mm;
        if (H < 10) {
            Hh = "0" + H;
        } else {
            Hh = H + "";
        }
        if (M < 10) {
            Mm = "0" + M;
        } else {
            Mm = M + "";
        }
        String time = Hh + ":" + Mm;
        times.add(time);
        sp.saveToShared(Keyword.GETSJTIME, times);
    }

    private Map<String, List<StaBean>> map;

    private int getShangChenumber(int stationId) {
        map = (Map<String, List<StaBean>>) sp.queryForSharedToObject(Keyword.MAPLIST);
        List<StaBean> list1 = map.get(stationId + "01");
        if (list1 != null) {
            return list1.size();
        }
        return 0;
    }

    private int getXiaCheNumber(int stationId) {
        map = (Map<String, List<StaBean>>) sp.queryForSharedToObject(Keyword.MAPLIST);
        List<StaBean> list1 = map.get(stationId + "02");
        if (list1 != null) {
            return list1.size();
        }
        return 0;
    }

    private int getPoistion() {
        List<StaBean> staBeen = (List<StaBean>) sp.queryForSharedToObject(Keyword.SELECTSTA);
        for (int i = 0; i < staBeen.size(); i++) {
            if (staBeen.get(i).getId() == stationSelId) {
                return i;
            }
        }
        return 0;
    }


}