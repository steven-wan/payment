package com.healthpay.dto.apidto;

import java.io.Serializable;

/**
 * @author steven-wan
 * @desc
 * @date 2020-11-05 18:48
 */

public class SessionCodeDTO implements Serializable {
    private long timestamp;

    private String mobile;

    private String merId;

    private String personalId;


    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }
}
