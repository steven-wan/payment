package com.healthpay.dto.apidto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author steven
 * @version 1.0
 * @desc
 * @date 2019-09-17 14:44
 */
@XmlRootElement(name = "data")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class R1001RetDTO {
    @XmlElement(name = "personal_id")
    private String personalId;

    @XmlElement(name = "withdraw_amt")
    private double withdrawAmt;

    public double getWithdrawAmt() {
        return withdrawAmt;
    }

    public void setWithdrawAmt(double withdrawAmt) {
        this.withdrawAmt = withdrawAmt;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }
}
