package com.fwk.school4.model;

import java.io.Serializable;

/**
 * Created by fanwenke on 2017/2/21.
 */

public class ZuofeiBean implements Serializable {

    /**
     * Success : 10000
     * Message : 修改成功
     * RerurnValue : null
     */

    private int Success;
    private String Message;
    private Object RerurnValue;

    public int getSuccess() {
        return Success;
    }

    public void setSuccess(int Success) {
        this.Success = Success;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public Object getRerurnValue() {
        return RerurnValue;
    }

    public void setRerurnValue(Object RerurnValue) {
        this.RerurnValue = RerurnValue;
    }
}
