package com.healthpay.dto.apidto;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * @author gyp
 * @version 1.0
 * @desc 自定义功能描述
 * @date 2016-10-14 15:57
 */
@XmlRootElement(name = "data")
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(propOrder = {"requestNo","amt","code","msg"})
public class P2009RetDTO implements Serializable {
    @XmlElement(name = "request_no")
    private String requestNo;
    @XmlElement(name = "amt")
    private Double amt;
    @XmlElement(name = "code")
    private String code;
    @XmlElement(name = "msg")
    private String msg;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
