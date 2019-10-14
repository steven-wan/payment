package com.healthpay.dto.httpbodydto;

import org.eclipse.persistence.oxm.annotations.XmlCDATA;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author gyp
 * @version 1.0
 * @desc 消息头
 * @date 2016-09-08 17:58
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "merchantNo", "funcId", "timestamp", "signature", "retCode", "retMsg" })
public class HeaderDTO {
    @NotEmpty(message = "{developer_id_IS_NULL}")
    @XmlElement(name = "developer_id")
    private String merchantNo; // 商户号

    @NotEmpty(message = "{func_id_IS_NULL}")
    @XmlElement(name = "func_id")
    private String funcId; // 功能码

    @NotEmpty(message = "{TIMESTAMP_IS_NULL}")
    @Digits(integer = 25,fraction = 0,message ="{TIMESTAMP_IS_DIGIT}")
    @XmlElement(name = "timestamp")
    private String timestamp; // 时间戳

    @NotNull(message = "{SIGNATURE_IS_NULL}")
    @XmlElement(name = "signature")
    @XmlCDATA
    private String signature; // 签名码

    @XmlElement(name = "ret_code")
    private String retCode; // 返回码

    @XmlElement(name = "ret_msg")
    private String retMsg; // 返回内容描述

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

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
}
