package com.fwk.school4.utils;

import com.fwk.school4.model.StaBean;

import java.util.Comparator;

/**
 * Created by fanwenke on 2017/2/6.
 */

public class StationComparator implements Comparator<StaBean> {
    @Override
    public int compare(StaBean o1, StaBean o2) {
        return o1.getOrder() - o2.getOrder();
    }
}
