package com.healthpay.dto.apidto;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @author gyp
 * @version 1.0
 * @desc 微信申请退款返回报文
 * @date 2016-10-14 10:48
 */
@XmlRootElement(name = "data")
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(propOrder = {"requestNo","amt"})
public class P2008RetDTO implements Serializable {
    @XmlElement(name = "request_no")
    private String requestNo;
    @XmlElement(name = "amt")
    private Double amt;

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }
}
