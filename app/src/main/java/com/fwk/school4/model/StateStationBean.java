package com.fwk.school4.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/15.
 */

public class StateStationBean implements Serializable {
    private boolean JUMPPOSITION;
    private int Position;
    private int stationSelId;
    private int dingwei;

    public int getDingwei() {
        return dingwei;
    }

    public void setDingwei(int dingwei) {
        this.dingwei = dingwei;
    }

    public boolean isJUMPPOSITION() {
        return JUMPPOSITION;
    }

    public void setJUMPPOSITION(boolean JUMPPOSITION) {
        this.JUMPPOSITION = JUMPPOSITION;
    }

    public int getPosition() {
        return Position;
    }

    public void setPosition(int position) {
        Position = position;
    }

    public int getStationSelId() {
        return stationSelId;
    }

    public void setStationSelId(int stationSelId) {
        this.stationSelId = stationSelId;
    }
}
