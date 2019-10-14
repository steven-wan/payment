package com.healthpay.dto.httpbodydto;




import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

/**
 * @author gyp
 * @version 1.0
 * @desc 自定义功能描述
 * @date 2016-12-23 10:10
 */
@XmlRootElement(name = "Xml")
@XmlAccessorType(value = XmlAccessType.FIELD)
@XmlType(propOrder = {"merchantNo","funcId","timestamp","platType","bundleId","signature","data"})
public class FrontDTO {
    @NotEmpty(message = "{MERCHANTNO_IS_NULL}")
    @XmlElement(name = "MerchantNo")
    private String merchantNo; // 商户号

    @NotEmpty(message = "{FUNCID_IS_NULL}")
    @XmlElement(name = "FuncId")
    private String funcId; //功能码

    @NotEmpty(message = "{SIGNATURE_IS_NULL}")
    @XmlElement(name = "Signature")
    private String signature; //签名码

    @NotEmpty(message = "{BODYMAP_IS_NULL}")
    @XmlElement(name = "Data")
    private String data;    //数据

    @NotEmpty(message = "{TIMESTAMP_IS_NULL}")
    @Digits(integer = 25,fraction = 0,message ="{TIMESTAMP_IS_DIGIT}")
    @XmlElement(name = "Timestamp")
    private String timestamp; //时间戳

    @NotNull(message = "{PLATTYPE_IS_NULL}")
    @XmlElement(name = "PlatType")
    private Integer platType; //SDK平台类型  1　ios 2 android

    @NotEmpty(message = "{BUNDLEID_IS_NULL}")
    @XmlElement(name = "BundleId")
    private String bundleId;  //APP 唯一标识

    public String getFuncId() {
        return funcId;
    }

    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMerchantNo() {
        return merchantNo;
    }

    public void setMerchantNo(String merchantNo) {
        this.merchantNo = merchantNo;
    }

    public Integer getPlatType() {
        return platType;
    }

    public void setPlatType(Integer platType) {
        this.platType = platType;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }
}
