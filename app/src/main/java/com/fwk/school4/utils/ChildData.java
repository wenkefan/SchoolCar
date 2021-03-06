package com.fwk.school4.utils;

import com.fwk.school4.constant.Keyword;
import com.fwk.school4.model.ChildBean;
import com.fwk.school4.model.StaBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fanwenke on 16/12/27.
 */

public class ChildData {

    private static SharedPreferencesUtils sp = new SharedPreferencesUtils();
//    private static SharedPreferencesUtils2 sp = new SharedPreferencesUtils2();

    /**
     * 送幼儿
     * <p>
     * 上车   选择之后重新分组
     *
     * @param map      整体站点幼儿数据
     * @param staBean  选择幼儿当前站点数据
     * @param mItem    选择幼儿的位置数
     * @param position 选择的类型
     *                 <p>
     *                 第一步，查找选中的幼儿是否为上车，
     *                 第二步，上车后，找到幼儿的下车站点
     *                 第三步，查找当前的map中是否有当前下车站点
     */
    public static void setSongData(Map<String, List<ChildBean.RerurnValueBean>> map, StaBean staBean, int mItem, int position) {
        //第一步
        List<StaBean> stationlist = (List<StaBean>) sp.queryForSharedToObject(Keyword.STAIDLIST);
        List<StaBean> bean = (List<StaBean>) sp.queryForSharedToObject(Keyword.SELECTSTA);
        StationComparator comparator = new StationComparator();
        if (position == 1) {
            int stationId = map.get(staBean.getStrid()).get(mItem).getSendStation();//下车站点ID
            LogUtils.d("staid--" + stationId);
            String strId = stationId + "02";
            StaBean station = new StaBean();
            for (int i = 0; i < stationlist.size(); i++) {
                if (stationlist.get(i).getStrid().equals(strId)) {
                    station = stationlist.get(i);
                    break;
                }
            }

            List<ChildBean.RerurnValueBean> list = map.get(strId);
            if (list == null) {
                list = new ArrayList<>();
                ChildBean.RerurnValueBean valueBean = map.get(staBean.getStrid()).get(mItem);
                list.add(setNew(valueBean));
                bean.add(station);
            } else {
                ChildBean.RerurnValueBean valueBean = map.get(staBean.getStrid()).get(mItem);
                list.add(setNew(valueBean));
            }
            Collections.sort(bean, comparator);
            map.put(strId, list);
            Songshangche(map, staBean, mItem);
        } else {
            //如果已上车在选择其他状态时，就要取消下车站点
            if (map.get(staBean.getStrid()).get(mItem).getSelectid() == 1) {
                ChildBean.RerurnValueBean bean1 = map.get(staBean.getStrid()).get(mItem);
                int stationId = bean1.getSendStation();//下车站点ID
                List<ChildBean.RerurnValueBean> list = map.get(stationId + "02");
                if (list.size() == 1) {
                    map.remove(stationId + "02");
                    for (int i = 0; i < bean.size(); i++) {
                        if (bean.get(i).getStrid().equals(stationId + "02")) {
                            bean.remove(i);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getChildId() == bean1.getChildId()) {
                            list.remove(i);
                            break;
                        }
                    }
                }
                Songshangche1(map, staBean, mItem);
            }
        }

        map.get(staBean.getStrid()).get(mItem).setSelectid(position);
        sp.saveToShared(Keyword.SELECTSTA, bean);
        sp.saveToShared(Keyword.MAPLIST, map);
    }

    /**
     * 接幼儿
     * <p>
     * 上车   选择之后重新分组
     *
     * @param map      整体站点幼儿数据
     * @param staBean  选择幼儿当前站点数据
     * @param mItem    选择幼儿的位置数
     * @param position 选择的类型
     *                 <p>
     *                 第一步，查找选中的幼儿是否为上车，
     *                 第二步，上车后，找到幼儿的下车站点
     *                 第三步，查找当前的map中是否有当前下车站点
     */
    public static void setJieData(Map<String, List<ChildBean.RerurnValueBean>> map, StaBean staBean, int mItem, int position) {
        //第一步
        List<StaBean> stationlist = (List<StaBean>) sp.queryForSharedToObject(Keyword.STAIDLIST);
        List<StaBean> bean = (List<StaBean>) sp.queryForSharedToObject(Keyword.SELECTSTA);
        StationComparator comparator = new StationComparator();
        boolean flag = false;
        if (position == 1) {


            int stationId = map.get(staBean.getStrid()).get(mItem).getConnectEndStation();//下车站点ID
            String strId = stationId + "02";
            StaBean station = new StaBean();
            for (int i = 0; i < stationlist.size(); i++) {
                if (stationlist.get(i).getStrid().equals(strId)) {
                    station = stationlist.get(i);
                    break;
                }
            }

            List<ChildBean.RerurnValueBean> list = map.get(strId);
            if (list == null) {
                list = new ArrayList<>();
                ChildBean.RerurnValueBean valueBean = map.get(staBean.getStrid()).get(mItem);
                list.add(setNew(valueBean));
                bean.add(station);
            } else {
                ChildBean.RerurnValueBean valueBean = map.get(staBean.getStrid()).get(mItem);
                list.add(setNew(valueBean));
            }
            Collections.sort(bean, comparator);
            map.put(strId, list);
            Jieshangche(map, staBean, mItem);
        } else {
            //如果已上车在选择其他状态时，就要取消下车站点
            if (map.get(staBean.getStrid()).get(mItem).getSelectid() == 1) {
                ChildBean.RerurnValueBean bean1 = map.get(staBean.getStrid()).get(mItem);
                int stationId = bean1.getConnectEndStation();//下车站点ID
                List<ChildBean.RerurnValueBean> list = map.get(stationId + "02");
                if (list.size() == 1) {
                    map.remove(stationId + "02");
                    for (int i = 0; i < bean.size(); i++) {
                        if (bean.get(i).getStrid().equals(stationId + "02")) {
                            bean.remove(i);
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getChildId() == bean1.getChildId()) {
                            list.remove(i);
                            break;
                        }
                    }
                }
                Jieshangche1(map, staBean, mItem);
            }
        }


        map.get(staBean.getStrid()).get(mItem).setSelectid(position);
        sp.saveToShared(Keyword.SELECTSTA, bean);
        sp.saveToShared(Keyword.MAPLIST, map);
    }


    public static int setXiache(Map<String, List<ChildBean.RerurnValueBean>> map, StaBean staBean, int mItem, int position, int sel) {
        if (map.get(staBean.getStrid()).get(mItem).getSelectid() == position) {
            return 0;
        }
        if (sel != 0) {
            map.get(staBean.getStrid()).get(mItem).setSelectid(position);
            sp.saveToShared(Keyword.MAPLIST, map);
            if (sel == 1) {
                Jiexiache(map, staBean, mItem);
            } else if (sel == 2) {
                Songxiache(map, staBean, mItem);
            }
        }
        return 1;
    }

    private static Map<Integer, Integer> Shangche;
    private static Map<Integer, Integer> Xiache;
    private static int shengyu;

    private static void Jieshangche(Map<String, List<ChildBean.RerurnValueBean>> map, StaBean staBean, int mItem) {
        Shangche = (Map<Integer, Integer>) sp.queryForSharedToObject(Keyword.SHANGCHENUMBER);
        if (Shangche == null) {
            Shangche = new HashMap<>();
        }
        if (Shangche.get(map.get(staBean.getStrid()).get(mItem).getConnectStation()) == null) {
            Shangche.put(map.get(staBean.getStrid()).get(mItem).getConnectStation(), 1);
        } else {
            int number = Shangche.get(map.get(staBean.getStrid()).get(mItem).getConnectStation());
            Shangche.put(map.get(staBean.getStrid()).get(mItem).getConnectStation(), number + 1);
        }
        shengyu = sp.getInt(Keyword.CARNUMBER);
        sp.setInt(Keyword.CARNUMBER, shengyu + 1);
        sp.saveToShared(Keyword.SHANGCHENUMBER, Shangche);
    }

    private static void Jieshangche1(Map<String, List<ChildBean.RerurnValueBean>> map, StaBean staBean, int mItem) {
        Shangche = (Map<Integer, Integer>) sp.queryForSharedToObject(Keyword.SHANGCHENUMBER);
        int number = Shangche.get(map.get(staBean.getStrid()).get(mItem).getConnectStation());
        Shangche.put(map.get(staBean.getStrid()).get(mItem).getConnectStation(), number - 1);
        shengyu = sp.getInt(Keyword.CARNUMBER);
        sp.setInt(Keyword.CARNUMBER, shengyu - 1);
        sp.saveToShared(Keyword.SHANGCHENUMBER, Shangche);
    }

    private static void Jiexiache(Map<String, List<ChildBean.RerurnValueBean>> map, StaBean staBean, int mItem) {
        Xiache = (Map<Integer, Integer>) sp.queryForSharedToObject(Keyword.XIACHENUMBER);
        if (Xiache == null) {
            Xiache = new HashMap<>();
        }
        if (Xiache.get(map.get(staBean.getStrid()).get(mItem).getConnectEndStation()) == null) {
            Xiache.put(map.get(staBean.getStrid()).get(mItem).getConnectEndStation(), 1);
        } else {
            int number = Xiache.get(map.get(staBean.getStrid()).get(mItem).getConnectEndStation());
            Xiache.put(map.get(staBean.getStrid()).get(mItem).getConnectEndStation(), number + 1);
        }
        shengyu = sp.getInt(Keyword.CARNUMBER);
        sp.setInt(Keyword.CARNUMBER, shengyu - 1);
        sp.saveToShared(Keyword.XIACHENUMBER, Xiache);
    }

    private static void Songshangche(Map<String, List<ChildBean.RerurnValueBean>> map, StaBean staBean, int mItem) {
        Shangche = (Map<Integer, Integer>) sp.queryForSharedToObject(Keyword.SHANGCHENUMBER);
        if (Shangche == null) {
            Shangche = new HashMap<>();
        }
        if (Shangche.get(map.get(staBean.getStrid()).get(mItem).getSendStartStation()) == null) {
            Shangche.put(map.get(staBean.getStrid()).get(mItem).getSendStartStation(), 1);
        } else {
            int number = Shangche.get(map.get(staBean.getStrid()).get(mItem).getSendStartStation());
            Shangche.put(map.get(staBean.getStrid()).get(mItem).getSendStartStation(), number + 1);
        }
        shengyu = sp.getInt(Keyword.CARNUMBER);
        sp.setInt(Keyword.CARNUMBER, shengyu + 1);
        sp.saveToShared(Keyword.SHANGCHENUMBER, Shangche);
    }

    private static void Songshangche1(Map<String, List<ChildBean.RerurnValueBean>> map, StaBean staBean, int mItem) {
        Shangche = (Map<Integer, Integer>) sp.queryForSharedToObject(Keyword.SHANGCHENUMBER);
        int number = Shangche.get(map.get(staBean.getStrid()).get(mItem).getSendStartStation());
        Shangche.put(map.get(staBean.getStrid()).get(mItem).getSendStartStation(), number - 1);
        shengyu = sp.getInt(Keyword.CARNUMBER);
        sp.setInt(Keyword.CARNUMBER, shengyu - 1);
        sp.saveToShared(Keyword.SHANGCHENUMBER, Shangche);
    }

    private static void Songxiache(Map<String, List<ChildBean.RerurnValueBean>> map, StaBean staBean, int mItem) {
        Xiache = (Map<Integer, Integer>) sp.queryForSharedToObject(Keyword.XIACHENUMBER);
        if (Xiache == null) {
            Xiache = new HashMap<>();
        }
        if (Xiache.get(map.get(staBean.getStrid()).get(mItem).getSendStation()) == null) {
            Xiache.put(map.get(staBean.getStrid()).get(mItem).getSendStation(), 1);
        } else {
            int number = Xiache.get(map.get(staBean.getStrid()).get(mItem).getSendStation());
            Xiache.put(map.get(staBean.getStrid()).get(mItem).getSendStation(), number + 1);
        }
        shengyu = sp.getInt(Keyword.CARNUMBER);
        sp.setInt(Keyword.CARNUMBER, shengyu - 1);
        sp.saveToShared(Keyword.XIACHENUMBER, Xiache);
    }

    public static ChildBean.RerurnValueBean setNew(ChildBean.RerurnValueBean valueBean) {
        ChildBean.RerurnValueBean valueBean1 = new ChildBean.RerurnValueBean();
        valueBean1.setSelectid(0);
        valueBean1.setChildId(valueBean.getChildId());
        valueBean1.setChildName(valueBean.getChildName());
        valueBean1.setConnectEndStation(valueBean.getConnectEndStation());
        valueBean1.setConnectLineId(valueBean.getConnectLineId());
        valueBean1.setConnectStation(valueBean.getConnectStation());
        valueBean1.setFatherName(valueBean.getFatherName());
        valueBean1.setFatherPhone(valueBean.getFatherPhone());
        valueBean1.setIsDU(2);
        valueBean1.setMotherName(valueBean.getMotherName());
        valueBean1.setMotherPhone(valueBean.getMotherPhone());
        valueBean1.setOperation(valueBean.getisOperation());
        valueBean1.setOrganizationId(valueBean.getOrganizationId());
        valueBean1.setSACardNo(valueBean.getSACardNo());
        valueBean1.setSendLineId(valueBean.getSendLineId());
        valueBean1.setSendStartStation(valueBean.getSendStartStation());
        valueBean1.setSendStation(valueBean.getSendStation());
        valueBean1.setTeacherName(valueBean.getTeacherName());
        valueBean1.setTeacherphone(valueBean.getTeacherphone());
        valueBean1.setClassName(valueBean.getClassName());
        return valueBean1;
    }
}
