package com.fwk.school4.ui.Jie;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.fwk.school4.R;
import com.fwk.school4.constant.Keyword;
import com.fwk.school4.constant.SpLogin;
import com.fwk.school4.listener.DaoZhanListener;
import com.fwk.school4.listener.NetWorkListener;
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
import com.fwk.school4.ui.BasaActivity;
import com.fwk.school4.ui.adapter.BaseRecyclerAdapter;
import com.fwk.school4.ui.adapter.MapRecyclerViewAdapter;
import com.fwk.school4.utils.GetDateTime;
import com.fwk.school4.utils.LogUtils;
import com.fwk.school4.utils.SharedPreferencesUtils;
import com.fwk.school4.utils.SharedPreferencesUtils2;
import com.fwk.school4.utils.Stationutil;
import com.fwk.school4.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * Created by fanwenke on 16/12/7.
 */

public class JieStationMapActivity extends BasaActivity implements NetWorkListener, BaseRecyclerAdapter.OnItemListener, DaoZhanListener {

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
    TextView renshu;
    @InjectView(R.id.tv_count)
    TextView mCount;
    private MapRecyclerViewAdapter adapter;

    private SharedPreferencesUtils sp;
    private SharedPreferencesUtils2 spData;

    private List<BanciBean.RerurnValueBean> list;

//    private List<StationBean.RerurnValueBean> stationList;

    private DisplayMetrics display;

    private BanciBean.RerurnValueBean bean;

    private int stationPosition = 0;

    private List<String> times;

    @Override
    public int getLayoutId() {
        return R.layout.station_map_activity2;
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean is = sp.getBoolean(Keyword.ISDAOZHAN);
        if (!sp.getBoolean(Keyword.BEGIN)) {
            this.finish();
            return;
        }
        if (is) {
            stationPosition = sp.getInt(Keyword.THISSATION);
            setTitleNemaTime();
            adapter.setPostion(stationPosition);
            adapter.setNumberSX();
            adapter.notifyDataSetChanged();
            LogUtils.d("aaa");
        } else {
            try {
                StateStationBean stateStationBean = (StateStationBean) sp.queryForSharedToObject(Keyword.STATESTATIONBEAN);
                Intent intent = new Intent(JieStationMapActivity.this, JieChildListActivity2.class);
                intent.putExtra(Keyword.JUMPPOSITION, stateStationBean.isJUMPPOSITION());
                intent.putExtra(Keyword.STATIONPOSITION, stateStationBean.getPosition());
                intent.putExtra(Keyword.SELECTSTATIONID, stateStationBean.getStationSelId());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void init() {
        title.setText(getResources().getString(R.string.map_tltile));
        sp = new SharedPreferencesUtils();
        spData = new SharedPreferencesUtils2();
        display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        Intent intent = getIntent();
        int position = intent.getIntExtra(Keyword.POTIONIT, 0);
        if (position == -1) {
            setData();
        } else {
            initData(position);
        }
    }

    private void initData(int position) {//初始化数据
        list = (List<BanciBean.RerurnValueBean>) spData.queryForSharedToObject(Keyword.SP_BANCI_LIST);
        bean = list.get(position);
        //站点url
        String url = String.format(HTTPURL.API_ZHANDIAN, SpLogin.getKgId(),
                bean.getAttendanceDirections(), bean.getLineId());
        LogUtils.d("站点接口:" + url);
        StaionNetWork staionNetWork = StaionNetWork.newInstance(this);
        staionNetWork.setNetWorkListener(this);
        staionNetWork.setUrl(Keyword.FLAGSTATION, url, StationBean.class);
    }

    private void setData() {//继续运行加载数据
        mCount.setText(sp.getInt(Keyword.CARNUMBER) + "");
        stationPosition = sp.getInt(Keyword.THISSATION);
        nextName.setText(sp.getString(Keyword.NEXTSTANAME));
        yjTime.setText(sp.getString(Keyword.NEXTTIME));
        recyclerInit();
    }

    private void recyclerInit() {
        times = (List<String>) sp.queryForSharedToObject(Keyword.GETSJTIME);
        if (times == null) {
            times = new ArrayList<>();
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        adapter = new MapRecyclerViewAdapter(display, stationPosition, times);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemListener(this);
        adapter.setOnClickListener(this);
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
                    ChildNetWork childNetWork = ChildNetWork.newInstance(JieStationMapActivity.this);
                    childNetWork.setNetWorkListener(JieStationMapActivity.this);
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
                    sp.saveToShared(Keyword.STATESTATIONBEAN, stateStationBean);
                    sp.setboolean(Keyword.ISDAOZHAN, false);
                    setSJTime();
                    Intent intent = new Intent(JieStationMapActivity.this, JieChildListActivity2.class);
                    intent.putExtra(Keyword.JUMPPOSITION, true);
                    intent.putExtra(Keyword.STATIONPOSITION, Position);
                    intent.putExtra(Keyword.SELECTSTATIONID, stationSelId);
                    startActivity(intent);
                    break;
                case Keyword.FLAGFACHE1:
                    setSJTime();
                    List<StationBean.RerurnValueBean> stationList = (List<StationBean.RerurnValueBean>) spData.queryForSharedToObject(Keyword.SP_STATION_LIST);
                    String url1 = String.format(HTTPURL.API_PROCESS, SpLogin.getKgId(), stationList.get(stationPosition).getStationId(), spData.getInt(Keyword.SP_PAICHEDANHAO), 2, GetDateTime.getdatetime());
                    LogUtils.d("--发车URL：" + url1);
                    CarDZNetWork carDZNetWork = CarDZNetWork.newInstance(JieStationMapActivity.this);
                    carDZNetWork.setNetWorkListener(JieStationMapActivity.this);
                    carDZNetWork.setUrl(Keyword.FLAGDAOZHAN, url1, StationFADAOBean.class);
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
                    ToastUtil.show("结束了");
                    sp.removData();
                    finish();
                    break;
            }
        }
    };

    @Override
    public void setOnItemListener(int position, BaseRecyclerAdapter.ClickableViewHolder holder) {

        Intent intent = new Intent(this, JieChildListActivity2.class);
        intent.putExtra(Keyword.JUMPPOSITION, false);
        intent.putExtra(Keyword.STATIONPOSITION, position);
        startActivity(intent);

    }

    private void setTitleNemaTime() {
        List<StationBean.RerurnValueBean> stationList =
                (List<StationBean.RerurnValueBean>) spData.queryForSharedToObject(Keyword.SP_STATION_LIST);
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
        List<StationBean.RerurnValueBean> stationList = (List<StationBean.RerurnValueBean>) spData.queryForSharedToObject(Keyword.SP_STATION_LIST);
        this.Position = position;
        this.stationSelId = stationList.get(position).getStationId();
        String url = String.format(HTTPURL.API_PROCESS, SpLogin.getKgId(), stationList.get(position).getStationId(), spData.getInt(Keyword.SP_PAICHEDANHAO), 1, GetDateTime.getdatetime());
        LogUtils.d("到站URL：" + url);
        CarFCNetWork carFCNetWork = CarFCNetWork.newInstance(this);
        carFCNetWork.setNetWorkListener(this);
        if (getShangChenumber(stationSelId) + getXiaCheNumber(stationSelId) > 0) {
            carFCNetWork.setUrl(Keyword.FLAGFACHE, url, StationFADAOBean.class);
        } else {
            showDialog();
            if (position != stationList.size() - 1) {
                carFCNetWork.setUrl(Keyword.FLAGFACHE1, url, StationFADAOBean.class);
            } else {
                String url1 = String.format(HTTPURL.API_OPEN, spData.getInt(Keyword.SP_PAICHEDANHAO), SpLogin.getKgId(), GetDateTime.getdatetime(), 2, SpLogin.getWorkerExtensionId());
                LogUtils.d("结束URL：" + url1);
                EndNetWork endNetWork = EndNetWork.newInstance(this);
                endNetWork.setNetWorkListener(this);
                endNetWork.setUrl(Keyword.FLAGENDDAOZHAN, url1, FristFaChe.class);
            }
        }
    }

    /**
     * 记录实际到站时间
     */
    private void setSJTime() {
        int H = GetDateTime.getH();
        int M = GetDateTime.getM();
        String Hh = "00";
        String Mm = "00";
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
        map = (Map<String, List<StaBean>>) spData.queryForSharedToObject(Keyword.MAPLIST);
        List<StaBean> list1 = map.get(stationId + "01");
        if (list1 != null) {
            return list1.size();
        }
        return 0;
    }

    private int getXiaCheNumber(int stationId) {
        map = (Map<String, List<StaBean>>) spData.queryForSharedToObject(Keyword.MAPLIST);
        List<StaBean> list1 = map.get(stationId + "02");
        if (list1 != null) {
            return list1.size();
        }
        return 0;
    }
}
