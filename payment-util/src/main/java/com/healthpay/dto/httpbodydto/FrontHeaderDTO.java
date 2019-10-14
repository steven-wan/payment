package com.healthpay.dto.httpbodydto;


import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import javax.xml.bind.annotation.*;

/**
 * @author gyp
 * @version 1.0
 * @desc 自定义功能描述
 * @date 2016-12-23 14:38
 */
@XmlRootElement(name = "Xml")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"retCode","retMsg","timestamp","data","signature","key"})
public class FrontHeaderDTO {
    @XmlElement(name = "RetCode")
    private String retCode;   // 返回码

    @XmlElement(name = "RetMsg")
    private String retMsg;    // 返回内容描述

    @XmlElement(name = "Timestamp")
    private String timestamp; // 时间戳

    @XmlCDATA
    @XmlElement(name = "Data")
    private String data;

    @XmlCDATA
    @XmlElement(name = "Signature")
    private String signature; // 签名码

    @XmlCDATA
    @XmlElement(name = "Key")
    private String key;

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public void setRetMsg(String retMsg) {
        this.retMsg = retMsg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
