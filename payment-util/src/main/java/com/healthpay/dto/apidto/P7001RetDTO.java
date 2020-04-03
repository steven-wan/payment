package com.healthpay.dto.apidto;


import com.healthpay.util.adapter.DateFormatLineAdapter;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * @author gyp
 * @version 1.0
 * @desc P7001返回报文
 * @date 2017-01-06 16:50
 */
@XmlRootElement(name = "data")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"returnCode","returnMsg","codeUrl","status","payOrderNo","orderNo","outTradeNo","timeEnd","amt","receiptAmt"})
public class P7001RetDTO {
    @XmlElement(name = "return_code")
    private String returnCode;

    @XmlElement(name = "return_msg")
    private String returnMsg;

    @XmlElement(name = "code_url")
    private String codeUrl;

    private String status;

    /**
     * 支付渠道流水号
     */
    @XmlElement(name = "channel_no")
    private String payOrderNo;

    /**
     * 支付平台流水号
     */
    @XmlElement(name = "trade_no")
    private String orderNo;

    /**
     * 外部订单号
     */
    @XmlElement(name = "order_no")
    private String outTradeNo;

    /**
     * 支付完成时间
     */
    @XmlJavaTypeAdapter(DateFormatLineAdapter.class)
    @XmlElement(name = "time_end")
    private Date timeEnd;

    /**
     * 实际支付金额
     */
    private Double amt;

    /**
     * 商家收到金额
     */
    @XmlElement(name = "receipt_amount")
    private Double receiptAmt;

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public Double getReceiptAmt() {
        return receiptAmt;
    }

    public void setReceiptAmt(Double receiptAmt) {
        this.receiptAmt = receiptAmt;
    }
}
