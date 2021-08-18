package com.healthpay.dto.apidto;


import com.healthpay.util.adapter.DateFormatLineAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * @author zhukp
 * @version 1.0
 * @desc 交易结果查询接口返回报文
 * @date 2016-09-10
 */
@XmlRootElement(name = "data")
@XmlAccessorType(value = XmlAccessType.FIELD)

public class P2005RetDTO {
    /**
     * 交易流水号
     */
    @XmlElement(name = "trade_no")
    private String tradeNo;
    /**
     * 通道流水号
     */
    @XmlElement(name = "channel_no")
    private String payTradeNo;
    /**
     * 外部订单号
     */
    @XmlElement(name = "order_no")
    private String orderNo;
    /**
     * 支付金额
     */
    @XmlElement(name = "amt")
    private Double amt;

    /**
     * 支付金额
     */
    @XmlElement(name = "pay_amt")
    protected Double payAmt;

    /**
     * 商家收到金额
     */
    @XmlElement(name = "receipt_amount")
    private Double receiptAmt;
    /**
     * 实际支付时间
     */
    @XmlJavaTypeAdapter(DateFormatLineAdapter.class)
    @XmlElement(name = "pay_time")
    private Date payTime;
    /**
     * 支付状态
     */
    @XmlElement(name = "pay_status")
    private String payStatus;

    @XmlElement(name = "his_charge_no")
    private String hisChargeNo;

    private Integer paychannel;

    public Double getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(Double payAmt) {
        this.payAmt = payAmt;
    }

    public Integer getPaychannel() {
        return paychannel;
    }

    public void setPaychannel(Integer paychannel) {
        this.paychannel = paychannel;
    }

    public String getHisChargeNo() {
        return hisChargeNo;
    }

    public void setHisChargeNo(String hisChargeNo) {
        this.hisChargeNo = hisChargeNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getPayTradeNo() {
        return payTradeNo;
    }

    public void setPayTradeNo(String payTradeNo) {
        this.payTradeNo = payTradeNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
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

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public Double getReceiptAmt() {
        return receiptAmt;
    }

    public void setReceiptAmt(Double receiptAmt) {
        this.receiptAmt = receiptAmt;
    }
}
