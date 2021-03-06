package com.fwk.school4.utils;

import com.fwk.school4.constant.Keyword;
import com.fwk.school4.constant.SpBanci;
import com.fwk.school4.model.ChildBean;
import com.fwk.school4.model.StaBean;
import com.fwk.school4.model.StationBean;
import com.fwk.school4.model.StationModeBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanwenke on 16/12/12.
 */

public class Stationutil {
    private SharedPreferencesUtils sp = new SharedPreferencesUtils();

    private List<ChildBean.RerurnValueBean> bean;
    private List<StationModeBean> stationIdList;
    private List<StaBean> list;

    public static Stationutil newInstance() {
        return new Stationutil();
    }

    private Stationutil() {

        bean = (List<ChildBean.RerurnValueBean>) sp.queryForSharedToObject(Keyword.SP_CHILD_LIST);
        stationIdList = (List<StationModeBean>) sp.queryForSharedToObject(Keyword.STATIONIDLIST);
        list = (List<StaBean>) sp.queryForSharedToObject(Keyword.STAIDLIST);
    }

    /**
     * 筛选出要上车的幼儿，根据站点来进行分组。顺序和站点列表保持一致。
     */
    public void setMaplist() {

        int attendanceDirections = SpBanci.getAttendancedirections();
        switch (attendanceDirections) {
            case 1:
                Jiefenzu();
                break;

            case 2:
                Songfenzu();
                break;
        }

    }

    private Map<String, List<ChildBean.RerurnValueBean>> map;
    private List<StaBean> selectSta;

    private void Jiefenzu() {
        map = new HashMap<>();
        selectSta = new ArrayList<>();
        for (StaBean staBean : list) {
            boolean number = false;
            List<ChildBean.RerurnValueBean> child = new ArrayList<>();
            for (ChildBean.RerurnValueBean childBean : bean) {
                if (childBean.getConnectStation() == staBean.getId() && staBean.getStrid().equals(staBean.getId() + "01")) {
                    child.add(childBean);
                    number = true;
                }
            }
            if (number) {
                map.put(staBean.getStrid(), child);
                selectSta.add(staBean);
            }
        }
        sp.saveToShared(Keyword.MAPLIST, map);
        sp.saveToShared(Keyword.SELECTSTA, selectSta);
    }

    private void Songfenzu() {
        map = new HashMap<>();
        selectSta = new ArrayList<>();
        for (StaBean staBean : list) {
            boolean number = false;
            List<ChildBean.RerurnValueBean> child = new ArrayList<>();
            for (ChildBean.RerurnValueBean childBean : bean) {
                if (childBean.getSendStartStation() == staBean.getId() && staBean.getStrid().equals(staBean.getId() + "01")) {
                    child.add(childBean);
                    number = true;
                }
            }
            if (number) {
                map.put(staBean.getStrid(), child);
                selectSta.add(staBean);
            }
        }
        sp.saveToShared(Keyword.MAPLIST, map);
        sp.saveToShared(Keyword.SELECTSTA, selectSta);
    }











    public int JumpPosition(int stationPosition) {
        int number = 0;
        try{
            for (int i = 0; i < (stationPosition - 1) * 2; i++) {
                number = number + map.get(list.get(i).getStrid()).size();
            }
            return number;
        } catch (Exception o) {
            return 0;
        }
    }


    /**
     * 分组
     * <p>
     * 按照站点顺序排练幼儿，并得数每个分组的开始位置
     * <p>
     * headerLocationList 每个分组的开始位置
     * <p>
     * list 按照站点排练之后的幼儿list
     * <p>
     * childCount 记录每个站点需要操作的人数
     */
    private List<String> stationName = new ArrayList<>();
    private List<Integer> headerLocationList = new ArrayList<>();
    private List<Integer> childCount = new ArrayList<>();

    private void jieList(List<ChildBean.RerurnValueBean> bean, List<StationModeBean> stationIdList) {
        List<ChildBean.RerurnValueBean> list = new ArrayList<>();
        int location = 0;
        int locat = location;
        headerLocationList.add(0);
        for (StationModeBean modeBean : stationIdList) {
            int counte1 = 0;
            for (ChildBean.RerurnValueBean valueBean : bean) {
                if (modeBean.getId() == valueBean.getConnectStation()) {
                    valueBean.setIsDU(1);
                    list.add(valueBean);
                    stationName.add(modeBean.getNameDown());
                    location++;
                    counte1++;
                }
            }
            if (locat != location) {
                locat = location;
                headerLocationList.add(location);
            }
            for (ChildBean.RerurnValueBean valueBean : bean) {
                if (modeBean.getId() == valueBean.getConnectEndStation()) {
                    valueBean.setIsDU(2);
                    list.add(valueBean);
                    stationName.add(modeBean.getNameUp());
                    location++;
                    counte1++;
                }
            }
            if (locat != location) {
                locat = location;
                headerLocationList.add(location);
            }
            childCount.add(counte1);
        }
//        headerLocationList.remove(headerLocationList.size() - 1);
        sp.saveToShared(Keyword.CHILDGROUP, list);
        sp.saveToShared(Keyword.HEADERLOCATION, headerLocationList);
        sp.saveToShared(Keyword.STATIONNAEM, stationName);
        sp.saveToShared(Keyword.CHILDCOUNT, childCount);
    }

    private void songList(List<ChildBean.RerurnValueBean> bean, List<StationModeBean> stationIdList) {
        List<ChildBean.RerurnValueBean> list = new ArrayList<>();
        int location = 0;
        int locat = location;
        headerLocationList.add(0);
        for (StationModeBean modeBean : stationIdList) {
            int counte1 = 0;
            for (ChildBean.RerurnValueBean valueBean : bean) {
                if (modeBean.getId() == valueBean.getSendStartStation()) {
                    valueBean.setIsDU(1);
                    list.add(valueBean);
                    stationName.add(modeBean.getNameDown());
                    counte1++;
                    location++;
                }
            }
            if (locat != location) {
                locat = location;
                headerLocationList.add(location);
            }
            for (ChildBean.RerurnValueBean valueBean : bean) {
                if (modeBean.getId() == valueBean.getSendStation()) {
                    valueBean.setIsDU(2);
                    list.add(valueBean);
                    stationName.add(modeBean.getNameUp());
                    location++;
                    counte1++;
                }
            }
            if (locat != location) {
                locat = location;
                headerLocationList.add(location);
            }
            childCount.add(counte1);
        }
//        headerLocationList.remove(headerLocationList.size() - 1);
        sp.saveToShared(Keyword.CHILDGROUP, list);
        sp.saveToShared(Keyword.HEADERLOCATION, headerLocationList);
        sp.saveToShared(Keyword.STATIONNAEM, stationName);
        sp.saveToShared(Keyword.CHILDCOUNT, childCount);
    }


}
