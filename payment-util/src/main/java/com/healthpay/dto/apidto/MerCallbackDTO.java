package com.healthpay.dto.apidto;

import com.healthpay.util.adapter.DateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * @author gyp
 * @version 1.0
 * @desc 商户回调
 * @date 2016-09-18 8:37
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
public class MerCallbackDTO {
    /**
     * 平台交易流水号
     */
    @XmlElement(name = "trade_no")
    private String tradeNo;

    /**
     * 商户订单号
     */
    @XmlElement(name = "order_no")
    private String orderNo;

    /**
     * 通道流水号
     */
    @XmlElement(name = "channel_no")
    private String payTradeNo;

    /**
     * 交易付款金额
     */
    @XmlElement(name = "amt")
    private Double amt;

    /**
     * 支付通道
     */
    private Integer paychannel;

    /**
     * 交易类型：
     */
    @XmlElement(name = "trans_type")
    private Integer transType;

    @XmlElement(name = "pay_time")
    @XmlJavaTypeAdapter(DateAdapter.class)
    private Date payTime;

    @XmlElement(name = "pay_status")
    private String payStatus;


    public String getPayTradeNo() {
        return payTradeNo;
    }

    public void setPayTradeNo(String payTradeNo) {
        this.payTradeNo = payTradeNo;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }



    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getPaychannel() {
        return paychannel;
    }

    public void setPaychannel(Integer paychannel) {
        this.paychannel = paychannel;
    }

    public Integer getTransType() {
        return transType;
    }

    public void setTransType(Integer transType) {
        this.transType = transType;
    }
}
