package com.healthpay.dto.apidto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Administrator on 2019/8/15.
 */
@XmlRootElement(name = "data")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class AcPayRetDto {
    @XmlElement(name = "order_no")
    private String orderNo;

    @XmlElement(name = "plat_no")
    private String platNo;

    @XmlElement(name = "personal_id")
    private String personalId;

    @XmlElement(name = "amt")
    private Double amt;

    @XmlElement(name = "status")
    private Integer status;

    @XmlElement(name = "token")
    private String token;

    @XmlElement(name = "cashier_html")
    private String cashierHtml;

    @XmlElement(name = "one_code_pay_url")
    private String oneCodePayUrl;

    @XmlElement(name = "one_code_pay_html")
    private String oneCodePayHtml;

    /**
     * 微信小程序支付地址
     */
    @XmlElement(name = "applet_pay_url")
    private String appletPayUrl;

    @XmlElement(name = "wechat_applet_app_id")
    private String wechatAppletPayAppId;

    public String getCashierHtml() {
        return cashierHtml;
    }

    public void setCashierHtml(String cashierHtml) {
        this.cashierHtml = cashierHtml;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getPlatNo() {
        return platNo;
    }

    public void setPlatNo(String platNo) {
        this.platNo = platNo;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }
}
