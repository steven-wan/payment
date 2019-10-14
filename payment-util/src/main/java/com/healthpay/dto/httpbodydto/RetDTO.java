package com.healthpay.dto.httpbodydto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author steven
 * @version 1.0
 * @desc
 * @date 2019-10-12 11:21
 */
@XmlRootElement(name = "xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class RetDTO {
    private String code;

    private String msg;

    private String coentent;

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

    public String getCoentent() {
        return coentent;
    }

    public void setCoentent(String coentent) {
        this.coentent = coentent;
    }
}
